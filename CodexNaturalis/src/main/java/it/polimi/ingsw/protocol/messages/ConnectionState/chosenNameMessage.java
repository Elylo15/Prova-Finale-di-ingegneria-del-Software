package it.polimi.ingsw.protocol.messages.ConnectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class chosenNameMessage implements Message, Serializable {
    String name;

    public chosenNameMessage(String name) {
        this.name = name;
    }

    /**
     * Getter of name attribute
     * @return String name
     */
    public String getName() {return name;}

}
