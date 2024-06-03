package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.chosenColorMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.chosenNameMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.pickCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.placeCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ClientConnection class
 *
 * @author elylo
 */
public abstract class ClientConnection implements Runnable, Serializable {
    private final String IP;
    private final String port;

    /**
     * method {@code ClientConnection}: constructs a new ClientConnection
     *
     * @param IP   the IP address of the server.
     * @param port the port of the server.
     */
    public ClientConnection(String IP, String port) {
        this.IP = IP;
        this.port = port;
    }

    /**
     * method {@code getIP}: the IP address of the server.
     *
     * @return IP: String
     */
    public String getIP() {
        return IP;
    }

    /**
     * method {@code getPort}: the port address of the server.
     *
     * @return port: String
     */
    public String getPort() {
        return port;
    }

    /**
     * methods implemented by the clientSocket and clientRMI classes
     */
    public abstract serverOptionMessage getServerOption(ArrayList<Integer> waitingMatches, ArrayList<Integer> runningMatches, ArrayList<Integer> savedMatches);

    public abstract void sendNewHostMessage(String hostNickname);

    public abstract expectedPlayersMessage getExpectedPlayer();

    public abstract void sendAnswer(boolean correct);

    public abstract void sendAnswerToConnection(connectionResponseMessage message);

    protected abstract void sendUnavailableName(ArrayList<String> unavailableNames);

    public abstract chosenNameMessage getName(ArrayList<String> unavailableNames);

    protected abstract void sendAvailableColor(ArrayList<String> availableColors);

    public abstract chosenColorMessage getColor(ArrayList<String> availableColors);

    public abstract void sendCurrentState(currentStateMessage currentState);

    public abstract starterCardMessage getStaterCard();

    public abstract objectiveCardMessage getChosenObjective(ArrayList<ObjectiveCard> objectiveCards);

    public abstract placeCardMessage getPlaceCard();

    public abstract pickCardMessage getChosenPick();

    public abstract void sendEndGame(HashMap<String, Integer> score, HashMap<String, Integer> numberOfObjectives);

    public abstract void sendUpdatePlayer(updatePlayerMessage updateMessage);

    public abstract void closeConnection();

    public abstract boolean isConnected();


}
