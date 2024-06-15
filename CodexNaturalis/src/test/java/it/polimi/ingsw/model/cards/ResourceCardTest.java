package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {

    private final ArrayList<Resource> resources = new ArrayList<>();

    private Reign reign;
    private int points;

    @Test
    void checkIsEqual() throws InvalidIdException {
        reign = Reign.Fungus;
        points = 0;
        int id = 1;
        resources.add(Resource.Fungus);
        resources.add(Resource.Empty);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Empty);
        test.add(Resource.Fungus);
        test.add(Resource.Blocked);
        ResourceCard resourceCard = new ResourceCard(id, points, reign, true, resources);
        ResourceCard testCard = new ResourceCard(1, 0, Reign.Fungus, true, test);
        assertEquals(resourceCard, testCard);
    }

    @Test
    void checkGetPoints_true() throws InvalidIdException {
        int test = 1;
        points = 1;
        ResourceCard resourceCard = new ResourceCard(40, points, reign, true, resources);
        Assertions.assertEquals(test, resourceCard.getPoints());
    }

    @Test
    void checkGetPoints_false() throws InvalidIdException {
        int test = 0;
        points = 1;
        ResourceCard resourceCard = new ResourceCard(40, points, reign, true, resources);
        Assertions.assertNotEquals(test, resourceCard.getPoints());
    }

    @Test
    void checkGetResourceFRONT_true() throws InvalidIdException {
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Empty);
        test.add(Resource.Fungus);
        test.add(Resource.Blocked);
        resources.add(Resource.Fungus);
        resources.add(Resource.Empty);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        ResourceCard resourceCard = new ResourceCard(1, 0, reign, true, resources);
        Assertions.assertEquals(test, resourceCard.getResource());

    }

    @Test
    void checkGetResourceFRONT_false() throws InvalidIdException {
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Animal);
        test.add(Resource.Empty);
        test.add(Resource.Fungus);
        test.add(Resource.Plant);
        resources.add(Resource.Fungus);
        resources.add(Resource.Empty);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        ResourceCard resourceCard = new ResourceCard(1, 0, reign, true, resources);
        Assertions.assertNotEquals(test, resourceCard.getResource());

    }

    @Test
    void checkGetResourceBACK_true() throws InvalidIdException {
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        for (int i = 1; i < 41; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, false, resources);
            Assertions.assertEquals(test, resourceCard.getResource());
        }
    }

    @Test
    void checkGetResourceBACK_false() throws InvalidIdException {
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        test.add(Resource.Animal);
        test.add(Resource.Empty);
        for (int i = 1; i < 41; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, false, resources);
            Assertions.assertNotEquals(test, resourceCard.getResource());
        }
    }

    @Test
    void checkGetPermanentResourceFungus() throws InvalidIdException {
        reign = Reign.Fungus;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        for (int i = 1; i < 11; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, false, resources);
            Assertions.assertEquals(test, resourceCard.getPermanentResource());
        }
    }

    @Test
    void checkGetPermanentResourcePlant() throws InvalidIdException {
        reign = Reign.Plant;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Plant);
        for (int i = 11; i < 21; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, false, resources);
            Assertions.assertEquals(test, resourceCard.getPermanentResource());
        }
    }

    @Test
    void checkGetPermanentResourceAnimal() throws InvalidIdException {
        reign = Reign.Animal;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Animal);
        for (int i = 21; i < 31; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, false, resources);
            Assertions.assertEquals(test, resourceCard.getPermanentResource());
        }
    }

    @Test
    void checkGetPermanentResourceInsect() throws InvalidIdException {
        reign = Reign.Insect;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Insect);
        for (int i = 31; i < 41; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, false, resources);
            Assertions.assertEquals(test, resourceCard.getPermanentResource());
        }
    }

    @Test
    void checkGetRequirement() throws InvalidIdException {
        for (int i = 1; i < 41; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, true, resources);
            Assertions.assertNull(resourceCard.getRequirement());
        }
    }

    @Test
    void checkRequirement_true() throws InvalidIdException {
        for (int i = 1; i < 41; i++) {
            ArrayList<Integer> requirementTest = new ArrayList<>();
            requirementTest.add(2);
            requirementTest.add(1);
            requirementTest.add(0);
            requirementTest.add(0);
            ResourceCard resourceCard = new ResourceCard(i, points, reign, true, resources);
            assertTrue(resourceCard.checkRequirement(requirementTest));
        }
    }

    @Test
    void checkRequirement_false() throws InvalidIdException {
        for (int i = 1; i < 41; i++) {
            ArrayList<Integer> requirementTest = new ArrayList<>();
            requirementTest.add(3);
            requirementTest.add(1);
            requirementTest.add(0);
            requirementTest.add(0);
            ResourceCard resourceCard = new ResourceCard(i, points, reign, true, resources);
            Assertions.assertNotEquals(false, resourceCard.checkRequirement(requirementTest));
        }
    }

    @Test
    void checkIsResource_true() throws InvalidIdException {
        for (int i = 1; i < 41; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, true, resources);
            assertTrue(resourceCard.isResource());
        }
    }

    @Test
    void checkIsResource_false() throws InvalidIdException {
        for (int i = 1; i < 41; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, true, resources);
            Assertions.assertNotEquals(false, resourceCard.isResource());
        }
    }

    @Test
    void checkClass() throws InvalidIdException {
        for (int i = 0; i < 41; i++) {
            ResourceCard resourceCard = new ResourceCard(i, points, reign, true, resources);
            assertInstanceOf(ResourceCard.class, resourceCard);
        }
    }

    @Test
    void shouldThrowExceptionWhenIdIsTooSmallOrTooBig() {
        assertThrows(InvalidIdException.class, () -> new ResourceCard(0));
        assertThrows(InvalidIdException.class, () -> new ResourceCard(41));
        assertThrows(InvalidIdException.class, () -> new ResourceCard(77));
    }

    @Test
    void shouldNotThrowExceptionWhenIdIsAtLimit() {
        assertDoesNotThrow(() -> new ResourceCard(1));
        assertDoesNotThrow(() -> new ResourceCard(40));
    }

    @Test
    void loadLoadDecksCard1() throws InvalidIdException {
        LoadDecks loadDecks = new LoadDecks();
        CommonArea commonArea = loadDecks.load();
        Deck<ResourceCard> cards = commonArea.getD1();
        ResourceCard card1 = (ResourceCard) cards.getCard(1);

        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Empty);
        test.add(Resource.Fungus);
        test.add(Resource.Blocked);
        ArrayList<Resource> testRequirement = new ArrayList<>();
        System.out.println(card1.toString());

        ResourceCard testCard = new ResourceCard(1, 0, Reign.Fungus, true, test);
        assertEquals(card1.getResource(),testCard.getResource());
        assertEquals(card1.getReign(),testCard.getReign());
        assertEquals(card1.getPoints(), testCard.getPoints());
        assertEquals(card1.getRequirement(),testRequirement);
        assertEquals(card1.getPermanentResource(),testRequirement); //the card is face-up
        assertTrue(card1.isResource());

        ArrayList<Resource> testReign = new ArrayList<>();
        testReign.add(Resource.Fungus);
        card1.setFront(false);
        assertEquals(card1.getPermanentResource(),testReign); //the card is face-down

    }
    @Test
    void loadLoadDecksCard40() throws InvalidIdException {
        LoadDecks loadDecks = new LoadDecks();
        CommonArea commonArea = loadDecks.load();
        Deck<ResourceCard> cards = commonArea.getD1();
        ResourceCard card40 = (ResourceCard) cards.getCard(40);

        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Blocked);
        test.add(Resource.Insect);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        ArrayList<Resource> testRequirement = new ArrayList<>();

        System.out.println(card40.toString());

        ResourceCard testCard = new ResourceCard(40, 1, Reign.Insect, true, test);
        assertEquals(card40.getResource(),testCard.getResource());
        assertEquals(card40.getReign(),testCard.getReign());
        assertEquals(card40.getPoints(), testCard.getPoints());
        assertEquals(card40.getRequirement(),testRequirement);
        assertEquals(card40.getPermanentResource(),testRequirement); //the card is face-up
        assertTrue(card40.isResource());

        ArrayList<Resource> testReign = new ArrayList<>();
        testReign.add(Resource.Insect);
        card40.setFront(false);
        assertEquals(card40.getPermanentResource(),testReign); //the card is face-down

    }



}