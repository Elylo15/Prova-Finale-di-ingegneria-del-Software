package it.polimi.ingsw.protocol.messages.PlayerTurnState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class pickCardMessage implements Message, Serializable {
    private final Integer card;
    private final boolean noResponse;

    public pickCardMessage(Integer card, boolean noResponse) {
        this.card = card;
        this.noResponse = noResponse;
    }

    public int getCard() {
        return card;
    }
    public boolean isNoResponse() {
        return noResponse;
    }

}
