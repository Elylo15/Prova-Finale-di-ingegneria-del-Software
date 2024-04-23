package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    int score;
    PlayerHand deck;
    PlayerArea playerArea;
    CommonArea commonArea;
    ObjectiveCard objective;

    @BeforeEach
    void set(){
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
    void playerTurn() throws noPlaceCardException {
        int cardPick = 2;
        int x = 3;
        int y = 4;
        int side = 1;
        int drawPick = 3;

        player.playTurn(cardPick, x, y, side, drawPick);

    }

    @Test
    void pickedResourceDeck(){
        int pick = 1;

        player.pickNewCard(pick);
        int objectiveId = objective.getID();
        boolean isInRange = objectiveId >= 1 && objectiveId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldDeck(){
        int pick = 2;

        player.pickNewCard(pick);
        int cardId = card.getID();
        boolean isInRange = cardId >= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceLeft(){
        int pick = 3;

        player.pickNewCard(pick);
        int objectiveId = objective.getID();
        boolean isInRange = objectiveId >= 1 && objectiveId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedResourceRight(){
        int pick = 4;

        player.pickNewCard(pick);
        int objectiveId = objective.getID();
        boolean isInRange = objectiveId >= 1 && objectiveId <= 40;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldLeft(){
        int pick = 5;

        player.pickNewCard(pick);
        int cardId = card.getID();
        boolean isInRange = cardId >= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

    @Test
    void pickedGoldRight(){
        int pick = 6;

        player.pickNewCard(pick);
        int cardId = card.getID();
        boolean isInRange = cardId >= 41 && cardId <= 80;
        assertTrue(isInRange);
    }

}