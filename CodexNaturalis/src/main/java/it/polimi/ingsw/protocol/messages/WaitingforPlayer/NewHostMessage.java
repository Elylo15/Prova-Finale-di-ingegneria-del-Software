package it.polimi.ingsw.protocol.messages.WaitingforPlayer;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class NewHostMessage implements Message, Serializable {
    private String newHostNickname;
    private boolean matchStarts;

    public String getNewHostNickname() {
        return newHostNickname;
    }

    public boolean getMatchStarts() {
        return matchStarts;
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
