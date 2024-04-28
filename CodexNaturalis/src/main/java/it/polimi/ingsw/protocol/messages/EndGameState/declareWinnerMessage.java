package it.polimi.ingsw.protocol.messages.EndGameState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.HashMap;

public class declareWinnerMessage implements Message, Serializable {
    private HashMap<String, Integer> scores;
    private HashMap<String, Integer> numberOfObjects;

    public declareWinnerMessage(HashMap<String, Integer> scores, HashMap<String, Integer> numberOfObjects) {
        this.scores = scores;
        this.numberOfObjects = numberOfObjects;
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
