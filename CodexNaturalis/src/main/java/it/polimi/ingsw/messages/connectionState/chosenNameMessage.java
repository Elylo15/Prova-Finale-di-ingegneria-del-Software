package it.polimi.ingsw.messages.connectionState;

import it.polimi.ingsw.messages.Message;

import java.io.Serializable;

/**
 * Message that contains the chosen name for the player
 */
public class chosenNameMessage implements Message, Serializable {
    private final String name;

    /**
     * Constructor for chosenNameMessage
     *
     * @param name: String
     */
    public chosenNameMessage(String name) {
        this.name = name;
    }

    /**
     * Getter of name attribute
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

}
