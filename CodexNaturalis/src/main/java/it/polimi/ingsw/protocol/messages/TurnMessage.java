package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public class TurnMessage extends Message implements Serializable {
    private Player currentPlayer;
    private CommonArea commonArea;
    private boolean lastTurn;

    public TurnMessage(Player currentPlayer, CommonArea commonArea, boolean lastTurn) {
        this.currentPlayer = currentPlayer;
        this.commonArea = commonArea;
        this.lastTurn = lastTurn;
    }

    public Player getCurrentPlayer() {return currentPlayer;}
    public CommonArea getCommonArea() {return commonArea;}
    public boolean getLastTurn() {return lastTurn;}
}
