package it.polimi.ingsw.protocol.server.exceptions;

public class FailedToJoinMatch extends Exception {
    public FailedToJoinMatch(String message) {
        super(message);
    }

    public FailedToJoinMatch() {
        super();
    }
}
