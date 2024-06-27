package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;

/**
 * Abstract class that represents a generic card.
 */
public abstract class Card implements Serializable {
    protected int ID;
    private boolean front;

    /**
     * Constructs a new {@code Card} object with the specified {@param ID}.
     *
     * @param ID    must be >= 0 and <= 102
     * @param front true if the card is front, false if the card is back
     * @throws InvalidIdException if ID´s condition is not met.
     */
    protected Card(int ID, boolean front) throws InvalidIdException {
        if (ID < 0 || ID > 102) throw new InvalidIdException("Invalid ID");
        this.ID = ID;
        this.front = front;
    }

    /**
     * Constructs a new {@code Card} object.
     * This constructor is an overload that creates a default Card object.
     */
    protected Card() {
    }

    /**
     * It gets the card ID.
     *
     * @return card ID: int
     */
    public int getID() {
        return ID;
    }

    /**
     * It gets the card's front status.
     *
     * @return front: boolean
     */
    public boolean isFront() {
        return front;
    }

    /**
     * It sets the card's front status.
     *
     * @param front: boolean
     */
    public void setFront(boolean front) {
        this.front = front;
    }

}
