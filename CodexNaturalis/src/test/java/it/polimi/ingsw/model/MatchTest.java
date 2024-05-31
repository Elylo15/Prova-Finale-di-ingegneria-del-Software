package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

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
    void addPlayer_ShouldNotAddIfMoreThanFour() throws Exception {
        Player player5 = new Player("Bia", "Pink", commonArea);

        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);

        assertThrows(Exception.class, () -> match.addPlayer(player5));
    }

    @Test
    void addPlayer_ShouldAddTheFirstPlayer() throws Exception {
        match.addPlayer(player1);

        assertEquals(players.size(), 1);
    }

    @Test
    void start_ShouldShuffleAndPlaceFrontCards() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);

        int beforeId1 = match.getCommonArea().getD1().getList().get(1).getID();
        int beforeId2 = match.getCommonArea().getD2().getList().get(6).getID();
        int beforeId3 = match.getCommonArea().getD3().getList().get(3).getID();
        int beforeId4 = match.getCommonArea().getD4().getList().get(2).getID();

        match.start();

        int id1 = match.getCommonArea().getTableCards().getFirst().getID();
        int id2 = match.getCommonArea().getTableCards().get(1).getID();
        int id3 = match.getCommonArea().getTableCards().get(2).getID();
        int id4 = match.getCommonArea().getTableCards().get(3).getID();

        boolean isInRange1 = id1 >= 1 && id1 <= 40;
        boolean isInRange2 = id2 >= 1 && id2 <= 40;
        boolean isInRange3 = id3 >= 41 && id3 <= 80;
        boolean isInRange4 = id4 >= 41 && id4 <= 80;

        //Maybe it is not the best idea, maybe it can be the same even shuffled
        boolean isShuffled1 = beforeId1 != match.getCommonArea().getD1().getList().get(1).getID();
        boolean isShuffled2 = beforeId2 != match.getCommonArea().getD2().getList().get(6).getID();
        boolean isShuffled3 = beforeId3 != match.getCommonArea().getD3().getList().get(3).getID();
        boolean isShuffled4 = beforeId4 != match.getCommonArea().getD4().getList().get(2).getID();

        assertTrue(isShuffled1);
        assertTrue(isShuffled2);
        assertTrue(isShuffled3);
        assertTrue(isShuffled4);

        assertNotNull(match.getCommonArea().getD1());
        assertNotNull(match.getCommonArea().getD2());
        assertNotNull(match.getCommonArea().getD3());
        assertNotNull(match.getCommonArea().getD4());
        assertNotNull(match.getCommonArea().getTableCards().getFirst());
        assertNotNull(match.getCommonArea().getTableCards().get(1));
        assertNotNull(match.getCommonArea().getTableCards().get(2));
        assertNotNull(match.getCommonArea().getTableCards().get(3));
        assertTrue(isInRange1);
        assertTrue(isInRange2);
        assertTrue(isInRange3);
        assertTrue(isInRange4);
    }

    @Test
    void drawCommonObjective_IdShouldBeInRange() {
        match.drawCommonObjective();
        int objectiveId1 = commonObjective[0].getID();
        int objectiveId2 = commonObjective[1].getID();
        boolean isInRange1 = objectiveId1 >= 87 && objectiveId1 <= 102;
        boolean isInRange2 = objectiveId2 >= 87 && objectiveId2 <= 102;
        assertTrue(isInRange1);
        assertTrue(isInRange2);
    }

    @Test
    void drawCommonObjective_ShouldNotDrawSameCard() {
        match.drawCommonObjective();

        assertNotEquals(match.getCommonObjective()[0], match.getCommonObjective()[1]);
    }

    @Test
    public void getCommonArea_ShouldNotBeNull() {
        assertNotNull(match.getCommonArea());
    }

    @Test
    public void getCommonObjective_idShouldBeInRange() {
        match.drawCommonObjective();
        int objectiveId1 = match.getCommonObjective()[0].getID();
        int objectiveId2 = match.getCommonObjective()[1].getID();
        boolean isInRange1 = objectiveId1 >= 87 && objectiveId1 <= 102;
        boolean isInRange2 = objectiveId2 >= 87 && objectiveId2 <= 102;
        assertTrue(isInRange1);
        assertTrue(isInRange2);
    }

    @Test
    public void getPlayers_ShouldReturnPlayer() throws Exception {
        match.addPlayer(player1);
        match.addPlayer(player2);

        assertEquals(match.getPlayers().getFirst(), player1);
        assertEquals(match.getPlayers().getLast(), player2);
    }

}