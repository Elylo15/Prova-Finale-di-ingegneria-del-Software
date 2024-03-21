package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class CommonArea {
    Deck<ResourceCard> d1;
    Deck<GoldCard> d2;
    Deck<StarterCard> d3;
    Deck<ObjectiveCard> d4;

    ArrayList<Card> tableCards;

    public CommonArea(){

        d1 = new Deck<ResourceCard>();
        d2 = new Deck<GoldCard>();
        d3 = new Deck<StarterCard>();
        d4 = new Deck<ObjectiveCard>();
        tableCards = new ArrayList<Card>();
    }

    public Card pickTableCard(int cardNumber){
        //solo le carte risorsa e oro posso essere tolte dalle carte scoperte
       //devo capire come da un id posso risalire al tipo di una carta
        for (int i = 0; i < tableCards.size(); i++) {
            if(cardNumber == tableCards.get(i).getID()){
                return tableCards.get(i);
            }
        }

        return null;
    }

    public Card drawFromDeck(int d){
       return null;
    }

    public Card drawFromToPlayer(){
        return null;
    }

    public ArrayList<Card> getTableCards(){
        return tableCards;
    }

}
