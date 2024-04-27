package it.polimi.ingsw.protocol.messages.ObjectiveState;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;


public class ObjectiveCardMessage implements Message, Serializable {

    private final ObjectiveCard[] objectives;
    private final int choice;


    public ObjectiveCardMessage( int choice, ObjectiveCard[] objectives) {
        this.objectives = objectives;
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }

    public ObjectiveCard[] getObjectives() {
        return objectives;
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
