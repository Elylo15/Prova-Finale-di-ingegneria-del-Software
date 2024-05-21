package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

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
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            testCard = new ResourceCard(2, 0, Reign.Fungus, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }
        PlaceableCard testCard2;
        resources = new ArrayList<>();
        resources.add(Resource.Quill);
        resources.add(Resource.Blocked);
        resources.add(Resource.Animal);
        resources.add(Resource.Fungus);
        try {
            testCard2 = new ResourceCard(27, 0, Reign.Animal, false, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }
        PlaceableCard testCard3;
        resources = new ArrayList<>();
        resources.add(Resource.Insect);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        resources.add(Resource.Blocked);
        try {
            testCard3 = new ResourceCard(31, 0, Reign.Insect, true, resources);
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1,1, true);
        playerArea.placeCard(testCard2,2,2,false);
        playerArea.placeCard(testCard3,-1,-1,true);

        viewCLI.showPlayerArea(playerArea);


    }
    @Test
    void testEnd() {
        HashMap<String, Integer> scores = new HashMap<>();
        scores.put("player1", 10);
        scores.put("player2", 20);
        scores.put("player3", 16);
        HashMap<String, Integer> objective = new HashMap<>();
        objective.put("player1", 3);
        objective.put("player2", 4);
        objective.put("player3", 2);
        declareWinnerMessage message = new declareWinnerMessage(scores,objective);
        viewCLI.endGame(message);
    }

    @Test
    void checkAnswer() {
        responseMessage message = new responseMessage(false);
        viewCLI.answer(message);
    }




}