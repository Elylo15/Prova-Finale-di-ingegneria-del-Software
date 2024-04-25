package it.polimi.ingsw.model.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * PlayerHand class
 * @author elylo
 */
public class PlayerHand implements Serializable {
    ArrayList<PlaceableCard> placeableCards;

    /**
     * Class costructor
     */
    public PlayerHand(){
        placeableCards = new ArrayList<PlaceableCard>();
    }
    /**
     * method removeplaceableCard: removes placeableCard from PlayerHand. Returns the placeableCard
     * @param numCard: id of the card the player wants to play
     * @return PlaceableCard: card played by the player
     */
    public PlaceableCard removeplaceableCard(int numCard){
        PlaceableCard c = null;
        for (int i = 0; i < placeableCards.size() ; i++) {
            if (numCard == placeableCards.get(i).getID()){
                c= placeableCards.get(i);
                placeableCards.remove(c);
            }
        }
        return c;
    }
    /**
     * method getplaceableCard: returns the placeableCards
     * @return ArrayList<PlaceableCard>: array list of player cards
     */
    public ArrayList<PlaceableCard> getPlaceableCards(){
        return placeableCards;
    }

    /**
     * method addNewplaceableCard: adds a new placeableCard (drawn by the player) to PlayerHand
     * @param card: card drawn by the player to add to the PlayerHand
     */
    public void addNewplaceableCard(PlaceableCard card){
        placeableCards.add(card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerHand that = (PlayerHand) o;
        return Objects.equals(placeableCards, that.placeableCards);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(placeableCards);
    }
}
