package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.controller.*;
import it.polimi.ingsw.protocol.client.view.*;
import it.polimi.ingsw.protocol.client.view.GUI.*;
import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static it.polimi.ingsw.protocol.messages.Message.lock;

public class Client {
    private String serverIP;
    private String serverPort;
    private Controller controller;
    private final View view;
    private ThreadPoolExecutor executor;

    private MainViewController mainViewController;
    private InsertIPPortController insertIPPortController;
    private ChooseSocketRMIController chooseSocketRMIController;
    private InsertServerOptionController insertServerOptionController;
    private UnavailableNamesController unavailableNamesController;
    private AvailableColorsController availableColorsController;
    private JoinMatchController joinMatchController;
    private LoadMatchController loadMatchController;
    private JoinRunningMatchController joinRunningMatchController;
    private WaitingController waitingController;

    /**
     * method {@code Client}: constructs a new Client
     *
     * @param view: default ViewGUI
     */
    public Client(View view) {
        this.view = view;

        int corePoolSize = 5;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

    }



    //servono per poter importare i controller a ViewGUI

    public MainViewController getMainViewController() {
        return mainViewController;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public InsertIPPortController getInsertIPPortController() {
        return insertIPPortController;
    }

    public void setInsertIPPortController(InsertIPPortController insertIPPortController) {
        this.insertIPPortController = insertIPPortController;
    }

    public ChooseSocketRMIController getChooseSocketRMIController() {
        return chooseSocketRMIController;
    }

    public void setChooseSocketRMIController(ChooseSocketRMIController chooseSocketRMIController) {
        this.chooseSocketRMIController = chooseSocketRMIController;
    }

    public InsertServerOptionController getInsertServerOptionController() {
        return insertServerOptionController;
    }

    public void setInsertServerOptionController(InsertServerOptionController insertServerOptionController) {
        this.insertServerOptionController = insertServerOptionController;
    }

    public UnavailableNamesController getUnavailableNamesController() {
        return unavailableNamesController;
    }

    public void setUnavailableNamesController(UnavailableNamesController unavailableNamesController) {
        this.unavailableNamesController = unavailableNamesController;
    }

    public AvailableColorsController getAvailableColorsController() {
        return availableColorsController;
    }

    public void setAvailableColorsController(AvailableColorsController availableColorsController) {
        this.availableColorsController = availableColorsController;
    }

    public JoinMatchController getJoinMatchController() {
        return joinMatchController;
    }

    public void setJoinMatchController(JoinMatchController joinMatchController) {
        this.joinMatchController = joinMatchController;
    }

    public LoadMatchController getLoadMatchController() {
        return loadMatchController;
    }

    public void setLoadMatchController(LoadMatchController loadMatchController) {
        this.loadMatchController = loadMatchController;
    }

    public JoinRunningMatchController getJoinRunningMatchController() {
        return joinRunningMatchController;
    }

    public void setJoinRunningMatchController(JoinRunningMatchController joinRunningMatchController) {
        this.joinRunningMatchController = joinRunningMatchController;
    }

    public WaitingController getWaitingController() {
        return waitingController;
    }

    public void setWaitingController(WaitingController waitingController) {
        this.waitingController = waitingController;
    }






    public View getView() {
        return view;
    }

    /**
     * method {@code connection}: sets socket or rmi. Enables GUI or CLI.
     * Connects to a socket server or rmi server.
     */
    public void connection(boolean isSocket) {

        if (isSocket) {
            try {
                controller.connectToServer(serverIP, "1024");
                connectionResponseMessage answer = controller.answerConnection();
                view.answerToConnection(answer);
            } catch (Exception e) {
                e.printStackTrace();
                view.playerDisconnected();
                throw new RuntimeException();
            }
        } else {
            try {
                controller.connectToServer(serverIP, "1099");
                connectionResponseMessage answer = controller.answerConnection();
                view.answerToConnection(answer);
            } catch (Exception e) {
                e.printStackTrace();
                view.playerDisconnected();
                throw new RuntimeException();
            }
        }
    }

    /**
     * method {@code run}: invocations of controller methods to send and receive messages to and from the server.
     * Invocations of view methods to display and receive player's info.
     */
    public void run() {

            String[] server = view.askPortIP();
            setIP(server[0]);
            //setPort(server[1]);

            boolean isSocket = view.askSocket();
            setController(server, isSocket);
            connection(isSocket);

            try {
                while (true) {

                    // REMOVE THIS
                    System.out.println("\nWaiting for current state");


                    currentStateMessage current = controller.getCurrent();
                    String state = current.getStateName();

                    // REMOVE THIS
                    System.out.println("Current state: " + state + "\n");

                    switch (state) {
                        case "ServerOptionState": {
                            serverOptions();
                            break;
                        }
                        case "ConnectionState": {
                            name();
                            color();
                            break;
                        }
                        case "WaitingForPlayerState": {
                            waitingPlayer(current);
                            break;
                        }
                        case "StarterCardState": {
                            view.updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                starter();
                            updatePlayerMessage update = controller.updatePlayer();
                            view.update(update);
                            break;
                        }
                        case "ObjectiveState": {

                            view.updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                pickObjective();
                            updatePlayerMessage update = controller.updatePlayer();
                            view.update(update);
                            break;
                        }
                        case "PlaceTurnState": {
                            view.updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                placeCard();
                            updatePlayerMessage update = controller.updatePlayer();
                            view.update(update);
                            break;
                        }
                        case "PickTurnState": {
                            view.updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                pickCard();
                            updatePlayerMessage update = controller.updatePlayer();
                            view.update(update);
                            break;
                        }
                        case "EndGameState": {
                            declareWinnerMessage end = controller.endGame();
                            view.endGame(end);
                            throw new Exception("Game ended.");
                        }

                        case "ConnectionFAState": {
                            pickNameFA();
                            break;
                        }

                        case "AnswerCheckConnection": {
                            controller.sendAnswerToPing();

                            // REMOVE THIS
                            System.out.println("Answered to ping");

                            break;
                        }
                    }
                }
            } catch (Exception e) {
//            view.playerDisconnected();
            }
    }

    /**
     * method {@code serverOptions}: invocations of controller methods to send and receive serverOptionMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void serverOptions() {
        while (true) {
            serverOptionMessage options = controller.serverOptions();
            options = view.serverOptions(options);
            controller.sendOptions(options);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code name}: invocations of controller methods to receive unavailableNamesMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void name() {
        while (true) {
            unavailableNamesMessage unavailableName = controller.getUnavailableName();
            String name = view.unavailableNames(unavailableName);
            controller.chooseName(name);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code color}: invocations of controller methods to receive availableColorsMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void color() {
        while (true) {
            availableColorsMessage availableColor = controller.getAvailableColor();
            String color = view.availableColors(availableColor);
            controller.chooseColor(color);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }


    /**
     * method {@code color}: invocations of controller methods to receive availableColorsMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void waitingPlayer(currentStateMessage current) {
        Integer expected;
        boolean noResponse = false;

        newHostMessage newHost = controller.newHost();

        while (Objects.equals(newHost.getNewHostNickname(), current.getPlayer().getNickname())) {
            Future<Integer> future = executor.submit(view::expectedPlayers);

            try {
                expected = future.get(120, TimeUnit.SECONDS);
            } catch (Exception e) {
                expected = 1000;
                noResponse = true;
            }

            controller.expectedPlayers(expected, noResponse);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }

    }


    /**
     * method {@code starter}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void starter() {
        Integer side;
        boolean noResponse = false;

        while (true) {
            Future<Integer> future = executor.submit(view::placeStarter);

            try {
                side = future.get(120, TimeUnit.SECONDS);
            } catch (Exception e) {
                side = 1000;
                noResponse = true;
            }

            controller.placeStarter(side, noResponse);
            responseMessage answer = controller.correctAnswer();

            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }


    /**
     * method {@code pickObjective}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void pickObjective() {
        Integer pick;
        boolean noResponse = false;


        while (true) {
            ArrayList<ObjectiveCard> objectives = controller.getObjectiveCards().getObjectiveCard();

            Future<Integer> future = executor.submit(() -> view.chooseObjective(objectives));

            try {
                pick = future.get(120, TimeUnit.SECONDS);
            } catch (Exception e) {
                pick = 1000;
                noResponse = true;
            }

            controller.chooseObjective(pick, noResponse);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }

    }


    /**
     * method {@code pickCard}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void pickCard() {
        Integer card;
        boolean noResponse = false;

        while (true) {
            Future<Integer> future = executor.submit(view::pickCard);

            try {
                card = future.get(120, TimeUnit.SECONDS);
            } catch (Exception e) {
                card = 1000;
                noResponse = true;
            }

            controller.pickCard(card, noResponse);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }



    /**
     * method {@code placeCard}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    public void placeCard() {
        AtomicIntegerArray card = new AtomicIntegerArray(4);
        boolean noResponse = false;

        while (true) {
            Future<int[]> future = executor.submit(view::placeCard);

            try {
                int[] cardArray = future.get(240, TimeUnit.SECONDS);
                card.set(0, cardArray[0]);
                card.set(1, cardArray[1]);
                card.set(2, cardArray[2]);
                card.set(3, cardArray[3]);
            } catch (Exception e) {
                card.set(0, 1000);
                card.set(1, 1000);
                card.set(2, 1000);
                card.set(3, 1000);
                noResponse = true;
            }

            controller.placeCard(card.get(0), card.get(1), card.get(2), card.get(3), noResponse);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code setIP}: sets serverIP
     *
     * @param serverIP: String
     */
    public void setIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * method {@code setPort}: sets serverPort
     *
     * @param serverPort: String
     */
    public void setPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * method {@code setController}: sets the Controller
     *
     * @param server:   String[]
     * @param isSocket: boolean
     */
    public void setController(String[] server, boolean isSocket) {
        if (isSocket) {
            try {
                this.controller = new ControllerSocket(server[0], "1024");
            } catch (Exception e) {
                view.playerDisconnected();
            }
        } else {
            try {
                this.controller = new ControllerRMI(server[0], "1099");
            } catch (Exception e) {
                view.playerDisconnected();
            }
        }
    }


    /**
     * method {@code pickNameFA}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * Asks the player to pick a name between the available ones.
     */
    public void pickNameFA() {
        String name;

        while (true) {
            unavailableNamesMessage unavailableName = controller.getUnavailableName();
            Future<String> future = executor.submit(() -> view.pickNameFA(unavailableName));

            try {
                name = future.get(120, TimeUnit.SECONDS);
            } catch (Exception e) {
                name = "1000";
            }

            controller.chooseName(name);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }

}