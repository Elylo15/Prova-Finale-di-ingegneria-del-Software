package it.polimi.ingsw.messages.connectionState;

import it.polimi.ingsw.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message that contains the available colors for the players
 */
public class availableColorsMessage implements Message, Serializable {
    private final ArrayList<String> colors;

    /**
     * Constructor for availableColorsMessage
     *
     * @param color: ArrayList<String>
     */
    public availableColorsMessage(ArrayList<String> color) {
        this.colors = color;
    }

    /**
     * Setter for colors
     */
    public String toString() {
        return colors.toString();
    }

    /**
     * Getter for available colors
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getColors() {
        return colors;
    }
}
