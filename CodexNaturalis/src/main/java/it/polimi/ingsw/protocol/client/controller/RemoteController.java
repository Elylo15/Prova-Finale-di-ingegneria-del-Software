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
    connectionResponseMessage answerConnection();

    currentStateMessage getCurrent();

    serverOptionMessage serverOptions();

    void sendOptions(serverOptionMessage options);

    unavailableNamesMessage getUnavailableName();

    void chooseName(String name);

    availableColorsMessage getAvailableColor();

    void chooseColor(String color);

    newHostMessage newHost();

    void expectedPlayers(int expected);

    void placeStarter(int side);


    void chooseObjective(int pick);

    void placeCard(int card, int side, int x, int y);

    void pickCard(int card);

    declareWinnerMessage endGame();

    responseMessage correctAnswer();
}
