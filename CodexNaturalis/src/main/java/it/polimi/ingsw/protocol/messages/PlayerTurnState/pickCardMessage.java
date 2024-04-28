package it.polimi.ingsw.protocol.messages.PlayerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class pickCardMessage implements Message, Serializable {
    private boolean gold;
    private boolean faceUp;
    private int position;

    public pickCardMessage(boolean gold, boolean faceUp, int position) {
        this.gold = gold;
        this.faceUp = faceUp;
        this.position = position;
    }

    public pickCardMessage(int card) {

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
