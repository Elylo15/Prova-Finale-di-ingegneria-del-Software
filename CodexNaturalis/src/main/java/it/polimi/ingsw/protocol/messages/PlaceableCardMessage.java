package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PlaceableCard;

public class PlaceableCardMessage extends Message{
    private final Player player;
    private final int positionInHand;
    private final int x;
    private final int y;
    private final boolean front;

    public PlaceableCardMessage(Player player, int positionInHand, int x, int y, boolean front) {
        this.player = player;
        this.positionInHand = positionInHand;
        this.x = x;
        this.y = y;
        this.front = front;
    }

    public Player getPlayer() {return player;}
    public int getPositionInHand() {return positionInHand;}
    public int getX() {return x;}
    public int getY() {return y;}
    public boolean isFront() {return front;}

}
