package it.polimi.ingsw.protocol.messages.StaterCardState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class starterCardMessage implements Message,Serializable {
    private int side;

    public starterCardMessage(int side) {

    }

    public int getSide() {
        return side;
    }

    @Override
    public void setLock() {

    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {

    }
}
