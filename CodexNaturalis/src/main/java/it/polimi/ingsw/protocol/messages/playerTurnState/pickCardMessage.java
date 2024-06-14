package it.polimi.ingsw.protocol.messages.playerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message used to pick a card
 */
public class pickCardMessage implements Message, Serializable {
    private final Integer card;
    private final boolean noResponse;

    /**
     * Create new message
     *
     * @param card       card to pick
     * @param noResponse true if no response is expected
     */
    public pickCardMessage(Integer card, boolean noResponse) {
        this.card = card;
        this.noResponse = noResponse;
    }

    /**
     * Get card picked
     *
     * @return id of the card
     */
    public int getCard() {
        return card;
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
