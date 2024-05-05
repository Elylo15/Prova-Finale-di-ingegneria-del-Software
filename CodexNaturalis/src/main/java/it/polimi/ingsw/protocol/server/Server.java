package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.server.FSM.MatchState;
import it.polimi.ingsw.protocol.server.FSM.State;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    // TODO logfile
    private ArrayList<ClientManager> games;
    private int portSocket;
    private int portRMI;

    private ServerSocket serverSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private ThreadPoolExecutor executor;

    private String defaultPath;

    private LogCreator logCreator;


    public Server() {
        portSocket = 30000;
        portRMI = 40000;
        games = new ArrayList<>();
        defaultPath = "savedGames/";
        logCreator = new LogCreator();

        int corePoolSize = 15;
        int maximumPoolSize = 100;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    public Server(int portSocket, int portRMI, int maximumPoolSize) {
        this.portSocket = portSocket;
        this.portRMI = portRMI;
        this.games = new ArrayList<>();
        logCreator = new LogCreator();

        if(maximumPoolSize < 2)
            maximumPoolSize = 2;

        int corePoolSize = 2;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    public void acceptConnectionSocket() {
        try {
            this.serverSocket = new ServerSocket(portSocket);


            while(true) {
                logCreator.log("server socket opened");
                Socket socket = serverSocket.accept();
                executor.submit(() -> this.handleConnectionSocket(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleConnectionSocket(Socket socket) {
        InetAddress clientAddress = socket.getInetAddress();
        int clientPort = socket.getPort();
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ClientConnection connection = new ClientSocket(clientAddress.toString(), Integer.toString(clientPort), socket);

            // Confirms the connection to the client
            connection.sendAnswerToConnection(new connectionResponseMessage(true));

            logCreator.log("New client socket accepted, answer sent: " + clientAddress.toString() + " " + clientPort);


            // Sends status: "ServerOptionState"
            executor.submit(() -> connection.sendCurrentState(new currentStateMessage(null, null, "ServerOptionState", false)));

            boolean correctResponse = false;

            Future<serverOptionMessage> serverOption;
            serverOptionMessage msg = null;
            // TODO update this part with TimerTask
            while(!correctResponse) {
                // Requests the ServerOptionMessage
                serverOption = executor.submit(connection::getServerOption);

                // If client loses connection, then this method ends
                while(!serverOption.isDone())
                {
                    if(!this.checkIsAlive(connection)) {
                        socket.close();
                        return;
                    }
                }

                msg = serverOption.get();

                // Checks if the response message is valid
                if(!msg.isNewMatch() && (msg.getNickname() == null || Objects.equals(msg.getNickname(), "") || msg.getStartedMatchID() == null) && !msg.isLoadMatch())
                    connection.sendAnswerToServerOption(false, null);
                else
                    correctResponse = true;
            }

            logCreator.log("Server option fetched from client socket " + clientAddress.toString() + " " + clientPort);

            if(msg.isNewMatch()) {

                ClientManager lobby;
                // Player wants to join a new game
                lobby = games.stream()
                        .filter(clientManager -> clientManager.getMatchInfo().getStatus() == MatchState.Waiting)
                        .filter(clientManager -> clientManager.getMatchInfo().getExpectedPlayers() > clientManager.getPlayersInfo().size())
                        .toList().getFirst();



                if (lobby != null) {
                    // Sends answer
                    connection.sendAnswerToServerOption(true, lobby.getMatchInfo().getID());

                    logCreator.log("Client socket " + clientAddress.toString() + " " + clientPort + " joins a match in WaitingForPlayerState");

                    this.welcomeNewPlayer(lobby, connection);

                    /*
                     The managing of the "host" (first player who decides the expected players number)
                     is implemented in ClientManager.
                    */

                } else {
                    // gets the other ID
                    Integer id = games.stream()
                            .map(clientManager -> clientManager.getMatchInfo().getID())
                            .max(Integer::compareTo)
                            .orElse(0);

                    MatchInfo matchInfo = new MatchInfo(new Match(), id + 1, this.defaultPath + id.toString() + ".savedgame" ,null, MatchState.Waiting);

                    connection.sendAnswerToServerOption(true, lobby.getMatchInfo().getID());
                    ClientManager lobbyManager = new ClientManager(matchInfo);
                    games.add(lobbyManager);

                    logCreator.log("Client socket " + clientAddress.toString() + " " + clientPort + " starts a new ClientManager");

                    this.welcomeNewPlayer(lobbyManager, connection);

                    executor.submit(lobbyManager);
                }

            } else if (msg.getStartedMatchID() != null && msg.getNickname() != null ) {
                // TODO finish this part
                // Player wants to join an already started game


                // TODO add status information

                // Finds the game
                ClientManager lobbyManager = games.stream()
                        .filter(clientManager -> clientManager.getMatchInfo().getID() == Integer.parseInt(msg.getStartedMatchID()))
                        .limit(1)
                        .findAny().orElse(null);

                // Game not found
                if(lobbyManager == null) {
                    connection.sendAnswer(false);
                    socket.close();
                }
                synchronized (lobbyManager) {

                    // Is lobby full?
                    if (lobbyManager.getMatchInfo().getExpectedPlayers() <= lobbyManager.getPlayersInfo().size() || lobbyManager.getMatchInfo().getStatus() == MatchState.Waiting) {
                        connection.sendAnswer(false);
                        socket.close();
                    }


                    // Checks if nickname is correct and if it is available
                    ArrayList<String> onlinePlayers = (ArrayList<String>) lobbyManager.getPlayersInfo().stream()
                            .map(playerInfo -> playerInfo.getPlayer().getNickname())
                            .toList();

                    if(lobbyManager.getMatch().getPlayers().contains(msg.getNickname()) && !onlinePlayers.contains(msg.getNickname())) {
                        connection.sendAnswer(true);
                    } else {
                        connection.sendAnswer(false);
                        socket.close();
                    }


                    // Add the player to the game
                    Player savedPlayer = lobbyManager.getMatch().getPlayers().stream()
                            .filter(player -> Objects.equals(player.getNickname(), msg.getNickname()))
                            .findAny().orElse(null);

                    if(lobbyManager.getMatchInfo().getStatus() == MatchState.Player1 ||
                            lobbyManager.getMatchInfo().getStatus() == MatchState.Player2 ||
                            lobbyManager.getMatchInfo().getStatus() == MatchState.Player3 ||
                            lobbyManager.getMatchInfo().getStatus() == MatchState.Player4) {

                        if(savedPlayer != null) {
                            PlayerInfo savedPlayerInfo = new PlayerInfo(savedPlayer, State.LastTurn, connection);
                            try {
                                lobbyManager.addPlayerInfo(savedPlayerInfo);
                            } catch (Exception e) {
                                connection.closeConnection();
                            }
                        } else {
                            socket.close();
                        }

                    } else if (lobbyManager.getMatchInfo().getStatus() == MatchState.Endgame) {

                        if(savedPlayer != null) {
                            PlayerInfo savedPlayerInfo = new PlayerInfo(savedPlayer, State.EndGame, connection);
                            try {
                                lobbyManager.addPlayerInfo(savedPlayerInfo);
                            } catch (Exception e) {
                                connection.closeConnection();
                            }
                        } else {
                            socket.close();
                        }
                    } else {
                        connection.send(false);
                        socket.close();
                    }
                }

            } else {
                // Player wants to load a saved game
                // TODO implement loading of a game
                // maybe start from here the custom loaded game and not from run
                // TODO use synchronized and if it falis kickThePlayer



            }




        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void acceptConnectionRMI() {

    }

    // TODO remove this method
    private boolean checkIsAlive(ClientConnection connection) {
        return connection != null && connection.getStatus().equals("online");
    }

    private void welcomeNewPlayer(ClientManager lobbyManager, ClientConnection connection) {
        // Sends status information
        currentStateMessage currState = new currentStateMessage(null,null,"ConnectionState", false);
        connection.sendCurrentState(currState);
        logCreator.log("ConnectionState sent to" + connection.getIP() + " " + connection.getPort());

        // Obtains unavailable names
        ArrayList<String> unavailableNames = (ArrayList<String>) lobbyManager.getPlayersInfo().stream()
                .map(playerInfo -> playerInfo.getPlayer().getNickname())
                .toList();

        if(unavailableNames.size() >= lobbyManager.getMatchInfo().getExpectedPlayers())
            connection.closeConnection();

        // Asks for the new player name
        String name = connection.getName(unavailableNames).getName();
        if(unavailableNames.contains(name))
            connection.sendAnswer(false);
        else
            connection.sendAnswer(true);

        // Asks again until it is a valid name
        while(unavailableNames.contains(name)) {
            name = connection.getName(unavailableNames);
            if(unavailableNames.contains(name))
                connection.sendAnswer(false);
            else
                connection.sendAnswer(true);
        }

        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " chose name: " + name);

        // Obtains unavailable colors
        ArrayList<String> unavailableColors = (ArrayList<String>) lobbyManager.getPlayersInfo().stream()
                .map(playerInfo -> playerInfo.getPlayer().getColor())
                .toList();

        if(unavailableColors.size() >= 4)
            connection.closeConnection();

        ArrayList<String> availableColors = new ArrayList<>();
        if(!unavailableColors.contains("Blue"))
            availableColors.add("Blue");
        if(!unavailableColors.contains("Red"))
            availableColors.add("Red");
        if(!unavailableColors.contains("Green"))
            availableColors.add("Green");
        if(!unavailableColors.contains("Yellow"))
            availableColors.add("Yellow");

        // Asks for the player color
        if(availableColors.isEmpty()) {
            try {
                connection.getSocket().close();
                logCreator.log("Client socket " + connection.getIP() + " " + connection.getPort() + " closed connection");
            } catch (IOException ignore) {}
        }
        String color = connection.getColor(availableColors);
        connection.sendAnswerToChosenColor(availableColors.contains(color));
        // Asks again until it is a valid color
        while(!availableColors.contains(color)) {
            color = connection.getColor(availableColors);
            connection.sendAnswerToChosenColor(availableColors.contains(color));
        }

        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " chose color: " + color);

        // Adds the new player to the waiting list if the clientManager in not full
        try {
            Player player = new Player(name, color, lobbyManager.getMatchInfo().getMatch().getCommonArea());
            PlayerInfo playerInfo = new PlayerInfo(player,State.WaitingForPlayers, connection);
            lobbyManager.addPlayerInfo(playerInfo);
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " joined match " + lobbyManager.getMatchInfo().getID());
        } catch(Exception e) {
            connection.closeConnection();
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
        }


    }





    public void closeMatch() {
        // TODO remove match from games list

        //TODO find a way to close a match at any moment, possibly involve executors
    }

    public void loadMatch(String fileName) {
        // TODO load the match
    }

    /**
     * Runs the server.
     */
    @Override
    public void run() {
        executor.submit(this::acceptConnectionSocket);
        executor.submit(this::acceptConnectionRMI);
        logCreator.log("Server started");

        // TODO maybe add the remove match from list method
    }
}
