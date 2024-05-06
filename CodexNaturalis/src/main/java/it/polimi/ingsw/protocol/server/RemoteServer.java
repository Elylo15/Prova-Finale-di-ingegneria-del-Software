package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.pickCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.placeCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.newHostMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteServer extends UnicastRemoteObject implements RemoteServerInterface {
    private final MessageRegistryInterface messageRegistry;

    /**
     * method {@code RemoteServer}: constructs a new RemoteServer
     */
    public RemoteServer(MessageRegistryInterface messageRegistry) throws RemoteException {
        this.messageRegistry = messageRegistry;
    }

    /**
     * method {@code sendNewHostMessageRMI}: Store the newHostMessage in the message registry
     * @param newHostMessage: newHostMessage
     */
    @Override
    public void sendNewHostMessageRMI(newHostMessage newHostMessage) {
        messageRegistry.sendMessage("newHostMessage", newHostMessage);
    }

    /**
     * method {@code newHostRMI}: Retrieve the newHostMessage from the message registry
     * @return newHostMessage
     */
    @Override
    public newHostMessage newHostRMI() {
        return (newHostMessage) messageRegistry.getMessage("newHostMessage");
    }

    /**
     * method {@code expectedPlayersRMI}: Store the expectedPlayersMessage in the message registry
     * @param message: expectedPlayersMessage
     */
    @Override
    public void expectedPlayersRMI(expectedPlayersMessage message) {
        messageRegistry.sendMessage("expectedPlayersMessage", message);
    }

    /**
     * method {@code getExpectedPlayerRMI}: Retrieve the expectedPlayersMessage from the message registry
     * @return expectedPlayersMessage
     */
    @Override
    public expectedPlayersMessage getExpectedPlayerRMI() {
        return (expectedPlayersMessage) messageRegistry.getMessage("expectedPlayersMessage");
    }

    /**
     * method {@code sendAnswerRMI}: Store the responseMessage in the message registry
     * @param responseMessage: responseMessage
     */
    @Override
    public void sendAnswerRMI(responseMessage responseMessage) {
        messageRegistry.sendMessage("responseMessage", responseMessage);
    }

    /**
     * method {@code correctAnswerRMI}: Retrieve the responseMessage from the message registry
     * @return responseMessage
     */
    @Override
    public responseMessage correctAnswerRMI() {
        return (responseMessage) messageRegistry.getMessage("responseMessage");
    }

    /**
     * method {@code sendCurrentStateRMI}: Store the currentStateMessage in the message registry
     * @param sendCurrentState: currentStateMessage
     */
    @Override
    public void sendCurrentStateRMI(currentStateMessage sendCurrentState) {
        messageRegistry.sendMessage("currentStateMessage", sendCurrentState);
    }

    /**
     * method {@code getCurrentRMI}: Retrieve the currentStateMessage from the message registry
     * @return currentStateMessage
     */
    @Override
    public currentStateMessage getCurrentRMI() {
        return (currentStateMessage) messageRegistry.getMessage("currentStateMessage");
    }

    /**
     * method {@code answerConnectionRMI}: Retrieve the connectionResponseMessage from the message registry
     * @return connectionResponseMessage
     */
    @Override
    public connectionResponseMessage answerConnectionRMI() {
        return (connectionResponseMessage) messageRegistry.getMessage("connectionResponseMessage");
    }

    /**
     * method {@code sendAnswerToConnectionRMI}: Store the connectionResponseMessage in the message registry
     * @param message: connectionResponseMessage
     */
    @Override
    public void sendAnswerToConnectionRMI(connectionResponseMessage message) {
        messageRegistry.sendMessage("connectionResponseMessage", message);
    }

    /**
     * method {@code getStaterCardRMI}: Retrieve the starterCardMessage from the message registry
     * @return starterCardMessage
     */
    @Override
    public starterCardMessage getStaterCardRMI() {
        return (starterCardMessage) messageRegistry.getMessage("starterCardMessage");
    }

    /**
     * method {@code placeStarterRMI}: Store the starterCardMessage in the message registry
     * @param starterCardMessage: starterCardMessage
     */
    @Override
    public void placeStarterRMI(starterCardMessage starterCardMessage) {
        messageRegistry.sendMessage("starterCardMessage", starterCardMessage);
    }

    /**
     * method {@code getPlaceCardRMI}: Retrieve the placeCardMessage from the message registry
     * @return placeCardMessage
     */
    @Override
    public placeCardMessage getPlaceCardRMI() {
        return (placeCardMessage) messageRegistry.getMessage("placeCardMessage");
    }

    /**
     * method {@code placeCardRMI}: Store the placeCardMessage in the message registry
     * @param message: placeCardMessage
     */
    @Override
    public void placeCardRMI(placeCardMessage message) {
        messageRegistry.sendMessage("placeCardMessage", message);
    }

    /**
     * method {@code getChosenPickRMI}: Retrieve the pickCardMessage from the message registry
     * @return pickCardMessage
     */
    @Override
    public pickCardMessage getChosenPickRMI() {
        return (pickCardMessage) messageRegistry.getMessage("pickCardMessage");
    }

    /**
     * method {@code pickCardRMI}: Store the pickCardMessage in the message registry
     * @param message: pickCardMessage
     */
    @Override
    public void pickCardRMI(pickCardMessage message) {
        messageRegistry.sendMessage("pickCardMessage", message);
    }

    /**
     * method {@code sendEndGameRMI}: Store the declareWinnerMessage in the message registry
     * @param winnerMessage: declareWinnerMessage
     */
    @Override
    public void sendEndGameRMI(declareWinnerMessage winnerMessage) {
        messageRegistry.sendMessage("declareWinnerMessage", winnerMessage);
    }

    /**
     * method {@code endGameRMI}: Retrieve the declareWinnerMessage from the message registry
     * @return declareWinnerMessage
     */
    @Override
    public declareWinnerMessage endGameRMI() {
        return (declareWinnerMessage) messageRegistry.getMessage("declareWinnerMessage");
    }

    /**
     * method {@code serverOptionsRMI}: Retrieve the serverOptionMessage from the message registry
     * @return serverOptionMessage
     */
    @Override
    public serverOptionMessage serverOptionsRMI() {
        return (serverOptionMessage) messageRegistry.getMessage("serverOptionMessage");
    }

    /**
     * method {@code sendOptionsRMI}: Store the serverOptionMessage in the message registry
     * @param options: serverOptionMessage
     */
    @Override
    public void sendOptionsRMI(serverOptionMessage options) {
        messageRegistry.sendMessage("serverOptionMessage", options);
    }

    /**
     * method {@code sendUnavailableNameRMI}: Store the unavailableNamesMessage in the message registry
     * @param unavailableNamesMessage: unavailableNamesMessage
     */
    @Override
    public void sendUnavailableNameRMI(unavailableNamesMessage unavailableNamesMessage) {
        messageRegistry.sendMessage("unavailableNamesMessage", unavailableNamesMessage);
    }

    /**
     * method {@code getUnavailableNameRMI}: Retrieve the unavailableNamesMessage from the message registry
     * @return unavailableNamesMessage
     */
    @Override
    public unavailableNamesMessage getUnavailableNameRMI() {
        return (unavailableNamesMessage) messageRegistry.getMessage("unavailableNamesMessage");
    }

    /**
     * method {@code getNameRMI}: Retrieve the chosenNameMessage from the message registry
     * @return chosenNameMessage
     */
    @Override
    public chosenNameMessage getNameRMI() {
        return (chosenNameMessage) messageRegistry.getMessage("chosenNameMessage");
    }

    /**
     * method {@code chooseNameRMI}: Store the chosenNameMessage in the message registry
     * @param name: chosenNameMessage
     */
    @Override
    public void chooseNameRMI(chosenNameMessage name) {
        messageRegistry.sendMessage("chosenNameMessage", name);
    }

    /**
     * method {@code sendAvailableColorRMI}: Store the availableColorsMessage in the message registry
     * @param availableColorsMessage: availableColorsMessage
     */
    @Override
    public void sendAvailableColorRMI(availableColorsMessage availableColorsMessage) {
        messageRegistry.sendMessage("availableColorsMessage", availableColorsMessage);
    }

    /**
     * method {@code getAvailableColorRMI}: Retrieve the availableColorsMessage from the message registry
     * @return availableColorsMessage
     */
    @Override
    public availableColorsMessage getAvailableColorRMI() {
        return (availableColorsMessage) messageRegistry.getMessage("availableColorsMessage");
    }

    /**
     * method {@code chooseColorRMI}: Store the chosenColorMessage in the message registry
     * @param color: chosenColorMessage
     */
    @Override
    public void chooseColorRMI(chosenColorMessage color) {
        messageRegistry.sendMessage("chosenColorMessage", color);
    }

    /**
     * method {@code getColorRMI}: Retrieve the chosenColorMessage from the message registry
     * @return chosenColorMessage
     */
    @Override
    public chosenColorMessage getColorRMI() {
        return (chosenColorMessage) messageRegistry.getMessage("chosenColorMessage");
    }

    /**
     * method {@code chooseObjectiveRMI}: Store the objectiveCardMessage in the message registry
     * @param message: objectiveCardMessage
     */
    @Override
    public void chooseObjectiveRMI(objectiveCardMessage message) {
        messageRegistry.sendMessage("objectiveCardMessage", message);
    }

    /**
     * method {@code getChosenObjectiveRMI}: Retrieve the objectiveCardMessage from the message registry
     * @return objectiveCardMessage
     */
    @Override
    public objectiveCardMessage getChosenObjectiveRMI() {
        return (objectiveCardMessage) messageRegistry.getMessage("objectiveCardMessage");
    }

    /**
     * method {@code sendUpdatePlayerRMI}: Store the updatePlayerMessage in the message registry
     * @param updateMessage: updatePlayerMessage
     */
    @Override
    public void sendUpdatePlayerRMI(updatePlayerMessage updateMessage) {
        messageRegistry.sendMessage("updatePlayerMessage", updateMessage);
    }

    /**
     * method {@code updatePlayerRMI}: Retrieve the updatePlayerMessage from the message registry
     * @return updatePlayerMessage
     */
    @Override
    public updatePlayerMessage updatePlayerRMI() {
        return (updatePlayerMessage) messageRegistry.getMessage("updatePlayerMessage");
    }
}
