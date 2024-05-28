package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;

import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private static final String RESET = "\033[0m";  // Reset to default color
    private static final String RED_TEXT = "\033[0;31m";
    private static final String GREEN_TEXT = "\033[0;32m";
    private Server server;
    private ThreadPoolExecutor executor;
    private Integer matchID;
    private final Object lock = new Object();
    private boolean isSocket;

    @BeforeEach
    void setUp() {
        isSocket = true;
        server = new Server();
        int corePoolSize = 15;
        int maximumPoolSize = 100;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
        executor.submit(server);
    }

    @Test
    @DisplayName("Simulation normal server run")
    void run() throws ExecutionException, InterruptedException {
        Future<Boolean> socketResult = executor.submit(this::clientSocket);
        Future<Boolean> rmiResult =  executor.submit(this::clientRMI);

        assertTrue(socketResult.get());
        assertTrue(rmiResult.get());

        executor.shutdown();
    }

    /**
     * Method {@code clientSocket}: simulates a clientSocket connection to the server
     * @return true if the clientSocket passed all the tests
     */
    private boolean clientSocket() {
        ControllerSocket controller = new ControllerSocket("localhost", "1024");
        synchronized (lock) {
            controller.connectToServer("localhost", "1024");
            controller.answerConnection();

            // ServerOptionState
            controller.getCurrent();
            serverOptionMessage serverOption = controller.serverOptions();
            controller.sendOptions(new serverOptionMessage(true, null, null, false, null));
            assertTrue(controller.correctAnswer().getCorrect());

            // ConnectionState (name and color)
            matchID =  controller.getCurrent().getMatchID();
            controller.getUnavailableName();
            controller.chooseName("ALFA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("ClientSocket: Name chosen");
            controller.getAvailableColor();
            controller.chooseColor("RED");
            System.out.println("ClientSocket: Color chosen");
            assertTrue(controller.correctAnswer().getCorrect());


            // WaitingState: choice of the number of players
            controller.getCurrent();
            controller.newHost();
            controller.expectedPlayers(2,false);
            assertTrue(controller.correctAnswer().getCorrect());

            // Wait for clientRMI to join
            isSocket = false;
            lock.notifyAll();
        }
        System.out.println("matchID: " + matchID);
        System.out.println("ClientSocket: Waiting for clientRMI to join...");
        assertTrue(clientSimulator(15, controller, "\033[0;31mClientSocket\033[0m", true));
        System.out.println("\033[0;31mClientSocket\033[0m disconnected");

        System.out.println(RED_TEXT + "ClientSocket" + RESET + ": Waiting for the other client to quit...");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException ignore) {}

        System.out.println(RED_TEXT + "ClientSocket" + RESET + ": Tries to reload the match...");

        controller = new ControllerSocket("localhost", "1024");
        synchronized (lock) {
            controller.connectToServer("localhost", "1024");
            controller.answerConnection();

            // ServerOptionState
            controller.getCurrent();
            while (true) {
                serverOptionMessage serverOption = controller.serverOptions();
                controller.sendOptions(new serverOptionMessage(false, null,null, true, matchID));
                if (controller.correctAnswer().getCorrect())
                    break;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }


            // ConnectionState (name and color)
            controller.getCurrent();
            controller.getUnavailableName();
            controller.chooseName("ALFA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("ClientSocket: Name chosen again");

            lock.notifyAll();
        }

        System.out.println(RED_TEXT + "ClientSocket" + RESET + ": Waiting for the other client to rejoin...");
        assertTrue(clientSimulator(8, controller, "\033[0;31mClientSocket\033[0m", false));
        System.out.println(RED_TEXT + "ClientSocket" + RESET + ": Disconnected");


        System.out.println("\033[0;31mClientSocket\033[0m tries to rejoin...");

        controller = new ControllerSocket("localhost", "1024");
        synchronized (lock) {
            controller.connectToServer("localhost", "1024");
            controller.answerConnection();

            // ServerOptionState
            controller.getCurrent();
            while (true) {
                serverOptionMessage serverOption = controller.serverOptions();
                controller.sendOptions(new serverOptionMessage(false, null, matchID, false, null));
                if(controller.correctAnswer().getCorrect())
                    break;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {}
            }

            // ConnectionState (name and color)
            controller.getCurrent();
            controller.getUnavailableName();
            controller.chooseName("ALFA");
            assertTrue(controller.correctAnswer().getCorrect());
            System.out.println("ClientSocket: Name chosen again");

            System.out.println(RED_TEXT + "ClientSocket" + RESET + ": Rejoining...");
            assertTrue(clientSimulator(8, controller, "\033[0;31mClientSocket\033[0m", false));
            System.out.println(RED_TEXT + "ClientSocket" + RESET + ": Disconnected");

            lock.notifyAll();
        }

        System.out.println("\033[0;31mClientSocket\033[0m: Rejoined the match");
        assertTrue(clientSimulator(5, controller, "\033[0;31mClientSocket\033[0m", false));
        controller = new ControllerSocket("localhost", "1024");
        System.out.println("\033[0;31mClientSocket\033[0m disconnected");
        System.out.println("Please wait the timeout to end the match...");
        return true;
    }

    /**
     * Method {@code clientRMI}: simulates a clientRMI connection to the server
     * @return true if the clientRMI passed all the tests
     */
    private boolean clientRMI() {
        ControllerRMI controller = new ControllerRMI("localhost", "1099");
        synchronized (lock) {
            while(isSocket) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                controller.connectToServer("localhost", "1099");
                controller.answerConnection();


                // ServerOptionState
                controller.getCurrent();
                serverOptionMessage serverOption = controller.serverOptions();
                controller.sendOptions(new serverOptionMessage(true, serverOption.getWaitingMatches().getFirst(), null, false, null));
                assertTrue(controller.correctAnswer().getCorrect());

                // ConnectionState (name and color with wrong choices)
                controller.getCurrent();
                controller.getUnavailableName();
                controller.chooseName("ALFA");
                assertFalse(controller.correctAnswer().getCorrect());
                controller.getUnavailableName();
                controller.chooseName("BETA");
                assertTrue(controller.correctAnswer().getCorrect());
                System.out.println("ClientRMI: Name chosen");
                controller.getAvailableColor();
                controller.chooseColor("RED");
                assertFalse(controller.correctAnswer().getCorrect());
                controller.getAvailableColor();
                controller.chooseColor("YELLOW");
                assertTrue(controller.correctAnswer().getCorrect());
                System.out.println("ClientRMI: Color chosen");
                isSocket = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("ClientRMI: Waiting for clientSocket to join...");
        assertTrue(clientSimulator(16, controller, "\033[0;32mClientRMI\033[0m", true));
        System.out.println(GREEN_TEXT + "ClientRMI" + RESET + ": Disconnected");

        System.out.println(GREEN_TEXT + "ClientRMI" + RESET + ": Waiting for the other client to reload the match...");


        controller = new ControllerRMI("localhost", "1099");
        controller.connectToServer("localhost", "1099");
        controller.answerConnection();

        // ServerOptionState
        while (true) {
            serverOptionMessage serverOption = controller.serverOptions();
            controller.sendOptions(new serverOptionMessage(false, matchID, null, false, null));
            if (controller.correctAnswer().getCorrect())
                break;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }

        // ConnectionState (name and color)
        controller.getCurrent();
        controller.getUnavailableName();
        controller.chooseName("BETA");
        assertTrue(controller.correctAnswer().getCorrect());
        System.out.println("ClientRMI: Name chosen again");

        System.out.println(GREEN_TEXT + "ClientRMI" + RESET + ": Waiting for the other client to rejoin...");
        assertTrue(clientSimulator(40, controller, "\033[0;32mClientRMI\033[0m", false));
        System.out.println(GREEN_TEXT + "ClientRMI" + RESET + ": Disconnected");

        return true;
    }

    /**
     * Method {@code clientSimulator}: simulates a client connection to the server
     * @param numberOfTurns number of turns to simulate or 0 if the simulation should continue indefinitely
     * @param controller the controller to use for the simulation
     */
    private boolean clientSimulator(int numberOfTurns, Controller controller, String name, boolean synchDisconnect) {
        Integer remainingTurns = numberOfTurns;
        Integer counter = 1;
        if(numberOfTurns == 0)
            remainingTurns = null;
        try {
            while (remainingTurns == null || remainingTurns > 0) {


                currentStateMessage current = controller.getCurrent();
                String state = current.getStateName();

                // To force the remaining client when the other has already disconnected
                if(counter > 5 && synchDisconnect && (current.getOnlinePlayers() == null || current.getOnlinePlayers().size() < 2)) {
                   return true;
                }
                // REMOVE THIS
                if(!Objects.equals(state, "AnswerCheckConnection") && (current.getCurrentPlayer() == null || Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())))
                    System.out.println( counter + " - " + name + " State: " + state);

                switch (state) {
                    case "StarterCardState": {
                        // Pick a starter card
                        if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                            starter(controller);
                        controller.updatePlayer();
                        break;
                    }
                    case "ObjectiveState": {
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
                    case "EndGameState": {
                        // End of the game;
                        declareWinnerMessage end = controller.endGame();
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
        controller.placeCard(0, 0, 0,0, false);
        assertFalse(controller.correctAnswer().getCorrect());

        // Random choice
        while(true) {
            ArrayList<Integer[]> availableCells = current.getCurrentPlayer().getPlayerArea().getAvailablePosition();
            int pick = new Random().nextInt(availableCells.size());
            int front = new Random().nextInt(2) + 1;
            Integer[] cell = availableCells.get(pick);
            controller.placeCard(0, front, cell[0], cell[1], false);
            if(controller.correctAnswer().getCorrect())
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






