package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.client.view.View;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.newHostMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public abstract  class Client {
    private String serverIP;
    private String serverPort;
    private Controller controller;
    private final View view;
    private ThreadPoolExecutor executor;


    public Client(View view){
        this.view = view;
        int corePoolSize = 5;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

    }
    public String getServerIP() {
        return serverIP;
    }

    public String serverPort() {
        return serverPort;
    }

    public Controller getController() {
        return controller;
    }

    public View getView(){
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
    public abstract void run();

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
                future.cancel(true);
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
                future.cancel(true);
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
                future.cancel(true);
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
                future.cancel(true);
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
                future.cancel(true);
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
     * method {@code setController}: sets the Controller
     *
     * @param server:   String[]
     * @param isSocket: boolean
     */
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
    public void pickNameFA() {
        String name;

        while (true) {
            unavailableNamesMessage unavailableName = controller.getUnavailableName();
            Future<String> future = executor.submit(() -> view.pickNameFA(unavailableName));

            try {
                name = future.get(120, TimeUnit.SECONDS);
            } catch (Exception e) {
                future.cancel(true);
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

