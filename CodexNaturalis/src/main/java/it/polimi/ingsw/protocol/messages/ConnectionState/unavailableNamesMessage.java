package it.polimi.ingsw.protocol.messages.ConnectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message that contains the unavailable names for the players
 */
public class unavailableNamesMessage implements Message, Serializable {

    ArrayList<String> unavailableNames;

    /**
     * Constructor for unavailableNamesMessage
     *
     * @param names: ArrayList<String>
     */
    public unavailableNamesMessage(ArrayList<String> names) {
        this.unavailableNames = names;
    }

    /**
     * Getter for unavailableNames
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getNames() {
        return unavailableNames;
    }

    /**
     * Setter for unavailableNames
     */
    public String toString() {
        return unavailableNames.toString();
    }


}
