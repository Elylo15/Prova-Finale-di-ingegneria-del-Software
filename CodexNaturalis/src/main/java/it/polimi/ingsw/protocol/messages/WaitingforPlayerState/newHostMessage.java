package it.polimi.ingsw.protocol.messages.WaitingforPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class newHostMessage implements Message, Serializable {
    private String newHostNickname;

    public newHostMessage(String newHostNickname) {
        this.newHostNickname = newHostNickname;
    }

    public String getNewHostNickname() {
        return newHostNickname;
    }


    public void setNewHostNickname(String newHostNickname) {
        this.newHostNickname = newHostNickname;
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
