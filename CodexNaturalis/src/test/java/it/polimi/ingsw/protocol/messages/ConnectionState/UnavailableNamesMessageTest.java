package it.polimi.ingsw.protocol.messages.ConnectionState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class UnavailableNamesMessageTest {

    private unavailableNamesMessage message;
    private ArrayList<String> names;

    @BeforeEach
    public void setup() {
        names = new ArrayList<>(Arrays.asList("ALFA", "BETA", "GAMMA"));
        message = new unavailableNamesMessage(names);
    }

    @Test
    @DisplayName("Names are correctly retrieved")
    public void namesAreCorrectlyRetrieved() {
        Assertions.assertEquals(names, message.getNames());
    }

    @Test
    @DisplayName("Names are correctly set")
    public void namesAreCorrectlySet() {
        ArrayList<String> newNames = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        message = new unavailableNamesMessage(newNames);
        Assertions.assertEquals(newNames, message.getNames());
    }

    @Test
    @DisplayName("Empty names list is handled correctly")
    public void emptyNamesListIsHandledCorrectly() {
        message = new unavailableNamesMessage(new ArrayList<>());
        Assertions.assertTrue(message.getNames().isEmpty());
    }

    @Test
    @DisplayName("toString returns correct format")
    public void toStringReturnsCorrectFormat() {
        Assertions.assertEquals("[John, Jane, Doe]", message.toString());
    }
}