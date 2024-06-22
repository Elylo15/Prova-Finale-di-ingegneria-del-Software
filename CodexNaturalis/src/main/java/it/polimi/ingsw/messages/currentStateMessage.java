package it.polimi.ingsw.messages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Message used to send the current state of the game contains all the necessary information about the match
 */
public class currentStateMessage implements Message, Serializable {
    private final Player currentPlayer; // player who is playing turn
    private final Player player;        // player who receives the message
    private final String stateName;
    private final boolean lastTurn;
    private final ArrayList<String> onlinePlayers; //ArrayList of names of online players
    private final ArrayList<ObjectiveCard> commonObjectiveCards;
    private final Integer MatchID;

    /**
     * Constructor for the currentStateMessage class
     *
     * @param currentPlayer        player who is playing turn
     * @param player               player who receives the message
     * @param stateName            name of the state
     * @param lastTurn             true if it is the last turn
     * @param onlinePlayers        list of online players
     * @param commonObjectiveCards list of common objective cards
     * @param MatchID              match ID
     */
    public currentStateMessage(Player currentPlayer, Player player, String stateName, boolean lastTurn, ArrayList<String> onlinePlayers, ObjectiveCard[] commonObjectiveCards, Integer MatchID) {
        this.currentPlayer = currentPlayer;
        this.player = player;
        this.stateName = stateName;
        this.lastTurn = lastTurn;
        this.onlinePlayers = onlinePlayers;
        this.MatchID = MatchID;
        if (commonObjectiveCards == null) {
            this.commonObjectiveCards = null;
        } else {
            this.commonObjectiveCards = new ArrayList<>();
            this.commonObjectiveCards.addAll(Arrays.asList(commonObjectiveCards));
        }

    }

    /**
     * Returns the player who is gaming
     *
     * @return the player who is gaming
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the player who receives the message
     *
     * @return the player who receives the message
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the name of the state
     *
     * @return the name of the state
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * Returns true if it is the last turn
     *
     * @return true if it is the last turn
     */
    public boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * Returns the list of online players
     *
     * @return the list of online players
     */
    public ArrayList<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    /**
     * Returns the list of common objective cards
     *
     * @return the list of common objective cards
     */
    public ArrayList<ObjectiveCard> getCommonObjectiveCards() {
        return commonObjectiveCards;
    }

    /**
     * Returns the match ID
     *
     * @return the match ID
     */
    public Integer getMatchID() {
        return MatchID;
    }

    /**
     * Returns the string representation of the message
     *
     * @return the string representation of the message
     */
    @Override
    public String toString() {
        String currentPlayerNickname;
        if (currentPlayer == null)
            currentPlayerNickname = "null";
        else
            currentPlayerNickname = currentPlayer.getNickname();

        String playerNickname;
        if (player == null)
            playerNickname = "null";
        else
            playerNickname = player.getNickname();

        return "currentStateMessage{" +
                "currentPlayer=" + currentPlayerNickname +
                ", player=" + playerNickname +
                ", stateName='" + stateName + '\'' +
                ", lastTurn=" + lastTurn +
                ", onlinePlayers=" + onlinePlayers +
                ", commonObjectiveCards=" + commonObjectiveCards +
                ", MatchID=" + MatchID +
                '}';
    }

}
