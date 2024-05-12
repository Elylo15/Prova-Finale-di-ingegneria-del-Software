package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.server.FSM.MatchState;
import it.polimi.ingsw.protocol.server.FSM.State;
import it.polimi.ingsw.protocol.server.RMI.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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


    private void handleConnection(ClientConnection connection) {
        if(connection == null)
            return;

        // Confirms the connection to the client
        connection.sendAnswerToConnection(new connectionResponseMessage(true));

        logCreator.log("New client socket accepted, answer sent: " + connection.getIP() + " " + connection.getPort());


        // Sends status: "ServerOptionState"

        connection.sendCurrentState(new currentStateMessage(null, null, "ServerOptionState", false, null));

        boolean correctResponse = false;

        Future<serverOptionMessage> serverOption;
        serverOptionMessage msg = null;

        while(!correctResponse) {
            ArrayList<Integer> runningGames = games.stream()
                    .filter(game -> game.getMatchInfo().getStatus() != MatchState.Waiting && game.getMatchInfo().getStatus() != MatchState.Waiting)
                    .map(game -> game.getMatchInfo().getID())
                    .collect(Collectors.toCollection(ArrayList::new));

            // TODO Collect all saved matches
            ArrayList<Integer> savedMatches = null;

            // Requests the ServerOptionMessage
            serverOption = executor.submit(() -> connection.getServerOption(runningGames, savedMatches));

            // Timer
//            Timer timer = new Timer();
//            Future<serverOptionMessage> finalServerOption = serverOption;
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    logCreator.log("Timer serverOption executed");
//                    if(!finalServerOption.isDone()) {
//                        finalServerOption.cancel(true);
//                    }
//                }
//            };
//
//            timer.schedule(task, 3*60*1000);

            // If client loses connection, then this method ends
            try {
                msg = serverOption.get(this.timeoutSeconds, TimeUnit.SECONDS);
//                timer.cancel();
            } catch (TimeoutException e) {
                connection.closeConnection();
                logCreator.log("Client " + connection.getIP() + " kicked due to no serverOption received");
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

//            // TODO remove this
//            System.out.println(msg.toString());

            // Checks if the response message is valid
            if(!msg.isNewMatch() && (msg.getNickname() == null || Objects.equals(msg.getNickname(), "") || msg.getStartedMatchID() == null) && !msg.isLoadMatch()) {
                connection.sendAnswer(false);
                logCreator.log("Client " + connection.getIP() + " has given wrong ServerOption");
            }
            else
                correctResponse = true;
        }

        connection.sendAnswer(true);

        logCreator.log("Server option fetched from client" + connection.getIP() + " " + connection.getPort());


        if(msg.isNewMatch()) {
            logCreator.log("Client " + connection.getIP() +":" + connection.getPort() + " wants to join a new game");
            this.newMatch(connection);
        } else if (msg.getStartedMatchID() != null && msg.getNickname() != null ) {
            logCreator.log("Client " + connection.getIP() +":" + connection.getPort() + " wants to join an already started game");
            this.joinStartedMatch(connection,msg);
        } else {
            logCreator.log("Client " + connection.getIP() +":" + connection.getPort() + " wants to load a game");
            this.loadSavedMatch(connection);
        }
    }

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
            return;
        }

    }


    private void newMatch(ClientConnection connection) {
        ClientManager lobby;

        // Player wants to join a new game
        lobby = games.stream()
                .filter(clientManager -> clientManager.getMatchInfo().getStatus() == MatchState.Waiting)
                .filter(clientManager -> clientManager.getMatchInfo().getExpectedPlayers() == null || clientManager.getMatchInfo().getExpectedPlayers() > clientManager.getPlayersInfo().size())
                .findFirst().orElse(null);

        if (lobby != null) {

            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " joins a match in WaitingForPlayerState");

            this.welcomeNewPlayer(lobby, connection);

            /*
             The managing of the "host" (first player who decides the expected players number)
             is implemented in ClientManager.
            */

        } else {
            Integer id = this.createNewMatchID();
            MatchInfo matchInfo = new MatchInfo(new Match(),id,this.defaultPath + id.toString() + ".savedgame" ,null, MatchState.Waiting);

            ClientManager lobbyManager = new ClientManager(matchInfo);
            games.add(lobbyManager);
            executor.submit(lobbyManager);

            logCreator.log("Client socket " + connection.getIP() + " " + connection.getPort() + " starts a new ClientManager");

            this.welcomeNewPlayer(lobbyManager, connection);


        }
    }

    private void joinStartedMatch(ClientConnection connection, serverOptionMessage msg) {

        // TODO finish this part
        // Player wants to join an already started game


        // TODO add status information

        // Finds the game
        ClientManager lobbyManager = games.stream()
                .filter(clientManager -> Objects.equals(clientManager.getMatchInfo().getID(), msg.getStartedMatchID()))
                .limit(1)
                .findAny().orElse(null);

        // Game not found
        if(lobbyManager == null) {
            connection.closeConnection();
            return;
        }
        synchronized (lobbyManager) {

            // Is lobby full?
            if (lobbyManager.getMatchInfo().getExpectedPlayers() <= lobbyManager.getPlayersInfo().size() || lobbyManager.getMatchInfo().getStatus() == MatchState.Waiting) {
                connection.closeConnection();
                return;
            }


            // Checks if nickname is correct and if it is available
            ArrayList<String> onlinePlayers = (ArrayList<String>) lobbyManager.getPlayersInfo().stream()
                    .map(playerInfo -> playerInfo.getPlayer().getNickname())
                    .toList();

            if(!lobbyManager.getMatch().getPlayers().contains(msg.getNickname()) || onlinePlayers.contains(msg.getNickname())) {
                connection.closeConnection();
                return;
            }


            // Add the player to the game
            serverOptionMessage finalMsg1 = msg;
            Player savedPlayer = lobbyManager.getMatch().getPlayers().stream()
                    .filter(player -> Objects.equals(player.getNickname(), finalMsg1.getNickname()))
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
                    connection.closeConnection();
                    return;
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
                    connection.closeConnection();
                    return;
                }
            } else {
                connection.closeConnection();
                return;
            }
        }

    }

    private void loadSavedMatch(ClientConnection connection) {
        // Player wants to load a saved game
        // TODO implement loading of a game
        // maybe start from here the custom loaded game and not from run
        // TODO use synchronized and if it falis kickThePlayer



    }

    private void welcomeNewPlayer(ClientManager lobbyManager, ClientConnection connection) {
        // Sends status information
        currentStateMessage currState = new currentStateMessage(null,null,"ConnectionState", false, null);
        connection.sendCurrentState(currState);
        logCreator.log("ConnectionState sent to" + connection.getIP() + " " + connection.getPort());

        // Obtains unavailable names
        ArrayList<String> unavailableNames = lobbyManager.getPlayersInfo().stream()
                .map(playerInfo -> playerInfo.getPlayer().getNickname().toLowerCase())
                .collect(Collectors.toCollection(ArrayList::new));

        if(lobbyManager.getMatchInfo().getExpectedPlayers() != null && unavailableNames.size() >= lobbyManager.getMatchInfo().getExpectedPlayers())
            connection.closeConnection();

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

        if(unavailableColors.size() >= 4)
            connection.closeConnection();

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
            connection.closeConnection();
            logCreator.log("Client socket " + connection.getIP() + " " + connection.getPort() + " closed connection");
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
            connection.closeConnection();
            logCreator.log("Client " + connection.getIP() + " " + connection.getPort() + " failed to join match " + lobbyManager.getMatchInfo().getID());
        }


    }




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

    private void loadSavedMatch(String fileName) {
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

//        Scanner scanner = new Scanner(System.in);
//        String answer;
//        while(this.serverRunning) {
//            System.out.println("Insert 'stop' to end the server: ");
//            answer = scanner.nextLine();
//            if(answer.equals("stop")) {
//                this.serverRunning = false;
//                System.out.println("Server stopped");
//            }
//        }
    }
}
