package it.polimi.ingsw.protocol.messages;

import java.io.Serializable;

/**
 * Message used to handle the response
 */
public class responseMessage implements Message, Serializable {
    private boolean correct;

    /**
     * Constructor for the responseMessage class
     *
     * @param correct true if the response is correct
     */
    public responseMessage(boolean correct) {
        this.correct = correct;
    }

    /**
     * Getter for correct
     *
     * @return true if the response is correct
     */
    public boolean getCorrect() {
        return correct;
    }

    /**
     * Setter for correct
     *
     * @param correct true if the response is correct
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

}
