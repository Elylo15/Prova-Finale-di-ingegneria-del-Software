package it.polimi.ingsw.protocol.messages.WaitingforPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class expectedPlayersMessage implements Message, Serializable {
    private String hostNickname;
    private boolean startSignal;

    public expectedPlayersMessage(String name, boolean startSignal) {
    }

    public String getHostNickname() {
        return hostNickname;
    }

    public boolean getStartSignal() {
        return startSignal;
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
