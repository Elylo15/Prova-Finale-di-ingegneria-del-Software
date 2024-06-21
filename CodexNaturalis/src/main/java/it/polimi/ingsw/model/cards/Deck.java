package it.polimi.ingsw.model.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This class represents a deck of cards.
 * It provides methods for adding, removing, shuffling cards and getting the size of the deck.
 * The type of cards in the deck is a subtype of the Card class.
 *
 * @param <E> This is a subtype of the Card class.
 */
public class Deck<E extends Card> implements Serializable {
    private final ArrayList<E> list;

    /**
     * This is the constructor for the Deck class.
     * It initializes the list of cards.
     */
    public Deck() {
        list = new ArrayList<>();
    }

    /**
     * This method adds a card to the end of the deck.
     *
     * @param c The card to add to the end of the deck.
     */
    public void addCard(E c) {
        list.add(c);
    }

    /**
     * This method shuffles the cards in the deck.
     */
    public void shuffle() {
        Collections.shuffle(list);
    }

    /**
     * This method removes the top card from the deck and returns it.
     *
     * @return E The removed card. If the deck is empty, it returns null.
     */
    public E removeCard() {
        if (list.isEmpty())
            return null;
        else
            return list.removeFirst();
    }

    /**
     * This method returns the number of cards in the deck.
     *
     * @return int The number of cards in the deck.
     */
    public int getSize() {
        return list.size();
    }

    /**
     * This method returns the card that matches the given ID.
     *
     * @param ID The ID of the card to look for in the deck.
     * @return Card The card that matches the given ID. If no card matches the ID, it returns null.
     */
    public Card getCard(int ID) {
        for (E e : list) {
            if (ID == e.ID) {
                return e;
            }
        }
        return null;
    }

    /**
     * This method returns a clone of the list of cards in the deck.
     *
     * @return ArrayList<E> A clone of the list of cards in the deck.
     */
    public ArrayList<E> getList() {

        return (ArrayList<E>) list.clone();
    }

}
