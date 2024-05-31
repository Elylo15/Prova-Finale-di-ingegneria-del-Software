package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CommonArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class LoadDecksTest {
    private LoadDecks loadDecks;
    private CommonArea commonArea;

    @BeforeEach
    void setUp() {
        loadDecks = new LoadDecks();
        commonArea = new CommonArea();
    }

    @Test
    void CheckResourceCardNumber() {
        commonArea = loadDecks.load();
        assertEquals(40, commonArea.getD1().getSize());
    }

    //print all ResourceCard load on d2
    @Test
    void PrintResourceCard() {
        commonArea = loadDecks.load();
        commonArea.getD1().getList().forEach(System.out::println);
    }

    @Test
    void CheckGoldCardNumber() {
        commonArea = loadDecks.load();
        assertEquals(40, commonArea.getD2().getSize());
    }

    //print all GoldCard load on d2
    @Test
    void PrintGoldCard() {
        commonArea = loadDecks.load();
        System.out.println(commonArea.getD2().getSize());
        commonArea.getD2().getList().forEach(System.out::println);
    }

    @Test
    void CheckStarterCardNumber() {
        commonArea = loadDecks.load();
        assertEquals(6, commonArea.getD3().getSize());
    }

    //print all StarterCard load on d3
    @Test
    void PrintStarterCard() {
        commonArea = loadDecks.load();
        commonArea.getD3().getList().forEach(System.out::println);
    }

    @Test
    void CheckObjectiveCardNumber() {
        commonArea = loadDecks.load();
        assertEquals(16, commonArea.getD4().getSize());
    }

    //print all ObjectiveCard load on d4
    @Test
    void PrintObjectiveCard() {
        commonArea = loadDecks.load();
        commonArea.getD4().getList().forEach(System.out::println);
    }

    @Test
    void CheckResourceCardDeck() {
        commonArea = loadDecks.load();
        for (int i = 0; i < commonArea.getD1().getSize(); i++) {
            Card card = commonArea.getD1().getCard(i);
            if (card != null) {
                assertInstanceOf(ResourceCard.class, card);
            }
        }
    }

    @Test
    void CheckGoldCardDeck() {
        commonArea = loadDecks.load();
        for (int i = 0; i < commonArea.getD2().getSize(); i++) {
            Card card = commonArea.getD2().getCard(i);
            if (card != null) {
                assertInstanceOf(GoldCard.class, card);
            }
        }
    }

    @Test
    void CheckStarterCardDeck() {
        commonArea = loadDecks.load();
        for (int i = 0; i < commonArea.getD3().getSize(); i++) {
            Card card = commonArea.getD3().getCard(i);
            if (card != null) {
                assertInstanceOf(StarterCard.class, card);
            }
        }
    }

    @Test
    void CheckObjectiveCardDeck() {
        commonArea = loadDecks.load();
        for (int i = 0; i < commonArea.getD4().getSize(); i++) {
            Card card = commonArea.getD4().getCard(i);
            if (card != null) {
                assertInstanceOf(ObjectiveCard.class, card);
            }
        }
    }

}