package it.polimi.ingsw.protocol.messages.endGameState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Message that contains the winner of the game
 */
public class declareWinnerMessage implements Message, Serializable {
    private final HashMap<String, Integer> scores;   //final score of each player
    private final HashMap<String, Integer> numberOfObjects;  //number of objectives achieved by each player

    /**
     * Constructor for declareWinnerMessage
     *
     * @param scores:          HashMap<String, Integer>
     * @param numberOfObjects: HashMap<String, Integer>
     */
    public declareWinnerMessage(HashMap<String, Integer> scores, HashMap<String, Integer> numberOfObjects) {
        this.scores = scores;
        this.numberOfObjects = numberOfObjects;
    }

    /**
     * Getter for the winner
     *
     * @return String
     */
    public HashMap<String, Integer> getPlayersPoints() {
        return scores;
    }

    /**
     * Getter for the number of objects
     *
     * @return String
     */
    public HashMap<String, Integer> getNumberOfObjects() {
        return numberOfObjects;
    }

}
