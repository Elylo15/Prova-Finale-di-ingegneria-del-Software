package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * CommonArea class
 *
 * @author elylo
 */
public class CommonArea implements Serializable {
    private final Deck<ResourceCard> d1;
    private final Deck<GoldCard> d2;
    private final Deck<StarterCard> d3;
    private final Deck<ObjectiveCard> d4;
    private final ArrayList<PlaceableCard> tableCards;

    /**
     * Class constructor
     */
    public CommonArea() {

        d1 = new Deck<>(); //resource cards
        d2 = new Deck<>(); //gold cards
        d3 = new Deck<>(); //starter cards
        d4 = new Deck<>(); //objective cards
        tableCards = new ArrayList<>();
    }

    /**
     * method pickTableCard: remove a face-up card
     *
     * @param cardNumber: card you want to take from the exposed cards
     * @return PlaceableCard: card removed from those face up and which will go to a player
     * @throws IllegalArgumentException: if the card is a StarterCard
     */
    public PlaceableCard pickTableCard(int cardNumber) {
        PlaceableCard c;
        int cardIndex;

        for (int i = 0; i < tableCards.size(); i++) {
            if (cardNumber == tableCards.get(i).getID()) {
                if (tableCards.get(i) instanceof StarterCard) {
                    throw new IllegalArgumentException("Cannot pick StarterCard");
                }
                c = tableCards.get(i); // get the card to remove
                cardIndex = i; // Save the index of the card to remove
                tableCards.remove(cardIndex);
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
     * method drawFromDeck: remove the top GoldCard/ResourceCard of the deck and places it as a face-up card
     *
     * @param d:     number of the deck from which you want to remove a card to put in among the face up cards
     * @param index: index where you want to place the card
     */
    public void drawFromDeck(int d, int index) {
        PlaceableCard c = null;
        switch (d) {
            case 1 -> c = d1.removeCard();
            case 2 -> c = d2.removeCard();
        }
        if (c != null && index >= 0 && index <= tableCards.size()) {
            tableCards.add(index, c); // Add the card to the same position in the tableCards
        } else if (c != null) {
            tableCards.add(c); // Fallback: Add the card to the end if the index is invalid
        }
    }


    /**
     * method drawFromDeck: remove the top GoldCard/ResourceCard of the deck and places it as a face-up card
     *
     * @param d: number of the deck from which you want to remove a card to put in among the face up cards
     */
    public void drawFromDeck(int d) {
        PlaceableCard c = null;
        switch (d) {
            case 1 -> c = d1.removeCard();
            case 2 -> c = d2.removeCard();
        }
        tableCards.add(c); //add the card to the table cards
    }

    /**
     * method drawFromPlayer: the player draws from the GoldCardDeck/ResourceCardDeck/StarterCardDeck and keeps the card
     *
     * @param d: number of the deck from which the player wants to draw
     * @return PlaceableCard: card removed from a deck and which will go to a player
     */
    public PlaceableCard drawFromToPlayer(int d) {
        PlaceableCard c = null;
        switch (d) {
            case 1 -> c = d1.removeCard();
            case 2 -> c = d2.removeCard();
            case 3 -> c = d3.removeCard();
            case 4 -> throw new IllegalArgumentException("Cannot draw from ObjectiveCardDeck");
        }
        return c;  //return the card picked
    }

    /**
     * method getTableCards:show the player the cards face-up
     *
     * @return ArrayList<PlaceableCard>: array list of face-up cards
     */
    public ArrayList<PlaceableCard> getTableCards() {
        return tableCards;
    }

    /**
     * method drawObjectiveCard:the player draws from the ObjectiveCardDeck and keeps the card
     *
     * @return ObjectiveCard: ObjectiveCard that will be given to a player
     */
    public ObjectiveCard drawObjectiveCard() {
        return d4.removeCard();

    }

    /**
     * method  getD1: Resource deck
     *
     * @return Deck<ResourceCard>: return all ResourceCardDeck
     */
    public Deck<ResourceCard> getD1() {
        return d1;
    }

    /**
     * method  getD2: GoldCard deck
     *
     * @return Deck<GoldCard>: return all GoldCardDeck
     */
    public Deck<GoldCard> getD2() {
        return d2;
    }

    /**
     * method  getD3: StarterCard deck
     *
     * @return Deck<GoldCard>: return all StarterCardDeck
     */
    public Deck<StarterCard> getD3() {
        return d3;
    }

    /**
     * method  getD4: ObjectiveCard deck
     *
     * @return Deck<GoldCard>: return all ObjectiveCardDeck
     */
    public Deck<ObjectiveCard> getD4() {
        return d4;
    }

}
