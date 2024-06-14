package it.polimi.ingsw.messages.connectionState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChosenColorMessageTest {

    private chosenColorMessage message;
    private String color;

    @BeforeEach
    public void setup() {
        color = "Red";
        message = new chosenColorMessage(color);
    }

    @Test
    @DisplayName("Color is correctly retrieved")
    public void colorIsCorrectlyRetrieved() {
        Assertions.assertEquals(color, message.getColor());
    }

    @Test
    @DisplayName("Color is correctly set")
    public void colorIsCorrectlySet() {
        String newColor = "Blue";
        message.setColor(newColor);
        Assertions.assertEquals(newColor, message.getColor());
    }
}