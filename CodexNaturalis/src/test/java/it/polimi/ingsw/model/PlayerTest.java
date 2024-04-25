package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.Json.LoadDecks;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    PlayerHand deck;
    PlayerArea playerArea;
    CommonArea commonArea;
    ObjectiveCard objective;

    @BeforeEach
    void setUp() {
        commonArea = (new LoadDecks()).load();
        player = new Player("Bianca", "Blue", commonArea);
        deck = player.getPlayerHand();
        playerArea = player.getPlayerArea();
        commonArea.getD1().shuffle();
        commonArea.getD2().shuffle();
        commonArea.getD3().shuffle();
        commonArea.getD4().shuffle();
    }


    @Test
    void initialHandNumberCards() {

        player.initialHand();
        assertEquals(4, deck.getPlaceableCards().size());
    }

    @Test
    void Objective() {

        player.setObjective(player.pickObjectiveCard(2));
        int objectiveId = player.getObjective().getID();
        boolean isInRange = objectiveId >= 86 && objectiveId <= 102;
        assertTrue(isInRange);
    }

    @Test
    void playTurnTest() {
        player.initialHand();
        try {
            player.playTurn(1, 2, 3, 1);
            assertEquals(3, deck.getPlaceableCards().size());
        } catch (noPlaceCardException e) {
            assertThrows(noPlaceCardException.class, () -> player.playTurn(1, 2, 3, 1));
        }

    }

    @Test
    void pickObjective() {
        int pick = 1;

        objective = player.pickObjectiveCard(pick);
        int objectiveId = objective.getID();
        boolean isInRange = objectiveId >= 86 && objectiveId <= 102;
        assertTrue(isInRange);
    }

    @Test
    void pickObjective2() {
        int pick = 2;

        objective = player.pickObjectiveCard(pick);
        int objectiveId = objective.getID();
        boolean isInRange = objectiveId >= 86 && objectiveId <= 102;
        assertTrue(isInRange);
    }

    @Test
    void PickSideTest(){
        assertTrue(player.pickSide(1));
        assertFalse(player.pickSide(0));
    }

    @Test
    void correctPickPosition(){
        int[] position = player.pickPosition(2, 3);
        assertEquals(position[0], 2);
        assertEquals(position[1], 3);
    }

    @Test
    void correctPickedCard1(){
        player.initialHand();
        PlaceableCard card = player.pickPlaceableCard(1);
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 80 ;
        assertTrue(isInRange);
    }
    @Test
    void correctPickedCard2(){
        player.initialHand();
        PlaceableCard card = player.pickPlaceableCard(2);
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 80 ;
        assertTrue(isInRange);
    }

    @Test
    void correctPickedCard3(){
        player.initialHand();
        PlaceableCard card = player.pickPlaceableCard(3);
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 80 ;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceDeck() throws InvalidIdException {
        int pick = 1;

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldDeck() throws InvalidIdException{
        int pick = 2;

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId>= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceLeft() throws InvalidIdException{
        int pick = 3;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().add(resourceCard1);
        commonArea.getTableCards().add(resourceCard2);
        commonArea.getTableCards().add(goldCard1);
        commonArea.getTableCards().add(goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceRight() throws InvalidIdException{
        int pick = 4;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().add(resourceCard1);
        commonArea.getTableCards().add(resourceCard2);
        commonArea.getTableCards().add(goldCard1);
        commonArea.getTableCards().add(goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldLeft() throws InvalidIdException{
        int pick = 5;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().add(resourceCard1);
        commonArea.getTableCards().add(resourceCard2);
        commonArea.getTableCards().add(goldCard1);
        commonArea.getTableCards().add(goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldRight() throws InvalidIdException{
        int pick = 6;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().add(resourceCard1);
        commonArea.getTableCards().add(resourceCard2);
        commonArea.getTableCards().add(goldCard1);
        commonArea.getTableCards().add(goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

    @Test
    public void getScoreTest() {
        player.setScore(22);
        int score = player.getScore();
        assertEquals(22, score);
    }

    @Test
    public void setScoreTest() {
        player.setScore(13);
        assertEquals(13,player.getScore());
    }

    @Test
    public void setObjectiveTest() {
        ObjectiveCard obj;
        ArrayList<Integer[]> pattern = new ArrayList<>();
        pattern.add(new Integer[]{2,0});
        pattern.add(new Integer[]{3,1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj = new ObjectiveCard(91,3,null,pattern,reigns);

        player.setObjective(obj);
        assertEquals(obj.getID(), player.getObjective().getID());
    }

    @Test
    public void getPlayerAreaTest() {
        assertNotNull(player.getPlayerArea());
    }

    @Test
    public void getObjectiveTest() {
        ObjectiveCard obj;
        ArrayList<Integer[]> pattern = new ArrayList<>();
        pattern.add(new Integer[]{2,0});
        pattern.add(new Integer[]{3,1});
        ArrayList<Reign> reigns = new ArrayList<>();
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Fungus);
        reigns.add(Reign.Plant);
        obj = new ObjectiveCard(91,3,null,pattern,reigns);

        player.setObjective(obj);
        assertEquals(obj, player.getObjective());
    }

    @Test
    public void getPlayerHandTest() {
        assertNotNull(player.getPlayerHand());
    }

}