package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonAreaTest {
    CommonArea commonArea;
    ResourceCard resourceCard;
    GoldCard goldCard;
    StarterCard starterCard;
    ObjectiveCard objectiveCard;

    ResourceCard resourceCard2;
    GoldCard goldCard2;

    @BeforeEach
    void setUp() throws InvalidIdException {
        commonArea = new CommonArea();
        resourceCard = new ResourceCard(1);
        goldCard = new GoldCard(41);
        starterCard = new StarterCard(81);
        objectiveCard = new ObjectiveCard(87);

        resourceCard2 = new ResourceCard(2);
        goldCard2 = new GoldCard(42);

    }

    //Test for the method pickTableCard
    @Test
    void pickTableResourceCardReturnsCorrectCard() {
        commonArea.getTableCards().set(0, resourceCard);
        assertEquals(resourceCard, commonArea.pickTableCard(resourceCard.getID()));
    }

    @Test
    void pickTableGoldCardReturnsCorrectCard() {
        commonArea.getTableCards().set(0, goldCard);
        assertEquals(goldCard, commonArea.pickTableCard(goldCard.getID()));
    }

    //Test for StartCard pickTableCard method
    @Test
    void pickTableStarterCardReturnsCorrectCard() {
        commonArea.getTableCards().set(0, starterCard);
        assertThrows(IllegalArgumentException.class, () -> commonArea.pickTableCard(starterCard.getID()));
    }

    @Test
    void pickTableCardRemovesCardFromTable() {
        commonArea.getTableCards().set(0, resourceCard);
        commonArea.pickTableCard(resourceCard.getID());
        assertFalse(commonArea.getTableCards().contains(resourceCard));
    }

    @Test
    void pickTableCardReturnsNullWhenCardNotFound() {
        assertNull(commonArea.pickTableCard(999));
    }
    //Test for drawToPlayer method

    @Test
    void drawFromToPlayerReturnsCorrectResourceCard() {
        commonArea.getD1().addCard(resourceCard);
        assertEquals(resourceCard, commonArea.drawFromToPlayer(1));
    }

    @Test
    void drawFromToPlayerReturnsCorrectGoldCard() {
        commonArea.getD2().addCard(goldCard);
        assertEquals(goldCard, commonArea.drawFromToPlayer(2));
    }

    @Test
    void drawFromToPlayerReturnsCorrectStarterCard() {
        commonArea.getD3().addCard(starterCard);
        assertEquals(starterCard, commonArea.drawFromToPlayer(3));
    }

    @Test
    void drawFromToPlayerReturnsCorrectObjectiveCard() {
        commonArea.getD4().addCard(objectiveCard);
        assertThrows(IllegalArgumentException.class, () -> commonArea.drawFromToPlayer(4));

    }

    @Test
    void drawFromToPlayerRemovesCardFromResourceDeck() {
        commonArea.getD1().addCard(resourceCard);
        commonArea.drawFromToPlayer(1);
        assertFalse(commonArea.getD1().getList().contains(resourceCard));
    }

    @Test
    void drawFromToPlayerRemovesCardFromGoldDeck() {
        commonArea.getD2().addCard(goldCard);
        commonArea.drawFromToPlayer(2);
        assertFalse(commonArea.getD2().getList().contains(goldCard));
    }

    @Test
    void drawFromToPlayerRemovesCardFromStarterDeck() {
        commonArea.getD3().addCard(starterCard);
        commonArea.drawFromToPlayer(3);
        assertFalse(commonArea.getD3().getList().contains(starterCard));
    }

    @Test
    void drawFromToPlayerReturnsNullWhenResourceDeckIsEmpty() {
        assertNull(commonArea.drawFromToPlayer(1));
    }

    @Test
    void drawFromToPlayerReturnsNullWhenGoldDeckIsEmpty() {
        assertNull(commonArea.drawFromToPlayer(2));
    }

    @Test
    void drawFromToPlayerReturnsNullWhenStarterDeckIsEmpty() {
        assertNull(commonArea.drawFromToPlayer(3));
    }

    //Test for drawObjectiveCard method
    @Test
    void drawObjectiveCardReturnsCorrectCard() {
        commonArea.getD4().addCard(objectiveCard);
        assertEquals(objectiveCard, commonArea.drawObjectiveCard());
    }

    @Test
    void drawObjectiveCardRemovesCardFromObjectiveDeck() {
        commonArea.getD4().addCard(objectiveCard);
        commonArea.drawObjectiveCard();
        assertFalse(commonArea.getD4().getList().contains(objectiveCard));
    }

    //Test for getTableCards method
    @Test
    void getCorrectNumberOfTableCards() {
        commonArea.getTableCards().set(0, resourceCard);
        commonArea.getTableCards().set(1, goldCard);
        commonArea.getTableCards().set(2, resourceCard2);
        commonArea.getTableCards().set(3, goldCard2);
        assertEquals(4, commonArea.getTableCards().size());
    }

    @Test
    void getCorrectTableCards() {
        //checks that the first two cards are Resource and the other two are Gold
        commonArea.getD1().addCard(resourceCard);
        commonArea.getD1().addCard(resourceCard2);
        commonArea.getD2().addCard(goldCard);
        commonArea.getD2().addCard(goldCard2);
        commonArea.drawFromDeck(1, 0);
        commonArea.drawFromDeck(1, 1);
        commonArea.drawFromDeck(2, 2);
        commonArea.drawFromDeck(2, 3);
        assertTrue(commonArea.getTableCards().get(0).isResource());
        assertTrue(commonArea.getTableCards().get(1).isResource());
        assertTrue(commonArea.getTableCards().get(2).isGold());
        assertTrue(commonArea.getTableCards().get(3).isGold());

    }

    @Test
    void DecksNotContainsCardsPicked() {
        commonArea.getD1().addCard(resourceCard);
        commonArea.getD2().addCard(goldCard);
        commonArea.drawFromToPlayer(1);
        commonArea.drawFromDeck(2, 0);
        assertFalse(commonArea.getD1().getList().contains(resourceCard));
        assertFalse(commonArea.getD2().getList().contains(goldCard));
    }

    @Test
    void getTableCardsReturnsNullWhenNoCards() {
        assertNull(commonArea.getTableCards().getFirst());
        assertNull(commonArea.getTableCards().getLast());
    }
}