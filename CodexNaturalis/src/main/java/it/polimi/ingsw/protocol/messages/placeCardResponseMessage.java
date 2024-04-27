package it.polimi.ingsw.protocol.messages;

import java.io.Serializable;

public class placeCardResponseMessage extends Message implements Serializable {
    private boolean correctPositioning;

    public placeCardResponseMessage(boolean correctPositionig) {
        this.correctPositioning = correctPositionig;
    }

    public boolean isCorrectPositioning() {return correctPositioning;}
}
