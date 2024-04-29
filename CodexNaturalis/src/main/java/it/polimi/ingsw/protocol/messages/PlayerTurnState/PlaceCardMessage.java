package it.polimi.ingsw.protocol.messages.PlayerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class PlaceCardMessage implements Message, Serializable {
    private int card;
    private int row;
    private int column;
    private int front;

    public PlaceCardMessage(int card, int front, int row, int column) {
        this.card = card;
        this.row = row;
        this.column = column;
        this.front = front;
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

    @Override
    public void setLock() {}

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {}
}
