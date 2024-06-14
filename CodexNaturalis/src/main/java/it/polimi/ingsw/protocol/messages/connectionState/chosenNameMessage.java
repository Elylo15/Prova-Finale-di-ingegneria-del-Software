package it.polimi.ingsw.protocol.messages.connectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message that contains the chosen name for the player
 */
public class chosenNameMessage implements Message, Serializable {
    String name;

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
