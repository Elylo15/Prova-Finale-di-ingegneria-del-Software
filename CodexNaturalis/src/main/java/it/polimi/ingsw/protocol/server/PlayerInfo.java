package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.server.FSM.State;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    private  Player player;
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
     * Sets the array of objective cards saved by the player.
     *
     * @param savedObjectives The array of saved objective cards.
     */
    public void setSavedObjectives(ObjectiveCard[] savedObjectives) {this.savedObjectives = savedObjectives;}
    /**
     * Retrieves the array of objective cards saved by the player.
     *
     * @return The array of saved objective cards.
     */
    public ObjectiveCard[] getSavedObjectives() {return savedObjectives;}
    /**
     * Retrieves the player object associated with this player information.
     *
     * @return The player object.
     */
    public Player getPlayer() { return player;}
    /**
     * Retrieves the current state of the player.
     *
     * @return The state object representing the player's state.
     */
    public State getState() { return state;}
    /**
     * Sets the player's state to the given state.
     *
     * @param state The new state.
     */
    public void setState(State state) {this.state = state;}
    /**
     * Retrieves the client connection object representing the player's connection.
     *
     * @return The client connection object.
     */
    public ClientConnection getConnection() { return connection;}

}
