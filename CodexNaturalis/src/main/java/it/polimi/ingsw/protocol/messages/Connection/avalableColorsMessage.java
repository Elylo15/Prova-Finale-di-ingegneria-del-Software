package it.polimi.ingsw.protocol.messages.Connection;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class avalableColorsMessage implements Message, Serializable {
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
