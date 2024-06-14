package it.polimi.ingsw.messages.staterCardState;

import it.polimi.ingsw.messages.Message;

import java.io.Serializable;

/**
 * Message used to handle the starter card
 */
public class starterCardMessage implements Message, Serializable {
    private final int side;
    private final boolean noResponse;

    /**
     * Constructor for the starterCardMessage class
     *
     * @param side       side of the card
     * @param noResponse true if no response is received
     */
    public starterCardMessage(int side, boolean noResponse) {
        this.side = side;
        this.noResponse = noResponse;
    }

    /**
     * Getter for the side attribute
     *
     * @return The side of the card
     */
    public int getSide() {
        return side;
    }

    /**
     * Getter for the noResponse attribute
     *
     * @return true if no response is received
     */
    public boolean isNoResponse() {
        return noResponse;
    }
}
