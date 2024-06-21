package it.polimi.ingsw.model.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class represents a player's hand in the game.
 * It provides methods for adding and removing cards from the hand, and getting the cards in the hand.
 * It implements the Serializable interface.
 */
public class PlayerHand implements Serializable {
    private final ArrayList<PlaceableCard> placeableCards;

    /**
     * This is the constructor for the PlayerHand class.
     * It initializes the list of placeable cards.
     */
    public PlayerHand() {
        placeableCards = new ArrayList<>();
    }

    /**
     * This method removes a placeable card from the player's hand.
     * It returns the removed card.
     *
     * @param numCard The ID of the card to remove from the hand.
     * @return PlaceableCard The removed card. If the card is not found, it returns null.
     */
    public PlaceableCard removeplaceableCard(int numCard) {
        PlaceableCard c = null;
        for (int i = 0; i < placeableCards.size(); i++) {
            if (numCard == placeableCards.get(i).getID()) {
                c = placeableCards.get(i);
                placeableCards.remove(c);  //remove the card from the arraylist of placeableCards passing as a parameter the card to remove
            }
        }
        return c;
    }

    /**
     * This method returns the list of placeable cards in the player's hand.
     *
     * @return ArrayList<PlaceableCard> The list of placeable cards in the player's hand.
     */
    public ArrayList<PlaceableCard> getPlaceableCards() {
        return placeableCards;
    }

    /**
     * This method adds a new placeable card to the player's hand.
     *
     * @param card The card to add to the player's hand.
     */
    public void addNewPlaceableCard(PlaceableCard card) {
        placeableCards.add(card);
    }

    /**
     * This method checks if the specified object is equal to this PlayerHand.
     * It overrides the equals method from the Object class.
     *
     * @param o The object to compare with this PlayerHand.
     * @return boolean True if the specified object is equal to this PlayerHand, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerHand that = (PlayerHand) o;
        return Objects.equals(placeableCards, that.placeableCards);
    }

    /**
     * This method returns the hash code of this PlayerHand.
     * It overrides the hashCode method from the Object class.
     *
     * @return int The hash code of this PlayerHand.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(placeableCards);
    }

    /**
     * This method returns a string representation of this PlayerHand.
     * It overrides the toString method from the Object class.
     *
     * @return String A string representation of this PlayerHand.
     */
    @Override
    public String toString() {
        return "PlayerHand{" +
                "placeableCards=" + placeableCards.stream().map(Card::getID).collect(Collectors.toCollection(ArrayList::new)) +
                '}';
    }

}
