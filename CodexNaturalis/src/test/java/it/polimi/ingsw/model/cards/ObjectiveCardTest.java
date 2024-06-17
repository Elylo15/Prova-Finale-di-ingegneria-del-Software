package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveCardTest {
    private CommonArea commonArea;

    @BeforeEach
    void setUp()
    {
        LoadDecks loadDecks = new LoadDecks();
        commonArea = loadDecks.load();
    }

    @Test
    void shouldThrowExceptionWhenIdIsTooSmallOrTooBig() {
        assertThrows(InvalidIdException.class, () -> new ObjectiveCard(109));
        assertThrows(InvalidIdException.class, () -> new ObjectiveCard(5));
    }

    @Test
    void shouldNotThrowExceptionWhenIdIsAtLimit() {
        assertDoesNotThrow(() -> new ObjectiveCard(102));
        assertDoesNotThrow(() -> new ObjectiveCard(87));
    }

    @Test
    void createObjectiveCardWithIdAndPoints() throws InvalidIdException {
        ObjectiveCard objectiveCard = new ObjectiveCard(90);
        assertEquals(90, objectiveCard.getID());
        assertEquals(2, objectiveCard.getPoints());
    }

    @Test
    void sassingThreePoints() throws InvalidIdException {
        ObjectiveCard objectiveCard = new ObjectiveCard(93);
        ObjectiveCard objectiveCard2 = new ObjectiveCard(99);
        assertEquals(3, objectiveCard.getPoints());
        assertEquals(3, objectiveCard2.getPoints());
    }

    @Test
    void assignTwoPoints() throws InvalidIdException {
        ObjectiveCard objectiveCard = new ObjectiveCard(97);
        ObjectiveCard objectiveCard2 = new ObjectiveCard(101);
        assertEquals(2, objectiveCard.getPoints());
        assertEquals(2, objectiveCard2.getPoints());
    }

    @Test
    void createObjectiveCard() {
        ArrayList<Resource> requirements = new ArrayList<>();
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Animal);
        ArrayList<int[]> pattern = new ArrayList<>();
        pattern.add(new int[]{1, 2});
        pattern.add(new int[]{3, 4});
        ArrayList<Reign> reignCards = new ArrayList<>();
        reignCards.add(Reign.Fungus);
        reignCards.add(Reign.Animal);

        ObjectiveCard objectiveCard = new ObjectiveCard(90, 2, requirements, pattern, reignCards);

        assertEquals(90, objectiveCard.getID());
        assertEquals(2, objectiveCard.getPoints());
        assertEquals(requirements, objectiveCard.getRequirements());
        assertEquals(pattern, objectiveCard.getPattern());
        assertEquals(reignCards, objectiveCard.getReignCards());
    }

    @Test
    void returnCorrectPoints() throws InvalidIdException {
        ObjectiveCard objectiveCard = new ObjectiveCard(90);
        assertEquals(2, objectiveCard.getPoints());
    }

    @Test
    void returnNullIfNoRequirements() throws InvalidIdException {
        ObjectiveCard objectiveCard = new ObjectiveCard(90);
        assertNull(objectiveCard.getRequirements());
    }

    @Test
    void returnNullIfNoPattern() throws InvalidIdException {
        ObjectiveCard objectiveCard = new ObjectiveCard(90);
        assertNull(objectiveCard.getPattern());
    }

    @Test
    void returnNullIfNoReignCards() throws InvalidIdException {
        ObjectiveCard objectiveCard = new ObjectiveCard(90);
        assertNull(objectiveCard.getReignCards());
    }
    @Test
    void LoadDecks92() throws InvalidIdException {
       Deck<ObjectiveCard> cards = commonArea.getD4();
       ObjectiveCard card = (ObjectiveCard) cards.getCard(92);

        ArrayList<int[]> patternTest  =  new ArrayList<>();
        int[] array1 = {2,0};
        int[] array2 = {3,-1};
        patternTest.add(array1);
        patternTest.add(array2);
        ArrayList<Reign> reignTest = new ArrayList<>();
        reignTest.add(Reign.Plant);
        reignTest.add(Reign.Plant);
        reignTest.add(Reign.Insect);


        ObjectiveCard testCard = new ObjectiveCard(92,3,null,patternTest,reignTest);
        Assertions.assertEquals(testCard.getPoints(), card.getPoints());
        Assertions.assertEquals(testCard.getReignCards(), card.getReignCards());
        Assertions.assertNull(card.getRequirements());
        int[] card92Pattern1 = card.getPattern().get(0);
        int[] card92Pattern2 = card.getPattern().get(1);
        int[] testPattern1 = testCard.getPattern().get(0);
        int[] testPattern2 = testCard.getPattern().get(1);

        Assertions.assertEquals(testPattern1[0],card92Pattern1[0]);
        Assertions.assertEquals(testPattern1[1],card92Pattern1[1]);
        Assertions.assertEquals(testPattern2[0],card92Pattern2[0]);
        Assertions.assertEquals(testPattern2[1],card92Pattern2[1]);

    }
    @Test
    void LoadDecks99() throws InvalidIdException {
        Deck<ObjectiveCard> cards = commonArea.getD4();
        ObjectiveCard card = (ObjectiveCard) cards.getCard(99);

        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Quill);
        test.add(Resource.Inkwell);
        test.add(Resource.Manuscript);
        ObjectiveCard testCard = new ObjectiveCard(99,3,test,null,null);

        Assertions.assertEquals(testCard.getPoints(), card.getPoints());
        Assertions.assertNull(card.getReignCards());
        Assertions.assertEquals(testCard.getRequirements(),card.getRequirements());

    }


}