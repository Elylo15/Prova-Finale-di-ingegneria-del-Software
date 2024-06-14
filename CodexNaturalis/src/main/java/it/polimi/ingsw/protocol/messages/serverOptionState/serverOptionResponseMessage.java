package it.polimi.ingsw.protocol.messages.serverOptionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message used to handle the server options
 */
public class serverOptionResponseMessage implements Message, Serializable {
    private final String matchID;
    private boolean correct;

    /**
     * Constructor for the serverOptionMessageResponse class
     *
     * @param correct true if the message is correct
     * @param matchID ID of the match the client wants to join
     */
    public serverOptionResponseMessage(boolean correct, String matchID) {
        this.correct = correct;
        this.matchID = matchID;
    }

    /**
     * Getter for the matchID
     *
     * @return ID of the match the client wants to join
     */
    public String getMatchID() {
        return matchID;
    }

    /**
     * Getter for correct
     *
     * @return true if the message is correct
     */
    public boolean getCorrect() {
        return correct;
    }

    /**
     * Setter for correct
     *
     * @param correct true if the message is correct
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

}

