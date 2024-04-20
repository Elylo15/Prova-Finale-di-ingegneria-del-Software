package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    private ArrayList<Resource> resources = new ArrayList<Resource>();
    private ArrayList<Resource> requirement = new ArrayList<Resource>();

    private int id;

    private boolean front;
    private Reign reign;
    private int points;

    private ArrayList<Cell> cells;

    @Test
    void checkIsEqual() throws InvalidIdException{
        reign = Reign.Fungus;
        points = 1;
        front = true;
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Fungus);
        requirement.add(Resource.Animal);
        ArrayList<Resource> testResource = new ArrayList<Resource>();
        testResource.add(Resource.Blocked);
        testResource.add(Resource.Empty);
        testResource.add(Resource.Empty);
        testResource.add(Resource.Fungus);
        ArrayList<Resource> testRequirement = new ArrayList<Resource>();
        testRequirement.add(Resource.Fungus);
        testRequirement.add(Resource.Fungus);
        testRequirement.add(Resource.Animal);
        GoldCard goldCard = new GoldCard(41, points, reign,front, resources, requirement);
        GoldCard test = new GoldCard(id,1,Reign.Fungus, true, testResource,testRequirement);
        Assertions.assertEquals(true, goldCard.equals(test));

    }

    @Test
    void checkGetPoints_true() throws InvalidIdException{
        points = 1;
        int test = 1;
        GoldCard goldCard = new GoldCard(41,points,Reign.Fungus,front,resources,requirement);
        Assertions.assertEquals(test, goldCard.getPoints());

    }
    @Test
    void checkGetPoints_false() throws InvalidIdException{
        points = 1;
        int test = 3;
        GoldCard goldCard = new GoldCard(41,points,Reign.Fungus,front,resources,requirement);
        Assertions.assertNotEquals(test, goldCard.getPoints());

    }


        @Test
    void checkGetResourceFRONT_true() throws InvalidIdException{
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Quill);
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Blocked);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Quill);
        GoldCard goldCard = new GoldCard(41,1,Reign.Fungus,true,resources,requirement);
        Assertions.assertEquals(test,goldCard.getResource());

    }
    @Test
    void checkGetResourceFRONT_false() throws InvalidIdException{
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Quill);
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Animal);
        test.add(Resource.Plant);
        test.add(Resource.Insect);
        test.add(Resource.Quill);
        GoldCard goldCard = new GoldCard(41,1,Reign.Fungus,true,resources,requirement);
        Assertions.assertNotEquals(test,goldCard.getResource());

    }

    @Test
    void checkGetResourceBACK_true() throws InvalidIdException{
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        GoldCard goldCard = new GoldCard(41,1,Reign.Fungus,false,resources,requirement);
        Assertions.assertEquals(test, goldCard.getResource());

    }
    @Test
    void checkGetResourceBACK_false() throws InvalidIdException{
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Animal);
        test.add(Resource.Plant);
        test.add(Resource.Empty);
        test.add(Resource.Empty);
        GoldCard goldCard = new GoldCard(41,1,Reign.Fungus,false,resources,requirement);
        Assertions.assertNotEquals(test, goldCard.getResource());

    }

        @Test
    void checkGetPermanentResourceFungus() throws InvalidIdException{
        reign = Reign.Fungus;
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Fungus);
        for (int i = 41; i < 51; i++) {
            GoldCard goldCard = new GoldCard(i,points,reign,false,resources,requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }
    @Test
    void checkGetPermanentResourcePlant() throws InvalidIdException{
        reign = Reign.Plant;
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Plant);
        for (int i = 51; i < 61; i++) {
            GoldCard goldCard = new GoldCard(i,points,reign,false,resources,requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }
    @Test
    void checkGetPermanentResourceAnimal() throws InvalidIdException{
        reign = Reign.Animal;
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Animal);
        for (int i = 61; i < 71; i++) {
            GoldCard goldCard = new GoldCard(i,points,reign,false,resources,requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }
    @Test
    void checkGetPermanentResourceInsect() throws InvalidIdException{
        reign = Reign.Insect;
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Insect);
        for (int i = 71; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i,points,reign,false,resources,requirement);
            Assertions.assertEquals(test, goldCard.getPermanentResource());
        }

    }


    @Test
    void checkGetRequirement_true() throws InvalidIdException{
        requirement.add(Resource.Plant);
        requirement.add(Resource.Plant);
        requirement.add(Resource.Animal);
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Plant);
        test.add(Resource.Plant);
        test.add(Resource.Animal);
        GoldCard goldCard = new GoldCard(53,1,Reign.Plant,true,resources,requirement);
        Assertions.assertEquals(test, goldCard.getRequirement());

    }
    @Test
    void checkGetRequirement_false() throws InvalidIdException{
        requirement.add(Resource.Plant);
        requirement.add(Resource.Plant);
        requirement.add(Resource.Animal);
        ArrayList<Resource> test = new ArrayList<Resource>();
        test.add(Resource.Insect);
        test.add(Resource.Fungus);
        test.add(Resource.Animal);
        GoldCard goldCard = new GoldCard(53,1,Reign.Plant,true,resources,requirement);
        Assertions.assertNotEquals(test, goldCard.getRequirement());

    }


    @Test
    void checkRequirement_true() throws InvalidIdException{
            requirement.add(Resource.Fungus);
            requirement.add(Resource.Fungus);
            requirement.add(Resource.Fungus);
            requirement.add(Resource.Plant);
            ArrayList<Integer> requirementTest = new ArrayList<Integer>();
            requirementTest.add(3);
            requirementTest.add(1);
            requirementTest.add(0);
            requirementTest.add(0);
            GoldCard goldCard = new GoldCard(45,2,Reign.Fungus,true,resources,requirement);
            Assertions.assertEquals(true, goldCard.checkRequirement(requirementTest));

    }
    @Test
    void checkRequirement_false() throws InvalidIdException{
            requirement.add(Resource.Fungus);
            requirement.add(Resource.Fungus);
            requirement.add(Resource.Fungus);
            requirement.add(Resource.Plant);
            ArrayList<Integer> requirementTest = new ArrayList<Integer>();
            requirementTest.add(1);
            requirementTest.add(1);
            requirementTest.add(1);
            requirementTest.add(0);           ;
            GoldCard goldCard = new GoldCard(45,2,Reign.Fungus,true,resources,requirement);
            Assertions.assertNotEquals(true, goldCard.checkRequirement(requirementTest));


    }
    @Test
    void checkRequirementBack() throws InvalidIdException{

        ArrayList<Integer> requirementTest = new ArrayList<Integer>();
        requirementTest.add(3);
        requirementTest.add(1);
        requirementTest.add(0);
        requirementTest.add(0);
        GoldCard goldCard = new GoldCard(45,2,Reign.Fungus,false,resources,requirement);
        Assertions.assertEquals(true, goldCard.checkRequirement(requirementTest));

    }


    @Test
    void checkIsGold_true() throws InvalidIdException {
        for (int i = 41; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i,points,reign,front,resources,requirement);
            Assertions.assertEquals(true, goldCard.isGold());
        }


    }

    @Test
    void checkIsGold_false() throws InvalidIdException {
        for (int i = 41; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i,points,reign,front,resources,requirement);
            Assertions.assertNotEquals(false, goldCard.isGold());


        }
    }

    @Test
    void checkclass () throws InvalidIdException {
        for (int i = 41; i < 81; i++) {
            GoldCard goldCard = new GoldCard(i,points,reign,front,resources,requirement);
            assertInstanceOf(GoldCard.class, goldCard);
        }

    }



}