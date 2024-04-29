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
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Client {
    private String serverIP;
    private String serverPort;
    private boolean isSocket;
    private boolean isGUI;
    private String name;
    private final ControllerSocket controller;
    private final ViewGUI view;

    public Client(ViewGUI view, ControllerSocket controller) {
        this.view = view; //GUI default poi ask
        this.controller = controller;
    }

    public void startGame() throws  IOException {
        connection();
        run();
    }

    private void connection() throws IOException {
        setSocket(view.askSocket());
        enableGUI(view.askGui());

        try {
            controller.connectToServer(serverIP, serverPort);
            controller.answerConnection();
            view.answerToConnection();
        } catch (SocketTimeoutException e) {
            //handle
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
                        waitingPlayer();
                        break;
                    }
                    case "StarterCardState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getPlayer().getNickname(), name)) starter();
                        break;
                    }
                    case "ObjectiveState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getPlayer().getNickname(), name)) pickObjective();
                        break;
                    }
                    case "PlayerTurnState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getPlayer().getNickname(), name)) {
                            placeCard();
                            pickCard();
                        }
                        break;
                    }
                    case "LastTurnState": {
                        view.updatePlayer(current);
                        if(Objects.equals(current.getPlayer().getNickname(), name)) placeCard();
                        break;
                    }
                    case "EndGameState": {
                        declareWinnerMessage end = controller.endGame();
                        view.endGame(end);
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions
        }
    }

    private void serverOptions() throws IOException {
        while(true) {
            serverOptionMessage options = controller.serverOptions();
            options = view.serverOptions(options);
            controller.sendOptions(options);
            answerServerOptionMessage answer = controller.correctOption();
            view.answerToOption();
            if (answer.getCorrect())
                break;
        }
    }

    private void name() throws IOException {
        while (true) {
            unavailableNamesMessage unavailableName = controller.getUnavailableName();
            name = view.unavaibleNames(unavailableName);
            controller.chooseName(name);
            answerNameMessage answer = controller.correctName();
            view.answerToNameChosen(answer);
            if(answer.getCorrect())
                break;
        }
    }

    private void color() throws IOException {
        while (true) {
            availableColorsMessage availableColor = controller.getAvailableColor();
            String color = view.availableColors(availableColor);
            controller.chooseColor(color);
            answerColorMessage answer = controller.correctColor();
            view.answerToColorChosen(answer);
            if(answer.getCorrect())
                break;
        }
    }

    private void waitingPlayer() throws IOException {
        int[] expected = new int[1];

        newHostMessage newHost = controller.newHost();
        String newHostName = newHost.getName();

        while (newHostName.equals(name)) {
            TimerTask task = new TimerTask() {
                public void run() {
                    expected[0] = 4;
                }
            };

            timer.schedule(task, 240000); //2 min

            expected[0] = view.expectedPlayers();
            timer.cancel();
            controller.expectedPlayers(expected[0]);
            answerExpectedPlayersMessage answer = controller.correctExpectedPlayers();
            view.answerToExpectedPlayers(answer);
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
            starterCardResponseMessage answer = controller.correctStarter();
            view.answerToPlaceStarter(answer);
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
            objectiveCardResponseMessage answer = controller.correctObjective();
            view.answerToChooseObjective(answer);
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
            pickCardResponseMessage answer = controller.correctPicked();
            view.answerToPickCard(answer);
            if(answer.getCorrect())
                break;
        }

        timer.cancel();
    }

    private void placeCard() throws IOException {
        final int[] card = new int[4];
        Timer timer = new Timer();

        while (true) {
            TimerTask task = new TimerTask() {
                public void run() {
                    Random rand = new Random();
                    card[0] = rand.nextInt(3);
                    card[1] = rand.nextInt(2);
                    //get available position and chose from there
                    //card[2] = rand.nextInt(3);
                    //card[3] = rand.nextInt(3);
                }
            };

            timer.schedule(task, 240000); //2 min

            card = view.placeCard();
            timer.cancel();
            controller.placeCard(card[0], card[1], card[2], card[3]);
            placeCardResponseMessage answer = controller.correctPlaced();
            view.answerToPlaceCard(answer);
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