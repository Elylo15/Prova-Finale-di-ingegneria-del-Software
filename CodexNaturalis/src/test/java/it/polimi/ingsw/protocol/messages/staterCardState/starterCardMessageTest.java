package it.polimi.ingsw.protocol.messages.staterCardState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StarterCardMessageTest {

    private starterCardMessage message;

    @BeforeEach
    void setup() {
        message = new starterCardMessage(1, false);
    }

    @Test
    @DisplayName("Side is correctly retrieved")
    void sideIsCorrectlyRetrieved() {
        Assertions.assertEquals(1, message.getSide());
    }

    @Test
    @DisplayName("No response is false when side is provided")
    void noResponseIsFalseWhenSideIsProvided() {
        Assertions.assertFalse(message.isNoResponse());
    }

    @Test
    @DisplayName("No response is true when no side is provided")
    void noResponseIsTrueWhenNoSideIsProvided() {
        message = new starterCardMessage(0, true);
        Assertions.assertTrue(message.isNoResponse());
    }
}