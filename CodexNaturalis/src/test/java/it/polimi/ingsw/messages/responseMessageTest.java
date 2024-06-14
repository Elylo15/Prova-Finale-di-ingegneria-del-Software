package it.polimi.ingsw.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResponseMessageTest {

    private responseMessage message;

    @BeforeEach
    void setup() {
        message = new responseMessage(true);
    }

    @Test
    @DisplayName("Correct flag is correctly retrieved")
    void correctFlagIsCorrectlyRetrieved() {
        Assertions.assertTrue(message.getCorrect());
    }

    @Test
    @DisplayName("Correct flag is correctly updated")
    void correctFlagIsCorrectlyUpdated() {
        message.setCorrect(false);
        Assertions.assertFalse(message.getCorrect());
    }
}