package it.polimi.ingsw.model.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck class
 * @author elylo
 */
public class Deck<E extends Card> implements Serializable {
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
     * @return E: returns the removed card which is a subtype of card
     */
    public E removeCard(){
        if (list.isEmpty())
            return null;
        else
            return list.removeFirst();
    }
    /**
     * method getCardNumber: return the number of cards in the decks
     * @return int: number of cards in the decks
     */
    public int getSize(){
        return list.size();
    }

    /**
     * method getCard: return the cards equal to the ID
     * @param ID: id of the card I'm looking for in the deck
     * @return Card: get the card that matches with that ID
     */
    public Card getCard(int ID){
        for (E e : list) {
            if (ID == e.ID) {
                return e;
            }
        }
        return null;
    }
    /**
     * method getList: return the list
     * @return ArrayList<E>: deck list
     */
    public ArrayList<E> getList() {

        return (ArrayList<E>) list.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


}
