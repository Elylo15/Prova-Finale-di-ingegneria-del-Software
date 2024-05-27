package it.polimi.ingsw.protocol.messages.WaitingforPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class expectedPlayersMessage implements Message, Serializable {
    private final int expectedPlayers;
    private final boolean noResponse;

    public expectedPlayersMessage(int expectedPlayers, boolean noResponse) {
        this.expectedPlayers = expectedPlayers;
        this.noResponse = noResponse;
    }

    public int getExpectedPlayers() {
        return expectedPlayers;
    }

    public boolean isNoResponse() {
        return noResponse;
    }
}
