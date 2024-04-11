package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Cell;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerAreaTest {

    private PlayerArea playerArea;

    @BeforeEach
    void setUp() {
        playerArea = new PlayerArea();
    }

    @Test
    void contains_notExistingCell() {
        assertFalse(playerArea.contains(1,1));
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

        Assertions.assertTrue(playerArea.contains(0,0));
        Assertions.assertTrue(playerArea.contains(0,1));
        Assertions.assertTrue(playerArea.contains(1,0));
        Assertions.assertTrue(playerArea.contains(1,1));
        Assertions.assertTrue(playerArea.contains(1,2));
        Assertions.assertTrue(playerArea.contains(2,1));
        Assertions.assertTrue(playerArea.contains(2,2));
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
        for(Integer[] position : counts) {
            if(checks.stream()
                    .noneMatch(check -> check[0] == position[0] && check[1] == position[1])) {
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

        Assertions.assertFalse(playerArea.checkPosition(0,0));
        Assertions.assertFalse(playerArea.checkPosition(1,1));

        Assertions.assertFalse(playerArea.checkPosition(2,0));
        Assertions.assertTrue(playerArea.checkPosition(-1,-1));
        Assertions.assertTrue(playerArea.checkPosition(-1,1));
        Assertions.assertTrue(playerArea.checkPosition(0,2));
        Assertions.assertTrue(playerArea.checkPosition(2,2));
        Assertions.assertTrue(playerArea.checkPosition(1,-1));

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

        Assertions.assertFalse(playerArea.checkPosition(0,0));

        Assertions.assertTrue(playerArea.contains(0,0));
        Assertions.assertTrue(playerArea.contains(0,1));
        Assertions.assertTrue(playerArea.contains(1,0));
        Assertions.assertTrue(playerArea.contains(1,1));

        Assertions.assertTrue(playerArea.checkPosition(-1,-1));
        Assertions.assertTrue(playerArea.checkPosition(1,-1));
        Assertions.assertTrue(playerArea.checkPosition(-1,1));
        Assertions.assertTrue(playerArea.checkPosition(1,1));

        Assertions.assertFalse(playerArea.checkPosition(0,-1));
        Assertions.assertFalse(playerArea.checkPosition(-1,0));
        Assertions.assertFalse(playerArea.checkPosition(0,1));
        Assertions.assertFalse(playerArea.checkPosition(1,0));
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

        Assertions.assertFalse(playerArea.checkPosition(0,0));
        Assertions.assertFalse(playerArea.checkPosition(1,1));

        Assertions.assertTrue(playerArea.contains(0,0));
        Assertions.assertTrue(playerArea.contains(0,1));
        Assertions.assertTrue(playerArea.contains(1,0));
        Assertions.assertTrue(playerArea.contains(1,1));
        Assertions.assertTrue(playerArea.contains(1,2));
        Assertions.assertTrue(playerArea.contains(2,1));
        Assertions.assertTrue(playerArea.contains(2,2));

        Assertions.assertFalse(playerArea.contains(2,0));
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

        //Objective card pattern 95

        //objective card pattern 96

        // Fungus card
        PlaceableCard fungusCard;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            fungusCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        //Plant card
        PlaceableCard plantCard;
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            plantCard = new ResourceCard(12, 0, Reign.Plant, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);

        playerArea.placeCard(fungusCard, 1, 1, false);
        playerArea.placeCard(fungusCard, -1, 1, false);
        playerArea.placeCard(plantCard, 2, 2, false);

        playerArea.placeCard(fungusCard, 1, 1, false);
        playerArea.placeCard(fungusCard, -1, 1, false);
        playerArea.placeCard(plantCard, 2, 2, false);

        playerArea.placeCard(fungusCard, -2, 2, false);
        playerArea.placeCard(fungusCard, 0, 2, false);
        playerArea.placeCard(plantCard, 1, 3, false);

        playerArea.placeCard(plantCard, -3, 1, false);
        playerArea.placeCard(fungusCard, -4, 2, false);
        playerArea.placeCard(plantCard, -1, 3, false);


        //TODO





    }

    @Test
    void countPattern() {

    }
}