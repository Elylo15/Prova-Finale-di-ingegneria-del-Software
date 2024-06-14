package it.polimi.ingsw.protocol.messages.connectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message that contains the chosen color for the player
 */
public class chosenColorMessage implements Message, Serializable {
    private String color;

    /**
     * Constructor for chosenColorMessage
     *
     * @param color: String
     */
    public chosenColorMessage(String color) {
        this.color = color;
    }

    /**
     * Getter for color
     *
     * @return String
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for color
     */
    public void setColor(String color) {
        this.color = color;
    }


}
