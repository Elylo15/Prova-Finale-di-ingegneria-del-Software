package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck class
 * @author elylo
 */
public class Deck {
    ArrayList<Card> list;
    /**
     * Class costructor
     */
    public Deck(){
       list = new ArrayList<Card>();
    }

    /**
     * method addCard: add a card to the end of the deck
     * @param c: cardo to add to the end of the deck
     */
    public void addGoldCard(Card c){
        list.add(c);
    }
    /**
     * method shuffle: shuffle the deck cards
     */
    public void shuffle(){
        Collections.shuffle(list);
    }
    /**
     * method removeCard:  remove the top card from deck
     */
    public ArrayList<Card> removeCard(){
        list.remove(0);
        return list;
    }
    /**
     * method getCardNumber: return the number of cards in the decks
     */
    public int getCardNumber(){
        return list.size();
    }


}
