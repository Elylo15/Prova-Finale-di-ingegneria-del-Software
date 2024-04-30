package it.polimi.ingsw.protocol.messages.ConnectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class connectionResponseMessage implements Message, Serializable {
    private boolean correct;

    public connectionResponseMessage(boolean correct) {
        this.correct = correct;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
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
