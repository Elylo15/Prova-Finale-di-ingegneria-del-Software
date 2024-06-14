package it.polimi.ingsw.protocol.messages.waitingForPlayerState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message used to handle the new host
 */
public class newHostMessage implements Message, Serializable {
    private String newHostNickname;

    /**
     * Constructor for the newHostMessage class
     *
     * @param newHostNickname nickname of the new host
     */
    public newHostMessage(String newHostNickname) {
        this.newHostNickname = newHostNickname;
    }

    /**
     * Getter for the newHostNickname
     *
     * @return nickname of the new host
     */
    public String getNewHostNickname() {
        return newHostNickname;
    }

    /**
     * Setter for the newHostNickname
     *
     * @param newHostNickname nickname of the new host
     */
    public void setNewHostNickname(String newHostNickname) {
        this.newHostNickname = newHostNickname;
    }

}
