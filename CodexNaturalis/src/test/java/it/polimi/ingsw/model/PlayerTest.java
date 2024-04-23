package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Json.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    PlayerHand deck;
    ObjectiveCard objective;
    CommonArea commonArea;

    @BeforeEach
    void set(){
        player = new Player("Bianca", "Blue", commonArea);
        deck = new PlayerHand();
    }


    @Test
    void initialHandNumberCards() {
        boolean side = true;
        int pick = 2;

        player.initialHand(side, pick);
        assertEquals(4, deck.getPlaceableCards().size());
    }

    @Test
    void initialHandObjective() {
        boolean side = true;
        int pick = 2;

        player.initialHand(side, pick);
        int objectiveId = objective.getID();
        boolean isInRange = objectiveId >= 86 && objectiveId <= 102;
        assertTrue(isInRange);
    }

    @Test
    void pickObjective() {
        int pick = 2;

        player.pickObjectiveCard(pick);
        int objectiveId = objective.getID();
        boolean isInRange = objectiveId >= 86 && objectiveId <= 102;
        assertTrue(isInRange);
    }

    @Test
    void correctPickPosition(){
        int[] position = player.pickPosition(2, 3);
        assertEquals(position[1], 2);
        assertEquals(position[2], 3);
    }

    @Test
    void correctPickedCard1(){
        PlaceableCard card = player.pickPlaceableCard(1);
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 80 ;
        assertTrue(isInRange);
    }
    @Test
    void correctPickedCard2(){
        PlaceableCard card = player.pickPlaceableCard(2);
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 80 ;
        assertTrue(isInRange);
    }

    @Test
    void correctPickedCard3(){
        PlaceableCard card = player.pickPlaceableCard(3);
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 80 ;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceDeck() throws InvalidIdException {
        int pick = 1;

        PlayerHand playerHand = player.getPlayerHand();
        player.pickNewCard(pick);
        PlaceableCard card = playerHand.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldDeck() throws InvalidIdException{
        int pick = 2;

        PlayerHand playerHand = player.getPlayerHand();
        player.pickNewCard(pick);
        PlaceableCard card = playerHand.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId>= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceLeft() throws InvalidIdException{
        int pick = 3;

        PlayerHand playerHand = player.getPlayerHand();
        player.pickNewCard(pick);
        PlaceableCard card = playerHand.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceRight() throws InvalidIdException{
        int pick = 4;

        PlayerHand playerHand = player.getPlayerHand();
        player.pickNewCard(pick);
        PlaceableCard card = playerHand.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 1 && cardId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldLeft() throws InvalidIdException{
        int pick = 5;

        PlayerHand playerHand = player.getPlayerHand();
        player.pickNewCard(pick);
        PlaceableCard card = playerHand.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldRight() throws InvalidIdException{
        int pick = 6;

        PlayerHand playerHand = player.getPlayerHand();
        player.pickNewCard(pick);
        PlaceableCard card = playerHand.getPlaceableCards().getLast();
        int cardId = card.getID();
        boolean isInRange = cardId >= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

}