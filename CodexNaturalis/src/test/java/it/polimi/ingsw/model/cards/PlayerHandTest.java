package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerHandTest {
    private PlayerHand playerHand;
    private ResourceCard card1;
    private GoldCard card2;

    @BeforeEach
    void setUp() throws InvalidIdException {
        playerHand = new PlayerHand();
        card1 = new ResourceCard(1);
        card2 = new GoldCard(41);
        playerHand.addNewplaceableCard(card1);
        playerHand.addNewplaceableCard(card2);
    }
    //Test for the method removeplaceableCard

    @Test
    void removeCorrectplaceableCard() {
        PlaceableCard removedCard = playerHand.removeplaceableCard(card1.getID());
        assertEquals(card1, removedCard);
        assertFalse(playerHand.getPlaceableCards().contains(card1));
    }

    @Test
    void removeplaceableCardReturnsNullWhenCardNotFound() {
        PlaceableCard removedCard = playerHand.removeplaceableCard(999);
        assertNull(removedCard);
    }

    //Test for the method addNewplaceableCard

    @Test
    void addNewplaceableCardAddsCardToHand() throws InvalidIdException {
        playerHand.addNewplaceableCard(card1);
        assertTrue(playerHand.getPlaceableCards().contains(card1));
    }

}