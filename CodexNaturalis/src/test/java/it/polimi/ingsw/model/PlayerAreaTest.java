package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PlayerAreaTest {

    private PlayerArea playerArea;

    @BeforeEach
    void setUp() {
        playerArea = new PlayerArea();
    }

    @Test
    void contains_notExistingCell() {
        assertFalse(playerArea.contains(1, 1));
    }

    @Test
    void contains_ExistingCell() throws noPlaceCardException {
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlaceableCard testCard;
        try {
            testCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1, 1, true);

        Assertions.assertTrue(playerArea.contains(0, 0));
        Assertions.assertTrue(playerArea.contains(0, 1));
        Assertions.assertTrue(playerArea.contains(1, 0));
        Assertions.assertTrue(playerArea.contains(1, 1));
        Assertions.assertTrue(playerArea.contains(1, 2));
        Assertions.assertTrue(playerArea.contains(2, 1));
        Assertions.assertTrue(playerArea.contains(2, 2));
    }

    @Test
    void getResources() throws noPlaceCardException {

        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlaceableCard testCard;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            testCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1, 1, true);

        ArrayList<Integer> counts = new ArrayList<>();
        counts.add(2); // FUNGUS
        counts.add(2); // INSECT
        counts.add(0); // ANIMAL
        counts.add(1); // PLANT
        counts.add(0); // MANUSCRIPT
        counts.add(0); // QUILL
        counts.add(0); // INKWELL
        counts.add(2); // EMPTY
        counts.add(1); // BLOCKED

        Assertions.assertEquals(counts, playerArea.getResources());
    }

    @Test
    void getAvailablePosition() throws noPlaceCardException {

        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlaceableCard testCard;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            testCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1, 1, true);

        ArrayList<Integer[]> counts = playerArea.getAvailablePosition();
        Assertions.assertEquals(5, counts.size());

        ArrayList<Integer[]> checks = new ArrayList<>();
        checks.add(new Integer[]{-1, -1});
        checks.add(new Integer[]{-1, 1});
        checks.add(new Integer[]{0, 2});
        checks.add(new Integer[]{2, 2});
        checks.add(new Integer[]{1, -1});

        boolean found = true;
        for (Integer[] position : counts) {
            if (checks.stream()
                    .noneMatch(check -> Objects.equals(check[0], position[0]) && Objects.equals(check[1], position[1]))) {
                found = false;
            }

        }
        Assertions.assertTrue(found);
    }

    @Test
    void checkPosition() throws noPlaceCardException {
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlaceableCard testCard;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            testCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1, 1, true);

        Assertions.assertFalse(playerArea.checkPosition(0, 0));
        Assertions.assertFalse(playerArea.checkPosition(1, 1));

        Assertions.assertFalse(playerArea.checkPosition(2, 0));
        Assertions.assertTrue(playerArea.checkPosition(-1, -1));
        Assertions.assertTrue(playerArea.checkPosition(-1, 1));
        Assertions.assertTrue(playerArea.checkPosition(0, 2));
        Assertions.assertTrue(playerArea.checkPosition(2, 2));
        Assertions.assertTrue(playerArea.checkPosition(1, -1));

    }


    @Test
    void placeStarterCard() {
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);

        Assertions.assertFalse(playerArea.checkPosition(0, 0));

        Assertions.assertTrue(playerArea.contains(0, 0));
        Assertions.assertTrue(playerArea.contains(0, 1));
        Assertions.assertTrue(playerArea.contains(1, 0));
        Assertions.assertTrue(playerArea.contains(1, 1));

        Assertions.assertTrue(playerArea.checkPosition(-1, -1));
        Assertions.assertTrue(playerArea.checkPosition(1, -1));
        Assertions.assertTrue(playerArea.checkPosition(-1, 1));
        Assertions.assertTrue(playerArea.checkPosition(1, 1));

        Assertions.assertFalse(playerArea.checkPosition(0, -1));
        Assertions.assertFalse(playerArea.checkPosition(-1, 0));
        Assertions.assertFalse(playerArea.checkPosition(0, 1));
        Assertions.assertFalse(playerArea.checkPosition(1, 0));
    }

    @Test
    void placeCard() throws noPlaceCardException {
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlaceableCard testCard;
        try {
            testCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1, 1, true);

        Assertions.assertFalse(playerArea.checkPosition(0, 0));
        Assertions.assertFalse(playerArea.checkPosition(1, 1));

        Assertions.assertTrue(playerArea.contains(0, 0));
        Assertions.assertTrue(playerArea.contains(0, 1));
        Assertions.assertTrue(playerArea.contains(1, 0));
        Assertions.assertTrue(playerArea.contains(1, 1));
        Assertions.assertTrue(playerArea.contains(1, 2));
        Assertions.assertTrue(playerArea.contains(2, 1));
        Assertions.assertTrue(playerArea.contains(2, 2));

        Assertions.assertFalse(playerArea.contains(2, 0));


        PlaceableCard goldCard1, goldCard2;
        resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Blocked);
        ArrayList<Resource> requirements = new ArrayList<>();
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Plant);
        try {
            goldCard1 = new GoldCard(45, 2, Reign.Fungus, true, resources, requirements);
            goldCard2 = new GoldCard(45, 2, Reign.Fungus, true, resources, requirements);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        // Fungus card
        ArrayList<PlaceableCard> fungusCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for (int i = 0; i < 5; i++)
                fungusCard.add(new ResourceCard(2, 0, Reign.Fungus, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        //Plant card
        ArrayList<PlaceableCard> plantCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for (int i = 0; i < 4; i++)
                plantCard.add(new ResourceCard(12, 0, Reign.Plant, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }


        //playerArea.placeCard(fungusCard.get(0), 1, 1, false);
        playerArea.placeCard(fungusCard.get(1), -1, 1, false);
        playerArea.placeCard(plantCard.getFirst(), 2, 2, false);

        playerArea.placeCard(fungusCard.get(2), -2, 2, false);
        playerArea.placeCard(fungusCard.get(3), 0, 2, false);
        playerArea.placeCard(plantCard.get(1), 1, 3, false);

        playerArea.placeCard(plantCard.get(2), -3, 1, false);
        playerArea.placeCard(fungusCard.get(4), -4, 2, false);
        playerArea.placeCard(plantCard.get(3), -1, 3, false);

        Assertions.assertEquals(2, playerArea.placeCard(goldCard1, -2, 4, true));
        Assertions.assertEquals(6, playerArea.placeCard(goldCard2, -3, 3, true));
        Assertions.assertEquals(0, playerArea.placeCard(fungusCard.getFirst(), -4, 4, true));
    }

    @Test
    void checkPattern() throws noPlaceCardException {
        //starter card
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        //Objective card pattern 91
        ObjectiveCard obj1;
        ArrayList<int[]> pattern = new ArrayList<>();
        pattern.add(new int[]{2, 0});
        pattern.add(new int[]{3, 1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj1 = new ObjectiveCard(91, 3, null, pattern, reigns);

        //Objective card pattern 95
        ObjectiveCard obj2;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        obj2 = new ObjectiveCard(95, 2, resources, null, null);

        //objective card pattern 96
        ObjectiveCard obj3;
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        obj3 = new ObjectiveCard(96, 2, resources, null, null);

        // Fungus card
        ArrayList<PlaceableCard> fungusCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for (int i = 0; i < 5; i++)
                fungusCard.add(new ResourceCard(2, 0, Reign.Fungus, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        //Plant card
        ArrayList<PlaceableCard> plantCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for (int i = 0; i < 4; i++)
                plantCard.add(new ResourceCard(12, 0, Reign.Plant, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);

        playerArea.placeCard(fungusCard.get(0), 1, 1, false);
        playerArea.placeCard(fungusCard.get(1), -1, 1, false);
        playerArea.placeCard(plantCard.getFirst(), 2, 2, false);

        playerArea.placeCard(fungusCard.get(2), -2, 2, false);
        playerArea.placeCard(fungusCard.get(3), 0, 2, false);
        playerArea.placeCard(plantCard.get(1), 1, 3, false);

        playerArea.placeCard(plantCard.get(2), -3, 1, false);
        playerArea.placeCard(fungusCard.get(4), -4, 2, false);
        playerArea.placeCard(plantCard.get(3), -1, 3, false);


        Assertions.assertEquals(6, playerArea.checkPattern(obj1));
        Assertions.assertEquals(2, playerArea.checkPattern(obj2));
        Assertions.assertEquals(2, playerArea.checkPattern(obj3));
    }

    @Test
    void countPattern() throws noPlaceCardException {
        //starter card
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard;
        try {
            starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        //Objective card pattern 91
        ObjectiveCard obj1;
        ArrayList<int[]> pattern = new ArrayList<>();
        pattern.add(new int[]{2, 0});
        pattern.add(new int[]{3, 1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj1 = new ObjectiveCard(91, 3, null, pattern, reigns);

        //Objective card pattern 95
        ObjectiveCard obj2;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        obj2 = new ObjectiveCard(95, 2, resources, null, null);

        //objective card pattern 96
        ObjectiveCard obj3;
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        obj3 = new ObjectiveCard(96, 2, resources, null, null);

        // Fungus card
        ArrayList<PlaceableCard> fungusCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for (int i = 0; i < 5; i++)
                fungusCard.add(new ResourceCard(2, 0, Reign.Fungus, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        //Plant card
        ArrayList<PlaceableCard> plantCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for (int i = 0; i < 4; i++)
                plantCard.add(new ResourceCard(12, 0, Reign.Plant, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);

        playerArea.placeCard(fungusCard.get(0), 1, 1, false);
        playerArea.placeCard(fungusCard.get(1), -1, 1, false);
        playerArea.placeCard(plantCard.getFirst(), 2, 2, false);

        playerArea.placeCard(fungusCard.get(2), -2, 2, false);
        playerArea.placeCard(fungusCard.get(3), 0, 2, false);
        playerArea.placeCard(plantCard.get(1), 1, 3, false);

        playerArea.placeCard(plantCard.get(2), -3, 1, false);
        playerArea.placeCard(fungusCard.get(4), -4, 2, false);
        playerArea.placeCard(plantCard.get(3), -1, 3, false);

        Assertions.assertEquals(2, playerArea.countPattern(obj1));
        Assertions.assertEquals(1, playerArea.countPattern(obj2));
        Assertions.assertEquals(1, playerArea.countPattern(obj3));
    }
}