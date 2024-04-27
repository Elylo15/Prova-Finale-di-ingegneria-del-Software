package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.Serializable;


public class ObjectiveCardMessage extends Message implements Serializable {
    private final ObjectiveCard obj1;
    private final ObjectiveCard obj2;
    private final ObjectiveCard chosenObjectiveCard;

    private Player player;

    /**
     * Used from server to client
     * @param obj1 first objective card
     * @param obj2 second objective card
     * @param player reference to the player
     */
    public ObjectiveCardMessage(ObjectiveCard obj1, ObjectiveCard obj2, Player player) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.chosenObjectiveCard = null;
    }

    /**
     * Used as answer to server from client
     * @param chosenObjectiveCard chosen objective between obj1 and obj2
     * @param player reference to the player
     */
    public ObjectiveCardMessage(ObjectiveCard chosenObjectiveCard, Player player) {
        this.obj1 = null;
        this.obj2 = null;
        this.chosenObjectiveCard = chosenObjectiveCard;
    }

    public ObjectiveCard getObj1() {return obj1;}
    public ObjectiveCard getObj2() {return obj2;}
    public ObjectiveCard getChosenObjectiveCard() {return chosenObjectiveCard;}
    public Player getPlayer() {return player;}
}
