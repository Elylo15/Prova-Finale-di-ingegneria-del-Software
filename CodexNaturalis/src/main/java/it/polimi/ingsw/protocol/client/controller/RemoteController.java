package it.polimi.ingsw.protocol.client.controller;

import java.rmi.Remote;

import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;

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
    void expectedPlayers(int expected, boolean noResponse);
    void placeStarter(int side, boolean noResponse);
    void chooseObjective(int pick, boolean noResponse);
    void placeCard(int card, int side, int x, int y, boolean noResponse);
    void pickCard(int card, boolean noResponse);
    declareWinnerMessage endGame();
    updatePlayerMessage updatePlayer();
    responseMessage correctAnswer();
}
