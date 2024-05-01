package it.polimi.ingsw.protocol.messages.ObjectiveState;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;


public class objectiveCardMessage implements Message, Serializable {
    private final int choice;
    private final boolean noResponse;

    public objectiveCardMessage(int choice, boolean noResponse) {
        this.choice = choice;
        this.noResponse = noResponse;
    }

    public int getChoice() {
        return choice;
    }

    public boolean isNoResponse() {
        return noResponse;
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
