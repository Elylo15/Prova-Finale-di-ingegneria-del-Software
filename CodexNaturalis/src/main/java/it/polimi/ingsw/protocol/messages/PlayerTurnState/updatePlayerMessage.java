package it.polimi.ingsw.protocol.messages.PlayerTurnState;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class updatePlayerMessage implements Message, Serializable {
    private final Player player;


    public updatePlayerMessage(Player player) {
        this.player = player;

    }

    public Player getPlayer() {
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
