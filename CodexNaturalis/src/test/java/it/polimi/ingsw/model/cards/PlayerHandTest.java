package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        playerHand.addNewPlaceableCard(card1);
        playerHand.addNewPlaceableCard(card2);
    }

    //Test for the method removePlaceableCard

    @Test
    void removeCorrectPlaceableCard() {
        PlaceableCard removedCard = playerHand.removeplaceableCard(card1.getID());
        assertEquals(card1, removedCard);
        assertFalse(playerHand.getPlaceableCards().contains(card1));
    }

    @Test
    void removePlaceableCardReturnsNullWhenCardNotFound() {
        PlaceableCard removedCard = playerHand.removeplaceableCard(999);
        assertNull(removedCard);
    }

    //Test for the method addNewPlaceableCard

    @Test
    void addNewPlaceableCardAddsCardToHand() {
        playerHand.addNewPlaceableCard(card1);
        assertTrue(playerHand.getPlaceableCards().contains(card1));
    }

    @Test
    void toStringShouldReturnCorrectFormat() {
        String expected = "PlayerHand{placeableCards=[1, 41]}";
        System.out.println(playerHand.toString());
        assertEquals(expected, playerHand.toString());
    }

    @Test
    void toStringShouldReturnEmptyWhenNoCards() {
        String expected = "PlayerHand{placeableCards=[]}";
        playerHand = new PlayerHand();
        assertEquals(expected, playerHand.toString());
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentCards() {
        PlayerHand playerHand2 = new PlayerHand();
        playerHand2.addNewPlaceableCard(card1);
        assertNotEquals(playerHand.hashCode(), playerHand2.hashCode());
    }

    @Test
    void equalsShouldReturnTrue() {
        PlayerHand playerHand2 = new PlayerHand();
        playerHand2.addNewPlaceableCard(card1);
        playerHand2.addNewPlaceableCard(card2);
        assertTrue(playerHand.equals(playerHand2));
    }

    @Test
    void equalsShouldReturnFalse() throws InvalidIdException {
        PlayerHand playerHand2 = new PlayerHand();
        playerHand2.addNewPlaceableCard(new GoldCard(42));
        playerHand2.addNewPlaceableCard(card1);
        assertFalse(playerHand.equals(playerHand2));
    }

    @Test
    void equalsShouldReturnTrueSameObject() {
        assertTrue(playerHand.equals(playerHand));
    }

    @Test
    void equalsShouldReturnFalseNullAndDifferentClass() {
        assertFalse(playerHand.equals(null));
        assertFalse(playerHand.equals(new Object()));
    }

}