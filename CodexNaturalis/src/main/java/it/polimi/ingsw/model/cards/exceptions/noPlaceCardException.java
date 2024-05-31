package it.polimi.ingsw.model.cards.exceptions;

import java.io.Serializable;

/**
 * Exception thrown when there a card cant be placed
 */
public class noPlaceCardException extends Exception implements Serializable {
    public noPlaceCardException() {
        super();
    }
}
