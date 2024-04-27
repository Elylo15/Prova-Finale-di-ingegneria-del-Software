package it.polimi.ingsw.protocol.messages;

public interface Message {
    boolean lock = false;

    public void setLock();
    public boolean isLocked();
    public void unlock();

}
