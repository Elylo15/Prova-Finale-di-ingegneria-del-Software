package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public class StarterCardMessage extends Message implements Serializable {
    private Player player;
    private boolean front;

    /**
     * Constructor used for the messages from client to server and vice versa.
     * @param player player that has to choose the starter card
     * @param front side of the starter card
     */
    public StarterCardMessage(Player player, boolean front) {
        this.player = player;
        this.front = front;
    }


    public Player getPlayer() {return player;}
    public int isFront() {
        if (front) return 1;
        return 0;
    }

    public void setFront(boolean front) {this.front = front;}
}
