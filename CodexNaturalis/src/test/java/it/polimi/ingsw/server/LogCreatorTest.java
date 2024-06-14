package it.polimi.ingsw.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogCreatorTest {
    private LogCreator logCreator;

    @BeforeEach
    public void setUp() {
        logCreator = new LogCreator();
    }

    @AfterEach
    public void tearDown() {
        logCreator.close();
    }

    @Test
    public void testLogCreationWithoutMatchID() throws IOException {
        // Log a message without match ID
        logCreator.log("Test message without match ID");

        // Read the last line of the log file and check if it contains the logged message
        String lastLine = getLastLineFromLogFile();
        assertTrue(lastLine.contains("Test message without match ID"));
    }

    @Test
    public void testLogCreationWithMatchID() throws IOException {
        // Set match ID
        String matchID = "12345";
        logCreator.setMatchID(matchID);

        // Log a message with match ID
        logCreator.log("Test message with match ID");

        // Read the last line of the log file and check if it contains the logged message with match ID
        String lastLine = getLastLineFromLogFile();
        assertTrue(lastLine.contains("Test message with match ID"));
        assertTrue(lastLine.contains("IdMatch=" + matchID));
    }

    private String getLastLineFromLogFile() throws IOException {
        // Read the last line of the log file
        try (BufferedReader reader = new BufferedReader(new FileReader(logCreator.getFileName()))) {
            String line;
            String lastLine = null;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            return lastLine;
        }
    }
}
