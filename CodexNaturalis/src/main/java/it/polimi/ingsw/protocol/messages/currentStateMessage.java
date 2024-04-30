package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.Player;

public class currentStateMessage implements Message{
    private final Player currentPlayer; // player who is gaming
    private final Player player;        // player who receives the message
    private final String stateName;
    private final boolean lastTurn;

    public currentStateMessage(Player currentPlayer, Player player, String stateName, boolean lastTurn){
        this.currentPlayer = currentPlayer;
        this.player = player;
        this.stateName = stateName;
        this.lastTurn = lastTurn;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getCurrentPlayer() {return currentPlayer;}

    public String getStateName() {
        return stateName;
    }
    
    public boolean isLastTurn() {
        return lastTurn;
    }

    @Override
    public void setLock() {}

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {}
}
