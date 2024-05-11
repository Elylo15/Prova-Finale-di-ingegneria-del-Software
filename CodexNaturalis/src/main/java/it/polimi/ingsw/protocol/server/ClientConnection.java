package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ClientConnection class
 * @author elylo
 */
public abstract class ClientConnection implements Runnable {
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

    public abstract serverOptionMessage getServerOption(ArrayList<Integer> runningMatches, ArrayList<Integer> savedMatches);
    public abstract void sendNewHostMessage(String hostNickname);
    public abstract expectedPlayersMessage getExpectedPlayer();
    public abstract void sendAnswer(boolean correct);
    public abstract void sendAnswerToConnection(connectionResponseMessage message);
    public abstract void sendUnavailableName(ArrayList<String> unavailableNames);
    public abstract chosenNameMessage getName(ArrayList<String> unavailableNames);
    public abstract void sendAvailableColor(ArrayList<String> availableColors);
    public abstract chosenColorMessage getColor(ArrayList<String> availableColors);
    public abstract void sendCurrentState(currentStateMessage currentState);
    public abstract starterCardMessage getStaterCard();
    public abstract objectiveCardMessage getChosenObjective(ObjectiveCard[] objectiveCards);
    public abstract placeCardMessage getPlaceCard();
    public abstract pickCardMessage getChosenPick();
    public abstract void sendEndGame(HashMap<String, Integer> score, HashMap<String, Integer> numberOfObjectives);
    public abstract void sendUpdatePlayer(updatePlayerMessage updateMessage);
    public abstract void closeConnection();
}
