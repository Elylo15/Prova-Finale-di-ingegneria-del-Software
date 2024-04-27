package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PlaceableCard;

public class PlaceableCardMessage extends Message{
    private final Player player;
    private final PlaceableCard placeableCard;
    private final int x;
    private final int y;
    private final boolean front;

    public PlaceableCardMessage(Player player, PlaceableCard placeableCard, int x, int y, boolean front) {
        this.player = player;
        this.placeableCard = placeableCard;
        this.x = x;
        this.y = y;
        this.front = front;
    }

    public Player getPlayer() {return player;}
    public PlaceableCard getPlaceableCard() {return placeableCard;}
    public int getX() {return x;}
    public int getY() {return y;}
    public boolean isFront() {return front;}

}
