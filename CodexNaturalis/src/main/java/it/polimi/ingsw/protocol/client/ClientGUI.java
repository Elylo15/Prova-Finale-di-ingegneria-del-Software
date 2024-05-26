package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.client.view.GUI.controller.SceneManager;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.client.view.View;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.newHostMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ClientGUI extends Client implements Runnable{
    private String serverIP;
    private Controller controller;
    private final ViewGUI view;
    private ThreadPoolExecutor executor;

    /**
     * method {@code Client}: constructs a new Client
     *
     * @param view: default ViewGUI
     */
    public ClientGUI(ViewGUI view) {
        this.view = view;
        int corePoolSize = 5;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

    }

    @Override
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

    @Override
    public void run() {
        view.startMain();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                String server = view.askIP();
                setIP(server);

                boolean isSocket = view.askSocket();
                setController(server, isSocket);
                connection(isSocket);
            } catch (Exception e) {
                System.out.println("\033[31mConnection failed.\033[0m");
                continue;
            }

            try {
                while (true) {

//                    // REMOVE THIS
//                    System.out.println("\n\033[41mWaiting for current state");


                    currentStateMessage current = controller.getCurrent();
                    String state = current.getStateName();

//                    // REMOVE THIS
//                    System.out.println("Current state: " + state + "\n\033[0m");

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
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                view.playerDisconnected();
            }
        }
    }



    /**
     * method {@code serverOptions}: invocations of controller methods to send and receive serverOptionMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public void setIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * method {@code setController}: sets the Controller
     *
     * @param server:   String[]
     * @param isSocket: boolean
     */
    @Override
    public void setController(String server, boolean isSocket) {
        if (isSocket) {
            try {
                this.controller = new ControllerSocket(server, "1024");
            } catch (Exception e) {
                view.playerDisconnected();
            }
        } else {
            try {
                this.controller = new ControllerRMI(server, "1099");
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
    @Override
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
