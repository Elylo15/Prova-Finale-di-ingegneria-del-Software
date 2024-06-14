package it.polimi.ingsw.protocol.messages.objectiveState;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message that contains the objective card and the choice of the player
 */
public class objectiveCardMessage implements Message, Serializable {
    private final ArrayList<ObjectiveCard> objectiveCard;
    private final Integer choice;
    private final boolean noResponse;

    /**
     * Constructor for objectiveCardMessage
     *
     * @param choice:     Integer
     * @param noResponse: boolean
     */
    public objectiveCardMessage(int choice, boolean noResponse) {
        this.objectiveCard = null;
        this.choice = choice;
        this.noResponse = noResponse;
    }

    /**
     * Constructor for objectiveCardMessage
     *
     * @param objectiveCard: ArrayList<ObjectiveCard>
     */
    public objectiveCardMessage(ArrayList<ObjectiveCard> objectiveCard) {
        this.objectiveCard = objectiveCard;
        this.choice = null;
        this.noResponse = false;
    }

    /**
     * Getter for choice
     *
     * @return Integer
     */
    public Integer getChoice() {
        return choice;
    }

    /**
     * Getter for noResponse
     *
     * @return boolean
     */
    public boolean isNoResponse() {
        return noResponse;
    }

    /**
     * Getter for objective card
     *
     * @return ArrayList<ObjectiveCard>
     */
    public ArrayList<ObjectiveCard> getObjectiveCard() {
        return objectiveCard;
    }

}
