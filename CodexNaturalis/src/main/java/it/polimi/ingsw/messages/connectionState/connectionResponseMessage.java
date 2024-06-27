package it.polimi.ingsw.messages.connectionState;

import it.polimi.ingsw.messages.Message;

import java.io.Serializable;
import java.util.Objects;

/**
 * Message that contains the response of the connection request
 */
public class connectionResponseMessage implements Message, Serializable {
    private boolean correct;

    /**
     * Constructor for connectionResponseMessage
     *
     * @param correct: boolean
     */
    public connectionResponseMessage(boolean correct) {
        this.correct = correct;
    }

    /**
     * Getter for correct
     *
     * @return boolean
     */
    public boolean getCorrect() {
        return correct;
    }

    /**
     * Setter for correct
     *
     * @param correct: boolean
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    /**
     * Method that returns the string representation of the object
     *
     * @return String
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        connectionResponseMessage that = (connectionResponseMessage) o;
        return correct == that.correct;
    }

    /**
     * Method that returns the hash of the object
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(correct);
    }
}
