package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Match implements Serializable {
    private final ArrayList<Player> players;
    private final ObjectiveCard[] commonObjective;
    private final CommonArea commonArea;

    /**
     * Constructs a new {@code Match} object.
     */
    public Match() {
        this.players = new ArrayList<>();
        this.commonObjective = new ObjectiveCard[2];
        this.commonArea = (new LoadDecks()).load();
    }

    /**
     * method {@code addPlayer}: adds a new player. Can't add more than 4 players.
     *
     * @param player: player to be added.
     * @throws Exception if players are more than 4.
     */
    public void addPlayer(Player player) throws Exception {
        if (players.size() < 4)
            players.add(player);
        else
            throw new Exception("The maximum number of players is 4. Can't add more players.");
    }

    /**
     * method {@code start}: set up and start the match.
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
     * Method {@code drawCommonObjective} draws the two commonObjective cards;
     */
    public void drawCommonObjective() {
        commonObjective[0] = commonArea.drawObjectiveCard();
        commonObjective[1] = commonArea.drawObjectiveCard();
    }

    /**
     * @return commonArea.
     */
    public CommonArea getCommonArea() {
        return commonArea;
    }

    /**
     * @return commonObjective.
     */
    public ObjectiveCard[] getCommonObjective() {
        return commonObjective;
    }

    /**
     * @return player.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

}
