package it.polimi.ingsw.messages.connectionState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConnectionResponseMessageTest {

    private connectionResponseMessage message;

    @BeforeEach
    public void setup() {
        message = new connectionResponseMessage(true);
    }

    @Test
    @DisplayName("Correct value is correctly retrieved")
    public void correctValueIsCorrectlyRetrieved() {
        Assertions.assertTrue(message.getCorrect());
    }

    @Test
    @DisplayName("Correct value is correctly set")
    public void correctValueIsCorrectlySet() {
        message.setCorrect(false);
        Assertions.assertFalse(message.getCorrect());
    }
}