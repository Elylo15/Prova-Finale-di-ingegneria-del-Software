package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ClientConnection class
 * @author elylo
 */
public abstract class ClientConnection implements Runnable, Serializable {
    private final String IP;
    private final String port;

    /**
     * method {@code ClientConnection}: constructs a new ClientConnection
     * @param IP the IP address of the server.
     * @param port the port of the server.
     */
    public ClientConnection(String IP, String port) {
        this.IP = IP;
        this.port = port;
    }

    /**
    * method {@code getIP}: the IP address of the server.
     * @return IP: String
    */
    public String getIP() {
        return IP;
    }

    /**
     * method {@code getPort}: the port address of the server.
     * @return port: String
     */
    public String getPort() {
        return port;
    }

    protected abstract serverOptionMessage getServerOption(ArrayList<Integer> waitingMatches,ArrayList<Integer> runningMatches, ArrayList<Integer> savedMatches);
    protected abstract void sendNewHostMessage(String hostNickname);
    protected abstract expectedPlayersMessage getExpectedPlayer();
    protected abstract void sendAnswer(boolean correct);
    public abstract void sendAnswerToConnection(connectionResponseMessage message);
    protected abstract void sendUnavailableName(ArrayList<String> unavailableNames);
    protected abstract chosenNameMessage getName(ArrayList<String> unavailableNames);
    protected abstract void sendAvailableColor(ArrayList<String> availableColors);
    protected abstract chosenColorMessage getColor(ArrayList<String> availableColors);
    protected abstract void sendCurrentState(currentStateMessage currentState);
    protected abstract starterCardMessage getStaterCard();
    protected abstract objectiveCardMessage getChosenObjective(ArrayList<ObjectiveCard> objectiveCards);
    protected abstract placeCardMessage getPlaceCard();
    protected abstract pickCardMessage getChosenPick();
    protected abstract void sendEndGame(HashMap<String, Integer> score, HashMap<String, Integer> numberOfObjectives);
    protected abstract void sendUpdatePlayer(updatePlayerMessage updateMessage);
    protected abstract void closeConnection();
    protected abstract boolean isConnected();


}
