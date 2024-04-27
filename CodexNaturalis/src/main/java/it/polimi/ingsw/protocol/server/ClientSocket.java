package it.polimi.ingsw.protocol.server;


import it.polimi.ingsw.protocol.messages.Connection.*;
import it.polimi.ingsw.protocol.messages.CurrentStateMessage;
import it.polimi.ingsw.protocol.messages.EndGame.DeclareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.ObjectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurn.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.StarterCardMessage;

import java.util.ArrayList;

public class ClientSocket extends ClientConnection {

    /**
     * Class constructor
     *
     * @param IP   the IP address of the server.
     * @param port the port of the server.
     */
    public ClientSocket(String IP, String port) {
        super(IP, port);
    }

    /**
     * Method asks the host if the match should start and return the answer
     *
     * @param hostNickname
     */
    @Override
    public boolean getStart(String hostNickname) {
        return false;
    }

    /**
     * Method that creates a message with whom the host is and whether the game can start or not
     *
     * @param hostNickname
     * @param startSignal
     */
    @Override
    public void sendNewHostMessage(String hostNickname, boolean startSignal) {

    }

    /**
     * Method start the connection
     */
    @Override
    public void startCheckConnection() {
        getConnection().startConnectionCheck();
    }

    /**
     * Method sends a message with unavailable names and receives the name chosen by the client as a response
     *
     * @param unavailableNames
     */
    @Override
    public String getName(ArrayList<String> unavailableNames) {
        return "";
    }

    /**
     * Method sends a message with the available colors and receives as a response the color chosen by the client
     *
     * @param availableColors
     */
    @Override
    public String getColor(ArrayList<String> availableColors) {
        return "";
    }

    /**
     * Method creates a currentState message and receives the ObjectiveCard chosen by the client as a response
     *
     * @param currentState
     */
    @Override
    public ObjectiveCardMessage getChosenObjective(CurrentStateMessage currentState) {
        return null;
    }

    /**
     * Method creates a currentState message and receives the StarterCard assigned to the client as a response
     *
     * @param currentState
     */
    @Override
    public StarterCardMessage getStarterCard(CurrentStateMessage currentState) {
        return null;
    }

    /**
     * Method creates a currentState message and receives the PlaceCard chosen by the client as a response
     *
     * @param currentState
     */
    @Override
    public PlaceCardMessage getPlaceCard(CurrentStateMessage currentState) {
        return null;
    }

    /**
     * Method creates an UpdatePlayer message and receives the card drawn by the client as a response
     *
     * @param message
     */
    @Override
    public pickCardMessage getChosenPick(UpdatePlayerMessage message) {
        return null;
    }

    /**
     * Method send a message to establish the connection
     *
     * @param message
     */
    @Override
    public void sendAnswerToConnection(answerConnectionMessage message) {

    }

    /**
     * Method send a message to ask for the name
     *
     * @param correctChoice
     */
    @Override
    public void sendAnswerToChosenName(boolean correctChoice) {

    }

    /**
     * Method send a message to ask for the color
     *
     * @param correctChoice
     */
    @Override
    public void sendAnswerToChosenColor(boolean correctChoice) {

    }

    /**
     * Method send a message to ask for the PlaceCard
     *
     * @param message
     */
    @Override
    public void sendShowAnswerToPlaceCard(placeCardResponseMessage message) {

    }

    /**
     * Method send a message to ask for the drawn card
     *
     * @param message
     */
    @Override
    public void sendShowAnswerToPickCard(pickCardResponseMessage message) {

    }

    /**
     * Method send a message to let you know who won
     *
     * @param currentState
     * @param winner
     */
    @Override
    public void sendEndGame(CurrentStateMessage currentState, DeclareWinnerMessage winner) {

    }

    /**
     * Method sends a message to throw the client out
     *
     * @param message
     */
    @Override
    void sendKicked(kickedMessage message) {

    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {

    }
}
