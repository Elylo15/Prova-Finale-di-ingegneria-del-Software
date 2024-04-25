package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.StarterCard;

public class StarterCardMessage {
    private String playerName;
    private CommonArea commonArea;
    private PlaceableCard starterCard;
    private boolean front;

    /**
     * Constructor used for the messages from client to server.
     * @param playerName nickname of the player.
     * @param starterCard starterCard
     * @param front side of the starterCard
     */
    public StarterCardMessage(String playerName,PlaceableCard starterCard, boolean front) {
        this.playerName = playerName;
        this.commonArea = null;
        this.starterCard = starterCard;
        this.front = front;
    }

    /**
     * Constructor used for the messages from server to client.
     * @param commonArea commonArea
     * @param starterCard starterCard
     */
    public StarterCardMessage(CommonArea commonArea, PlaceableCard starterCard) {
        this.playerName = null;
        this.commonArea = commonArea;
        this.starterCard = starterCard;
        this.front = true;
    }

    public String getPlayerName() {return playerName;}
    public CommonArea getCommonArea() {return commonArea;}
    public PlaceableCard getStarterCard() {return starterCard;}
    public boolean isFront() {return front;}

    public void setFront(boolean front) {this.front = front;}
}
