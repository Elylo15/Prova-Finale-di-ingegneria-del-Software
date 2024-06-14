package it.polimi.ingsw.protocol.messages.connectionState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChosenNameMessageTest {

    private chosenNameMessage message;
    private String name;

    @BeforeEach
    public void setup() {
        name = "alfa";
        message = new chosenNameMessage(name);
    }

    @Test
    @DisplayName("Name is correctly retrieved")
    public void nameIsCorrectlyRetrieved() {
        Assertions.assertEquals(name, message.getName());
    }

    @Test
    @DisplayName("Name is correctly set")
    public void nameIsCorrectlySet() {
        String newName = "beta";
        message = new chosenNameMessage(newName);
        Assertions.assertEquals(newName, message.getName());
    }
}