package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.server.FSM.State;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    private final Player player;
    private State state;
    private ClientConnection connection;
    private ObjectiveCard[] savedObjectives;

    /**
     * Constructs a new PlayerInfo object with the given player, state, and connection.
     *
     * @param player     The player object.
     * @param state      The current state of the player's FSM.
     * @param connection The client connection object.
     */
    public PlayerInfo(Player player, State state, ClientConnection connection) {
        this.player = player;
        this.state = state;
        this.connection = connection;
        this.savedObjectives = null;
    }


    /**
     * Constructs a new PlayerInfo object by copying the player and state from another PlayerInfo object.
     * The connection is set to null and the saved objectives are copied from the original PlayerInfo object.
     *
     * @param playerInfo The PlayerInfo object to copy from.
     */
    public PlayerInfo(PlayerInfo playerInfo) {
        this.player = playerInfo.getPlayer();
        this.state = playerInfo.getState();
        this.connection = null;
        this.savedObjectives = playerInfo.getSavedObjectives();
    }

    /**
     * Retrieves the array of objective cards saved by the player.
     *
     * @return The array of saved objective cards.
     */
    public ObjectiveCard[] getSavedObjectives() {
        return savedObjectives;
    }

    /**
     * Sets the array of objective cards saved by the player.
     *
     * @param savedObjectives The array of saved objective cards.
     */
    public void setSavedObjectives(ObjectiveCard[] savedObjectives) {
        this.savedObjectives = savedObjectives;
    }

    /**
     * Retrieves the player object associated with this player information.
     *
     * @return The player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the current state of the player.
     *
     * @return The state object representing the player's state.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the player's state to the given state.
     *
     * @param state The new state.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Retrieves the client connection object representing the player's connection.
     *
     * @return The client connection object.
     */
    public ClientConnection getConnection() {
        return connection;
    }

    /**
     * Sets the player connection manager.
     *
     * @param connection new connection object.
     */
    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

}
