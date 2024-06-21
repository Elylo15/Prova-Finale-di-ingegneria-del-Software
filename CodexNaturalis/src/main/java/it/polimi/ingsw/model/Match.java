package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents a match in the game.
 * It contains a list of players, common objectives, and a common area.
 * It provides methods for adding players, starting the match, and drawing common objectives.
 * It implements the Serializable interface.
 */
public class Match implements Serializable {
    private final ArrayList<Player> players;
    private final ObjectiveCard[] commonObjective;
    private final CommonArea commonArea;

    /**
     * This is the constructor for the Match class.
     * It initializes the list of players, the array of common objectives, and the common area.
     */
    public Match() {
        this.players = new ArrayList<>();
        this.commonObjective = new ObjectiveCard[2];
        this.commonArea = (new LoadDecks()).load();
    }

    /**
     * This method adds a new player to the match.
     * It throws an exception if there are already 4 players in the match.
     *
     * @param player The player to add to the match.
     * @throws Exception If there are already 4 players in the match.
     */
    public void addPlayer(Player player) throws Exception {
        if (players.size() < 4)
            players.add(player);
        else
            throw new Exception("The maximum number of players is 4. Can't add more players.");
    }

    /**
     * This method sets up and starts the match.
     * It shuffles the decks and the list of players, and places the face-up cards on the table.
     */
    public void start() {

        commonArea.getD1().shuffle();
        commonArea.getD2().shuffle();
        commonArea.getD3().shuffle();
        commonArea.getD4().shuffle();

        Collections.shuffle(players); //randomizes the order of the players

        commonArea.drawFromDeck(1); //places the front-up cards on the table
        commonArea.drawFromDeck(1);
        commonArea.drawFromDeck(2);
        commonArea.drawFromDeck(2);
    }

    /**
     * This method draws the two common objective cards.
     */
    public void drawCommonObjective() {
        commonObjective[0] = commonArea.drawObjectiveCard();
        commonObjective[1] = commonArea.drawObjectiveCard();
    }

    /**
     * This method returns the common area in the match.
     *
     * @return CommonArea The common area in the match.
     */
    public CommonArea getCommonArea() {
        return commonArea;
    }

    /**
     * This method returns the array of common objectives in the match.
     *
     * @return ObjectiveCard[] The array of common objectives in the match.
     */
    public ObjectiveCard[] getCommonObjective() {
        return commonObjective;
    }

    /**
     * This method returns the list of players in the match.
     *
     * @return ArrayList<Player> The list of players in the match.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

}
