package it.polimi.ingsw.protocol.messages.waitingForPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message used to handle the expected players
 */
public class expectedPlayersMessage implements Message, Serializable {
    private final int expectedPlayers;
    private final boolean noResponse;

    /**
     * Constructor for the expectedPlayersMessage class
     *
     * @param expectedPlayers number of expected players
     * @param noResponse      true if no response is received
     */
    public expectedPlayersMessage(int expectedPlayers, boolean noResponse) {
        this.expectedPlayers = expectedPlayers;
        this.noResponse = noResponse;
    }

    /**
     * Getter for the expectedPlayers attribute
     *
     * @return The number of expected players
     */
    public int getExpectedPlayers() {
        return expectedPlayers;
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
