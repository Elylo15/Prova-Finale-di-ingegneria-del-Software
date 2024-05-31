package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CellTest {

    private PlaceableCard card1;
    private PlaceableCard card2;
    private Cell cell;

    @BeforeEach
    void setUp() {
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Empty);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        try {
            card1 = new ResourceCard(1, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }
        resources = new ArrayList<>();
        resources.add(Resource.Manuscript);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Blocked);
        ArrayList<Resource> requirements = new ArrayList<>();
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Insect);
        try {
            card2 = new GoldCard(43, 1, Reign.Fungus, false, resources, requirements);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }
        cell = new Cell(1, 2, card1);
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(cell);
        cells.add(null);
        cells.add(null);
        cells.add(null);
        card1.setCells(cells);
        cells = new ArrayList<>();
        cells.add(null);
        cells.add(null);
        cells.add(null);
        cells.add(cell);
        card2.setCells(cells);
    }

    @Test
    void linkCard() {
        cell.linkCard(card2);
        Assertions.assertEquals(cell.getTopCard(), card2);
        Assertions.assertEquals(cell.getBottomCard(), card1);
    }

    @Test
    void getResource_noTopCard() {
        Assertions.assertEquals(cell.getResource(), cell.getBottomCard().getResource().getFirst());
    }

    @Test
    void getResource_withTopCard() {
        cell.linkCard(card2);
        Assertions.assertEquals(cell.getResource(), cell.getTopCard().getResource().getLast());
    }

    @Test
    void isAvailable_noTopCard() {
        Assertions.assertTrue(cell.isAvailable());
    }

    @Test
    void isAvailable_withTopCard() {
        cell.linkCard(card2);
        Assertions.assertFalse(cell.isAvailable());
    }
}