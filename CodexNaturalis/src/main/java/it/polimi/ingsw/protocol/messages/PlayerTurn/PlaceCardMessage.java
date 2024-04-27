package it.polimi.ingsw.protocol.messages.PlayerTurn;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class PlaceCardMessage implements Message, Serializable {
    private int card;
    private int row;
    private int column;
    private int front;

    public PlaceCardMessage(int card, int row, int column, int front) {
        this.card = card;
        this.row = row;
        this.column = column;
        this.front = front;
    }


    @Override
    public void setLock() {

    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {

    }
}
