package it.polimi.ingsw.messages.serverOptionState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class ServerOptionMessageTest {

    private serverOptionMessage message;
    private ArrayList<Integer> waitingMatches;
    private ArrayList<Integer> runningMatches;
    private ArrayList<Integer> savedMatches;

    @BeforeEach
    void setup() {
        waitingMatches = new ArrayList<>(Arrays.asList(1, 2, 3));
        runningMatches = new ArrayList<>(Arrays.asList(4, 5, 6));
        savedMatches = new ArrayList<>(Arrays.asList(7, 8, 9));
        message = new serverOptionMessage(true, 1, 2, true, 3, waitingMatches, runningMatches, savedMatches);
    }

    @Test
    @DisplayName("New match flag is correctly retrieved")
    void newMatchFlagIsCorrectlyRetrieved() {
        Assertions.assertTrue(message.isNewMatch());
    }

    @Test
    @DisplayName("Match ID is correctly retrieved")
    void matchIDIsCorrectlyRetrieved() {
        Assertions.assertEquals(1, message.getMatchID());
    }

    @Test
    @DisplayName("Started match ID is correctly retrieved")
    void startedMatchIDIsCorrectlyRetrieved() {
        Assertions.assertEquals(2, message.getStartedMatchID());
    }

    @Test
    @DisplayName("Load match flag is correctly retrieved")
    void loadMatchFlagIsCorrectlyRetrieved() {
        Assertions.assertTrue(message.isLoadMatch());
    }

    @Test
    @DisplayName("Saved match ID is correctly retrieved")
    void savedMatchIDIsCorrectlyRetrieved() {
        Assertions.assertEquals(3, message.getSavedMatchID());
    }

    @Test
    @DisplayName("Waiting matches are correctly retrieved")
    void waitingMatchesAreCorrectlyRetrieved() {
        Assertions.assertEquals(waitingMatches, message.getWaitingMatches());
    }

    @Test
    @DisplayName("Running matches are correctly retrieved")
    void runningMatchesAreCorrectlyRetrieved() {
        Assertions.assertEquals(runningMatches, message.getRunningMatches());
    }

    @Test
    @DisplayName("Saved matches are correctly retrieved")
    void savedMatchesAreCorrectlyRetrieved() {
        Assertions.assertEquals(savedMatches, message.getSavedMatches());
    }
}