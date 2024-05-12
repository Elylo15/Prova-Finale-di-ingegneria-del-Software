package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;

public class currentStateMessage implements Message, Serializable {
    private final Player currentPlayer; // player who is gaming
    private final Player player;        // player who receives the message
    private final String stateName;
    private final boolean lastTurn;
    private final ArrayList<String> onlinePlayers;
    private final ObjectiveCard[] commonObjectiveCards;

    public currentStateMessage(Player currentPlayer, Player player, String stateName, boolean lastTurn, ArrayList<String> onlinePlayers, ObjectiveCard[] commonObjectiveCards){
        this.currentPlayer = currentPlayer;
        this.player = player;
        this.stateName = stateName;
        this.lastTurn = lastTurn;
        this.onlinePlayers = onlinePlayers;
        this.commonObjectiveCards = commonObjectiveCards;
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

    public ArrayList<String> getOnlinePlayers() {return onlinePlayers;}

    public ObjectiveCard[] getCommonObjectiveCards() {return commonObjectiveCards;}

    @Override
    public String toString() {
        return "currentStateMessage{" +
                "currentPlayer=" + currentPlayer.getNickname() +
                ", player=" + player.getNickname() +
                ", stateName='" + stateName + '\'' +
                ", lastTurn=" + lastTurn +
                '}';
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
