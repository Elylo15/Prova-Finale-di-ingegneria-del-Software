package it.polimi.ingsw.protocol.messages;

public class disconnectedMessage implements Message {
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