package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

public abstract class Card {
    protected int ID;

    /**
     * Constructs a new {@code Card} object with the specified {@param ID}.
     * @param ID must be <103 and >0
     * @throws InvalidIdException if ID´s condition is not met.
     */
    protected Card(int ID) throws InvalidIdException {
        if(ID<0 || ID>102) throw new InvalidIdException("Invalid ID");
        this.ID = ID;
    }

    /**
     * Constructs a new {@code Card} object.
     * This constructor is an overload that creates a default Card object.
     */
    protected Card(){}

    /**
     * @return card ID
     */
    public int getID() {
        return ID;
    }
}
