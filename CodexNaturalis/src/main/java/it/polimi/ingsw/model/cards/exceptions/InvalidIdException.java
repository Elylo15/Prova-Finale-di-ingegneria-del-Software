package it.polimi.ingsw.model.cards.exceptions;

import java.io.Serializable;

/**
 * Exception thrown when the ID of a card is not valid
 */
public class InvalidIdException extends Exception implements Serializable {
    public InvalidIdException(String message) {
        super(message);
    }
    public InvalidIdException(){super();}
}
