package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.pickCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.placeCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayerMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.newHostMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;

import java.rmi.Remote;
import java.util.ArrayList;

public interface RemoteServer extends Remote {

    void sendNewHostMessage(String hostNickname);

    expectedPlayerMessage getExpectedPlayer();

    void sendAnswerToExpected(boolean correct);

    void sendAnswerToConnection(connectionResponseMessage message);

    void sendUnvailableName(ArrayList<String> unavailableNames);

    chosenNameMessage getName();

    void sendAvailableColor(ArrayList<String> availableColors);

    chosenColorMessage getColor();

    void sendCurrentState(currentStateMessage sendCurrentState);

    starterCardMessage getStaterCard();

    objectiveCardMessage getChosenObjective();

    placeCardMessage getPlaceCard();

    pickCardMessage getChosenPick();

    void sendEndGame(declareWinnerMessage winnerMessage);

    void sendUpdatePlayer(updatePlayerMessage updateMessage);
}
