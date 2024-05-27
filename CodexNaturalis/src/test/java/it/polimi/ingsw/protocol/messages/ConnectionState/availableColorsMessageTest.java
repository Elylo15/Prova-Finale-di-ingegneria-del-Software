package it.polimi.ingsw.protocol.messages.ConnectionState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class availableColorsMessageTest {

    private availableColorsMessage message;
    private ArrayList<String> colors;

    @BeforeEach
    public void setup() {
        colors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green"));
        message = new availableColorsMessage(colors);
    }

    @Test
    @DisplayName("Colors are correctly retrieved")
    public void colorsAreCorrectlyRetrieved() {
        Assertions.assertEquals(colors, message.getColors());
    }

    @Test
    @DisplayName("Colors are correctly set")
    public void colorsAreCorrectlySet() {
        ArrayList<String> newColors = new ArrayList<>(Arrays.asList("Yellow", "Purple"));
        message = new availableColorsMessage(newColors);
        Assertions.assertEquals(newColors, message.getColors());
    }

    @Test
    @DisplayName("Empty colors list is handled correctly")
    public void emptyColorsListIsHandledCorrectly() {
        message = new availableColorsMessage(new ArrayList<>());
        Assertions.assertTrue(message.getColors().isEmpty());
    }

    @Test
    @DisplayName("toString returns correct format")
    public void toStringReturnsCorrectFormat() {
        Assertions.assertEquals("[Red, Blue, Green]", message.toString());
    }
}