package it.polimi.ingsw.protocol.messages.PlayerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class pickCardMessage implements Message, Serializable {
    private final int card;

    public pickCardMessage(int card) {
        this.card = card;
    }

    public int getCard() {
        return card;
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
