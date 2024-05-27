package it.polimi.ingsw.protocol.messages.WaitingforPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class newHostMessage implements Message, Serializable {
    private String newHostNickname;

    public newHostMessage(String newHostNickname) {
        this.newHostNickname = newHostNickname;
    }

    public String getNewHostNickname() {
        return newHostNickname;
    }


    public void setNewHostNickname(String newHostNickname) {
        this.newHostNickname = newHostNickname;
    }

}
