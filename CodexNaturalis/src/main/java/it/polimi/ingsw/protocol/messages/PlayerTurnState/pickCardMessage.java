package it.polimi.ingsw.protocol.messages.PlayerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class pickCardMessage implements Message, Serializable {
    private final int card;
    private final boolean noResponse;

    public pickCardMessage(int card, boolean noResponse) {
        this.card = card;
        this.noResponse = noResponse;
    }

    public int getCard() {
        return card;
    }
    public boolean isNoResponse() {
        return noResponse;
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
