package it.polimi.ingsw.protocol.messages.Connection;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class kickedMessage implements Message, Serializable {

    String messagge = "You have been kicked from the game";

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
