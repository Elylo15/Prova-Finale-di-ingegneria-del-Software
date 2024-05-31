package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;

public abstract class Card implements Serializable {
    protected int ID;
    private boolean front;

    /**
     * Constructs a new {@code Card} object with the specified {@param ID}.
     *
     * @param ID must be <103 and >0
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
     * @return card ID: int
     */
    public int getID() {
        return ID;
    }

    /**
     * @return front: boolean
     */
    public boolean isFront() {
        return front;
    }

    /**
     * @param front: boolean
     */
    public void setFront(boolean front) {
        this.front = front;
    }

}
