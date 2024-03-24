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
    ArrayList<Card> tableCards;
    /**
     * Class costructor
     */
    public CommonArea(){

        d1 = new Deck<ResourceCard>();
        d2 = new Deck<GoldCard>();
        d3 = new Deck<StarterCard>();
        d4 = new Deck<ObjectiveCard>();
        tableCards = new ArrayList<Card>();
    }
    /**
     * method pickTableCard: remove a face-up card
     * @param cardNumber: card you want to take from the exposed cards
     */
    public Card pickTableCard(int cardNumber){
        for (Card tableCard : tableCards) {
            if (cardNumber == tableCard.getID()) {
                return tableCard;
            }
        }

        return null;
    }
    /**
     * method drawFromDeck: remove the top resource card of the deck and places it as a face-up card
     * @param d: number of the deck from witch you want to remove a card to put in among those discovered
     */
    private void drawFromDeck(int d){
        Card c = null;
        switch (d) {
            case 1 -> c= d1.removeCard();
            case 2 -> c= d2.removeCard();
            case 3 -> c = d3.removeCard();
            case 4 -> c= d4.removeCard();
        };
        tableCards.add(c);
    }
    /**
     * method drawFromPlayer: the player draws from the deck and keeps the card
     * @param d: number of the deck from wich the player wants to draw
     */
    public Card drawFromToPlayer(int d){
        Card c = null;
        switch (d) {
            case 1 -> c= d1.removeCard();
            case 2 -> c= d2.removeCard();
            case 3 -> c = d3.removeCard();
            case 4 -> c= d4.removeCard();
        };
        return c;
    }
    /**
     * method getTableCards:show the player the cards face-up
     */
    public ArrayList<Card> getTableCards(){
        return tableCards;
    }

}
