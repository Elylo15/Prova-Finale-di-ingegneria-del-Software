package it.polimi.ingsw.protocol.messages.ServerOptionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class serverOptionResponseMessage implements Message, Serializable {
    private boolean correct;
    private String matchID;

    public serverOptionResponseMessage(boolean correct, String matchID) {
        this.correct = correct;
        this.matchID = matchID;
    }

    public String getMatchID() {
        return matchID;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
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

