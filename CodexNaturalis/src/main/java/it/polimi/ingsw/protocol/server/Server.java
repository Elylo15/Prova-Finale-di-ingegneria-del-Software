package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.server.FSM.MatchState;
import it.polimi.ingsw.protocol.server.FSM.State;
import it.polimi.ingsw.protocol.server.RMI.*;
import it.polimi.ingsw.protocol.server.exceptions.FailedToJoinMatch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Server class that manages the connections with the clients.
 */
public class Server implements Runnable {
    private ArrayList<ClientManager> games;
    private int portSocket;
    private int portRMI;

    private int timeoutSeconds;

    private ServerSocket serverSocket;

    private ThreadPoolExecutor executor;

    private String defaultPath;

    private LogCreator logCreator;

    private boolean serverRunning;


    /**
     * Constructor of the class.
     */
    public Server() {
        portSocket = 1024;
        portRMI = 1099;
        games = new ArrayList<>();
        defaultPath = "savedGames/";
        logCreator = new LogCreator();
        this.serverRunning = false;
        this.timeoutSeconds = 2*60;

        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * Constructor of the class.
     *
     * @param portSocket port of the socket connection.
     * @param portRMI port of the RMI connection.
     * @param maximumPoolSize maximum number of threads in the pool.
     */
    public Server(int portSocket, int portRMI, int maximumPoolSize) {
        this.portSocket = portSocket;
        this.portRMI = portRMI;
        this.games = new ArrayList<>();
        logCreator = new LogCreator();
        this.serverRunning = false;
        this.timeoutSeconds = 2*60;

        if(maximumPoolSize < 2)
            maximumPoolSize = 2;

        int corePoolSize = 2;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * Sets the timeout to the new value.
     *
     * @param timeoutSeconds new value, accepted only if positive.
     */
    public void setTimeoutSeconds(int timeoutSeconds) {
        if(timeoutSeconds > 0)
            this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Accepts a connection via socket from a client and creates a new ClientConnection.
      */
    public void acceptConnectionSocket() {
        try {
            this.serverSocket = new ServerSocket(portSocket);


            while(this.serverRunning) {
                logCreator.log("server socket opened");
                Socket socket = serverSocket.accept();
                ClientConnection connection = null;
                InetAddress clientAddress = socket.getInetAddress();
                int clientPort = socket.getPort();
                try {
                        connection = new ClientSocket(clientAddress.toString(), Integer.toString(clientPort), socket);
                    } catch (Exception e) {
                        logCreator.log("Client " + clientAddress.toString() + " not connected");
                        socket.close();
                    }
                ClientConnection finalConnection = connection;
                executor.submit(() -> this.handleConnection(finalConnection));
            }
        } catch (IOException e) {
            logCreator.log("Server socket accept service crashed");
            return;
        }
    }


    /**
     * Accepts the connection from the client and creates a new ClientConnection.
     */
    public void acceptConnectionRMI() {
        try {
            Registry registry = LocateRegistry.createRegistry(portRMI);
            MainRemoteServer server = new MainRemoteServer();
            registry.bind("MainServer", server);

            while(this.serverRunning) {
                try {
                    // Listens for new clients and returns a ClientConnection to them
                    ClientConnection connection = server.clientConnected(registry);
                    this.executor.submit(() -> this.handleConnection(connection));
                } catch (Exception e) {
                    logCreator.log("Error accepting client connection: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            logCreator.log("Server RMI service crashed");
        }

    }


    /**
     * Handles the connection with the client.
     *
     * @param connection connection with the client.
     */
    private void handleConnection(ClientConnection connection) {
        if(connection == null)
            return;

        while (true) {
            // Confirms the connection to the client
            connection.sendAnswerToConnection(new connectionResponseMessage(true));

            logCreator.log("New client accepted, answer sent: " + connection.getIP() + " " + connection.getPort());


            serverOptionMessage msg = this.obtainServerOption(connection);

            if(msg == null) {
                return;
            }

            if(msg.isNewMatch()) {
                logCreator.log("Client " + connection.getIP() +":" + connection.getPort() + " wants to join a new game");
                if(msg.getMatchID() == null) {
                    this.joinNewMatch(connection);
                    break;
                }
                else {
                    try {
                        this.joinWaitingMatch(connection, msg);
                        break;
                    } catch (FailedToJoinMatch e) {
                        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + ": sending again the serverOption");
                    }
                }
            } else if (msg.getStartedMatchID() != null) {
                logCreator.log("Client " + connection.getIP() +":" + connection.getPort() + " wants to join an already started game");
                try {
                    this.joinStartedMatch(connection,msg);
                    break;
                } catch (FailedToJoinMatch e) {
                    logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getStartedMatchID() + ": sending again the serverOption");
                }
            } else {
                logCreator.log("Client " + connection.getIP() +":" + connection.getPort() + " wants to load a game");
                try {
                    this.joinSavedMatch(connection);
                    break;
                } catch (FailedToJoinMatch e) {
                    logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getStartedMatchID() + ": sending again the serverOption");
                }
            }
        }
    }


    /**
     * Obtains the serverOptionMessage from the client.
     *
     * @param connection connection with the client.
     * @return serverOptionMessage obtained from the client.
     */
    private serverOptionMessage obtainServerOption(ClientConnection connection) {

        // Sends status: "ServerOptionState"
        connection.sendCurrentState(new currentStateMessage(null, null, "ServerOptionState", false, null, null));

        boolean correctResponse = false;

        Future<serverOptionMessage> serverOption;
        serverOptionMessage msg = null;


        while(!correctResponse) {
            ArrayList<Integer> waitingGames = games.stream()
                    .filter(game -> game.getMatchInfo().getStatus() == MatchState.Waiting)
                    .map(game -> game.getMatchInfo().getID())
                    .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<Integer> runningGames = games.stream()
                    .filter(game -> game.getMatchInfo().getStatus() != MatchState.Waiting && game.getMatchInfo().getStatus() != MatchState.Endgame)
                    .map(game -> game.getMatchInfo().getID())
                    .collect(Collectors.toCollection(ArrayList::new));

            // TODO Collect all saved matches
            ArrayList<Integer> savedMatches = new ArrayList<>();


            // Requests the ServerOptionMessage
            serverOption = executor.submit(() -> connection.getServerOption(waitingGames, runningGames, savedMatches));

            try {
                msg = serverOption.get(this.timeoutSeconds, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to no serverOption received");
                return null;
            } catch (InterruptedException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to INTERRUPT");
                return null;
            } catch (ExecutionException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to EXECUTION ERROR: \n" + e.getCause().getMessage());
                return null;
            }

            // Checks if the response message is valid
            if(msg.isNewMatch() && (msg.getMatchID() == null || waitingGames.contains(msg.getMatchID()))) {
                correctResponse = true;
            } else if (msg.getStartedMatchID() != null && runningGames.contains(msg.getStartedMatchID())) {
                correctResponse = true;
            } else if (msg.isLoadMatch() && (msg.getSavedMatchID() != null) && savedMatches.contains(msg.getSavedMatchID())) {
                correctResponse = true;
            } else {
                // correctResponse = false;
                connection.sendAnswer(false);
                logCreator.log("Client " + connection.getIP() + " has given wrong ServerOption");
            }

        }

        connection.sendAnswer(true);

        logCreator.log("Server option fetched from client" + connection.getIP() + " " + connection.getPort());

        return msg;
    }


    /**
     * Client joins a not yet created new match.
     *
     * @param connection connection with the client.
     */
    private void joinNewMatch(ClientConnection connection) {
        // Client wants to join a new game
        boolean correctJoining = false;
        while(!correctJoining) {
            Integer id = this.createNewMatchID();
            MatchInfo matchInfo = new MatchInfo(new Match(),id,this.defaultPath + id.toString() + ".savedgame" ,null, MatchState.Waiting);

            ClientManager lobbyManager = new ClientManager(matchInfo);
            games.add(lobbyManager);
            executor.submit(lobbyManager);

            logCreator.log("Client socket " + connection.getIP() + " " + connection.getPort() + " starts a new ClientManager");

            try {
                this.welcomeNewPlayer(lobbyManager, connection);
                correctJoining = true;
            } catch (FailedToJoinMatch e) {
                logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + id);
            }
        }
    }

    /**
     * Client joins a waiting match.
     *
     * @param connection connection with the client.
     * @param msg serverOptionMessage obtained from the client.
     * @throws FailedToJoinMatch if the player cannot join the game.
     */
    private void joinWaitingMatch(ClientConnection connection, serverOptionMessage msg) throws FailedToJoinMatch {
        // Player wants to join a waiting game
        ClientManager lobbyManager = games.stream()
                .filter(clientManager -> Objects.equals(clientManager.getMatchInfo().getID(), msg.getMatchID()))
                .limit(1)
                .findAny().orElse(null);

        // Checks again: Game not found
        if(lobbyManager == null) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + " due to not found lobby");
            throw new FailedToJoinMatch("Failed to join match " + msg.getMatchID() + " due to not found lobby");
        }

        this.welcomeNewPlayer(lobbyManager, connection);
    }

    private void joinStartedMatch(ClientConnection connection, serverOptionMessage msg) throws FailedToJoinMatch {
        // Find the lobby
        // getName
        // AddPlayer


        // TODO finish this part
        // Player wants to join an already started game


        // TODO add status information

        // Finds the game
        ClientManager lobbyManager = games.stream()
                .filter(clientManager -> Objects.equals(clientManager.getMatchInfo().getID(), msg.getStartedMatchID()))
                .limit(1)
                .findAny().orElse(null);

        // Checks again: Game not found
        if(lobbyManager == null) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + " due to not found lobby");
            throw new FailedToJoinMatch("Failed to join match " + msg.getMatchID() + " due to not found lobby");
        }

        // TODO Send status information

        // TODO Get the player name

        // TODO Add the player to the game

    }

    private void joinSavedMatch(ClientConnection connection) throws FailedToJoinMatch{
        // Player wants to load a saved game
        // TODO implement loading of a game
        // maybe start from here the custom loaded game and not from run
        // TODO use synchronized and if it falis kickThePlayer



    }

    /**
     * Tries to add a new player to the waiting list of a match.
     * Asks for the player name and color to the client.
     *
     * @param lobbyManager ClientManager of the game.
     * @param connection connection with the client.
     * @throws FailedToJoinMatch if the player cannot join the game.
     */
    private void welcomeNewPlayer(ClientManager lobbyManager, ClientConnection connection) throws FailedToJoinMatch {
        // Sends status information
        currentStateMessage currState = new currentStateMessage(null,null,"ConnectionState", false, null, null);
        connection.sendCurrentState(currState);
        logCreator.log("ConnectionState sent to" + connection.getIP() + " " + connection.getPort());

        // Obtains unavailable names
        ArrayList<String> unavailableNames = lobbyManager.getPlayersInfo().stream()
                .map(playerInfo -> playerInfo.getPlayer().getNickname().toLowerCase())
                .collect(Collectors.toCollection(ArrayList::new));

        if(lobbyManager.getMatchInfo().getExpectedPlayers() != null && unavailableNames.size() >= lobbyManager.getMatchInfo().getExpectedPlayers()) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
            throw new FailedToJoinMatch("Failed to join match " + lobbyManager.getMatchInfo().getID() + " due to full lobby");
        }

        // Asks for the new player name
        String name = connection.getName(unavailableNames).getName().toLowerCase();
        if(unavailableNames.contains(name))
            connection.sendAnswer(false);
        else
            connection.sendAnswer(true);

        // Asks again until it is a valid name
        while(unavailableNames.contains(name)) {
            name = connection.getName(unavailableNames).getName().toLowerCase();
            if(unavailableNames.contains(name))
                connection.sendAnswer(false);
            else
                connection.sendAnswer(true);
        }

        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " chose name: " + name);

        // Obtains unavailable colors
        ArrayList<String> unavailableColors = lobbyManager.getPlayersInfo().stream()
                .map(playerInfo -> playerInfo.getPlayer().getColor().toLowerCase())
                .collect(Collectors.toCollection(ArrayList::new));

        if(unavailableColors.size() >= 4) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
            throw new FailedToJoinMatch("Failed to join match " + lobbyManager.getMatchInfo().getID() + " due to full lobby");
        }

        ArrayList<String> availableColors = new ArrayList<>();
        if(!unavailableColors.contains("blue"))
            availableColors.add("blue");
        if(!unavailableColors.contains("red"))
            availableColors.add("red");
        if(!unavailableColors.contains("green"))
            availableColors.add("green");
        if(!unavailableColors.contains("yellow"))
            availableColors.add("yellow");

        // Asks for the player color
        if(availableColors.isEmpty()) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
            throw new FailedToJoinMatch("Failed to join match " + lobbyManager.getMatchInfo().getID() + " due to full lobby");
        }
        String color = connection.getColor(availableColors).getColor().toLowerCase();
        connection.sendAnswer(availableColors.contains(color));
        // Asks again until it is a valid color
        while(!availableColors.contains(color)) {
            color = connection.getColor(availableColors).getColor().toLowerCase();
            connection.sendAnswer(availableColors.contains(color));
        }

        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " chose color: " + color);

        // Adds the new player to the waiting list if the clientManager in not full
        try {
            Player player = new Player(name, color, lobbyManager.getMatchInfo().getMatch().getCommonArea());
            PlayerInfo playerInfo = new PlayerInfo(player,State.WaitingForPlayers, connection);
            lobbyManager.addPlayerInfo(playerInfo);
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " joined match " + lobbyManager.getMatchInfo().getID());
        } catch(Exception e) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
            throw new FailedToJoinMatch("Failed to join match " + lobbyManager.getMatchInfo().getID() + " due to full lobby");
        }
    }


    /**
     * Closes the match when the ClientManager is not running anymore.
     */
    private void closeMatch() {
        while(this.serverRunning || !this.games.isEmpty()) {
            games.removeIf(clientManager -> clientManager.getMatchInfo().getStatus() == MatchState.KickingPlayers);
        }
    }

    private Integer createNewMatchID() {
        int MatchID = 0;
        boolean correct = false;
        while(!correct || MatchID == 1000) {
            MatchID = (new Random()).nextInt(100000);
            Integer finalMatchID = MatchID;
            if(games.stream()
                    .map(clientManager -> clientManager.getMatchInfo().getID())
                    .noneMatch(a -> Objects.equals(a, finalMatchID)))
                correct = true;
        }
        return  MatchID;
    }

    private void joinSavedMatch(String fileName) {
        // TODO load the match

    }

    /**
     * Runs the server.
     */
    @Override
    public void run() {
        this.serverRunning = true;
        executor.submit(this::acceptConnectionSocket);
        executor.submit(this::acceptConnectionRMI);
        executor.submit(this::closeMatch);
        logCreator.log("Server started");
    }
}
