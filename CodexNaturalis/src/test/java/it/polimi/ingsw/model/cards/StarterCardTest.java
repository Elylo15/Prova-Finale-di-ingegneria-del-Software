package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StarterCardTest {
    private final ArrayList<Resource> resources = new ArrayList<>();
    private final ArrayList<Resource> permanentResources = new ArrayList<>();
    private final ArrayList<Resource> bottomResources = new ArrayList<>();
    private int id;
    private int points;
    private CommonArea commonArea;

    //we create the cards with the constructor of their class in order to test whether methods meet their expected behaviour
    //then we check if the cards converted from json file are correctly loaded

    @BeforeEach
    void setUp()
    {
        LoadDecks loadDecks = new LoadDecks();
        commonArea = loadDecks.load();
    }

    @Test
    void cardIsEqual81_true() throws InvalidIdException {
        points = 0;
        id = 81;
        permanentResources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        ArrayList<Resource> testPermanentResources = new ArrayList<>();
        ArrayList<Resource> testBottomResources = new ArrayList<>();
        testPermanentResources.add(Resource.Insect);
        testBottomResources.add(Resource.Empty);
        testBottomResources.add(Resource.Plant);
        testBottomResources.add(Resource.Insect);
        testBottomResources.add(Resource.Empty);
        StarterCard card81 = new StarterCard(id, points, null, false, resources, permanentResources, bottomResources);
        StarterCard test = new StarterCard(id, points, null, false, resources, testPermanentResources, testBottomResources);
        assertEquals(card81, test);
    }

    @Test
    void cardIsEqual81_false() throws InvalidIdException {
        points = 0;
        id = 81;
        permanentResources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        ArrayList<Resource> testPermanentResources = new ArrayList<>();
        ArrayList<Resource> testBottomResources = new ArrayList<>();
        testPermanentResources.add(Resource.Fungus);
        testPermanentResources.add(Resource.Insect);
        testBottomResources.add(Resource.Empty);
        testBottomResources.add(Resource.Plant);
        testBottomResources.add(Resource.Insect);
        testBottomResources.add(Resource.Empty);
        StarterCard card81 = new StarterCard(id, points, null, false, resources, permanentResources, bottomResources);
        StarterCard test = new StarterCard(id, points, null, false, resources, testPermanentResources, testBottomResources);
        Assertions.assertNotEquals(true, card81.equals(test));
    }

    @Test
    void checkPermanentResourcesCard81_true() throws InvalidIdException {
        permanentResources.add(Resource.Insect);
        StarterCard card81 = new StarterCard(81, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Insect);
        Assertions.assertEquals(test, card81.getPermanentResource());
    }

    @Test
    void checkPermanentResourcesCard81_false() throws InvalidIdException {
        permanentResources.add(Resource.Insect);
        StarterCard card81 = new StarterCard(81, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        Assertions.assertNotEquals(test, card81.getPermanentResource());
    }

    @Test
    void checkPermanentResourcesCard86_true() throws InvalidIdException {
        permanentResources.add(Resource.Plant);
        permanentResources.add(Resource.Animal);
        permanentResources.add(Resource.Fungus);
        StarterCard card86 = new StarterCard(86, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Plant);
        test.add(Resource.Animal);
        test.add(Resource.Fungus);
        Assertions.assertEquals(test, card86.getPermanentResource());
    }

    @Test
    void checkPermanentResourcesCard86_false() throws InvalidIdException {
        permanentResources.add(Resource.Plant);
        permanentResources.add(Resource.Animal);
        permanentResources.add(Resource.Fungus);
        StarterCard card86 = new StarterCard(86, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Insect);
        Assertions.assertNotEquals(test, card86.getPermanentResource());
    }

    @Test
    void checkResourcesCard81_true() throws InvalidIdException {
        resources.add(Resource.Fungus);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Animal);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        StarterCard card81 = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        ArrayList<Resource> testResources = new ArrayList<>();
        testResources.add(Resource.Fungus);
        testResources.add(Resource.Plant);
        testResources.add(Resource.Insect);
        testResources.add(Resource.Animal);
        ArrayList<Resource> testButton = new ArrayList<>();
        testButton.add(Resource.Empty);
        testButton.add(Resource.Plant);
        testButton.add(Resource.Insect);
        testButton.add(Resource.Empty);
        Assertions.assertEquals(testResources, card81.getResource());
        Assertions.assertNotEquals(testButton,card81.getResource());
    }

    @Test
    void checkResourcesCard81_false() throws InvalidIdException {
        resources.add(Resource.Fungus);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Animal);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        StarterCard card81 = new StarterCard(81, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        test.add(Resource.Insect);
        test.add(Resource.Animal);
        ArrayList<Resource> testButton = new ArrayList<>();
        testButton.add(Resource.Empty);
        testButton.add(Resource.Plant);
        testButton.add(Resource.Insect);
        testButton.add(Resource.Empty);
        //if front is false method getResource() will return an ArrayList containing the bottom resources
        Assertions.assertNotEquals(test, card81.getResource());
        Assertions.assertEquals(testButton,card81.getResource());
    }



    @Test
    void checkResourcesCard86_true() throws InvalidIdException {
        resources.add(Resource.Fungus);
        resources.add(Resource.Animal);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Blocked);
        bottomResources.add(Resource.Blocked);
        StarterCard card86 = new StarterCard(86, 0, null, true, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        ArrayList<Resource> testButton = new ArrayList<>();
        testButton.add(Resource.Empty);
        testButton.add(Resource.Empty);
        testButton.add(Resource.Blocked);
        testButton.add(Resource.Blocked);
        Assertions.assertEquals(test, card86.getResource());
        Assertions.assertNotEquals(testButton,card86.getResource());

    }

    @Test
    void checkResourcesCard86_false() throws InvalidIdException {
        resources.add(Resource.Fungus);
        resources.add(Resource.Animal);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Empty);
        bottomResources.add(Resource.Blocked);
        bottomResources.add(Resource.Blocked);
        StarterCard card86 = new StarterCard(86, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        ArrayList<Resource> testButton = new ArrayList<>();
        testButton.add(Resource.Empty);
        testButton.add(Resource.Empty);
        testButton.add(Resource.Blocked);
        testButton.add(Resource.Blocked);
        Assertions.assertEquals(testButton, card86.getResource());
        Assertions.assertNotEquals(test,card86.getResource());
    }


    @Test
    void checkRequirement_true() throws InvalidIdException {
        for (int i = 81; i < 87; i++) {
            ArrayList<Integer> requirementTest = new ArrayList<>();
            requirementTest.add(2);
            requirementTest.add(1);
            requirementTest.add(0);
            requirementTest.add(0);
            StarterCard starterCard = new StarterCard(i, 0, null, true, resources, permanentResources, bottomResources);
            assertTrue(starterCard.checkRequirement(requirementTest));
        }
    }

    @Test
    void checkRequirement_false() throws InvalidIdException {
        for (int i = 81; i < 87; i++) {
            ArrayList<Integer> requirementTest = new ArrayList<>();
            requirementTest.add(3);
            requirementTest.add(1);
            requirementTest.add(0);
            requirementTest.add(0);
            StarterCard starterCard = new StarterCard(i, 0, null, false, resources, permanentResources, bottomResources);
            Assertions.assertNotEquals(false, starterCard.checkRequirement(requirementTest));
        }
    }

    @Test
    void checkIsStarter_true() throws InvalidIdException {
        for (int i = 81; i < 87; i++) {
            StarterCard starterCard = new StarterCard(i, 0, null, false, resources, permanentResources, bottomResources);
            assertTrue(starterCard.isStarter());
        }
    }

    @Test
    void checkIsStarter_false() throws InvalidIdException {
        for (int i = 81; i < 87; i++) {
            StarterCard starterCard = new StarterCard(i, 0, null, false, resources, permanentResources, bottomResources);
            Assertions.assertNotEquals(false, starterCard.isStarter());
        }
    }

    @Test
    void checkClass() throws InvalidIdException {
        for (int i = 81; i < 87; i++) {
            StarterCard starterCard = new StarterCard(i, 0, null, true, resources, permanentResources, bottomResources);
            assertInstanceOf(StarterCard.class, starterCard);
        }
    }

    @Test
    void shouldThrowExceptionWhenIdIsTooSmallOrTooBig() {
        assertThrows(InvalidIdException.class, () -> new StarterCard(1));
        assertThrows(InvalidIdException.class, () -> new StarterCard(39));
        assertThrows(InvalidIdException.class, () -> new StarterCard(98));
    }

    @Test
    void shouldNotThrowExceptionWhenIdIsAtLimit() {
        for (int i = 81; i < 87; i++) {
            int a = i;
            assertDoesNotThrow(() -> new StarterCard(a));
        }
    }
    @Test
    void loadLoadDecksCard() throws InvalidIdException {
        Deck<StarterCard> cards = commonArea.getD3();
        StarterCard card = (StarterCard) cards.getCard(83);
        ArrayList<Resource> test = new ArrayList<>();
        ArrayList<Resource> testButton = new ArrayList<>();
        ArrayList<Resource> testPermanent = new ArrayList<>();
        test.add(Resource.Insect);
        test.add(Resource.Animal);
        test.add(Resource.Fungus);
        test.add(Resource.Plant);
        testButton.add(Resource.Empty);
        testButton.add(Resource.Empty);
        testButton.add(Resource.Empty);
        testButton.add(Resource.Empty);
        testPermanent.add(Resource.Plant);
        testPermanent.add(Resource.Fungus);

        StarterCard testCard = new StarterCard(83,0,null,true,test,testPermanent,testButton);

        Assertions.assertEquals(testCard.getResource(),card.getResource());
        Assertions.assertEquals(testCard.getPoints(),card.getPoints());
        assertTrue(card.equals(testCard));
        card.setFront(false);
        testCard.setFront(false);
        Assertions.assertEquals(testCard.getResource(),card.getResource());
        Assertions.assertEquals(testCard.getPermanentResource(),card.getPermanentResource());


    }

    @Test
    void loadLoadDecksCards() throws InvalidIdException {
        Deck<StarterCard> cards = commonArea.getD3();
        for (int i=81; i< 87; i++){
            ArrayList<Integer> requirement = new ArrayList<>();
            requirement.add(1);
            StarterCard card = (StarterCard) cards.getCard(i);
            Assertions.assertTrue(card.isStarter());
            Assertions.assertEquals(0,card.getPoints());
            Assertions.assertTrue(card.checkRequirement(requirement));


        }

    }

}