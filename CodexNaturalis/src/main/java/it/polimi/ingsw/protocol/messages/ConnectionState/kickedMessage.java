package it.polimi.ingsw.protocol.messages.ConnectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class kickedMessage implements Message, Serializable {

    String kicked = "You have been kicked from the game";

    public String toString(){
        return kicked.toString();
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
