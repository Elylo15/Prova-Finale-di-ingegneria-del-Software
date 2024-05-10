package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.controller.*;
import it.polimi.ingsw.protocol.client.view.*;
import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Client {
    private String serverIP;
    private String serverPort;
    private Controller controller;
    private final View view;

    /**
     * method {@code Client}: constructs a new Client
     * @param view: default ViewGUI
     */
    public Client(View view) {
        this.view = view;
    }

    /**
     * method {@code connection}: sets socket or rmi. Enables GUI or CLI.
     * Connects to a socket server or rmi server.
     */
    public void connection(boolean isSocket)  {

        if(isSocket) {
            try {
                controller.connectToServer(serverIP, serverPort);
                connectionResponseMessage answer = controller.answerConnection();
                view.answerToConnection(answer);
            } catch (Exception e) {
                view.playerDisconnected();
                throw new RuntimeException();
            }
        } else {
            try {
                connectionResponseMessage answer = controller.answerConnection();
                view.answerToConnection(answer);
            } catch (Exception e) {
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
        setPort(server[1]);

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
                System.out.println("Current state: " + state +"\n");

                switch (state) {
                    case "ServerOptionState":{
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
                        if(Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) starter();
                        updatePlayerMessage update = controller.updatePlayer();
                        view.update(update);
                        break;
                    }
                    case "ObjectiveState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) pickObjective();
                        updatePlayerMessage update = controller.updatePlayer();
                        view.update(update);
                        break;
                    }
                    case "PlayerTurnState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) {
                            placeCard();
                            updatePlayerMessage update = controller.updatePlayer();
                            view.update(update);
                            pickCard();
                            updatePlayerMessage update2 = controller.updatePlayer();
                            view.update(update2);
                        }
                        break;
                    }
                    case "LastTurnState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) placeCard();
                        updatePlayerMessage update = controller.updatePlayer();
                        view.update(update);
                        break;
                    }
                    case "EndGameState": {
                        declareWinnerMessage end = controller.endGame();
                        view.endGame(end);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            view.playerDisconnected();
            throw new RuntimeException();
        }
    }

    /**
     * method {@code serverOptions}: invocations of controller methods to send and receive serverOptionMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void serverOptions() {
        while(true) {
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
    private void name() {
        while (true) {
            unavailableNamesMessage unavailableName = controller.getUnavailableName();
            String name = view.unavailableNames(unavailableName);
            controller.chooseName(name);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code color}: invocations of controller methods to receive availableColorsMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void color() {
        while (true) {
            availableColorsMessage availableColor = controller.getAvailableColor();
            String color = view.availableColors(availableColor);
            controller.chooseColor(color);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code color}: invocations of controller methods to receive availableColorsMessage.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void waitingPlayer(currentStateMessage current) {
        int[] expected = new int[1];
        final boolean[] noResponse = {false};

        Timer timer = new Timer();

        newHostMessage newHost = controller.newHost();

        while (Objects.equals(newHost.getNewHostNickname(), current.getPlayer().getNickname())) {
            TimerTask task = new TimerTask() {
                public void run() {
                    expected[0] = 1000;
                    noResponse[0] = true;
                }
            };

            timer.schedule(task, 240000); //2 min

            expected[0] = view.expectedPlayers();
            timer.cancel();
            controller.expectedPlayers(expected[0], noResponse[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    /**
     * method {@code starter}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void starter() {
        final int[] side = new int[1];
        final boolean[] noResponse = {false};
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    side[0] = 1000;
                    noResponse[0] = true;

                }
            };

            timer.schedule(task, 240000); //2 min

            side[0] = view.placeStarter();
            timer.cancel();
            controller.placeStarter(side[0], noResponse[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    /**
     * method {@code pickObjective}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void pickObjective() {
        final int[] pick = new int[1];
        final boolean[] noResponse = {false};
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    pick[0] = 1000;
                    noResponse[0] = true;
                }
            };

            timer.schedule(task, 240000); //2 min

            pick[0] = view.chooseObjective();
            timer.cancel();
            controller.chooseObjective(pick[0], noResponse[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    /**
     * method {@code pickCard}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void pickCard() {
        final int[] card = new int[1];
        final boolean[] noResponse = {false};
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    card[0] = 1000;
                    noResponse[0] = true;
                }
            };

            timer.schedule(task, 240000); //2 min

            card[0] = view.pickCard();
            timer.cancel();
            controller.pickCard(card[0], noResponse[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    /**
     * method {@code placeCard}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void placeCard() {
        AtomicIntegerArray card = new AtomicIntegerArray(4);
        final boolean[] noResponse = {false};
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    card.set(0, 1000);
                    card.set(1, 1000);
                    card.set(2, 1000);
                    card.set(3, 1000);
                    noResponse[0] = true;
                }
            };

            timer.schedule(task, 240000); //2 min

            int[] cardArray = view.placeCard();
            card.set(0, cardArray[0]);
            card.set(1, cardArray[1]);
            card.set(2, cardArray[2]);
            card.set(3, cardArray[3]);
            timer.cancel();
            controller.placeCard(card.get(0), card.get(1), card.get(2), card.get(3), noResponse[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    /**
     * method {@code setIP}: sets serverIP
     * @param serverIP: String
     */
    public void setIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * method {@code setPort}: sets serverPort
     * @param serverPort: String
     */
    public void setPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * method {@code setController}: sets the Controller
     * @param  server: String[]
     * @param isSocket: boolean
     */
    public void setController(String[] server, boolean isSocket) {
        if (isSocket) {
            try {
                this.controller = new ControllerSocket(server[0], server[1]);
            } catch (Exception e) {
                view.playerDisconnected();
            }
        } else {
            try {
                this.controller = new ControllerRMI(server[0], server[1]);
            } catch (Exception e) {
                view.playerDisconnected();
            }
        }
    }

}