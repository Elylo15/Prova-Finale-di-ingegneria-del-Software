package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections; //bisogna chiedere se si può usare

public class Deck {
    ArrayList<Card> list;

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
