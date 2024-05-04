package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.server.FSM.MatchState;
import it.polimi.ingsw.protocol.server.FSM.PlayerFSM;
import it.polimi.ingsw.protocol.server.FSM.State;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.HashMap;
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


    public Server() {
        portSocket = 30000;
        portRMI = 40000;
        games = new ArrayList<>();
        defaultPath = "./savedGames/";

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

            // Confirm the connection to the client
            connection.sendAnswerToConnection(new connectionResponseMessage(true));

            // Start checking if the client is alive
            executor.submit(connection::startCheckConnection);

            // Send status: "ServerOptionState"
            executor.submit(() -> connection.sendCurrentState(new currentStateMessage(null, null, "ServerOptionState", false)));

            boolean correctResponse = false;

            Future<serverOptionMessage> serverOption;
            serverOptionMessage msg = null;

            while(!correctResponse) {
                // Request the ServerOptionMessage
                serverOption = executor.submit(connection::getServerOption);

                // If client loses connection, the methods ends
                while(!serverOption.isDone())
                {
                    if(!this.checkIsAlive(connection)) {
                        socket.close();
                        return;
                    }
                }

                msg = serverOption.get();

                // Check if the response message is valid
                if(!msg.isNewMatch() && (msg.getNickname() == null || Objects.equals(msg.getNickname(), "") || msg.getStartedMatchID() == null) && !msg.isLoadMatch())
                    connection.sendAnswerToServerOption(false, null);
                else
                    correctResponse = true;
            }


            if(msg.isNewMatch()) {

                ClientManager lobby;
                // Player wants to join a new game
                lobby = games.stream()
                        .filter(clientManager -> clientManager.getMatchInfo().getStatus() == MatchState.Waiting)
                        .filter(clientManager -> clientManager.getMatchInfo().getExpectedPlayers() > clientManager.getPlayersInfo().size())
                        .toList().getFirst();



                if (lobby != null) {
                    // Send answer
                    connection.sendAnswerToServerOption(true, lobby.getMatchInfo().getID());

                    this.welcomeNewPlayer(lobby, connection);

                    // The managing of the "host" (first player who decides the expected players number)
                    // is implemented in ClientManager.

                } else {
                    //get the other ID
                    Integer id = games.stream()
                            .map(clientManager -> clientManager.getMatchInfo().getID())
                            .max(Integer::compareTo)
                            .orElse(0);

                    MatchInfo matchInfo = new MatchInfo(new Match(), id + 1, this.defaultPath + id.toString() ,4, MatchState.Waiting);

                    connection.sendAnswerToServerOption(true, lobby.getMatchInfo().getID());
                    ClientManager lobbyManager = new ClientManager(matchInfo);
                    games.add(lobbyManager);

                    this.welcomeNewPlayer(lobbyManager, connection);
                }

            } else if (msg.getStartedMatchID() != null && msg.getNickname() != null ) {
                // TODO finish this part
                // Player wants to join an already started game


                // TODO add status information

                // Find the game
                ClientManager lobbyManager = games.stream()
                        .filter(clientManager -> clientManager.getMatchInfo().getID() == Integer.parseInt(msg.getStartedMatchID()))
                        .limit(1)
                        .findAny().orElse(null);

                // Game not found
                if(lobbyManager == null) {
                    connection.sendAnswerToServerOption(false, 0);
                    socket.close();
                }
                synchronized (lobbyManager) {

                    // Lobby full
                    if (lobbyManager.getMatchInfo().getExpectedPlayers() <= lobbyManager.getPlayersInfo().size() || lobbyManager.getMatchInfo().getStatus() == MatchState.Waiting) {
                        connection.sendAnswerToServerOption(false, 0);
                        socket.close();
                    }


                    // Check if nickname is correct and if it is available
                    ArrayList<String> onlinePlayers = (ArrayList<String>) lobbyManager.getPlayersInfo().stream()
                            .map(playerInfo -> playerInfo.getPlayer().getNickname())
                            .toList();

                    if(lobbyManager.getMatch().getPlayers().contains(msg.getNickname()) && !onlinePlayers.contains(msg.getNickname())) {
                        connection.sendAnswerToServerOption(true, lobbyManager.getID());
                    } else {
                        connection.sendAnswerToServerOption(false, 0);
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
                            PlayerInfo savedPlayerInfo = new PlayerInfo(savedPlayer, new PlayerFSM(State.NotPlayerTurn), connection);
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
                            PlayerInfo savedPlayerInfo = new PlayerInfo(savedPlayer, new PlayerFSM(State.EndGame), connection);
                            try {
                                lobbyManager.addPlayerInfo(savedPlayerInfo);
                            } catch (Exception e) {
                                connection.closeConnection();
                            }
                        } else {
                            socket.close();
                        }
                    } else {
                        connection.sendAnswerToServerOption(false, 0);
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


    private boolean checkIsAlive(ClientConnection connection) {
        return connection != null && connection.getStatus().equals("online");
    }

    private void welcomeNewPlayer(ClientManager lobbyManager, ClientConnection connection) throws Exception {
        // Send status information
        currentStateMessage currState = new currentStateMessage(null,null,"ConnectionState", false);
        connection.sendCurrentState(currState);

        // Obtain unavailable names
        ArrayList<String> unavailableNames = (ArrayList<String>) lobbyManager.getPlayersInfo().stream()
                .map(playerInfo -> playerInfo.getPlayer().getNickname())
                .toList();

        if(unavailableNames.size() >= lobbyManager.getMatchInfo().getExpectedPlayers())
            connection.closeConnection();

        // Ask for the new player name
        String name = connection.getName(unavailableNames).getName();
        if(unavailableNames.contains(name))
            connection.sendAnswerToChosenName(false);
        else
            connection.sendAnswerToChosenName(true);

        // Ask again until it is a valid name
        while(unavailableNames.contains(name)) {
            name = connection.getName(unavailableNames);
            if(unavailableNames.contains(name))
                connection.sendAnswerToChosenName(false);
            else
                connection.sendAnswerToChosenName(true);
        }

        // Obtain unavailable colors
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

        // Ask for the player color
        if(availableColors.isEmpty())
            connection.getSocket().close();
        String color = connection.getColor(availableColors);
        connection.sendAnswerToChosenColor(availableColors.contains(color));
        while(!availableColors.contains(color)) {
            color = connection.getColor(availableColors);
            connection.sendAnswerToChosenColor(availableColors.contains(color));
        }

        // Add the new player to the waiting list if the clientManager in not full
        try {
            Player player = new Player(name, color, lobbyManager.getMatchInfo().getMatch().getCommonArea());
            PlayerInfo playerInfo = new PlayerInfo(player, new PlayerFSM(), connection);
        } catch(Exception e) {
            connection.closeConnection();
        }


    }



    private void waitTheEnd(Socket socket, MatchInfo lobby, ClientConnection connection) throws IOException {
        while(lobby.getStatus() != MatchState.KickingPlayers) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {}
        }

        socket.close();

    }







    public void closeMatch() {
        //TODO find a way to close a match at any momenta, possibly involve executors
    }

    public void loadMatch(String fileName) {

    }

    /**
     * Runs the server.
     */
    @Override
    public void run() {
        executor.submit(this::acceptConnectionSocket);
        executor.submit(this::acceptConnectionRMI);
    }
}
