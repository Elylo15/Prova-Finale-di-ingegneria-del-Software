package it.polimi.ingsw.protocol.messages.PlayerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class placeCardMessage implements Message, Serializable {
    private final int card;
    private final int row;
    private final int column;
    private final boolean noResponse;
    private final int front;

    public placeCardMessage(int card, int front, int row, int column, boolean noResponse) {
        this.card = card;
        this.row = row;
        this.column = column;
        this.front = front;
        this.noResponse = noResponse;
    }

    public int getCard() {
        return card;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getFront() {
        return front;
    }

    public boolean isNoResponse() {
        return noResponse;
    }

}
