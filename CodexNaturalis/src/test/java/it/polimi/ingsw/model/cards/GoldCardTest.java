package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    private final ArrayList<Resource> resources = new ArrayList<>();
    private final ArrayList<Resource> requirement = new ArrayList<>();

    private boolean front;
    private Reign reign;
    private int points;

    private CommonArea commonArea;

    @BeforeEach
    void setUp()
    {
        LoadDecks loadDecks = new LoadDecks();
        commonArea = loadDecks.load();
    }

    @Test
    void checkIsEqual() throws InvalidIdException {
        reign = Reign.Fungus;
        points = 1;
        front = true;
        int id = 41;
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Animal);
        ArrayList<Resource> testResource = new ArrayList<>();
        testResource.add(Resource.Blocked);
        testResource.add(Resource.Empty);
        testResource.add(Resource.Empty);
        testResource.add(Resource.Fungus);
        ArrayList<Resource> testRequirement = new ArrayList<>();
        testRequirement.add(Resource.Fungus);
        testRequirement.add(Resource.Fungus);
        testRequirement.add(Resource.Animal);
        GoldCard goldCard = new GoldCard(id, points, reign, true, resources, requirement);
        GoldCard test = new GoldCard(id, 1, Reign.Fungus, true, testResource, testRequirement);
        assertEquals(goldCard, test);
    }

    @Test
    void checkGetPoints_true() throws InvalidIdException {
        points = 1;
        int test = 1;
        GoldCard goldCard = new GoldCard(41, points, Reign.Fungus, front, resources, requirement);
        Assertions.assertEquals(test, goldCard.getPoints());

    }

    @Test
    void checkGetPoints_false() throws InvalidIdException {
        points = 1;
        int test = 3;
        GoldCard goldCard = new GoldCard(41, points, Reign.Fungus, front, resources, requirement);
        Assertions.assertNotEquals(test, goldCard.getPoints());

    }

    @Test
    void checkGetResourceFRONT_true() throws InvalidIdException {
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Quill);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Blocked);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Quill);
        GoldCard goldCard = new GoldCard(41, 1, Reign.Fungus, true, resources, requirement);
        Assertions.assertEquals(test, goldCard.getResource());

    }

    @Test
    void checkGetResourceFRONT_false() throws InvalidIdException {
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Quill);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Animal);
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        test.add(Resource.Quill);
        GoldCard goldCard = new GoldCard(41, 1, Reign.Fungus, true, resources, requirement);
        Assertions.assertNotEquals(test, goldCard.getResource());

    }

    @Test
    void checkGetResourceBACK_true() throws InvalidIdException {
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        GoldCard goldCard = new GoldCard(41, 1, Reign.Fungus, false, resources, requirement);
        Assertions.assertEquals(test, goldCard.getResource());

    }

    @Test
    void checkGetResourceBACK_false() throws InvalidIdException {
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Animal);
        test.add(Resource.Plant);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        GoldCard goldCard = new GoldCard(41, 1, Reign.Fungus, false, resources, requirement);
        Assertions.assertNotEquals(test, goldCard.getResource());

    }

    @Test
    void checkGetPermanentResourceFungus() throws InvalidIdException {
        reign = Reign.Fungus;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Fungus);
        for (int i = 41; i < 51; i++) {
            GoldCard goldCard = new GoldCard(i, points, reign, false, resources, requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }

    @Test
    void checkGetPermanentResourcePlant() throws InvalidIdException {
        reign = Reign.Plant;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Plant);
        for (int i = 51; i < 61; i++) {
            GoldCard goldCard = new GoldCard(i, points, reign, false, resources, requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }

    @Test
    void checkGetPermanentResourceAnimal() throws InvalidIdException {
        reign = Reign.Animal;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Animal);
        for (int i = 61; i < 71; i++) {
            GoldCard goldCard = new GoldCard(i, points, reign, false, resources, requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }

    @Test
    void checkGetPermanentResourceInsect() throws InvalidIdException {
        reign = Reign.Insect;
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Insect);
        for (int i = 71; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i, points, reign, false, resources, requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }

    @Test
    void checkGetRequirement_true() throws InvalidIdException {
        requirement.add(Resource.Plant);
        requirement.add(Resource.Plant);
        requirement.add(Resource.Animal);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Plant);
        test.add(Resource.Plant);
        test.add(Resource.Animal);
        GoldCard goldCard = new GoldCard(53, 1, Reign.Plant, true, resources, requirement);
        Assertions.assertEquals(test, goldCard.getRequirement());

    }

    @Test
    void checkGetRequirement_false() throws InvalidIdException {
        requirement.add(Resource.Plant);
        requirement.add(Resource.Plant);
        requirement.add(Resource.Animal);
        ArrayList<Resource> test = new ArrayList<>();
        test.add(Resource.Insect);
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        GoldCard goldCard = new GoldCard(53, 1, Reign.Plant, true, resources, requirement);
        Assertions.assertNotEquals(test, goldCard.getRequirement());

    }

    @Test
    void checkRequirement_true() throws InvalidIdException {
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Plant);
        ArrayList<Integer> requirementTest = new ArrayList<>(Arrays.asList(3, 0, 0, 1));
        GoldCard goldCard = new GoldCard(45, 2, Reign.Fungus, true, resources, requirement);
        assertTrue(goldCard.checkRequirement(requirementTest));
    }

    @Test
    void checkRequirement_false() throws InvalidIdException {
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Plant);
        ArrayList<Integer> requirementTest = new ArrayList<>();
        requirementTest.add(1);
        requirementTest.add(1);
        requirementTest.add(1);
        requirementTest.add(0);
        GoldCard goldCard = new GoldCard(45, 2, Reign.Fungus, true, resources, requirement);
        assertFalse(goldCard.checkRequirement(requirementTest));
    }

    @Test
    void checkRequirementBack() throws InvalidIdException {

        ArrayList<Integer> requirementTest = new ArrayList<>();
        requirementTest.add(3);
        requirementTest.add(1);
        requirementTest.add(0);
        requirementTest.add(0);
        GoldCard goldCard = new GoldCard(45, 2, Reign.Fungus, false, resources, requirement);
        assertTrue(goldCard.checkRequirement(requirementTest));
    }

    @Test
    void checkIsGold_true() throws InvalidIdException {
        for (int i = 41; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i, points, reign, front, resources, requirement);
            assertTrue(goldCard.isGold());
        }
    }

    @Test
    void checkIsGold_false() throws InvalidIdException {
        for (int i = 41; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i, points, reign, front, resources, requirement);
            Assertions.assertNotEquals(false, goldCard.isGold());
        }
    }

    @Test
    void checkClass() throws InvalidIdException {
        for (int i = 41; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i, points, reign, front, resources, requirement);
            assertInstanceOf(GoldCard.class, goldCard);
        }
    }

    @Test
    void shouldCreateGoldCardWithValidId() throws InvalidIdException {
        GoldCard goldCard = new GoldCard(50);
        assertEquals(50, goldCard.getID());
    }

    @Test
    void shouldThrowExceptionWhenIdIsTooSmallOrTooBig() {
        assertThrows(InvalidIdException.class, () -> new GoldCard(39));
        assertThrows(InvalidIdException.class, () -> new GoldCard(81));
    }

    @Test
    void shouldNotThrowExceptionWhenIdIsAtLimit() {
        for (int i=41; i<81; i++) {
            int a = i;
            assertDoesNotThrow(() -> new GoldCard(a));
        }
    }
    @Test
    void LoadDecks54() throws InvalidIdException {
        Deck<GoldCard> cards = commonArea.getD2();
        GoldCard card = (GoldCard) cards.getCard(54);

        ArrayList<Resource> testResource = new ArrayList<>();
        testResource.add(Resource.Blocked);
        testResource.add(Resource.Empty);
        testResource.add(Resource.Empty);
        testResource.add(Resource.Empty);
        ArrayList<Resource> testRequirement = new ArrayList<>();
        testRequirement.add(Resource.Plant);
        testRequirement.add(Resource.Plant);
        testRequirement.add(Resource.Plant);
        testRequirement.add(Resource.Insect);


        GoldCard testCard = new GoldCard(54,2,Reign.Plant,true,testResource,testRequirement);

        Assertions.assertEquals(testCard.getPoints(),card.getPoints());
        Assertions.assertEquals(testCard.getReign(),card.getReign());
        Assertions.assertEquals(testCard.getResource(),card.getResource());
        Assertions.assertEquals(testCard.getRequirement(),card.getRequirement());
        Assertions.assertTrue(card.isGold());
        Assertions.assertTrue(card.equals(testCard));

        ArrayList<Integer> req = new ArrayList<>();
        req.add(1); //fungus
        req.add(1); //insect
        req.add(2); //animal
        req.add(0); //plant
        req.add(0); //manuscript
        req.add(0); //quill
        req.add(0); //inkwell
        req.add(3); //empty
        req.add(1); //blocked
        Assertions.assertFalse(card.checkRequirement(req));

        card.setFront(false);
        Assertions.assertTrue(card.checkRequirement(req));

    }
    @Test
    void loadLoadDecksCards() throws InvalidIdException {
        Deck<GoldCard> cards = commonArea.getD2();
        for (int i = 41; i< 81; i++){

            GoldCard card = (GoldCard) cards.getCard(i);
            Assertions.assertTrue(card.isGold());
            //the back of the cards should have only empty resources
            ArrayList<Resource> test = new ArrayList<>();
            test.add(Resource.Empty);
            test.add(Resource.Empty);
            test.add(Resource.Empty);
            test.add(Resource.Empty);

            card.setFront(false);
            ArrayList<Integer> req = new ArrayList<>();
            req.add(2);
            Assertions.assertEquals(test,card.getResource());
            Assertions.assertTrue(card.checkRequirement(req));

            ArrayList<Resource> permanent = new ArrayList<>();

            if(card.getID()>40 && card.getID()<51){
                permanent.add(Resource.Fungus);
                assertEquals(permanent, card.getPermanentResource());
            }
            if(card.getID()>50 && card.getID()<61){
                permanent.add(Resource.Plant);
                assertEquals(permanent, card.getPermanentResource());
            }if(card.getID()>60 && card.getID()<71){
                permanent.add(Resource.Animal);
                assertEquals(permanent, card.getPermanentResource());
            }if(card.getID()>70 && card.getID()<81){
                permanent.add(Resource.Insect);
                assertEquals(permanent, card.getPermanentResource());
            }

        }


    }

}
