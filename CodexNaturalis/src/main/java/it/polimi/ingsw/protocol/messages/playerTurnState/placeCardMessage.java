package it.polimi.ingsw.protocol.messages.playerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message used to place a card
 */
public class placeCardMessage implements Message, Serializable {
    private final int card;
    private final int row;
    private final int column;
    private final boolean noResponse;
    private final int front;

    /**
     * Create new message
     *
     * @param card       card to place
     * @param front      front of the card
     * @param row        row of the card
     * @param column     column of the card
     * @param noResponse true if no response is received
     */
    public placeCardMessage(int card, int front, int row, int column, boolean noResponse) {
        this.card = card;
        this.row = row;
        this.column = column;
        this.front = front;
        this.noResponse = noResponse;
    }

    /**
     * Getter for the card
     *
     * @return id of the card
     */
    public int getCard() {
        return card;
    }

    /**
     * Getter for the row
     *
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for the column
     *
     * @return column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter for the front
     *
     * @return front
     */
    public int getFront() {
        return front;
    }

    /**
     * Get if no response received
     *
     * @return true if no response is received
     */
    public boolean isNoResponse() {
        return noResponse;
    }

}
