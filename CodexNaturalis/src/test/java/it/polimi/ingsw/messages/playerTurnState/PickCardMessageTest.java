package it.polimi.ingsw.messages.playerTurnState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PickCardMessageTest {

    private pickCardMessage message;
    private Integer card;

    @BeforeEach
    public void setup() {
        card = 1;
        message = new pickCardMessage(card, false);
    }

    @Test
    @DisplayName("Card is correctly retrieved")
    public void cardIsCorrectlyRetrieved() {
        Assertions.assertEquals(card, message.getCard());
    }

    @Test
    @DisplayName("No response is false when card is provided")
    public void noResponseIsFalseWhenCardIsProvided() {
        Assertions.assertFalse(message.isNoResponse());
    }

    @Test
    @DisplayName("No response is true when no card is provided")
    public void noResponseIsTrueWhenNoCardIsProvided() {
        message = new pickCardMessage(null, true);
        Assertions.assertTrue(message.isNoResponse());
    }
}