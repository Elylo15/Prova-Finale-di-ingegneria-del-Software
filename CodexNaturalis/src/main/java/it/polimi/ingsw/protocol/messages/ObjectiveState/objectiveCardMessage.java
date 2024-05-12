package it.polimi.ingsw.protocol.messages.ObjectiveState;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;


public class objectiveCardMessage implements Message, Serializable {
    private ObjectiveCard[] objectiveCard;
    private final Integer choice;
    private final boolean noResponse;

    public objectiveCardMessage(int choice, boolean noResponse) {
        this.objectiveCard = null;
        this.choice = choice;
        this.noResponse = noResponse;
    }

    public objectiveCardMessage(ObjectiveCard[] objectiveCard) {
        this.objectiveCard = objectiveCard;
        this.choice = null;
        this.noResponse = false;
    }

    public Integer getChoice() {
        return choice;
    }

    public boolean isNoResponse() {
        return noResponse;
    }

    public ObjectiveCard[] getObjectiveCard() {return objectiveCard;}

    @Override
    public void setLock() {}

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {}
}
