package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.connectionState.connectionResponseMessage;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.exceptions.FailedToJoinMatch;
import it.polimi.ingsw.server.fsm.MatchState;
import it.polimi.ingsw.server.fsm.State;
import it.polimi.ingsw.server.rmi.MainRemoteServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Server class that manages the connections with the clients.
 */
public class Server implements Runnable {
    private final CopyOnWriteArrayList<MatchManager> games;
    private final int timeoutSeconds;
    private final String defaultPath;
    private final LogCreator logCreator;
    private final int portSocket;
    private final int portRMI;
    private final ThreadPoolExecutor executor;
    private ServerSocket serverSocket;
    private MainRemoteServer server;
    private Registry registry;
    private boolean serverRunning;


    /**
     * Constructor of the class.
     */
    public Server() {
        portSocket = 1024;
        portRMI = 1099;
        games = new CopyOnWriteArrayList<>();
        defaultPath = "savedMatches";
        logCreator = new LogCreator();
        this.serverRunning = false;
        this.timeoutSeconds = 2 * 60;

        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300; //after 300 seconds idle threads in excess of corePoolSize are terminated
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());
    }


    /**
     * Accepts a connection via socket from a client and creates a new ClientConnection.
     */
    protected void acceptConnectionSocket() {
        try {
            this.serverSocket = new ServerSocket(portSocket);


            while (this.serverRunning) {
                logCreator.log("server socket opened");
                Socket socket = serverSocket.accept();
                ClientConnection connection = null;
                InetAddress clientAddress = socket.getInetAddress(); //get IP of the client connected to the server
                int clientPort = socket.getPort(); //get port of the client connected to the server
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
            logCreator.log("Server socket accept service closed: " + e.getMessage());
        }
    }


    /**
     * Accepts the connection from the client and creates a new ClientConnection.
     */
    protected void acceptConnectionRMI() {
        try {
            logCreator.log("Opening rmi service");
            registry = LocateRegistry.createRegistry(portRMI);
            server = new MainRemoteServer();
            registry.bind("MainServer", server);

            while (this.serverRunning) {
                try {
                    logCreator.log("Waiting for new rmi client connection");

                    // Listens for new clients and returns a ClientConnection to them
                    ClientConnection connection = server.clientConnected(registry);
                    this.executor.submit(() -> this.handleConnection(connection));
                } catch (Exception e) {
                    logCreator.log("Error accepting client connection: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            logCreator.log("Server rmi service closed: " + e.getMessage());
        }

    }


    /**
     * Handles the connection with the client.
     *
     * @param connection connection with the client.
     */
    private void handleConnection(ClientConnection connection) {
        if (connection == null)
            return;
        // Confirms the positive establishment of the connection to the client
        connection.sendAnswerToConnection(new connectionResponseMessage(true));

        while (true) {


            logCreator.log("New client accepted, answer sent: " + connection.getIP() + " " + connection.getPort());


            serverOptionMessage msg = this.obtainServerOption(connection);

            if (msg == null) {
                logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " kicked due to no serverOption received");
                connection.closeConnection();
                return;
            }

            if (msg.isNewMatch()) {
                logCreator.log("Client " + connection.getIP() + ":" + connection.getPort() + " wants to join a new game");
                if (msg.getMatchID() == null) {
                    this.joinNewMatch(connection);
                    break;
                } else {
                    try {
                        this.joinWaitingMatch(connection, msg);
                        break;
                    } catch (FailedToJoinMatch e) {
                        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + ": sending again the serverOption");
                    }
                }
            } else if (msg.getStartedMatchID() != null) {
                logCreator.log("Client " + connection.getIP() + ":" + connection.getPort() + " wants to join an already started game");
                try {
                    this.joinStartedMatch(connection, msg);
                    break;
                } catch (FailedToJoinMatch e) {
                    logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getStartedMatchID() + ": sending again the serverOption");
                }
            } else {
                logCreator.log("Client " + connection.getIP() + ":" + connection.getPort() + " wants to load a game");
                try {
                    this.joinSavedMatch(connection, msg);
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

        // Sends status: "serverOptionState"
        connection.sendCurrentState(new currentStateMessage(null, null, "serverOptionState", false, null, null, null));

        boolean correctResponse = false;

        Future<serverOptionMessage> serverOption;
        serverOptionMessage msg = null;


        while (!correctResponse) {
            // Obtains the list of games in waiting
            ArrayList<Integer> waitingGames = games.stream()
                    .filter(game -> game.getMatchInfo().getStatus() == MatchState.Waiting)
                    .map(game -> game.getMatchInfo().getID())
                    .collect(Collectors.toCollection(ArrayList::new)); // Arraylist of ID of the matches that are in MatchState Waiting

            // Obtains the list of running games
            ArrayList<Integer> runningGames = games.stream()
                    .filter(game -> game.getMatchInfo().getStatus() != MatchState.Waiting && game.getMatchInfo().getStatus() != MatchState.Endgame)
                    //stream must contain only matches whose number of online players is lower than the number of expected players
                    .filter(game -> game.getMatchInfo().getExpectedPlayers() != null && game.getOnlinePlayerInfo().size() < game.getMatchInfo().getExpectedPlayers())
                    .map(game -> game.getMatchInfo().getID())
                    .collect(Collectors.toCollection(ArrayList::new));

            // Obtains the list of saved matches
            ArrayList<Integer> savedMatches = this.listSavedMatches();
            savedMatches.removeAll(waitingGames);
            savedMatches.removeAll(runningGames);

            //Sends towards the client waitingMatches, runningMatches, savedMatches.
            //Requests to receive the ServerOptionMessage with the choice of the user
            serverOption = executor.submit(() -> connection.getServerOption(waitingGames, runningGames, savedMatches));

            try {
                msg = serverOption.get(this.timeoutSeconds, TimeUnit.SECONDS); //try to obtain the serverOptionMessage, blocking program execution, if necessary, until timeout is over
            } //if it does not manage to obtain the result of the task submitted to the executor we print the error in the log file
            catch (TimeoutException e) {
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

            if (msg == null)
                return null;

            // Checks if the response message is valid
               //the user wants to create a new game or join an existing one (not yet started)
            if (msg.isNewMatch() && (msg.getMatchID() == null || waitingGames.contains(msg.getMatchID()))) {
                correctResponse = true;
            } //the user wants to join a running match
            else if (msg.getStartedMatchID() != null && runningGames.contains(msg.getStartedMatchID())) {
                correctResponse = true;
            }  //the user wants to load a saved match
            else if (msg.isLoadMatch() && (msg.getSavedMatchID() != null) && savedMatches.contains(msg.getSavedMatchID())) {
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
        // User wants to join a new game
        boolean correctJoining = false;
        while (!correctJoining) {
            Integer id = this.createNewMatchID();
            MatchInfo matchInfo = new MatchInfo(new Match(), id, null, MatchState.Waiting);

            MatchManager lobbyManager = new MatchManager(matchInfo);
            games.add(lobbyManager);
            executor.submit(lobbyManager); // run() method of MatchManager is submitted to the executor

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
     * @param msg        serverOptionMessage obtained from the client.
     * @throws FailedToJoinMatch if the player cannot join the game.
     */
    private void joinWaitingMatch(ClientConnection connection, serverOptionMessage msg) throws FailedToJoinMatch {
        // User wants to join a waiting game
        MatchManager lobbyManager = games.stream()
                .filter(matchManager -> Objects.equals(matchManager.getMatchInfo().getID(), msg.getMatchID()))
                .limit(1)  //the stream must contain only matchManager object whose id match related is equal to the matchID in the serverOptionMessage
                .findAny().orElse(null);

        // Checks again: Game not found
        if (lobbyManager == null) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + " due to not found lobby");
            throw new FailedToJoinMatch("Failed to join match " + msg.getMatchID() + " due to not found lobby");
        }

        this.welcomeNewPlayer(lobbyManager, connection);
    }

    /**
     * Client joins a started match.
     *
     * @param connection connection with the client.
     * @param msg        serverOptionMessage obtained from the client.
     * @throws FailedToJoinMatch if the player cannot join the game.
     */
    private void joinStartedMatch(ClientConnection connection, serverOptionMessage msg) throws FailedToJoinMatch {

        // Finds the game
        MatchManager lobbyManager = games.stream()
                .filter(matchManager -> Objects.equals(matchManager.getMatchInfo().getID(), msg.getStartedMatchID()))
                .limit(1)
                .findAny().orElse(null);

        // there is no MatchManager in games whose related match ID is equal to startedMatchID in serverOptionMessage or the number of expected players is not lower to the number of online players
        if (lobbyManager == null || lobbyManager.getMatchInfo().getExpectedPlayers() <= lobbyManager.getOnlinePlayerInfo().size()) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + " due to not found lobby");
            throw new FailedToJoinMatch("Failed to join match " + msg.getMatchID() + " due to not found lobby");
        }

        this.joinNotNewMatch(connection, msg, lobbyManager);

    }

    /**
     * Client joins a saved match.
     *
     * @param connection connection with the client.
     * @param message    serverOptionMessage obtained from the client.
     * @throws FailedToJoinMatch if the player cannot join the game.
     */
    private void joinSavedMatch(ClientConnection connection, serverOptionMessage message) throws FailedToJoinMatch {

        MatchManager lobbyManager;
        synchronized (this) {
            //if savedMatchID of serverOptionMessage is already present in games it means the match has already started
            if (this.games.stream().anyMatch(matchManager -> Objects.equals(matchManager.getMatchInfo().getID(), message.getSavedMatchID()))) {
                logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + message.getSavedMatchID() + " due to already started game");
                throw new FailedToJoinMatch("Failed to join match " + message.getSavedMatchID() + " due to already started game");
            }

            ArrayList<Integer> matchFiles = this.listSavedMatches();

            // Loads the game
            if (matchFiles.contains(message.getSavedMatchID())) {
                MatchInfo matchInfo = this.loadMatch(message.getSavedMatchID());

                if (matchInfo == null) { //loadMatch will return false if it can't find the file
                    logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + message.getSavedMatchID() + " due to not found saved game");
                    throw new FailedToJoinMatch("Failed to join match " + message.getSavedMatchID() + " due to not found saved game");
                }

                lobbyManager = new MatchManager(matchInfo);
                games.add(lobbyManager); //add the MatchManager to games
                executor.submit(lobbyManager::loadAndWaitSavedMatch); // submit the method loadAndWaitSavedMatch() of MatchManager to the executor
            } else {
                logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + message.getSavedMatchID() + " due to not found saved game");
                throw new FailedToJoinMatch("Failed to join match " + message.getSavedMatchID() + " due to not found saved game");
            }

        }


        this.joinNotNewMatch(connection, message, lobbyManager);
    }

    /**
     * Tries to add a new player to a match that is not in Waiting state.
     *
     * @param connection   connection with the client.
     * @param msg          serverOptionMessage obtained from the client.
     * @param lobbyManager ClientManager of the game.
     * @throws FailedToJoinMatch if the player cannot join the game.
     */
    private void joinNotNewMatch(ClientConnection connection, serverOptionMessage msg, MatchManager lobbyManager) throws FailedToJoinMatch {
        // Obtains the offline players nicknames
        ArrayList<String> offlineNames;

        offlineNames = lobbyManager.getOfflinePlayerInfo().stream()
                .map(playerInfo -> playerInfo.getPlayer().getNickname().toLowerCase())
                .collect(Collectors.toCollection(ArrayList::new));

        //throws exception if there are no offline players or the number of expected players is not bigger than the number of online players
        if (offlineNames.isEmpty() || lobbyManager.getOnlinePlayerInfo().size() >= lobbyManager.getMatchInfo().getExpectedPlayers()) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + " due to full lobby");
            throw new FailedToJoinMatch("Failed to join match " + msg.getMatchID() + " due to full lobby");
        }

        // Sends status information
        currentStateMessage currState = new currentStateMessage(null, null, "ConnectionFAState", false, null, null, lobbyManager.getMatchInfo().getID());
        connection.sendCurrentState(currState); //send the currentStateMessage to the client

        // Obtains the name of the player
        String name = "";
        boolean correctChoice = false;
        while (!correctChoice) {

            // Asks for the new player name
            Future<String> nameFuture = executor.submit(() -> connection.getName(offlineNames).getName().toLowerCase());

            try {
                name = nameFuture.get(this.timeoutSeconds, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to no name received");
                return;
            } catch (InterruptedException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to INTERRUPT");
                return;
            } catch (ExecutionException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to EXECUTION ERROR: \n" + e.getCause().getMessage());
                return;
            }

            if (name == null) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to no name received");
                return;
            }

            // Checks if the name is correct
            if (offlineNames.contains(name)) {
                connection.sendAnswer(true);
                correctChoice = true;
            } else {
                connection.sendAnswer(false);
            }
        }

        // Tries to add the player to the game
        try {
            lobbyManager.wakeUpPlayer(name, connection);
        } catch (FailedToJoinMatch e) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + msg.getMatchID() + " due to full lobby");
            throw new FailedToJoinMatch("Failed to join match " + msg.getMatchID() + " due to full lobby");
        }
    }

    /**
     * Tries to add a new player to the waiting list of a match.
     * Asks for the player name and color to the client.
     *
     * @param lobbyManager ClientManager of the game.
     * @param connection   connection with the client.
     * @throws FailedToJoinMatch if the player cannot join the game.
     */
    private void welcomeNewPlayer(MatchManager lobbyManager, ClientConnection connection) throws FailedToJoinMatch {
        // Sends status information
        currentStateMessage currState = new currentStateMessage(null, null, "connectionState", false, null, null, lobbyManager.getMatchInfo().getID());
        connection.sendCurrentState(currState);
        logCreator.log("connectionState sent to" + connection.getIP() + " " + connection.getPort());

        // Obtains unavailable names
        ArrayList<String> unavailableNames;
        synchronized (lobbyManager) {
            unavailableNames = lobbyManager.getOnlinePlayerInfo().stream()
                    .map(playerInfo -> playerInfo.getPlayer().getNickname().toLowerCase())
                    .collect(Collectors.toCollection(ArrayList::new));


            if (lobbyManager.getMatchInfo().getExpectedPlayers() != null && unavailableNames.size() >= lobbyManager.getMatchInfo().getExpectedPlayers()) {
                logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
                throw new FailedToJoinMatch("Failed to join match " + lobbyManager.getMatchInfo().getID() + " due to full lobby");
            }
        }
        String name = "";
        boolean correctChoice = false;
        while (!correctChoice) {
            // Asks for the new player name
            Future<String> nameFuture = executor.submit(() -> connection.getName(unavailableNames).getName());


            try {
                name = nameFuture.get(this.timeoutSeconds, TimeUnit.SECONDS);

                if (name == null) {
                    connection.closeConnection();
                    logCreator.log("Client " + connection.getIP() + " kicked due to no name received");
                    return;
                }

                if (unavailableNames.contains(name.toLowerCase()))
                    connection.sendAnswer(false);
                else {
                    connection.sendAnswer(true);
                    correctChoice = true;
                }
            } catch (TimeoutException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to no name received");
                return;
            } catch (InterruptedException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to INTERRUPT");
                return;
            } catch (ExecutionException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to EXECUTION ERROR: \n" + e.getCause().getMessage());
                return;
            }

        }


        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " chose name: " + name);

        // Obtains unavailable colors
        ArrayList<String> unavailableColors;
        synchronized (lobbyManager) {
            unavailableColors = lobbyManager.getOnlinePlayerInfo().stream()
                    .map(playerInfo -> playerInfo.getPlayer().getColor().toLowerCase())
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (unavailableColors.size() >= 4) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
            throw new FailedToJoinMatch("Failed to join match " + lobbyManager.getMatchInfo().getID() + " due to full lobby");
        }

        ArrayList<String> availableColors = new ArrayList<>();
        if (!unavailableColors.contains("blue"))
            availableColors.add("blue");
        if (!unavailableColors.contains("red"))
            availableColors.add("red");
        if (!unavailableColors.contains("green"))
            availableColors.add("green");
        if (!unavailableColors.contains("purple"))
            availableColors.add("purple");

        correctChoice = false;
        String color = "";
        while (!correctChoice) {
            // Asks for the player color
            Future<String> colorFuture = executor.submit(() -> connection.getColor(availableColors).getColor().toLowerCase());

            try {
                color = colorFuture.get(this.timeoutSeconds, TimeUnit.SECONDS);

                if (color == null) {
                    connection.closeConnection();
                    logCreator.log("Client " + connection.getIP() + " kicked due to no color received");
                    return;
                }

                if (availableColors.contains(color)) {
                    connection.sendAnswer(true);
                    correctChoice = true;
                } else {
                    connection.sendAnswer(false);
                }
            } catch (TimeoutException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to no color received");
                return;
            } catch (InterruptedException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to INTERRUPT");
                return;
            } catch (ExecutionException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to EXECUTION ERROR: \n" + e.getCause().getMessage());
                return;

            }
        }

        logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " chose color: " + color);

        // Adds the new player to the waiting list if the clientManager in not full
        try {
            Player player = new Player(name, color, lobbyManager.getMatchInfo().getMatch().getCommonArea());
            PlayerInfo playerInfo = new PlayerInfo(player, State.WaitingForPlayers, connection);
            lobbyManager.addPlayerInfo(playerInfo);
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " joined match " + lobbyManager.getMatchInfo().getID());
        } catch (Exception e) {
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
            throw new FailedToJoinMatch("Failed to join match " + lobbyManager.getMatchInfo().getID() + " due to full lobby");
        }
    }


    /**
     * Closes the match when the MatchManager is not running anymore.
     */
    private void closeMatch() {
        while (this.serverRunning || !this.games.isEmpty()) {
            games.removeIf(matchManager -> matchManager.getMatchInfo().getStatus() == MatchState.KickingPlayers);
        }
    }

    /**
     * Creates a new match ID avoiding duplicates.
     *
     * @return new match ID.
     */
    private Integer createNewMatchID() {
        int MatchID = 0;
        boolean correct = false;
        ArrayList<Integer> matchFiles = this.listSavedMatches();
        while (!correct || MatchID == 1000 || matchFiles.contains(MatchID)) {
            MatchID = (new Random()).nextInt(100000);
            Integer finalMatchID = MatchID;
            if (games.stream()
                    .map(matchManager -> matchManager.getMatchInfo().getID())
                    .noneMatch(a -> Objects.equals(a, finalMatchID)))
                correct = true;  //the random ID generated is not equal to any ID of the saved matches and to any ID of the matches contained in games attribute
        }
        return MatchID;
    }

    /**
     * Lists the saved matches.
     *
     * @return an ArrayList of the saved matches.
     */
    private ArrayList<Integer> listSavedMatches() {
        File directory = new File(defaultPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".match")); //obtains the files contained in the directory that ends with ".match"
        ArrayList<Integer> matchFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                try (FileInputStream fileIn = new FileInputStream(file);
                     ObjectInputStream in = new ObjectInputStream(fileIn)) {
                     MatchInfo matchInfo = (MatchInfo) in.readObject();
                     if (matchInfo.getStatus() != MatchState.Endgame && matchInfo.getStatus() != MatchState.KickingPlayers && matchInfo.getStatus() != MatchState.Waiting)
                         matchFiles.add(Integer.parseInt(file.getName().substring(6, file.getName().length() - 6))); //extract a substring from index 6 to length - 6
                } catch (IOException | ClassNotFoundException e) {
                    logCreator.log("Error reading match " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        return matchFiles;
    }

    /**
     * Loads a match from the saved games.
     *
     * @param matchID ID of the match to load.
     * @return MatchInfo of the loaded match.
     */
    private MatchInfo loadMatch(Integer matchID) {
        String filename = this.defaultPath + "/match_" + matchID + ".match"; //path of the file to read from
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (MatchInfo) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logCreator.log("Error loading match " + matchID + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Closes the server.
     */
    protected void closeServer() {
        this.serverRunning = false;
        try {
            executor.shutdown();
            this.serverSocket.close();
            this.registry.unbind("MainServer");
            UnicastRemoteObject.unexportObject(this.server, true);
            UnicastRemoteObject.unexportObject(this.registry, true);
            this.server = null;
            this.registry = null;
        } catch (Exception e) {
            logCreator.log("Error closing server: " + e.getMessage());
        }
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

//        Scanner scanner = new Scanner(System.in);
//        while (this.serverRunning) {
//            System.out.println("\nStop the server? (yes/no)");
//            String input = scanner.nextLine();
//            if (input.equals("yes") || input.equals("y")) {
//                this.serverRunning = false;
//            }
//        }
//        executor.shutdown();
//        this.closeServer();
//        logCreator.log("Server stopped");
//        logCreator.close();
    }
}
