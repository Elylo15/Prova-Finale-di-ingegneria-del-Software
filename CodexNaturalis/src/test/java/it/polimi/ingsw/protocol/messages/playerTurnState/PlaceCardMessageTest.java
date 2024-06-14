package it.polimi.ingsw.protocol.messages.playerTurnState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlaceCardMessageTest {

    private placeCardMessage message;

    @BeforeEach
    public void setup() {
        message = new placeCardMessage(1, 2, 3, 4, false);
    }

    @Test
    @DisplayName("Card is correctly retrieved")
    public void cardIsCorrectlyRetrieved() {
        Assertions.assertEquals(1, message.getCard());
    }

    @Test
    @DisplayName("Row is correctly retrieved")
    public void rowIsCorrectlyRetrieved() {
        Assertions.assertEquals(3, message.getRow());
    }

    @Test
    @DisplayName("Column is correctly retrieved")
    public void columnIsCorrectlyRetrieved() {
        Assertions.assertEquals(4, message.getColumn());
    }

    @Test
    @DisplayName("Front is correctly retrieved")
    public void frontIsCorrectlyRetrieved() {
        Assertions.assertEquals(2, message.getFront());
    }

    @Test
    @DisplayName("No response is false when card is placed")
    public void noResponseIsFalseWhenCardIsPlaced() {
        Assertions.assertFalse(message.isNoResponse());
    }

    @Test
    @DisplayName("No response is true when no card is placed")
    public void noResponseIsTrueWhenNoCardIsPlaced() {
        message = new placeCardMessage(0, 0, 0, 0, true);
        Assertions.assertTrue(message.isNoResponse());
    }
}