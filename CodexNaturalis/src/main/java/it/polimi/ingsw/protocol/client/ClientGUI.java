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

    /**
     * method {@code Client}: constructs a new Client
     *
     * @param view: default ViewGUI
     */
    public ClientGUI(ViewGUI view) {
        super(view);
    }

    @Override
    public void run() {
        ((ViewGUI) getView()).startMain();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true) {
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

//                    // REMOVE THIS
//                    System.out.println("\n\033[41mWaiting for current state");


                    currentStateMessage current = getController().getCurrent();
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
