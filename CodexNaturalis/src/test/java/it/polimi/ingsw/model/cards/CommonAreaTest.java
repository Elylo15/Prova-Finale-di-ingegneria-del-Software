package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CommonArea;
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

         resourceCard2= new ResourceCard(2);
         goldCard2 = new GoldCard(42);

    }
    //Test for the method pickTableCard
    @Test
    void pickTableResourceCardReturnsCorrectCard() {
        commonArea.getTableCards().add(resourceCard);
        assertEquals(resourceCard, commonArea.pickTableCard(resourceCard.getID()));
    }

    @Test
    void pickTableGoldCardReturnsCorrectCard() {
        commonArea.getTableCards().add(goldCard);
        assertEquals(goldCard, commonArea.pickTableCard(goldCard.getID()));
    }

    //Test for StartCard pickTableCard method
    @Test
    void pickTableStarterCardReturnsCorrectCard() {
        commonArea.getTableCards().add(starterCard);
        assertThrows(IllegalArgumentException.class, () -> commonArea.pickTableCard(starterCard.getID()));
    }

    @Test
    void pickTableCardRemovesCardFromTable() {
        commonArea.getTableCards().add(resourceCard);
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
    void drawFromToPlayerReturnsNullWhenReourceDeckIsEmpty() {
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
        commonArea.getTableCards().add(resourceCard);
        commonArea.getTableCards().add(goldCard);
        commonArea.getTableCards().add(resourceCard2);
        commonArea.getTableCards().add(goldCard2);
        assertEquals(4, commonArea.getTableCards().size());
    }

    @Test
    void getCorrectTableCards() {
        //controlla che le prime due carte siano Resource e le altre due Gold
    }

    @Test
    void getTableCardsReturnsEmptyListWhenNoCards() {
        assertTrue(commonArea.getTableCards().isEmpty());
    }
}