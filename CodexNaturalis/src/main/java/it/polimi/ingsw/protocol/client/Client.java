package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
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

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.Callable;

public class Client implements Runnable {
    private final View view;
    private final ThreadPoolExecutor executor;
    private String serverIP;
    private Controller controller;
    private final Integer defaultValue = 1000;

    /**
     * method {@code Client}: Constructor for the Client class
     *
     * @param view view of the client
     */
    public Client(View view) {
        this.view = view;
        int corePoolSize = 5;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());

    }

    /**
     * method {@code getServerIP}:Getter for the serverIP
     *
     * @return the serverIP
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * method {@code getController}: Getter for the controller
     *
     * @return the controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * method {@code getView}: Getter for the view
     *
     * @return the view
     */
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
        AtomicBoolean noResponse = new AtomicBoolean(false);

        newHostMessage newHost = controller.newHost();

        while (Objects.equals(newHost.getNewHostNickname(), current.getPlayer().getNickname())) {
            expected = getFutureResult(view::expectedPlayers, noResponse);

            controller.expectedPlayers(expected, noResponse.get());
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
        AtomicBoolean noResponse = new AtomicBoolean(false);

        while (true) {
            side = getFutureResult(view::placeStarter, noResponse);

            controller.placeStarter(side, noResponse.get());
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
                pick = defaultValue;
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
        AtomicBoolean noResponse = new AtomicBoolean(false);

        while (true) {
            card = getFutureResult(view::pickCard, noResponse);


            controller.pickCard(card, noResponse.get());
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code getFutureResult}: gets the result of a future task.
     *
     * @param task: Callable<T>
     * @param noResponse: AtomicBoolean
     * @return the result of the future task or the default value
     */
    private <T> T getFutureResult(Callable<T> task, AtomicBoolean noResponse) {
        Future<T> future = executor.submit(task);

        try {
            return future.get(120, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(true);
            noResponse.set(true); // Update the noResponse flag
            return (T) defaultValue;
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
                card.set(0, defaultValue);
                card.set(1, defaultValue);
                card.set(2, defaultValue);
                card.set(3, defaultValue);
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
                name = "defaultName";
            }

            controller.chooseName(name);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code whileRun}: main loop of the client
     */
    @Override
    public void run() {
        if (getView() instanceof ViewGUI) {
            ((ViewGUI) getView()).startMain();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {

            }
        }

        while (true) {
            try {
                String server = getView().askIP();
                setIP(server);

                boolean isSocket = getView().askSocket();
                setController(server, isSocket);
                connection(isSocket);
            } catch (Exception e) {
                System.out.println("\033[31mConnection failed.\033[0m");
                continue;
            }

            try {
                while (true) {

                    currentStateMessage current = getController().getCurrent();
                    String state = current.getStateName();

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
                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                starter();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "ObjectiveState": {

                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                pickObjective();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "PlaceTurnState": {
                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                placeCard();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "PickTurnState": {
                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                pickCard();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "EndGameState": {
                            declareWinnerMessage end = getController().endGame();
                            getView().endGame(end);
                            throw new Exception("Game ended.");
                        }

                        case "ConnectionFAState": {
                            pickNameFA();
                            break;
                        }

                        case "AnswerCheckConnection": {
                            getController().sendAnswerToPing();
                            break;
                        }

                    }
                }
            } catch (Exception e) {
                getView().playerDisconnected();
            }
        }
    }

}

