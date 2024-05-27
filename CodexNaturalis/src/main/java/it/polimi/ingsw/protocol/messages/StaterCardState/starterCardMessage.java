package it.polimi.ingsw.protocol.messages.StaterCardState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class starterCardMessage implements Message,Serializable {
    private final int side;
    private final boolean noResponse;

    public starterCardMessage(int side, boolean noResponse) {
        this.side = side;
        this.noResponse = noResponse;
    }

    public int getSide() {
        return side;
    }

    public boolean isNoResponse() {
        return noResponse;
    }
}
