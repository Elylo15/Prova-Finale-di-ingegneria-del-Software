package it.polimi.ingsw.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogCreatorTest {

    //we test both constructors of LogCreatorTest and check if we can correctly add lines in the log file with the desired message

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
        logCreator.log("Test message without match ID - first line");
        logCreator.log("Test message without match ID - second line");

        // Read the last line of the log file
        String lastLine = getLastLineFromLogFile(logCreator);

        //check if the lines contain the corresponding logged message
         assertTrue(lastLine.contains("Test message without match ID - second line"));

    }

    @Test
    public void testLogCreationWithMatchID() throws IOException {
        // Set match ID
        String matchID = "12345";
        logCreator.setMatchID(matchID);

        // Log messages with match ID
        logCreator.log("Test message with match ID - first line");
        logCreator.log("Test message with match ID - second line");
        logCreator.log("Test message with match ID - third line");

        // Read the last line of the log file
        String lastLine = getLastLineFromLogFile(logCreator);

        //check if lines contain the corresponding logged message with match ID
        assertTrue(lastLine.contains("Test message with match ID - third line"));
       assertTrue(lastLine.contains("IdMatch=" + matchID));

    }
    @Test
    public void testConstructorWithMatchID() throws IOException {
        // Set match ID
        String matchID = "12345";
        LogCreator test = new LogCreator(matchID);

        // Log messages with match ID
        test.log("(Constructor with match ID) Test message with match ID - first line");
        test.log("(Constructor with match ID) Test message with match ID - second line");
        test.log("(Constructor with match ID) Test message with match ID - third line");

        //Read first line of the log file
        String firstLine = getFirstLineFromLogFile(test);

        // Read the last line of the log file
        String lastLine = getLastLineFromLogFile(test);

        //check if lines contain the corresponding logged message with match ID
        assertTrue(firstLine.contains("(Constructor with match ID) Test message with match ID - first line"));
        assertTrue(firstLine.contains("IdMatch=" + matchID));

        assertTrue(lastLine.contains("(Constructor with match ID) Test message with match ID - third line"));
        assertTrue(lastLine.contains("IdMatch=" + matchID));
        test.close();
    }

    private String getLastLineFromLogFile(LogCreator logCreator) throws IOException {
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
    private String getFirstLineFromLogFile(LogCreator logCreator) throws IOException {
        // Read the first line of the log file
        try (BufferedReader reader = new BufferedReader(new FileReader(logCreator.getFileName()))) {
            String firstLine = null;
            firstLine = reader.readLine();
            return firstLine;
        }
    }
}
