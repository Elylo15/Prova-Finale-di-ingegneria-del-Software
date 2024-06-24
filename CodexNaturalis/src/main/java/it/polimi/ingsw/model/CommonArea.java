package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the common area in the game.
 * It contains decks of different types of cards and a list of face-up cards on the table.
 * It provides methods for drawing cards from the decks and picking face-up cards from the table.
 * It implements the Serializable interface.
 */
public class CommonArea implements Serializable {
    private final Deck<ResourceCard> d1;
    private final Deck<GoldCard> d2;
    private final Deck<StarterCard> d3;
    private final Deck<ObjectiveCard> d4;
    private final ArrayList<PlaceableCard> tableCards;

    /**
     * This is the constructor for the CommonArea class.
     * It initializes the decks and the list of face-up cards.
     */
    public CommonArea() {
        d1 = new Deck<>(); // Initialize the deck of resource cards
        d2 = new Deck<>(); // Initialize the deck of gold cards
        d3 = new Deck<>(); // Initialize the deck of starter cards
        d4 = new Deck<>(); // Initialize the deck of objective cards
        tableCards = new ArrayList<>(); // Initialize the list of face-up cards
        for (int i = 0; i < 4; i++) {
            tableCards.add(null); //Initialize the list of face-up cards with 4 nulls
        }
    }

    /**
     * This method removes a face-up card from the table.
     * It throws an IllegalArgumentException if the card is a StarterCard.
     *
     * @param cardNumber The ID of the card to remove from the table.
     * @return PlaceableCard The removed card. If the card is not found, it returns null.
     */
    public PlaceableCard pickTableCard(int cardNumber) {
        PlaceableCard c;
        int cardIndex;

        for (int i = 0; i < tableCards.size(); i++) { // Iterate over the face-up cards that should be 4, if there is no card with the given ID return null
            if (tableCards.get(i) == null) {
                return null;
            } else if (cardNumber == tableCards.get(i).getID()) {
                if (tableCards.get(i) instanceof StarterCard) {
                    throw new IllegalArgumentException("Cannot pick StarterCard");
                }
                c = tableCards.get(i); // get the card to remove
                cardIndex = i; // Save the index of the card to remove
                tableCards.set(cardIndex, null); //put null in the place of the card removed
                if (c.getClass() == ResourceCard.class) {
                    //if the card removed is a resource card pick a card from first deck to replace it, otherwise from second deck
                    drawFromDeck(1, cardIndex);
                } else {
                    drawFromDeck(2, cardIndex);
                }
                return c;
            }
        }

        return null;
    }

    /**
     * This method removes the top card from the specified deck and places it at the specified position among the face-up cards.
     *
     * @param d     The number of the deck from which to remove a card.
     * @param index The index at which to place the card among the face-up cards.
     */
    public void drawFromDeck(int d, int index) {
        PlaceableCard c = null;
        switch (d) {
            case 1 -> c = d1.removeCard(); //null if deck is empty
            case 2 -> c = d2.removeCard();
        }
        if (index >= 0 && index < 4) {
            tableCards.set(index, c); // Add the card to the same position in the tableCards
        } else {
            tableCards.add(c); // Fallback: Add the card to the end if the index is invalid
        }

        //By accepting the null value, we have an ArrayList with gaps, so the cards position of
        //the tableCards will not change when a card is picked
    }

    /**
     * This method removes the top card from the specified deck and gives it to a player.
     * It throws an IllegalArgumentException if the deck is the ObjectiveCardDeck.
     *
     * @param d The number of the deck from which the player wants to draw.
     * @return PlaceableCard The removed card.
     */
    public PlaceableCard drawFromToPlayer(int d) {
        PlaceableCard c = null;
        switch (d) {
            case 1 -> c = d1.removeCard();
            case 2 -> c = d2.removeCard();
            case 3 -> c = d3.removeCard();
            case 4 -> throw new IllegalArgumentException("Cannot draw from ObjectiveCardDeck");
        }
        return c;  //return the card picked (null if there is no card)
    }

    /**
     * This method returns the list of face-up cards on the table.
     *
     * @return ArrayList<PlaceableCard> The list of face-up cards on the table.
     */
    public ArrayList<PlaceableCard> getTableCards() {
        return tableCards;
    }

    /**
     * This method removes the top card from the ObjectiveCardDeck and gives it to a player.
     *
     * @return ObjectiveCard The removed card.
     */
    public ObjectiveCard drawObjectiveCard() {
        return d4.removeCard();

    }

    /**
     * This method returns the deck of resource cards.
     *
     * @return Deck<ResourceCard> The deck of resource cards.
     */
    public Deck<ResourceCard> getD1() {
        return d1;
    }

    /**
     * This method returns the deck of gold cards.
     *
     * @return Deck<GoldCard> The deck of gold cards.
     */
    public Deck<GoldCard> getD2() {
        return d2;
    }

    /**
     * This method returns the deck of starter cards.
     *
     * @return Deck<StarterCard> The deck of starter cards.
     */
    public Deck<StarterCard> getD3() {
        return d3;
    }

    /**
     * This method returns the deck of objective cards.
     *
     * @return Deck<ObjectiveCard> The deck of objective cards.
     */
    public Deck<ObjectiveCard> getD4() {
        return d4;
    }

}
