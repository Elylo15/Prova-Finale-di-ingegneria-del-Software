package it.polimi.ingsw.model.cards.exceptions;

import java.io.Serializable;

public class InvalidIdException extends Exception implements Serializable {
    public InvalidIdException(String message) {
        super(message);
    }
    public InvalidIdException(){super();}
}
