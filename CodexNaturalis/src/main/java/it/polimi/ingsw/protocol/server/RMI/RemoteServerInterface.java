package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.*;

import java.rmi.Remote;

public interface RemoteServerInterface extends Remote {
    void sendNewHostMessageRMI(newHostMessage newHostMessage);
    expectedPlayersMessage getExpectedPlayerRMI();
    void sendAnswerRMI(responseMessage responseMessage);
    void sendAnswerToConnectionRMI(connectionResponseMessage message);
    void sendUnavailableNameRMI(unavailableNamesMessage unavailableNamesMessage);
    chosenNameMessage getNameRMI();
    void sendAvailableColorRMI(availableColorsMessage availableColorsMessage);
    chosenColorMessage getColorRMI();
    void sendCurrentStateRMI(currentStateMessage sendCurrentState);
    starterCardMessage getStaterCardRMI();
    objectiveCardMessage getChosenObjectiveRMI();
    placeCardMessage getPlaceCardRMI();
    pickCardMessage getChosenPickRMI();
    void sendEndGameRMI(declareWinnerMessage winnerMessage);
    void sendUpdatePlayerRMI(updatePlayerMessage updateMessage);
    connectionResponseMessage answerConnectionRMI();
    currentStateMessage getCurrentRMI();
    serverOptionMessage serverOptionsRMI();
    void sendOptionsRMI(serverOptionMessage options);
    unavailableNamesMessage getUnavailableNameRMI();
    void chooseNameRMI(chosenNameMessage name);
    availableColorsMessage getAvailableColorRMI();
    void chooseColorRMI(chosenColorMessage color);
    newHostMessage newHostRMI();
    void expectedPlayersRMI(expectedPlayersMessage message);
    void placeStarterRMI(starterCardMessage starterCardMessage);
    void chooseObjectiveRMI(objectiveCardMessage message);
    void placeCardRMI(placeCardMessage message);
    void pickCardRMI(pickCardMessage message);
    declareWinnerMessage endGameRMI();
    updatePlayerMessage updatePlayerRMI();
    responseMessage correctAnswerRMI();
}
