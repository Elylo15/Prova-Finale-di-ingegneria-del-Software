package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveCardTest {

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

}