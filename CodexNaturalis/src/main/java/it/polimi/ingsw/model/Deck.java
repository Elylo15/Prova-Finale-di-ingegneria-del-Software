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

    public void addGoldCard(Card c){
        list.add(c);
    }

    public void shuffle(){
        Collections.shuffle(list);
    }

    public ArrayList<Card> removeCard(){
        list.remove(0);
        return list;
    }

    public int getCardNumber(){
        return list.size();
    }


}
