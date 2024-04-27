package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.messages.Connection.*;
import it.polimi.ingsw.protocol.messages.CurrentStateMessage;
import it.polimi.ingsw.protocol.messages.EndGame.DeclareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.ObjectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurn.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.StarterCardMessage;

import java.util.ArrayList;

/**
 * ClientConnection class
 * @author elylo
 */
public abstract class ClientConnection implements Runnable {
    private String status;
    private String IP;
    private String port;
    private CheckConnection connection;

    /**
     * Class constructor
     *
     * @param IP the IP address of the server.
     * @param port the port of the server.
     */
    public ClientConnection(String IP, String port) {
        this.IP = IP;
        this.port = port;
    }

    /**
     * Method return the status of the client.
     */
    public String getStatus() {
        return status;
    }

    /**
    * Method return the IP address of the server.
    */
    public String getIP() {
        return IP;
    }
    /**
     * Method return the port address of the server.
     */
    public String getPort() {
        return port;
    }

    /**
     * Method return the connection.
     */
    public CheckConnection getConnection(){
        return connection;
    }

    /**
     * Method asks the host if the match should start and return the answer
     */
    public abstract boolean getStart(String hostNickname);

    /**
     * Method that creates a message with whom the host is and whether the game can start or not
     */
    public abstract void sendNewHostMessage(String hostNickname, boolean startSignal);

    /**
     * Method start the connection
     */
    public abstract void startCheckConnection();

    /**
     * Method sends a message with unavailable names and receives the name chosen by the client as a response
     */
    public abstract String getName(ArrayList<String> unavailableNames);

    /**
     *Method sends a message with the available colors and receives as a response the color chosen by the client
     */
    public abstract String getColor(ArrayList<String> availableColors);

    /**
     * Method creates a currentState message and receives the ObjectiveCard chosen by the client as a response
     */
    public abstract ObjectiveCardMessage getChosenObjective(CurrentStateMessage currentState);

    /**
     * Method creates a currentState message and receives the StarterCard assigned to the client as a response
     */
    public abstract StarterCardMessage getStarterCard(CurrentStateMessage currentState);

    /**
     * Method creates a currentState message and receives the PlaceCard chosen by the client as a response
     */
    public abstract PlaceCardMessage getPlaceCard(CurrentStateMessage currentState);

    /**
     * Method creates an UpdatePlayer message and receives the card drawn by the client as a response
     */
    public abstract pickCardMessage getChosenPick(UpdatePlayerMessage message);

    /**
     * Method send a message to establish the connection
     */
    public abstract void sendAnswerToConnection(answerConnectionMessage message);


    /**
     * Method send a message to ask for the name
     */
    public abstract void sendAnswerToChosenName(boolean correctChoice);

    /**
     * Method send a message to ask for the color
     */
    public abstract void sendAnswerToChosenColor(boolean correctChoice);

    /**
     * Method send a message to ask for the PlaceCard
     */
    public abstract void sendShowAnswerToPlaceCard(placeCardResponseMessage message);

    /**
     * Method send a message to ask for the drawn card
     */
    public abstract void sendShowAnswerToPickCard(pickCardResponseMessage message);

    /**
     * Method send a message to let you know who won
     */
    public abstract void sendEndGame(CurrentStateMessage currentState, DeclareWinnerMessage winner);

    /**
     * Method sends a message to throw the client out
     */
    abstract void sendKicked(kickedMessage message);






}
