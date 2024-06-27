package it.polimi.ingsw.server;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.ControllerRMI;
import it.polimi.ingsw.client.controller.ControllerSocket;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTest {

    private static final String RESET = "\033[0m";  // Reset to default color
    private static final String RED_TEXT = "\033[0;31m";
    private static final String GREEN_TEXT = "\033[0;32m";
    private static final String PURPLE_TEXT = "\033[0;35m";
    private static final String BLUE_TEXT = "\033[0;34m";
    private final Object lock_1 = new Object();
    private final Object lock_2 = new Object();
    private Server server;
    private ThreadPoolExecutor executor;
    private Integer matchID_1;
    private Integer matchID_2;
    private boolean setup_Match_1;
    private boolean setup_Match_2;
    private boolean timeToDisconnect;

    @BeforeEach
    void setUp() {
        // Clean up eventual previous registry
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            registry.list();
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (Exception ignore) {
        }

        setup_Match_1 = true;
        setup_Match_2 = true;
        timeToDisconnect = false;
        server = new Server();
        int corePoolSize = 15;
        int maximumPoolSize = 100;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());
        executor.submit(server);
    }

    @AfterEach
    void tearDown() {
        server.closeServer();
        executor.shutdown();
    }

    @Test
    @DisplayName("Simulation normal server run")
    void runTest() throws ExecutionException, InterruptedException {
        Future<Boolean> socketResult = executor.submit(this::client_1);
        Future<Boolean> rmiResult = executor.submit(this::client_2);
        Future<Boolean> socketResult2 = executor.submit(this::client_4);
        Future<Boolean> rmiResult2 = executor.submit(this::client_3);

        assertTrue(socketResult.get());
        assertTrue(rmiResult.get());
        assertTrue(socketResult2.get());
        assertTrue(rmiResult2.get());
    }

    /**
     * Method {@code client_1}: simulates a client_1 connection to the server
     *
     * @return true if the client_1 passed all the tests
     */
    private boolean client_1() {
        // Connection 1
        Controller controller = new ControllerSocket("localhost", "1024");
        synchronized (lock_1) {
            controller.connectToServer("localhost", "1024");
            controller.answerConnection();

            // serverOptionState
            controller.getCurrent();
            serverOptionMessage serverOption = controller.serverOptions();
            controller.sendOptions(new serverOptionMessage(true, null, null, false, null));
            assertTrue(controller.correctAnswer().getCorrect());

            // connectionState (name and color)
            matchID_1 = controller.getCurrent().getMatchID();
            controller.getUnavailableName();
            controller.chooseName("ALFA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("client_1: Name chosen");
            controller.getAvailableColor();
            controller.chooseColor("RED");
            System.out.println("client_1: Color chosen");
            assertTrue(controller.correctAnswer().getCorrect());


            // WaitingState: choice of the number of players
            controller.getCurrent();
            controller.newHost();
            controller.expectedPlayers(2, false);
            assertTrue(controller.correctAnswer().getCorrect());

            // Wait for client_2 to join
            setup_Match_1 = false;
            lock_1.notifyAll();
        }
        System.out.println("matchID_1: " + matchID_1);
        System.out.println("client_1: Waiting for client_2 to join...");
        assertTrue(clientSimulator(15, controller, RED_TEXT + "client_1" + RESET));
        System.out.println("\033[0;31mclient_1\033[0m disconnected");


        System.out.println("\033[0;31mclient_1\033[0m tries to rejoin...");

        // Switch to rmi

        // Connection 2
        controller = new ControllerRMI("localhost", "1099");
        synchronized (lock_1) {
            controller.connectToServer("localhost", "1099");
            controller.answerConnection();

            // serverOptionState
            controller.getCurrent();
            while (true) {
                serverOptionMessage serverOption = controller.serverOptions();
                controller.sendOptions(new serverOptionMessage(false, null, matchID_1, false, null));
                if (controller.correctAnswer().getCorrect())
                    break;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }

            // connectionState (name and color)
            controller.getCurrent();
            controller.getUnavailableName();
            controller.chooseName("ALFA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("client_1: Name chosen again");

            lock_1.notifyAll();
        }

        System.out.println(RED_TEXT + "client_1" + RESET + ": Rejoining...");
        assertTrue(clientSimulator(8, controller, RED_TEXT + "client_1" + RESET));
        System.out.println(RED_TEXT + "client_1" + RESET + ": Disconnected");
        System.out.println(RED_TEXT + "Please wait for the timeout to end the match..." + RESET);
        return true;
    }

    /**
     * Method {@code client_2}: simulates a client_2 connection to the server
     *
     * @return true if the client_2 passed all the tests
     */
    private boolean client_2() {
        // Connection 1
        Controller controller = new ControllerRMI("localhost", "1099");
        synchronized (lock_1) {
            while (setup_Match_1) {
                try {
                    lock_1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                controller.connectToServer("localhost", "1099");
                controller.answerConnection();


                // serverOptionState
                controller.getCurrent();
                serverOptionMessage serverOption = controller.serverOptions();
                controller.sendOptions(new serverOptionMessage(true, serverOption.getWaitingMatches().getFirst(), null, false, null));
                assertTrue(controller.correctAnswer().getCorrect());

                // connectionState (name and color with wrong choices)
                controller.getCurrent();
                controller.getUnavailableName();
                controller.chooseName("ALFA");
                assertFalse(controller.correctAnswer().getCorrect());
                controller.getUnavailableName();
                controller.chooseName("BETA");
                assertTrue(controller.correctAnswer().getCorrect());
                System.out.println("client_2: Name chosen");
                controller.getAvailableColor();
                controller.chooseColor("RED");
                assertFalse(controller.correctAnswer().getCorrect());
                controller.getAvailableColor();
                controller.chooseColor("PURPLE");
                assertTrue(controller.correctAnswer().getCorrect());
                System.out.println("client_2: Color chosen");
                setup_Match_1 = true;
                lock_1.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("client_2: Joining...");
        assertTrue(clientSimulator(10, controller, GREEN_TEXT + "client_2" + RESET));
        System.out.println(GREEN_TEXT + "client_2" + RESET + ": Disconnected");

        System.out.println(GREEN_TEXT + "client_2" + RESET + " tries to rejoin...");

        // Switch to socket

        // Connection 2
        controller = new ControllerSocket("localhost", "1024");
        controller.connectToServer("localhost", "1024");
        controller.answerConnection().getCorrect();

        // serverOptionState
        controller.getCurrent();
        while (true) {
            serverOptionMessage serverOption = controller.serverOptions();
            controller.sendOptions(new serverOptionMessage(false, null, matchID_1, false, null));
            if (controller.correctAnswer().getCorrect())
                break;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }

        // connectionState (name and color)
        controller.getCurrent();
        controller.getUnavailableName();
        controller.chooseName("BETA");
        assertTrue(controller.correctAnswer().getCorrect());
        System.out.println("client_2: Name chosen again");

        System.out.println(GREEN_TEXT + "client_2" + RESET + ": Rejoining...");
        assertTrue(clientSimulator(40, controller, GREEN_TEXT + "client_2" + RESET));
        System.out.println(GREEN_TEXT + "client_2" + RESET + ": Disconnected");

        return true;
    }

    /**
     * Method {@code client_3}: simulates a client_3 connection to the server
     *
     * @return true if the client_3 passed all the tests
     */
    private boolean client_3() {
        Controller controller = new ControllerRMI("localhost", "1099");
        synchronized (lock_1) {
            while (setup_Match_1) {
                try {
                    lock_1.wait();
                } catch (InterruptedException ignore) {
                }
            }

            while (!setup_Match_1) {
                try {
                    lock_1.wait();
                } catch (InterruptedException ignore) {
                }
            }

        }
        synchronized (lock_2) {
            controller.connectToServer("localhost", "1099");
            controller.answerConnection();

            // serverOptionState
            controller.getCurrent();
            serverOptionMessage serverOption = controller.serverOptions();
            controller.sendOptions(new serverOptionMessage(true, null, null, false, null));
            assertTrue(controller.correctAnswer().getCorrect());

            // connectionState (name and color)
            controller.getCurrent();
            controller.getUnavailableName();
            controller.chooseName("GAMMA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("client_3: Name chosen");
            controller.getAvailableColor();
            controller.chooseColor("BLUE");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("client_3: Color chosen");

            // WaitingState: choice of the number of players
            matchID_2 = controller.getCurrent().getMatchID();
            controller.newHost();
            controller.expectedPlayers(2, false);
            assertTrue(controller.correctAnswer().getCorrect());

            setup_Match_2 = false;
            lock_2.notifyAll();
        }

        System.out.println("matchID_2: " + matchID_2);
        System.out.println("client_3: Waiting for client_4 to join...");
        assertTrue(clientSimulatorScheduledDisconnections(7, controller, BLUE_TEXT + "client_3" + RESET));
        timeToDisconnect = true;
        System.out.println(BLUE_TEXT + "client_3" + RESET + ": Disconnected");

        // Waiting for client_4 to leave the match
        synchronized (lock_2) {
            while (!timeToDisconnect) {
                try {
                    lock_2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Waiting 15sec to be sure that the server has closed the match
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            controller = new ControllerRMI("localhost", "1099");
            controller.connectToServer("localhost", "1099");
            controller.answerConnection();

            // serverOptionState
            controller.getCurrent();
            serverOptionMessage serverOption = controller.serverOptions();
            controller.sendOptions(new serverOptionMessage(false, null, null, true, matchID_2));
            assertTrue(controller.correctAnswer().getCorrect());

            // connectionState (name and color)
            controller.getCurrent();
            controller.getUnavailableName();
            controller.chooseName("GAMMA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("client_3: Name chosen again");


            setup_Match_2 = false;
            System.out.println(BLUE_TEXT + "client_3" + RESET + ": joined the loaded match");
            lock_2.notifyAll();
        }

        assertTrue(clientSimulator(10, controller, BLUE_TEXT + "client_3" + RESET));
        System.out.println(BLUE_TEXT + "client_3" + RESET + ": Disconnected");
        System.out.println(BLUE_TEXT + "Please wait for the timeout to end the match..." + RESET);

        return true;
    }

    /**
     * Method {@code client_4}: simulates a client_4 connection to the server
     *
     * @return true if the client_4 passed all the tests
     */
    private boolean client_4() {
        Controller controller = new ControllerSocket("localhost", "1024");
        synchronized (lock_2) {
            while (setup_Match_2) {
                try {
                    lock_2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                controller.connectToServer("localhost", "1024");
                controller.answerConnection();

                System.out.println("client_4: Connection established");

                // serverOptionState
                controller.getCurrent();
                serverOptionMessage serverOption = controller.serverOptions();
                controller.sendOptions(new serverOptionMessage(true, matchID_2, null, false, null));
                assertTrue(controller.correctAnswer().getCorrect());

                // connectionState (name and color)
                controller.getCurrent();
                controller.getUnavailableName();
                controller.chooseName("DELTA");
                assertTrue(controller.correctAnswer().getCorrect());
                System.out.println("client_4: Name chosen");
                controller.getAvailableColor();
                controller.chooseColor("PURPLE");
                assertTrue(controller.correctAnswer().getCorrect());
                System.out.println("client_4: Color chosen");
            }

            setup_Match_2 = true;
            lock_2.notifyAll();
        }

        System.out.println("client_4: Joining...");
        assertTrue(clientSimulatorScheduledDisconnections(20, controller, PURPLE_TEXT + "client_4" + RESET));

        synchronized (lock_2) {
            timeToDisconnect = true;
            lock_2.notifyAll();
        }

        System.out.println(PURPLE_TEXT + "client_4" + RESET + ": Disconnected after " + BLUE_TEXT + "client_3" + RESET);


        controller = new ControllerRMI("localhost", "1099");

        // Waiting for client_3 to load the match
        synchronized (lock_2) {
            while (setup_Match_2) {
                try {
                    lock_2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            controller.connectToServer("localhost", "1099");
            controller.answerConnection();

            // serverOptionState
            while (true) {

                controller.getCurrent();
                serverOptionMessage serverOption = controller.serverOptions();
                controller.sendOptions(new serverOptionMessage(false, null, matchID_2, false, null));
                if (controller.correctAnswer().getCorrect())
                    break;
            }

            // connectionState (name and color)
            controller.getCurrent();
            controller.getUnavailableName();
            controller.chooseName("DELTA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("client_4: Name chosen again");

            System.out.println(PURPLE_TEXT + "client_4" + RESET + ": Rejoining...");

            lock_2.notifyAll();
        }

        assertTrue(clientSimulator(30, controller, PURPLE_TEXT + "client_4" + RESET));
        System.out.println(PURPLE_TEXT + "client_4" + RESET + ": Disconnected");

        return true;
    }


    /**
     * Method {@code clientSimulator}: simulates a client connection to the server
     *
     * @param numberOfTurns number of turns to simulate or 0 if the simulation should continue indefinitely
     * @param controller    the controller to use for the simulation
     */
    private boolean clientSimulator(int numberOfTurns, Controller controller, String name) {
        Integer remainingTurns = numberOfTurns;
        Integer counter = 1;
        if (numberOfTurns == 0)
            remainingTurns = null;
        try {
            while (remainingTurns == null || remainingTurns > 0) {


                currentStateMessage current = controller.getCurrent();
                String state = current.getStateName();

                // REMOVE THIS
                if (!Objects.equals(state, "AnswerCheckConnection") && (current.getCurrentPlayer() == null || Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())))
                    System.out.println(String.format("%2d", counter) + " - " + name + " State: " + state);

                switch (state) {
                    case "StarterCardState": {
                        // Pick a starter card
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            starter(controller);
                        controller.updatePlayer();
                        break;
                    }
                    case "objectiveState": {
                        // Pick an objective
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            pickObjective(controller);
                        controller.updatePlayer();
                        break;
                    }
                    case "PlaceTurnState": {
                        // Place a card
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            placeCard(controller, current);
                        controller.updatePlayer();
                        break;
                    }
                    case "PickTurnState": {
                        // Pick a card
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            pickCard(controller);
                        controller.updatePlayer();
                        break;
                    }
                    case "endGameState": {
                        // End of the game;
                        declareWinnerMessage end = controller.endGame();

                        //ViewCLI view = new ViewCLI();
                        //view.endGame(end);

                        System.out.println(name + " wins the game");
                        return true;
                    }

                    case "AnswerCheckConnection": {
                        // Answer to the ping
                        controller.sendAnswerToPing();
                        break;
                    }
                }

                if (remainingTurns != null && !state.equals("AnswerCheckConnection") && Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) {
                    remainingTurns--;
                    counter++;

                }


            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }


        return true;
    }


    /**
     * Method {@code clientSimulatorScheduledDisconnections}: simulates a client connection to the server.
     * When the first client disconnects, the second client will try to disconnect too.
     *
     * @param numberOfTurns number of turns to simulate or 0 if the simulation should continue indefinitely
     * @param controller    the controller to use for the simulation
     */
    private boolean clientSimulatorScheduledDisconnections(int numberOfTurns, Controller controller, String name) {
        Integer remainingTurns = numberOfTurns;
        Integer counter = 1;
        if (numberOfTurns == 0)
            remainingTurns = null;
        try {
            while (remainingTurns == null || remainingTurns > 0) {


                currentStateMessage current = controller.getCurrent();
                String state = current.getStateName();

                if (timeToDisconnect) {
                    return true;
                }

                // REMOVE THIS
                if (!Objects.equals(state, "AnswerCheckConnection") && (current.getCurrentPlayer() == null || Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())))
                    System.out.println(String.format("%2d", counter) + " - " + name + " State: " + state);

                switch (state) {
                    case "StarterCardState": {
                        // Pick a starter card
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            starter(controller);
                        controller.updatePlayer();
                        break;
                    }
                    case "objectiveState": {
                        // Pick an objective
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            pickObjective(controller);
                        controller.updatePlayer();
                        break;
                    }
                    case "PlaceTurnState": {
                        // Place a card
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            placeCard(controller, current);
                        controller.updatePlayer();
                        break;
                    }
                    case "PickTurnState": {
                        // Pick a card
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            pickCard(controller);
                        controller.updatePlayer();
                        break;
                    }
                    case "endGameState": {
                        // End of the game;
                        declareWinnerMessage end = controller.endGame();
                        System.out.println(name + " wins the game");
                        return true;
                    }

                    case "AnswerCheckConnection": {
                        // Answer to the ping
                        controller.sendAnswerToPing();
                        break;
                    }
                }

                if (remainingTurns != null && !state.equals("AnswerCheckConnection") && Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) {
                    remainingTurns--;
                    counter++;

                }


            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Method {@code starter}: simulates the choice of the starter card
     */
    public void starter(Controller controller) {
        Integer side;
        boolean noResponse = false;
        // Wrong choice
        side = 2;
        controller.placeStarter(side, false);
        assertFalse(controller.correctAnswer().getCorrect());
        // Correct choice
        side = 0;
        controller.placeStarter(side, false);
        assertTrue(controller.correctAnswer().getCorrect());
    }

    /**
     * Method {@code pickObjective}: simulates the choice of the objective
     */
    public void pickObjective(Controller controller) {
        // Wrong choice
        controller.getObjectiveCards().getObjectiveCard();
        Integer pick = 0;
        controller.chooseObjective(pick, false);
        assertFalse(controller.correctAnswer().getCorrect());
        // Correct choice
        controller.getObjectiveCards().getObjectiveCard();
        pick = 1;
        controller.chooseObjective(pick, false);
        assertTrue(controller.correctAnswer().getCorrect());

    }

    /**
     * Method {@code placeCard}: simulates the placement of a card
     */
    public void placeCard(Controller controller, currentStateMessage current) {
        // Wrong choice
        controller.placeCard(0, 0, 0, 0, false);
        assertFalse(controller.correctAnswer().getCorrect());

        // Random choice
        while (true) {
            ArrayList<Integer[]> availableCells = current.getCurrentPlayer().getPlayerArea().getAvailablePosition();
            int pick = new Random().nextInt(availableCells.size());
            int front = new Random().nextInt(2) + 1;
            Integer[] cell = availableCells.get(pick);
            controller.placeCard(0, front, cell[0], cell[1], false);
            if (controller.correctAnswer().getCorrect())
                break;
        }
    }

    /**
     * Method {@code pickCard}: simulates the choice of a card
     */
    public void pickCard(Controller controller) {
        // Correct choice
        int pick = new Random().nextInt(2) + 1;
        controller.pickCard(pick, false);
        assertTrue(controller.correctAnswer().getCorrect());
    }


}






