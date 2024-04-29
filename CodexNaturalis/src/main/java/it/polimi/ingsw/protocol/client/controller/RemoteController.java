package it.polimi.ingsw.protocol.client.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;


import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;

public interface RemoteController extends Remote {
    connectionResponseMessage answerConnection() throws RemoteException;

    currentStateMessage getCurrent() throws RemoteException;

    serverOptionMessage serverOptions() throws RemoteException;

    void sendOptions(serverOptionMessage options) throws RemoteException;

    serverOptionResponseMessage correctOption() throws RemoteException;

    unavailableNamesMessage getUnavailableName() throws RemoteException;

    void chooseName(String name) throws RemoteException;

    nameResponseMessage correctName() throws RemoteException;

    availableColorsMessage getAvailableColor() throws RemoteException;

    void chooseColor(String color) throws RemoteException;

    colorResponseMessage correctColor() throws RemoteException;

    newHostMessage newHost() throws RemoteException;

    void expectedPlayers(int expected) throws RemoteException;

    expectedPlayersResponseMessage correctExpectedPlayers() throws RemoteException;

    void placeStarter(int side) throws RemoteException;

    starterCardResponseMessage correctStarter() throws RemoteException;

    void chooseObjective(int pick) throws RemoteException;

    objectiveCardResponseMessage correctObjective() throws RemoteException;

    void placeCard(int card, int side, int x, int y) throws RemoteException;

    placeCardResponseMessage correctPlaced() throws RemoteException;

    void pickCard(int card) throws RemoteException;

    pickCardResponseMessage correctPicked() throws RemoteException;

    declareWinnerMessage endGame() throws RemoteException;
}
