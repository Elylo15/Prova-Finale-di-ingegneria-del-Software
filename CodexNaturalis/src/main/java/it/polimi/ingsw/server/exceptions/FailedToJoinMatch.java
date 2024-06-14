package it.polimi.ingsw.server.exceptions;

public class FailedToJoinMatch extends Exception {
    /**
     * Exception thrown when a player fails to join a match
     */
    public FailedToJoinMatch(String message) {
        super(message);
    }

    /**
     * Exception thrown when a player fails to join a match
     */
    public FailedToJoinMatch() {
        super();
    }
}
