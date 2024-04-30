package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.controller.*;
import it.polimi.ingsw.protocol.client.view.*;
import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Client {
    private String serverIP;
    private String serverPort;
    private boolean isSocket;
    private boolean isGUI;
    private Controller controller;
    private View view;

    public Client(ViewGUI view, ControllerSocket controller) {
        this.view = view; //GUI default
        this.controller = controller; //Socket default
    }

    public void startGame() throws  IOException {
        connection();
        run();
    }

    private void connection()  {
        setSocket(view.askSocket());
        enableGUI(view.askGui());

        if(isSocket) {
            try {
                controller.connectToServer(serverIP, serverPort);
                connectionResponseMessage answer = controller.answerConnection();
                view.answerToConnection(answer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                controller = new ControllerRMI(serverIP, serverPort);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        if(!isGUI) {
            view = new ViewCLI();
        }
    }

    public void run() {
        try {
            while (true) {
                currentStateMessage current = controller.getCurrent();
                String state = current.getStateName();

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
                        break;
                    }
                    case "ObjectiveState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) pickObjective();
                        break;
                    }
                    case "PlayerTurnState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) {
                            placeCard(current);
                            pickCard();
                        }
                        break;
                    }
                    case "LastTurnState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) placeCard(current);
                        break;
                    }
                    case "EndGameState": {
                        declareWinnerMessage end = controller.endGame();
                        view.endGame(end);
                        break;
                    }
                }
            }
        } catch (RuntimeException | IOException | ClassNotFoundException e) {
            view.playerDisconnected();
        }
    }

    private void serverOptions() throws IOException {
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

    private void name() throws IOException {
        while (true) {
            unavailableNamesMessage unavailableName = controller.getUnavailableName();
            String name = view.unavaibleNames(unavailableName);
            controller.chooseName(name);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }
    }

    private void color() throws IOException {
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

    private void waitingPlayer(currentStateMessage current) throws IOException {
        int[] expected = new int[1];
        Timer timer = new Timer();

        newHostMessage newHost = controller.newHost();

        while (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname())) {
            TimerTask task = new TimerTask() {
                public void run() {
                    expected[0] = 4;
                }
            };

            timer.schedule(task, 240000); //2 min

            expected[0] = view.expectedPlayers();
            timer.cancel();
            controller.expectedPlayers(expected[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    private void starter() throws IOException {
        final int[] side = new int[1];
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    Random rand = new Random();
                    side[0] = rand.nextInt(2);
                }
            };

            timer.schedule(task, 240000); //2 min

            side[0] = view.placeStarter();
            timer.cancel();
            controller.placeStarter(side[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    private void pickObjective() throws IOException {
        final int[] pick = new int[1];
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    Random rand = new Random();
                    pick[0] = rand.nextInt(2);
                }
            };

            timer.schedule(task, 240000); //2 min

            pick[0] = view.chooseObjective();
            timer.cancel();
            controller.chooseObjective(pick[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    private void pickCard() throws IOException {
        final int[] card = new int[1];
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    Random rand = new Random();
                    card[0] = rand.nextInt(7);
                }
            };

            timer.schedule(task, 240000); //2 min

            card[0] = view.pickCard();
            timer.cancel();
            controller.pickCard(card[0]);
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    private void placeCard(currentStateMessage current) throws IOException {
        AtomicIntegerArray card = new AtomicIntegerArray(4);
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    Random rand = new Random();
                    card.set(0, rand.nextInt(3));
                    card.set(1, rand.nextInt(2));
                    ArrayList<Integer[]> position = current.getPlayer().getPlayerArea().getAvailablePosition();
                    card.set(2, position.getFirst()[0]);
                    card.set(3, position.getFirst()[1]);
                }
            };

            timer.schedule(task, 240000); //2 min

            int[] cardArray = view.placeCard();
            card.set(0, cardArray[0]);
            card.set(1, cardArray[1]);
            card.set(2, cardArray[2]);
            card.set(3, cardArray[3]);
            timer.cancel();
            controller.placeCard(card.get(0), card.get(1), card.get(2), card.get(3));
            responseMessage answer = controller.correctAnswer();
            view.answer(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    public void setIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getIP() {
        return serverIP;
    }

    public String getPort() {
        return serverPort;
    }

    public void setSocket(boolean isSocket) {
        this.isSocket = isSocket;
    }

    public void enableGUI(boolean guiEnabled) {
        this.isGUI = guiEnabled;
    }
}