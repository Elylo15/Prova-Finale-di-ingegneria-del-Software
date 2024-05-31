package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck<StarterCard> deckStarterCard;
    private Deck<GoldCard> deckGoldCard;
    private Deck<ResourceCard> deckResourceCard;
    private Deck<ObjectiveCard> deckObjectiveCard;


    private StarterCard starterCard1;
    private GoldCard goldCard1;
    private ResourceCard resourceCard1;
    private ObjectiveCard objectiveCard1;

    private StarterCard starterCard2;
    private GoldCard goldCard2;
    private ResourceCard resourceCard2;
    private ObjectiveCard objectiveCard2;

    @BeforeEach
    void setUp() throws InvalidIdException {
        deckStarterCard = new Deck<>();
        deckGoldCard = new Deck<>();
        deckResourceCard = new Deck<>();
        deckObjectiveCard = new Deck<>();


        starterCard1 = new StarterCard(81);
        goldCard1 = new GoldCard(41);
        resourceCard1 = new ResourceCard(1);
        objectiveCard1 = new ObjectiveCard(87);

        starterCard2 = new StarterCard(82);
        goldCard2 = new GoldCard(42);
        resourceCard2 = new ResourceCard(2);
        objectiveCard2 = new ObjectiveCard(88);

    }

    // Test for the first addCard method
    @Test
    void addFirstStarterCard() {
        deckStarterCard.addCard(starterCard1);
        assertEquals(1, deckStarterCard.getSize());
        assertInstanceOf(StarterCard.class, deckStarterCard.getList().getFirst());
    }

    @Test
    void addFirstGoldCard() {
        deckGoldCard.addCard(goldCard1);
        assertEquals(1, deckGoldCard.getSize());
        assertInstanceOf(GoldCard.class, deckGoldCard.getList().getFirst());
    }

    @Test
    void addFirstResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        assertEquals(1, deckResourceCard.getSize());
        assertInstanceOf(ResourceCard.class, deckResourceCard.getList().getFirst());
    }

    @Test
    void addFirstObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        assertEquals(1, deckObjectiveCard.getSize());
        assertInstanceOf(ObjectiveCard.class, deckObjectiveCard.getList().getFirst());
    }

    // Test for the second addCard method
    @Test
    void addStarterCardToEndOfDeck() {
        deckStarterCard.addCard(starterCard1);
        deckStarterCard.addCard(starterCard2);
        assertEquals(2, deckStarterCard.getSize());
        assertEquals(starterCard2, deckStarterCard.getList().getLast());
    }

    @Test
    void addGoldCardToEndOfDeck() {
        deckGoldCard.addCard(goldCard1);
        deckGoldCard.addCard(goldCard2);
        assertEquals(2, deckGoldCard.getSize());
        assertEquals(goldCard2, deckGoldCard.getList().getLast());
    }

    @Test
    void addResourceCardToEndOfDeck() {
        deckResourceCard.addCard(resourceCard1);
        deckResourceCard.addCard(resourceCard2);
        assertEquals(2, deckResourceCard.getSize());
        assertEquals(resourceCard2, deckResourceCard.getList().getLast());
    }

    @Test
    void addObjectiveCardToEndOfDeck() {
        deckObjectiveCard.addCard(objectiveCard1);
        deckObjectiveCard.addCard(objectiveCard2);
        assertEquals(2, deckObjectiveCard.getSize());
        assertEquals(objectiveCard2, deckObjectiveCard.getList().getLast());
    }
    // Test for the shuffle method
    @Test
    void shuffleStarterCardDeck() {
        deckStarterCard.addCard(starterCard1);
        deckStarterCard.addCard(starterCard2);
        deckStarterCard.shuffle();
        assertTrue(deckStarterCard.getList().contains(starterCard1));
        assertTrue(deckStarterCard.getList().contains(starterCard2));
    }

    @Test
    void shuffleGoldCardDeck() {
        deckGoldCard.addCard(goldCard1);
        deckGoldCard.addCard(goldCard2);
        deckGoldCard.shuffle();
        assertTrue(deckGoldCard.getList().contains(goldCard1));
        assertTrue(deckGoldCard.getList().contains(goldCard2));
    }

    @Test
    void shuffleResourceCardDeck() {
        deckResourceCard.addCard(resourceCard1);
        deckResourceCard.addCard(resourceCard2);
        deckResourceCard.shuffle();
        assertTrue(deckResourceCard.getList().contains(resourceCard1));
        assertTrue(deckResourceCard.getList().contains(resourceCard2));
    }

    @Test
    void shuffleObjectiveCardDeck() {
        deckObjectiveCard.addCard(objectiveCard1);
        deckObjectiveCard.addCard(objectiveCard2);
        deckObjectiveCard.shuffle();
        assertTrue(deckObjectiveCard.getList().contains(objectiveCard1));
        assertTrue(deckObjectiveCard.getList().contains(objectiveCard2));
    }
    // Test for the removeCard method

    @Test
    void removeStarterCard() {
        deckStarterCard.addCard(starterCard1);
        assertEquals(starterCard1, deckStarterCard.removeCard());
        assertEquals(0, deckStarterCard.getSize());
    }

    @Test
    void removeGoldCard() {
        deckGoldCard.addCard(goldCard1);
        assertEquals(goldCard1, deckGoldCard.removeCard());
        assertEquals(0, deckGoldCard.getSize());
    }

    @Test
    void removeResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        assertEquals(resourceCard1, deckResourceCard.removeCard());
        assertEquals(0, deckResourceCard.getSize());
    }

    @Test
    void removeObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        assertEquals(objectiveCard1, deckObjectiveCard.removeCard());
        assertEquals(0, deckObjectiveCard.getSize());
    }

    // Test for top card with removeCard method
    @Test
    void removeTopStarterCard() {
        deckStarterCard.addCard(starterCard1);
        deckStarterCard.addCard(starterCard2);
        assertEquals(starterCard1, deckStarterCard.removeCard());
        assertEquals(1, deckStarterCard.getSize());
    }

    @Test
    void removeTopGoldCard() {
        deckGoldCard.addCard(goldCard1);
        deckGoldCard.addCard(goldCard2);
        assertEquals(goldCard1, deckGoldCard.removeCard());
        assertEquals(1, deckGoldCard.getSize());
    }

    @Test
    void removeTopResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        deckResourceCard.addCard(resourceCard2);
        assertEquals(resourceCard1, deckResourceCard.removeCard());
        assertEquals(1, deckResourceCard.getSize());
    }

    // Test for the removeCard method with an empty deck
    @Test
    void removeCardFromEmptyStarterCard() {
        assertNull(deckStarterCard.removeCard());
    }

    @Test
    void removeCardFromEmptyGoldCard() {
        assertNull(deckGoldCard.removeCard());
    }

    @Test
    void removeCardFromEmptyResourceCard() {
        assertNull(deckResourceCard.removeCard());
    }

    @Test
    void removeCardFromEmptyObjectiveCard() {
        assertNull(deckObjectiveCard.removeCard());
    }

    @Test
    void removeTopObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        deckObjectiveCard.addCard(objectiveCard2);
        assertEquals(objectiveCard1, deckObjectiveCard.removeCard());
        assertEquals(1, deckObjectiveCard.getSize());
    }
    // Test for the getCard method
    @Test
    void getStarterCard() {
        deckStarterCard.addCard(starterCard1);
        assertEquals(starterCard1, deckStarterCard.getCard(81));
    }

    @Test
    void getGoldCard() {
        deckGoldCard.addCard(goldCard1);
        assertEquals(goldCard1, deckGoldCard.getCard(41));
    }

    @Test
    void getResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        assertEquals(resourceCard1, deckResourceCard.getCard(1));
    }

    @Test
    void getObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        assertEquals(objectiveCard1, deckObjectiveCard.getCard(87));
    }

    // Test for the getCard method with a card not in the deck

    @Test
    void getCardNotInStarterCard() {
        deckStarterCard.addCard(starterCard1);
        assertNull(deckStarterCard.getCard(82));
    }

    @Test
    void getCardNotInGoldDeck() {
        deckGoldCard.addCard(goldCard1);
        assertNull(deckGoldCard.getCard(42));
    }

    @Test
    void getCardNotInResourceDeck() {
        deckResourceCard.addCard(resourceCard1);
        assertNull(deckResourceCard.getCard(2));
    }

    @Test
    void getCardNotInObjectiveDeck() {
        deckObjectiveCard.addCard(objectiveCard1);
        assertNull(deckObjectiveCard.getCard(88));
    }

    // Test for the getSize method
    @Test
    void getSizeStarterCard() {
        deckStarterCard.addCard(starterCard1);
        assertEquals(1, deckStarterCard.getSize());
    }

    @Test
    void getSizeGoldCard() {
        deckGoldCard.addCard(goldCard1);
        assertEquals(1, deckGoldCard.getSize());
    }

    @Test
    void getSizeResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        assertEquals(1, deckResourceCard.getSize());
    }

    @Test
    void getSizeObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        assertEquals(1, deckObjectiveCard.getSize());
    }

    // Test for the getSize method with multiple cards
    @Test
    void getSizeMultipleStarterCard() {
        deckStarterCard.addCard(starterCard1);
        deckStarterCard.addCard(starterCard2);
        assertEquals(2, deckStarterCard.getSize());
    }

    @Test
    void getSizeMultipleGoldCard() {
        deckGoldCard.addCard(goldCard1);
        deckGoldCard.addCard(goldCard2);
        assertEquals(2, deckGoldCard.getSize());
    }

    @Test
    void getSizeMultipleResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        deckResourceCard.addCard(resourceCard2);
        assertEquals(2, deckResourceCard.getSize());
    }

    @Test
    void getSizeMultipleObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        deckObjectiveCard.addCard(objectiveCard2);
        assertEquals(2, deckObjectiveCard.getSize());
    }

    // Test for the getList method
    @Test
    void getListStarterCard() {
        deckStarterCard.addCard(starterCard1);
        assertEquals(starterCard1, deckStarterCard.getList().getFirst());
    }

    @Test
    void getListGoldCard() {
        deckGoldCard.addCard(goldCard1);
        assertEquals(goldCard1, deckGoldCard.getList().getFirst());
    }

    @Test
    void getListResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        assertEquals(resourceCard1, deckResourceCard.getList().getFirst());
    }

    @Test
    void getListObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        assertEquals(objectiveCard1, deckObjectiveCard.getList().getFirst());
    }

    // Test for the getList method with multiple cards
    @Test
    void getListMultipleStarterCard() {
        deckStarterCard.addCard(starterCard1);
        deckStarterCard.addCard(starterCard2);
        assertEquals(starterCard1, deckStarterCard.getList().getFirst());
        assertEquals(starterCard2, deckStarterCard.getList().getLast());
    }

    @Test
    void getListMultipleGoldCard() {
        deckGoldCard.addCard(goldCard1);
        deckGoldCard.addCard(goldCard2);
        assertEquals(goldCard1, deckGoldCard.getList().getFirst());
        assertEquals(goldCard2, deckGoldCard.getList().getLast());
    }

    @Test
    void getListMultipleResourceCard() {
        deckResourceCard.addCard(resourceCard1);
        deckResourceCard.addCard(resourceCard2);
        assertEquals(resourceCard1, deckResourceCard.getList().getFirst());
        assertEquals(resourceCard2, deckResourceCard.getList().getLast());
    }

    @Test
    void getListMultipleObjectiveCard() {
        deckObjectiveCard.addCard(objectiveCard1);
        deckObjectiveCard.addCard(objectiveCard2);
        assertEquals(objectiveCard1, deckObjectiveCard.getList().getFirst());
        assertEquals(objectiveCard2, deckObjectiveCard.getList().getLast());
    }

    // Test for the getList method with an empty deck
    @Test
    void getListEmptyStarterCard() {
        assertTrue(deckStarterCard.getList().isEmpty());
    }

    @Test
    void getListEmptyGoldCard() {
        assertTrue(deckGoldCard.getList().isEmpty());
    }

    @Test
    void getListEmptyResourceCard() {
        assertTrue(deckResourceCard.getList().isEmpty());
    }

    @Test
    void getListEmptyObjectiveCard() {
        assertTrue(deckObjectiveCard.getList().isEmpty());
    }

}