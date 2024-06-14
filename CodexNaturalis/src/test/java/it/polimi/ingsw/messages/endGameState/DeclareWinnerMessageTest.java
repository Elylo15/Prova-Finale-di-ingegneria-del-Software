package it.polimi.ingsw.messages.endGameState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class DeclareWinnerMessageTest {

    private declareWinnerMessage message;
    private HashMap<String, Integer> scores;
    private HashMap<String, Integer> numberOfObjects;

    @BeforeEach
    public void setup() {
        scores = new HashMap<>();
        scores.put("player1", 10);
        scores.put("player2", 20);

        numberOfObjects = new HashMap<>();
        numberOfObjects.put("player1", 2);
        numberOfObjects.put("player2", 3);

        message = new declareWinnerMessage(scores, numberOfObjects);
    }

    @Test
    @DisplayName("Players points are correctly retrieved")
    public void playersPointsAreCorrectlyRetrieved() {
        Assertions.assertEquals(scores, message.getPlayersPoints());
    }

    @Test
    @DisplayName("Number of objects are correctly retrieved")
    public void numberOfObjectsAreCorrectlyRetrieved() {
        Assertions.assertEquals(numberOfObjects, message.getNumberOfObjects());
    }
}