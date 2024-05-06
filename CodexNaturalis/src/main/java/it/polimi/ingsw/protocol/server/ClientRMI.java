package it.polimi.ingsw.protocol.server;


import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardResponseMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.pickCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.placeCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardResponseMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayerMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayersResponseMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientRMI extends ClientConnection implements RemoteServer {
    private RemoteServer remoteServer;

    /**
     * Class constructor
     *
     * @param IP   the IP address of the server.
     * @param port the port of the server.
     */
    public ClientRMI(String IP, String port) {
        super(IP, port);
    }

    @Override
    public void run() {
        try {
            String remoteObjectName = "RemoteServer";
            String url = "rmi://" + getIP() + ":" + getPort() + "/" + remoteObjectName;
            remoteServer = (RemoteServer) Naming.lookup(url);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNewHostMessage(String hostNickname){
        try {
            remoteServer.sendNewHostMessage(hostNickname);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public expectedPlayerMessage getExpectedPlayer(){
        try {
            return remoteServer.getExpectedPlayer();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAnswer (boolean correct){
        try {
            remoteServer.sendAnswerToExpected(correct);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAnswerToConnection(connectionResponseMessage message){
        try {
            remoteServer.sendAnswerToConnection(message);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void  sendUnvailableName(ArrayList<String> unavailableNames) {
        try {
            remoteServer.sendUnvailableName(unavailableNames);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public chosenNameMessage getName(){
        try {
            return remoteServer.getName();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAvailableColor(ArrayList<String> availableColors){
        try {
            remoteServer.sendAvailableColor(availableColors);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public chosenColorMessage getColor(){
        try {
            return remoteServer.getColor();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendCurrentState(currentStateMessage sendCurrentState){
        try {
            remoteServer.sendCurrentState(sendCurrentState);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public starterCardMessage getStaterCard(){
        try {
            return remoteServer.getStaterCard();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public objectiveCardMessage getChosenObjective(){
        try {
            return remoteServer.getChosenObjective();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public placeCardMessage getPlaceCard(){
        try {
            return remoteServer.getPlaceCard();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public pickCardMessage getChosenPick(){
        try {
            return remoteServer.getChosenPick();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEndGame(declareWinnerMessage winnerMessage){
        try {
            remoteServer.sendEndGame(winnerMessage);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendUpdatePlayer(updatePlayerMessage updateMessage) {
        try {
            remoteServer.sendUpdatePlayer(updateMessage);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}


