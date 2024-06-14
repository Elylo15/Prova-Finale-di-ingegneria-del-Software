package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StarterCardTest {
    private final ArrayList<Resource> resources = new ArrayList<>();
    private final ArrayList<Resource> permanentResources = new ArrayList<>();
    private final ArrayList<Resource> bottomResources = new ArrayList<>();
    private int id;

    private int points;

    @Test
    void cardIsEqual81_true() throws InvalidIdException {
        points = 0;
        id = 81;
        permanentResources.add(Resource.Insect);
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        ArrayList<Resource> testPermanentResources = new ArrayList<>();
        ArrayList<Resource> testBottomResources = new ArrayList<>();
        testPermanentResources.add(Resource.Insect);
        testBottomResources.add(Resource.Fungus);
        testBottomResources.add(Resource.Plant);
        testBottomResources.add(Resource.Insect);
        testBottomResources.add(Resource.Animal);
        StarterCard card81 = new StarterCard(id, points, null, false, resources, permanentResources, bottomResources);
        StarterCard test = new StarterCard(id, points, null, false, resources, testPermanentResources, testBottomResources);
        assertEquals(card81, test);
    }

    @Test
    void cardIsEqual81_false() throws InvalidIdException {
        points = 0;
        id = 81;
        permanentResources.add(Resource.Insect);
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        ArrayList<Resource> testPermanentResources = new ArrayList<>();
        ArrayList<Resource> testBottomResources = new ArrayList<>();
        testPermanentResources.add(Resource.Fungus);
        testBottomResources.add(Resource.Plant);
        testBottomResources.add(Resource.Animal);
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
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        StarterCard card81 = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Empty);
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        test.add(Resource.Empty);
        Assertions.assertEquals(test, card81.getResource());
    }

    @Test
    void checkResourcesCard81_false() throws InvalidIdException {
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        StarterCard card81 = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        test.add(Resource.Insect);
        test.add(Resource.Empty);
        Assertions.assertNotEquals(test, card81.getResource());
    }

    @Test
    void checkBottomResources81_true() throws InvalidIdException {
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        StarterCard card81 = new StarterCard(81, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        test.add(Resource.Animal);
        Assertions.assertEquals(test, card81.getResource());
    }

    @Test
    void checkBottomResources81_false() throws InvalidIdException {
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        StarterCard card81 = new StarterCard(81, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Plant);
        test.add(Resource.Fungus);
        test.add(Resource.Insect);
        test.add(Resource.Empty);
        Assertions.assertNotEquals(test, card81.getResource());
    }

    @Test
    void checkResourcesCard86_true() throws InvalidIdException {
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Blocked);
        resources.add(Resource.Blocked);
        StarterCard card86 = new StarterCard(86, 0, null, true, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Blocked);
        test.add(Resource.Blocked);
        Assertions.assertEquals(test, card86.getResource());
    }

    @Test
    void checkResourcesCard86_false() throws InvalidIdException {
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Blocked);
        resources.add(Resource.Blocked);
        StarterCard card86 = new StarterCard(86, 0, null, true, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        test.add(Resource.Insect);
        test.add(Resource.Empty);
        Assertions.assertNotEquals(test, card86.getResource());
    }

    @Test
    void checkBottomResourcesCard86_true() throws InvalidIdException {
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Animal);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        StarterCard card86 = new StarterCard(86, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        Assertions.assertEquals(test, card86.getResource());
    }

    @Test
    void checkBottomResourcesCard86_false() throws InvalidIdException {
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Animal);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        StarterCard card86 = new StarterCard(86, 0, null, false, resources, permanentResources, bottomResources);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Plant);
        test.add(Resource.Fungus);
        test.add(Resource.Insect);
        test.add(Resource.Empty);
        Assertions.assertNotEquals(test, card86.getResource());
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
            StarterCard starterCard = new StarterCard(i, 0, null, true, resources, permanentResources, bottomResources);
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
            StarterCard starterCard = new StarterCard(i, 0, null, false, resources, permanentResources, bottomResources);
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
        assertDoesNotThrow(() -> new StarterCard(81));
        assertDoesNotThrow(() -> new StarterCard(86));
    }

}