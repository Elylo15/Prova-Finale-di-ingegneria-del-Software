package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


class PlaceableCardTest {

    @Test
    void checkRequirement_true() throws InvalidIdException {
        PlaceableCard goldCard;
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Blocked);
        ArrayList<Resource> requirements = new ArrayList<>();
        requirements.add(Resource.Insect);
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Fungus);
        goldCard = new GoldCard(46, 2, Reign.Fungus, true, resources, requirements);

        ArrayList<Integer> req = new ArrayList<>();
        req.add(5);
        req.add(1);
        req.add(0);
        req.add(2);
        req.add(0);
        req.add(0);
        req.add(0);
        req.add(0);
        req.add(0);

        Assertions.assertTrue(goldCard.checkRequirement(req));
    }

    @Test
    void checkRequirement_false() throws InvalidIdException {

        PlaceableCard goldCard;
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Blocked);
        ArrayList<Resource> requirements = new ArrayList<>();
        requirements.add(Resource.Insect);
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Fungus);
        requirements.add(Resource.Fungus);
        goldCard = new GoldCard(46, 2, Reign.Fungus, true, resources, requirements);

        ArrayList<Integer> req = new ArrayList<>();
        req.add(1);
        req.add(1);
        req.add(0);
        req.add(2);
        req.add(0);
        req.add(0);
        req.add(0);
        req.add(0);
        req.add(0);

        Assertions.assertFalse(goldCard.checkRequirement(req));
    }

    @Test
    void isResource() throws InvalidIdException {
        PlaceableCard card1, card2;
        card1 = new ResourceCard(2);
        card2 = new GoldCard(46);

        Assertions.assertTrue(card1.isResource());
        Assertions.assertFalse(card2.isResource());
    }

    @Test
    void isGold() throws InvalidIdException {
        PlaceableCard card1, card2;
        card1 = new ResourceCard(2);
        card2 = new GoldCard(46);

        Assertions.assertFalse(card1.isGold());
        Assertions.assertTrue(card2.isGold());
    }

    @Test
    void isStarter() throws InvalidIdException {
        PlaceableCard card1, card2;
        card1 = new StarterCard(81);
        card2 = new GoldCard(46);

        Assertions.assertTrue(card1.isStarter());
        Assertions.assertFalse(card2.isStarter());
    }
}