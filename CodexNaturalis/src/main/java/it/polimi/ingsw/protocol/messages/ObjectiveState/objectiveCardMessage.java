package it.polimi.ingsw.protocol.messages.ObjectiveState;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;


public class objectiveCardMessage implements Message, Serializable {
    private int choice;

    public objectiveCardMessage(int choice) {
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
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
