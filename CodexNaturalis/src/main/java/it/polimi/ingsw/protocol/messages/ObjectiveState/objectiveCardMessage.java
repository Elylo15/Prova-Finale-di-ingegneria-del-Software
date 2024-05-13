package it.polimi.ingsw.protocol.messages.ObjectiveState;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;


public class objectiveCardMessage implements Message, Serializable {
    private ArrayList<ObjectiveCard> objectiveCard;
    private final Integer choice;
    private final boolean noResponse;

    public objectiveCardMessage(int choice, boolean noResponse) {
        this.objectiveCard = null;
        this.choice = choice;
        this.noResponse = noResponse;
    }

    public objectiveCardMessage(ArrayList<ObjectiveCard> objectiveCard) {
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

    public ArrayList<ObjectiveCard> getObjectiveCard() {return objectiveCard;}

    @Override
    public void setLock() {}

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {}
}
