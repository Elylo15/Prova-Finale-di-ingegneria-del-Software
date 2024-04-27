package it.polimi.ingsw.protocol.messages.PlayerTurn;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class UpdatePlayerMessage implements Message, Serializable {
    private final String player;


    public UpdatePlayerMessage(String player) {
        this.player = player;

    }

    public String getPlayer() {
        return player;
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
