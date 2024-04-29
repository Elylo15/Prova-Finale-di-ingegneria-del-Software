package it.polimi.ingsw.protocol.messages.WaitingforPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class expectedPlayersMessage implements Message, Serializable {
    private int expectedPlayers;

    public expectedPlayersMessage(int expectedPlayers) {
    }

    public int getExpectedPlayers() {
        return expectedPlayers;
    }

    @Override
    public void setLock() {}

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {}
}
