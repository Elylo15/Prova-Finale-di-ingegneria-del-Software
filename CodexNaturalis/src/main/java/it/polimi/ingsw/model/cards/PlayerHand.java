package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
/**
 * PlayerHand class
 * @author elylo
 */
public class PlayerHand {
    ArrayList<PlaceableCard> placeableGoldCards;

    /**
     * Class costructor
     */
    public PlayerHand(){
        placeableGoldCards = new ArrayList<PlaceableCard>();
    }
    /**
     * method removeplaceableCard: removes placeableCard from PlayerHand. Returns the placeableCard
     * @param numCard: id of the card the player wants to play
     */
    public PlaceableCard removeplaceableCard(int numCard){
        PlaceableCard c = null;
        for (int i = 0; i < placeableGoldCards.size() ; i++) {
            if (numCard == placeableGoldCards.get(i).getID()){
                c= placeableGoldCards.get(i);
                placeableGoldCards.remove(c);
            }
        }
        return c;
    }
    /**
     * method getplaceableCard: returns the placeableCards
     */
    public ArrayList<PlaceableCard> getPlaceableCards(){
        return placeableGoldCards;
    }

    /**
     * method addNewplaceableCard: adds a new placeableCard (drawn by the player) to PlayerHand
     * @param card: card drawn by the player to add to the PlayerHand
     */
    public void addNewplaceableCard(PlaceableCard card){
        placeableGoldCards.add(card);
    }

}
