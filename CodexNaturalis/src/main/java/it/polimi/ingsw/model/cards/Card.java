package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

/**
 * Card class
 * @author bianca
 */

public abstract class Card {
    protected int ID;

    /**
     * Class constructor
     *
     * @param ID must be <103 and >0
     * @throws InvalidIdException if ID´s condition is not met.
     */
    public Card(int ID) throws InvalidIdException {
        if(this.ID<0 || this.ID>102) throw new InvalidIdException("Invalid ID");
            this.ID = ID;
    }

    /**
     * @return card ID
     */
    public int getID() {
        return this.ID;
    }
}
