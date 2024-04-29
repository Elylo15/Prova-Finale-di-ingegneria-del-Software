package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.ConnectionState.answerConnectionMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
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
    private HashMap<MatchInfo,ArrayList<PlayerInfo>> games;
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
        games = new HashMap<>();
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
        this.games = new HashMap<>();

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
            ClientConnection connection = new ClientSocket(clientAddress, clientPort, 180, output, input);

            // Confirm the connection to the client
            connection.sendAnswerToConnection(new answerConnectionMessage());
            executor.submit(connection::startCheckConnection);

            Future<serverOptionMessage> serverOption = executor.submit(connection::getServerOption);

            serverOptionMessage msg = serverOption.get();
            if(msg.isNewMatch()) {

                MatchInfo lobby;
                // Player wants to join a new game
                synchronized(games) {
                    lobby = games.keySet().stream()
                            .filter(matchInfo -> matchInfo.getStatus() == MatchState.Waiting)
                            .toList().getFirst();



                    if (lobby != null) {
                        // Send answer
                        if(lobby.getExpectedPlayers() == games.get(lobby).size())
                            connection.sendAnswerToServerOption(false, lobby.getID());
                        else
                            connection.sendAnswerToServerOption(true, lobby.getID());

                        this.welcomeNewPlayer(lobby, connection);

                    } else {
                        //get the other ID
                        Integer id = games.keySet().stream()
                                .map(MatchInfo::getID)
                                .max(Integer::compareTo)
                                .orElse(0);

                        lobby = new MatchInfo(new Match(), id + 1, this.defaultPath + id.toString() ,4, MatchState.Waiting);

                        connection.sendAnswerToServerOption(true, lobby.getID());

                        games.put(lobby, new ArrayList<>());

                        this.welcomeNewPlayer(lobby, connection);
                    }
                }



            } else if (msg.getStartedMatchID() != null && msg.getNickname() != null ) {
                // Player wants to join an already started game

                // Find the game
                MatchInfo lobby = games.keySet().stream()
                        .filter(matchInfo -> matchInfo.getID() == Integer.parseInt(msg.getStartedMatchID()))
                        .limit(1)
                        .findAny().orElse(null);
                // Game not found or lobby full
                if (lobby == null || lobby.getExpectedPlayers() == games.get(lobby).size() || lobby.getStatus() == MatchState.Waiting) {
                    connection.sendAnswerToServerOption(false, 0);
                    socket.close();
                }

                // Check if nickname is correct and if it is available
                ArrayList<String> onlinePlayers = (ArrayList<String>) games.get(lobby).stream()
                        .map(playerInfo -> playerInfo.getPlayer().getNickname())
                        .toList();

                if(lobby != null && lobby.getMatch().getPlayers().contains(msg.getNickname()) && !onlinePlayers.contains(msg.getNickname())) {
                    connection.sendAnswerToServerOption(true, lobby.getID());
                } else {
                    connection.sendAnswerToServerOption(false, 0);
                    socket.close();
                }


                // Add the player to the game
                Player savedPlayer = lobby.getMatch().getPlayers().stream()
                        .filter(player -> Objects.equals(player.getNickname(), msg.getNickname()))
                        .findAny().orElse(null);

                if(lobby.getStatus() == MatchState.Player1 ||
                        lobby.getStatus() == MatchState.Player2 ||
                        lobby.getStatus() == MatchState.Player3 ||
                        lobby.getStatus() == MatchState.Player4) {

                    if(savedPlayer != null) {
                        PlayerInfo savedPlayerInfo = new PlayerInfo(savedPlayer, new PlayerFSM(State.NotPlayerTurn), connection);
                        games.get(lobby).add(savedPlayerInfo);
                    } else {
                        socket.close();
                    }

                } else if (lobby.getStatus() == MatchState.Endgame) {

                    if(savedPlayer != null) {
                        PlayerInfo savedPlayerInfo = new PlayerInfo(savedPlayer, new PlayerFSM(State.EndGame), connection);
                        games.get(lobby).add(savedPlayerInfo);
                    } else {
                        socket.close();
                    }
                } else {
                    connection.sendAnswerToServerOption(false, 0);
                    socket.close();
                }

            } else {
                // Player wants to load a saved game
                // TODO implement loading of a game

            }




        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void acceptConnectionRMI() {

    }

    private void welcomeNewPlayer(MatchInfo lobby, ClientConnection connection) throws Exception {
        // Obtain unavailable names
        ArrayList<String> unavailableNames = (ArrayList<String>) lobby.getMatch().getPlayers().stream()
                .map(Player::getNickname)
                .toList();

        // Ask for the new player name
        String name = connection.getName(unavailableNames);
        if(unavailableNames.contains(name))
            connection.sendAnswerToChosenName(false);
        else
            connection.sendAnswerToChosenName(true);

        while(unavailableNames.contains(name)) {
            name = connection.getName(unavailableNames);
            if(unavailableNames.contains(name))
                connection.sendAnswerToChosenName(false);
            else
                connection.sendAnswerToChosenName(true);
        }

        // Obtain unavailable colors
        ArrayList<String> unavailableColors = (ArrayList<String>) lobby.getMatch().getPlayers().stream()
                .map(Player::getColor)
                .toList();
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
        String color = connection.getColor(availableColors);
        connection.sendAnswerToChosenColor(availableColors.contains(color));
        while(!availableColors.contains(color)) {
            color = connection.getColor(availableColors);
            connection.sendAnswerToChosenColor(availableColors.contains(color));
        }

        // Add the new player
        Player player = new Player(name, color, lobby.getMatch().getCommonArea());
        lobby.getMatch().addPlayer(player);
        games.get(lobby).add(new PlayerInfo(player, new PlayerFSM(), connection));
    }

    private void waitTheEnd(Socket socket, MatchInfo lobby, ClientConnection connection) throws IOException {
        while(lobby.getStatus() != MatchState.KickingPlayers) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {}
        }

        socket.close();

    }

    public void createMatch() {

    }

    public void kickPlayer(String name) {

    }

    public void saveMatch() {

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

    }
}
