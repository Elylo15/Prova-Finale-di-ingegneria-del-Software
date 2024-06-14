package it.polimi.ingsw.protocol.messages.waitingForPlayerState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExpectedPlayersMessageTest {

    private expectedPlayersMessage message;

    @BeforeEach
    void setup() {
        message = new expectedPlayersMessage(3, false);
    }

    @Test
    @DisplayName("Expected players are correctly retrieved")
    void expectedPlayersAreCorrectlyRetrieved() {
        Assertions.assertEquals(3, message.getExpectedPlayers());
    }

    @Test
    @DisplayName("No response is false when expected players are provided")
    void noResponseIsFalseWhenExpectedPlayersAreProvided() {
        Assertions.assertFalse(message.isNoResponse());
    }

    @Test
    @DisplayName("No response is true when no expected players are provided")
    void noResponseIsTrueWhenNoExpectedPlayersAreProvided() {
        message = new expectedPlayersMessage(0, true);
        Assertions.assertTrue(message.isNoResponse());
    }
}