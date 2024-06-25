    package it.polimi.ingsw.client;

    import it.polimi.ingsw.client.controller.Controller;
    import it.polimi.ingsw.client.controller.ControllerRMI;
    import it.polimi.ingsw.client.controller.ControllerSocket;
    import it.polimi.ingsw.client.view.View;
    import it.polimi.ingsw.client.view.ViewCLI;
    import it.polimi.ingsw.messages.connectionState.availableColorsMessage;
    import it.polimi.ingsw.messages.connectionState.connectionResponseMessage;
    import it.polimi.ingsw.messages.connectionState.unavailableNamesMessage;
    import it.polimi.ingsw.messages.currentStateMessage;
    import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
    import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
    import it.polimi.ingsw.messages.responseMessage;
    import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
    import it.polimi.ingsw.messages.waitingForPlayerState.newHostMessage;
    import it.polimi.ingsw.model.cards.ObjectiveCard;

    import java.util.ArrayList;
    import java.util.Objects;
    import java.util.concurrent.*;
    import java.util.concurrent.atomic.AtomicBoolean;
    import java.util.concurrent.atomic.AtomicIntegerArray;
/**
 * This class is the Client class.
 * It is responsible for the client side of the application.
 * It is responsible for the connection to the server and the communication with it.
 */
public class Client implements Runnable {
    private final View view;
    private final ThreadPoolExecutor executor;
    //initializes a default value, the methods of the view will return this value in case the user does not provide an answer in time
    private final Integer defaultValue = 1000;
    private String serverIP;
    private Controller controller;

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
     * method {@code getController}: Getter for the controller
     *
     * @return the controller
     */
    private Controller getController() {
        return controller;
    }

    /**
     * method {@code getView}: Getter for the view
     *
     * @return the view
     */
    private View getView() {
        return view;
    }

    /**
     * method {@code connection}: sets socket or rmi. Enables GUI or CLI.
     * Connects to a socket server or rmi server.
     */
    private void connection(boolean isSocket) {

        if (isSocket) {
            try {
                controller.connectToServer(serverIP, "1024");
                connectionResponseMessage answer = controller.answerConnection();
                if (view instanceof ViewCLI)
                    ((ViewCLI) view).answerToConnection(answer);
            } catch (Exception e) {
                view.playerDisconnected(e);
                throw new RuntimeException();
            }
        } else {
            try {
                controller.connectToServer(serverIP, "1099");
                connectionResponseMessage answer = controller.answerConnection();
                if (view instanceof ViewCLI)
                    ((ViewCLI) view).answerToConnection(answer);
            } catch (Exception e) {
                view.playerDisconnected(e);
                throw new RuntimeException();
            }
        }
    }

    /**
     * method {@code serverOptions}: invocations of controller methods to send and receive serverOptionMessage.
     * Invocations of view methods to display options and receive user's choices.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void serverOptions() {
        while (true) {
            serverOptionMessage options = controller.serverOptions();
            options = view.serverOptions(options);
            controller.sendOptions(options);
            responseMessage answer = controller.correctAnswer();

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

            if (answer.getCorrect())
                break; //breaks the cycle if server received valid values
        }
    }

    /**
     * method {@code name}: invocations of controller methods to receive unavailableNamesMessage.
     * Invocations of view methods to display unavailable names and receive name chosen.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void name() {
        //in case the cli user does not provide an answer (only press enter), name is set to " "
        //the loop will repeat until server receive a valid value
        while (true) {
            unavailableNamesMessage unavailableName = controller.getUnavailableName();
            String name = view.unavailableNames(unavailableName);
            controller.chooseName(name);
            responseMessage answer = controller.correctAnswer();

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code color}: invocations of controller methods to receive availableColorsMessage.
     * Invocations of view methods to display available colors and receive user's choice.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void color() {
        while (true) {
            availableColorsMessage availableColor = controller.getAvailableColor();
            String color = view.availableColors(availableColor);
            controller.chooseColor(color);
            responseMessage answer = controller.correctAnswer();

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

            if (answer.getCorrect())
                break;
        }
    }

    /**
     * Invocations of view methods to ask and receive from host the number of expected player.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void waitingPlayer(currentStateMessage current) {
        Integer expected;
        AtomicBoolean noResponse = new AtomicBoolean(false);

        newHostMessage newHost = controller.newHost();

        while (Objects.equals(newHost.getNewHostNickname(), current.getPlayer().getNickname())) {
            //if the method expectedPlayers is not computed in time or user provide no answer, noResponse will be set to true
            expected = getFutureResult(view::expectedPlayers, noResponse);

            controller.expectedPlayers(expected, noResponse.get());
            responseMessage answer = controller.correctAnswer();

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

            if (answer.getCorrect()) {
                view.waiting();
                break;
            }
        }

    }

    /**
     * method {@code starter}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void starter() {
        Integer side;
        AtomicBoolean noResponse = new AtomicBoolean(false);

        while (true) {
            side = getFutureResult(view::placeStarter, noResponse);

            controller.placeStarter(side, noResponse.get());
            responseMessage answer = controller.correctAnswer();


            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

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
    private void pickObjective() {
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

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

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
    private void pickCard() {
        Integer card;
        AtomicBoolean noResponse = new AtomicBoolean(false);

        while (true) {
            card = getFutureResult(view::pickCard, noResponse);


            controller.pickCard(card, noResponse.get());
            responseMessage answer = controller.correctAnswer();

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code getFutureResult}: gets the result of a future task.
     *
     * @param task:       Callable<T>
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

//    /**
//     *
//     * @param task: Callable<Integer>, the result of the task will be an Integer
//     * @param noResponse: AtomicBoolean
//     * @return the result of the future task or defaultValue
//     */
//     Alternative: in this class we use this method submitting to the executor view.pickCard, view.expectedPlayers, view.placeStarter
//     they all return an int value so there is no need use a generic value <T>
//    private Integer getFutureResult(Callable<Integer> task, AtomicBoolean noResponse) {
//        Future<Integer> future = executor.submit(task);
//        Integer valueToReturn;
//        try {
//            valueToReturn = future.get(120, TimeUnit.SECONDS);
//            return valueToReturn;
//        } catch (Exception e) {
//            //if the computation of the task is not completed within 2 minutes noResponse is set to true,
//            // and we return the default value
//            future.cancel(true);
//            noResponse.set(true); // Update the noResponse flag
//            return defaultValue;
//        }
//    }

    /**
     * method {@code placeCard}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * invocations of controller methods to send received info.
     * invocations of controller methods to receive responseMessage. If responseMessage is correct, the loop ends.
     */
    private void placeCard() {
        AtomicIntegerArray card = new AtomicIntegerArray(4);
        boolean noResponse = false;

        while (true) {
            Future<int[]> future = executor.submit(view::placeCard);
            //we wait two minutes to obtain the result of the submitted task
            //then if computation is not completed an exception is thrown, noResponse is set to true, user's choices are set to default value
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

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code setIP}: sets serverIP
     *
     * @param serverIP: String
     */
    private void setIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * method {@code setController}: sets the Controller
     *
     * @param server:   String[]
     * @param isSocket: boolean
     */
    private void setController(String server, boolean isSocket) {
        if (isSocket) {
            try {
                this.controller = new ControllerSocket(server, "1024");
            } catch (Exception e) {
                view.playerDisconnected(e);
            }
        } else {
            try {
                this.controller = new ControllerRMI(server, "1099");
            } catch (Exception e) {
                view.playerDisconnected(e);
            }
        }
    }


    /**
     * method {@code pickNameFA}: invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     * Asks the player to pick a name between the available ones.
     */
    private void pickNameFA() {
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

            boolean ok = false;
            while (!ok)
                ok = view.answer(answer);

            if (answer.getCorrect())
                break;
        }
    }

    /**
     * method {@code whileRun}: main loop of the client
     * Invocations of controller methods to receive and send messages.
     * Invocations of view methods to display and receive player's info.
     */
    @Override
    public void run() {

        boolean ok = false;
        while (!ok)
            ok = getView().startMain();


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
                        case "serverOptionState": {
                            serverOptions();
                            break;
                        }
                        case "connectionState": {
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
                        case "objectiveState": {
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
                        case "endGameState": {
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
                getView().playerDisconnected(e);
            }
        }
    }

}

