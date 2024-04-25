package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class MatchTest {
    Match match;
    CommonArea commonArea;
    ObjectiveCard[] commonObjective;
    ArrayList<Player> players;
    Player player1, player2, player3, player4;

    @BeforeEach
    void setUp() {
        player1 = new Player("Bianca", "Blue", commonArea);
        player2 = new Player("Agnese", "Yellow", commonArea);
        player3 = new Player("Nicoló", "Green", commonArea);
        player4 = new Player("Elisabetta", "Red", commonArea);
        match = new Match();
        players = match.getPlayers();
        commonObjective = match.getCommonObjective();
        commonArea = match.getCommonArea();
    }

    @Test
    void notAddingPlayerIfMoreThanFour() throws Exception {
        Player player5 = new Player("Bia", "Pink", commonArea);

        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        assertThrows(Exception.class, () -> match.addPlayer(player5));
    }

    @Test
    void addingFirstPlayer() throws Exception {
        match.addPlayer(player1);

        assertEquals(players.size(), 1);
    }

    @Test
    void addingSecondPlayer() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);

        assertEquals(players.size(), 2);
    }

    @Test
    void notStartingIfLessThanTwo() throws Exception {
        match.addPlayer(player1);

        Exception exception = assertThrows(Exception.class, () -> match.start(0, 1));
        assertEquals("Not enough players to start the match", exception.getMessage());
    }

    @Test
    void drawCommonObj1() {
        match.drawCommonObjective();
        int objectiveId1 = commonObjective[0].getID();
        int objectiveId2 = commonObjective[1].getID();
        boolean isInRange1 = objectiveId1 >= 87 && objectiveId1 <= 102;
        boolean isInRange2 = objectiveId2 >= 87 && objectiveId2 <= 102;
        assertTrue(isInRange1);
        assertTrue(isInRange2);
    }

    @Test
    void fromLastToFirstPlayerReturned() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        assertEquals(player2, match.nextPlayer(player1));
        assertEquals(player1, match.nextPlayer(player4));
    }

    @Test
    void winnerHasMorePoints() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        players.getFirst().setScore(24);
        players.get(1).setScore(23);
        players.get(2).setScore(26);
        players.get(3).setScore(23);

        assertEquals(match.winner(),  players.get(2));
    }

    @Test
    void winnerHasMoreObjectives() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        players.getFirst().setScore(24);
        players.get(1).setScore(23);

        //set cards
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
        starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);

        ObjectiveCard obj1;
        ArrayList<Integer[]> pattern = new ArrayList<>();
        pattern.add(new Integer[]{2,0});
        pattern.add(new Integer[]{3,1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj1 = new ObjectiveCard(91,3,null,pattern,reigns);

        ObjectiveCard obj2;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        obj2 = new ObjectiveCard(95,2,resources,null,null);

        ObjectiveCard obj3;
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        obj3 = new ObjectiveCard(96,2,resources,null,null);

        ObjectiveCard obj4;
        resources = new ArrayList<>();
        resources.add(Resource.Animal);
        resources.add(Resource.Animal);
        resources.add(Resource.Animal);
        obj4 = new ObjectiveCard(97,2,resources,null,null);

        ArrayList<PlaceableCard> fungusCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for(int i=0; i<5; i++)
                fungusCard.add(new ResourceCard(2, 0, Reign.Fungus, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        ArrayList<PlaceableCard> plantCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for(int i=0; i<4; i++)
                plantCard.add(new ResourceCard(12, 0, Reign.Plant, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlayerArea playerArea = players.getFirst().getPlayerArea();
        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(fungusCard.getFirst(), 1, 1, false);
        playerArea.placeCard(fungusCard.get(1), -1, 1, false);
        playerArea.placeCard(plantCard.getFirst(), 2, 2, false);
        playerArea.placeCard(fungusCard.get(2), -2, 2, false);
        playerArea.placeCard(fungusCard.get(3), 0, 2, false);
        playerArea.placeCard(plantCard.get(1), 1, 3, false);
        playerArea.placeCard(plantCard.get(2), -3, 1, false);
        playerArea.placeCard(fungusCard.get(4), -4, 2, false);
        playerArea.placeCard(plantCard.get(3), -1, 3, false);

        PlayerArea playerArea2 = players.get(1).getPlayerArea();
        playerArea2.placeStarterCard(starterCard, true);
        playerArea2.placeCard(fungusCard.getFirst(), 1, 1, false);
        playerArea2.placeCard(fungusCard.get(1), -1, 1, false);
        playerArea2.placeCard(plantCard.getFirst(), 2, 2, false);
        playerArea2.placeCard(fungusCard.get(2), -2, 2, false);
        playerArea2.placeCard(fungusCard.get(3), 0, 2, false);

        players.getFirst().setObjective(obj1);
        players.get(1).setObjective(obj4);
        commonObjective[0] = obj2;
        commonObjective[1] = obj3;

        match.addObjectivePoints(players.getFirst());
        match.addObjectivePoints(players.get(1));

        assertEquals(match.winner(),  players.getFirst());
    }

    @Test
    void addObjectivesPointsCorrectly() throws Exception {
        match.addPlayer(player1);
        players.getFirst().setScore(23);

        //set cards
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
        starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);

        ObjectiveCard obj1;
        ArrayList<Integer[]> pattern = new ArrayList<>();
        pattern.add(new Integer[]{2,0});
        pattern.add(new Integer[]{3,1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj1 = new ObjectiveCard(91,3,null,pattern,reigns);

        ObjectiveCard obj2;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        obj2 = new ObjectiveCard(95,2,resources,null,null);

        ObjectiveCard obj3;
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        obj3 = new ObjectiveCard(96,2,resources,null,null);

        ArrayList<PlaceableCard> fungusCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for(int i=0; i<5; i++)
                fungusCard.add(new ResourceCard(2, 0, Reign.Fungus, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        ArrayList<PlaceableCard> plantCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for(int i=0; i<4; i++)
                plantCard.add(new ResourceCard(12, 0, Reign.Plant, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlayerArea playerArea = players.getFirst().getPlayerArea();
        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(fungusCard.getFirst(), 1, 1, false);
        playerArea.placeCard(fungusCard.get(1), -1, 1, false);
        playerArea.placeCard(plantCard.getFirst(), 2, 2, false);
        playerArea.placeCard(fungusCard.get(2), -2, 2, false);
        playerArea.placeCard(fungusCard.get(3), 0, 2, false);
        playerArea.placeCard(plantCard.get(1), 1, 3, false);
        playerArea.placeCard(plantCard.get(2), -3, 1, false);
        playerArea.placeCard(fungusCard.get(4), -4, 2, false);
        playerArea.placeCard(plantCard.get(3), -1, 3, false);

        players.getFirst().setObjective(obj1);
        commonObjective[0] = obj2;
        commonObjective[1] = obj3;

        match.addObjectivePoints(players.getFirst());
        assertEquals(33, players.getFirst().getScore());

    }

    @Test
    void totalObjectiveCountedCorrectly() throws Exception {
        match.addPlayer(player1);
        players.getFirst().setScore(23);

        //set cards
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
        starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);

        ObjectiveCard obj1;
        ArrayList<Integer[]> pattern = new ArrayList<>();
        pattern.add(new Integer[]{2,0});
        pattern.add(new Integer[]{3,1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj1 = new ObjectiveCard(91,3,null,pattern,reigns);

        ObjectiveCard obj2;
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        obj2 = new ObjectiveCard(95,2,resources,null,null);

        ObjectiveCard obj3;
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        obj3 = new ObjectiveCard(96,2,resources,null,null);

        ArrayList<PlaceableCard> fungusCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Fungus);
        resources.add(Resource.Fungus);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for(int i=0; i<5; i++)
                fungusCard.add(new ResourceCard(2, 0, Reign.Fungus, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        ArrayList<PlaceableCard> plantCard = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(Resource.Plant);
        resources.add(Resource.Plant);
        resources.add(Resource.Blocked);
        resources.add(Resource.Empty);
        try {
            for(int i=0; i<4; i++)
                plantCard.add(new ResourceCard(12, 0, Reign.Plant, false, resources));
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        PlayerArea playerArea = players.getFirst().getPlayerArea();
        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(fungusCard.getFirst(), 1, 1, false);
        playerArea.placeCard(fungusCard.get(1), -1, 1, false);
        playerArea.placeCard(plantCard.getFirst(), 2, 2, false);
        playerArea.placeCard(fungusCard.get(2), -2, 2, false);
        playerArea.placeCard(fungusCard.get(3), 0, 2, false);
        playerArea.placeCard(plantCard.get(1), 1, 3, false);
        playerArea.placeCard(plantCard.get(2), -3, 1, false);
        playerArea.placeCard(fungusCard.get(4), -4, 2, false);
        playerArea.placeCard(plantCard.get(3), -1, 3, false);

        players.getFirst().setObjective(obj1);
        commonObjective[0] = obj2;
        commonObjective[1] = obj3;

        int totalObjectives = match.totalObjective(player1);
        assertEquals(4, totalObjectives);
    }

    @Test
    public void getCommonAreaTest() {
        assertNotNull(match.getCommonArea());
    }

    @Test
    public void getCommonObjectiveTest() {
        ObjectiveCard obj1;
        ArrayList<Integer[]> pattern = new ArrayList<>();
        pattern.add(new Integer[]{2,0});
        pattern.add(new Integer[]{3,1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj1 = new ObjectiveCard(91,3,null,pattern,reigns);

        commonObjective[0] = obj1;

        assertEquals(obj1, match.getCommonObjective()[0]);
    }

    @Test
    public void getPlayersTest() {
        assertNotNull(match.getPlayers());
    }

}