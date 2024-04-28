package it.polimi.ingsw.protocol.messages.ObjectiveState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class objectiveCardResponseMessage implements Message, Serializable {
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
