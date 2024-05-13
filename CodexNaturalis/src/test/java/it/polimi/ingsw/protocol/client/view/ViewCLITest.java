package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Cell;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import it.polimi.ingsw.protocol.messages.responseMessage;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ViewCLITest {

   private ViewCLI viewCLI;
    private PlayerArea playerArea;

    @BeforeEach
    void setUp() {
        playerArea = new PlayerArea();
        viewCLI = new ViewCLI();
    }


    @Test
    void checkShowArea() throws noPlaceCardException {
        String[][] test = new String[4][4];
        test[0][0] = "81F/nullnull";
        test[0][1] = "81F/nullnull";
        test[0][2] = "null";
        test[0][3] = "null";
        test[1][0] = "81F/nullnull";
        test[1][1] = "2F/81F";
        test[1][2] = "2F/nullnull";
        test[1][3] = "null";
        test[2][0] = "null";
        test[2][1] = "2F/nullnull";
        test[2][2] = "3B/2F";
        test[2][3] = "3B/nullnull";
        test[3][0] = "null";
        test[3][1] = "null";
        test[3][2] = "3B/nullnull";
        test[3][3] = "4F/3B";



        viewCLI = new ViewCLI();
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
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        try {
            testCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }
        PlaceableCard testCard2;
        resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        resources.add(Resource.Empty);
        try {
            testCard2 = new ResourceCard(3, 0, Reign.Fungus, false, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }
        PlaceableCard testCard3;
        try {
            testCard3 = new ResourceCard(4, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1,1, true);
        playerArea.placeCard(testCard2,2,2,false);
        playerArea.placeCard(testCard3,3,3,true);



//        String[][] matrix = viewCLI.showPlayerArea(playerArea);
//
//        viewCLI.showPlayerArea(playerArea);
//
//        Assertions.assertEquals(test[0][0], matrix[0][0]);
//        Assertions.assertEquals(test[0][1], matrix[0][1]);
//        Assertions.assertNull(matrix[0][2]);
//        Assertions.assertNull(matrix[0][3]);
//        Assertions.assertEquals(test[1][0], matrix[1][0]);
//        Assertions.assertEquals(test[1][1], matrix[1][1]);
//        Assertions.assertEquals(test[1][2], matrix[1][2]);
//        Assertions.assertNull(matrix[1][3]);
//        Assertions.assertNull(matrix[2][0]);
//        Assertions.assertEquals(test[2][1], matrix[2][1]);
//        Assertions.assertEquals(test[2][2], matrix[2][2]);
//        Assertions.assertEquals(test[2][3], matrix[2][3]);
//        Assertions.assertNull(matrix[3][0]);
//        Assertions.assertNull(matrix[3][1]);
//        Assertions.assertEquals(test[3][2], matrix[3][2]);
//        Assertions.assertEquals(test[3][3], matrix[3][3]);





    }

    @Test
    void checkAnswer() {
        responseMessage message = new responseMessage(true);
        ViewCLI viewCLI = new ViewCLI();
        viewCLI.answer(message);
    }




}