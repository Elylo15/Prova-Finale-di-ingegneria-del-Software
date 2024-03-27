package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import java.util.ArrayList;

/**
 * CommonArea class
 * @author elylo
 */
public class CommonArea {
    Deck<ResourceCard> d1;
    Deck<GoldCard> d2;
    Deck<StarterCard> d3;
    Deck<ObjectiveCard> d4;
    ArrayList<PlaceableCard> tableCards;
    /**
     * Class costructor
     */
    public CommonArea(){

        d1 = new Deck<ResourceCard>();
        d2 = new Deck<GoldCard>();
        d3 = new Deck<StarterCard>();
        d4 = new Deck<ObjectiveCard>();
        tableCards = new ArrayList<PlaceableCard>();
    }
    /**
     * method pickTableCard: remove a face-up card
     * @param cardNumber: card you want to take from the exposed cards
     */
    public PlaceableCard pickTableCard(int cardNumber){
        for (PlaceableCard tableCard : tableCards) {
            if (cardNumber == tableCard.getID()) {
                return tableCard;
            }
        }

        return null;
    }
    /**
     * method drawFromDeck: remove the top GoldCard/ResourceCard of the deck and places it as a face-up card
     * @param d: number of the deck from witch you want to remove a card to put in among those discovered
     */
    private void drawFromDeck(int d){
        PlaceableCard c = null;
        switch (d) {
            case 1 -> c= d1.removeCard();
            case 2 -> c= d2.removeCard();
        };
        tableCards.add(c);
    }
    /**
     * method drawFromPlayer: the player draws from the GoldCardDeck/ResourceCardDeck/StarterCardDeck and keeps the card
     * @param d: number of the deck from wich the player wants to draw
     */
    public PlaceableCard drawFromToPlayer(int d){
        PlaceableCard c = null;
        switch (d) {
            case 1 -> c= d1.removeCard();
            case 2 -> c= d2.removeCard();
            case 3 -> c = d3.removeCard();
        };
        return c;
    }
    /**
     * method getTableCards:show the player the cards face-up
     */
    public ArrayList<PlaceableCard> getTableCards(){
        return tableCards;
    }

    /**
     * method drawObjectiveCard:the player draws from the ObjectiveCardDeck and keeps the card
     */
    public ObjectiveCard drawObjectiveCard(){
        return  d4.removeCard();

    }
    public Deck<ResourceCard> getD1() {
        return d1;
    }

    public Deck<GoldCard> getD2() {
        return d2;
    }

    public Deck<StarterCard> getD3() {
        return d3;
    }

    public Deck<ObjectiveCard> getD4() {
        return d4;
    }


}
