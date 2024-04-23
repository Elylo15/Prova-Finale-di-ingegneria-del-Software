package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class MatchTest {
    Match match;
    CommonArea commonArea;
    ObjectiveCard[] commonObjective;
    Player player1, player2, player3, player4;

    @BeforeEach
    void setUp() {
        player1 = new Player("Bianca", "Blue", commonArea);
        player2 = new Player("Agnese", "Yellow", commonArea);
        player3 = new Player("Nicoló", "Green", commonArea);
        player4 = new Player("Elisabetta", "Red", commonArea);
        match = new Match();
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

        assertEquals(match.players.size(), 1);
    }

    @Test
    void addingSecondPlayer() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);

        assertEquals(match.players.size(), 2);
    }

    @Test
    void notStartingIfLessThanTwo() throws Exception {
        match.addPlayer(player1);

        Exception exception = assertThrows(Exception.class, () -> match.start(true, 1));
        assertEquals("Not enough players to start the match", exception.getMessage());
    }

    @Test
    void nextPlayerReturned() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);

        assertEquals(player2, match.nextPlayer(player1));
    }

    @Test
    void fromLastToFirstPlayerReturned() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        assertEquals(player1, match.nextPlayer(player4));
    }

    @Test
    void winnerHasMorePoints() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        match.players.getFirst().setScore(24);
        match.players.get(1).setScore(23);
        match.players.get(2).setScore(26);
        match.players.get(3).setScore(23);

        assertEquals(match.winner(),  match.players.get(2));
    }

    //@Test
    void winnerHasMoreObjectives() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        match.players.getFirst().setScore(23);
        match.players.get(1).setScore(23);
        match.players.get(2).setScore(26);
        match.players.get(3).setScore(23);
        //like in totalObjectiveCorrect, match simulated
        //right now card is null
        assertEquals(match.winner(),  match.players.get(3));
    }

    //@Test
    void totalObjectivesCorrect() throws Exception {
        match.addPlayer(player1);
        this.commonObjective = new ObjectiveCard[2];


        match.drawCommonObjective();
        //simulated match?

        int totalObjectives = match.totalObjective(player1);
        int expectedObjectives = 0;

        assertEquals(expectedObjectives, totalObjectives);
    }

    @Test
    void drawCommonObj1(){
        match.drawCommonObjective();
        int objectiveId = commonObjective[0].getID();
        boolean isInRange = objectiveId >= 87 && objectiveId <= 102;
        assertTrue(isInRange);
    }

    @Test
    void drawCommonObj2(){
        this.commonObjective = new ObjectiveCard[2];
        match.drawCommonObjective();
        int objectiveId = commonObjective[1].getID();
        boolean isInRange = objectiveId >= 87 && objectiveId <= 102;
        assertTrue(isInRange);
    }

    //@Test
    void pointsAddedCorrectly() throws Exception {
        //Correct
    }


}