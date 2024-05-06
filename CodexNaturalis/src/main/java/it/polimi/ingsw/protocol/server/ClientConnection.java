package it.polimi.ingsw.protocol.server;




import it.polimi.ingsw.protocol.messages.ConnectionState.chosenColorMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.chosenNameMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ClientConnection class
 * @author elylo
 */
public abstract class ClientConnection implements Runnable {
    private String status;
    private final String IP;
    private final String port;
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

    public CheckConnection getConnection(){
        return connection;
    }

    public abstract void sendNewHostMessage(String hostNickname);

    public abstract expectedPlayerMessage getExpectedPlayer();

    public abstract void sendAnswer(boolean correct);

    public abstract void sendAnswerToConnection(connectionResponseMessage message);

    public abstract void sendUnvailableName(ArrayList<String> unavailableNames);

    public abstract chosenNameMessage getName();

    public abstract void sendAvailableColor(ArrayList<String> availableColors);

    public abstract chosenColorMessage getColor();

    public abstract void sendCurrentState(currentStateMessage currentState);

    public abstract starterCardMessage getStaterCard();

    public abstract objectiveCardMessage getChosenObjective();

    public abstract placeCardMessage getPlaceCard();

    public abstract pickCardMessage getChosenPick();

    public abstract void sendEndGame(declareWinnerMessage winnerMessage);

    //RMI?
    public abstract void closeConnection();

}
