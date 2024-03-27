package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck class
 * @author elylo
 */
public class Deck<E extends Card> {
    ArrayList<E> list;
    /**
     * Class costructor
     */
    public Deck(){
       list = new ArrayList<E>();
    }

    /**
     * method addCard: add a card to the end of the deck
     * @param c: card to add to the end of the deck
     */
    public void addCard(E c){
        list.add(c);
    }
    /**
     * method shuffle: shuffle the deck cards
     */
    public void shuffle(){
        Collections.shuffle(list);
    }
    /**
     * method removeCard:  remove the top card from deck and return it
     */
    public E removeCard(){
        E card;
        card = list.getFirst();
        list.removeFirst();
        return card;
    }
    /**
     * method getCardNumber: return the number of cards in the decks
     */
    public int getCardNumber(){
        return list.size();
    }

    /**
     * method getCard: return the cards equal to the ID
     * @param ID: id of the card I'm looking for in the deck
     */
    public Card getCard(int ID){
        for (int i = 0; i < list.size(); i++) {
            if (ID== list.get(i).ID) {
                return list.get(i);
            }
        }
        return null;
    }
    /**
     * method getList: return the list
     */
    public ArrayList<E> getList() {
        return list;
    }
}
