package it.polimi.ingsw.protocol.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogCreator {
    private final String fileName;
    private String matchID;
    private BufferedWriter writer;

    /**
     * Constructs a LogCreator object with a default log file name based on the current date and time.
     * Initializes the BufferedWriter for writing to the log file.
     */
    public LogCreator() {
        this.fileName = "./log/log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".log";
        this.matchID = null;
        try {
            FileWriter fw = new FileWriter(fileName, true);
            writer = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a LogCreator object with a custom match ID appended to the log file name.
     * Initializes the BufferedWriter for writing to the log file.
     * @param matchID the match ID to include in the log file name
     */
    public LogCreator(String matchID) {
        this.matchID = matchID;
        this.fileName =  "log/log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_match"+ this.matchID +".log";
        try {
            FileWriter fw = new FileWriter(fileName, true);
            writer = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the match ID for the log file.
     * @param matchID the match ID to set
     */
    public void setMatchID(String matchID) {this.matchID = matchID;}

    /**
     * Adds a new line to the log file with the specified message.
     * The log message includes a timestamp and optionally a match ID.
     * @param message the content of the log message
     */
    public void log(String message) {
        LocalDateTime currentTime = LocalDateTime.now();
        String timestamp = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try {
            if(matchID != null)
                writer.write(timestamp + " IdMatch=" + matchID + " -> " + message);
            else
                writer.write(timestamp + " -> " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the BufferedWriter used for writing to the log file.
     */
    public void close() {
        try {
            if(writer != null)
                writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
