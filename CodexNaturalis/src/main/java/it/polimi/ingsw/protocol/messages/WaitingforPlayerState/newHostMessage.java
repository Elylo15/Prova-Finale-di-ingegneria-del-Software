package it.polimi.ingsw.protocol.messages.WaitingforPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class newHostMessage implements Message, Serializable {
    private String newHostNickname;
    private boolean matchStarts;

    public newHostMessage(String newHostNickname, boolean matchStarts) {
        this.newHostNickname = newHostNickname;
        this.matchStarts = matchStarts;
    }

    public String getNewHostNickname() {
        return newHostNickname;
    }

    public boolean getMatchStarts() {
        return matchStarts;
    }

    public void setNewHostNickname(String newHostNickname) {
        this.newHostNickname = newHostNickname;
    }

    public void setMatchStarts(boolean matchStarts) {
        this.matchStarts = matchStarts;
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
