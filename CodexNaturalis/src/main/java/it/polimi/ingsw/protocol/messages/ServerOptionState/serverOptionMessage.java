package it.polimi.ingsw.protocol.messages.ServerOptionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class serverOptionMessage implements Message, Serializable {
    private final boolean newMatch;         // true if the client wants to join a new game
    private final Integer matchID;                // ID of the match (not yet started) the client wants to join. null if the client wants to join a new game
    private final Integer  startedMatchID;  // ID of the started match the client wants to join
    private final boolean loadMatch;        // true if the client wants to load a custom match
    private final Integer savedMatchID;        // name of the saved game that the client wants to load

    private ArrayList<Integer> WaitingMatches;
    private ArrayList<Integer> runningMatches;
    private ArrayList<Integer> savedMatches;

    /**
     * Constructor for the serverOptionMessage class
     * @param newMatch true if the client wants to join a new game
     * @param matchID ID of the match (not yet started) the client wants to join. null if the client wants to join a new game
     * @param startedMatchID ID of the started match the client wants to join
     * @param loadMatch true if the client wants to load a custom match
     * @param savedMatchID name of the saved game that the client wants to load
     */
    public serverOptionMessage(boolean newMatch, Integer matchID, Integer startedMatchID, boolean loadMatch, Integer savedMatchID) {
       this.newMatch = newMatch;
       this.matchID = matchID;
       this.startedMatchID = startedMatchID;
       this.loadMatch = loadMatch;
       this.savedMatchID = savedMatchID;
       this.WaitingMatches = null;
       this.runningMatches = null;
       this.savedMatches = null;
   }

    /**
     * Constructor for the serverOptionMessage class
     * @param newMatch true if the client wants to join a new game
     * @param matchID ID of the match (not yet started) the client wants to join. null if the client wants to join a new game
     * @param startedMatchID ID of the started match the client wants to join
     * @param loadMatch true if the client wants to load a custom match
     * @param savedMatchID name of the saved game that the client wants to load
     * @param waitingMatches list of waiting matches
     * @param runningMatches list of running matches
     * @param savedMatches list of saved matches
     */
    public serverOptionMessage(boolean newMatch, Integer matchID , Integer startedMatchID, boolean loadMatch, Integer savedMatchID, ArrayList<Integer> waitingMatches, ArrayList<Integer> runningMatches, ArrayList<Integer> savedMatches) {
        this.newMatch = newMatch;
        this.matchID = matchID;
        this.startedMatchID = startedMatchID;
        this.loadMatch = loadMatch;
        this.savedMatchID = savedMatchID;
        this.WaitingMatches = waitingMatches;
        this.runningMatches = runningMatches;
        this.savedMatches = savedMatches;
    }

    /**
     * @return true if the client wants to join a new game
     */
    public boolean isNewMatch() {
       return newMatch;
    }

    /**
     * @return ID of the match (not yet started) the client wants to join. null if the client wants to join a new game
     */
    public Integer getMatchID() {
        return matchID;
    }

    /**
     * @return ID of the started match the client wants to join
     */
    public Integer getStartedMatchID() {
        return startedMatchID;
    }


    /**
     * @return true if the client wants to load a custom match
     */
    public boolean isLoadMatch() {
       return loadMatch;
   }

   /**
    * @return name of the saved game that the client wants to load
    */
    public Integer getSavedMatchID() {
        return savedMatchID;
    }

    /**
     * @return list of waiting matches
     */
    public ArrayList<Integer> getWaitingMatches() {return WaitingMatches;}

    /**
     * @return list of running matches
     */
    public ArrayList<Integer> getRunningMatches() {return runningMatches;}

    /**
     * @return list of saved matches
     */
    public ArrayList<Integer> getSavedMatches() {return savedMatches;}


    @Override
    public void setLock() {}

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {}

    /**
     * @return a string representation of the serverOptionMessage
     */
    @Override
    public String toString() {
        return "serverOptionMessage{" +
                "newMatch=" + newMatch +
                ", matchID=" + matchID +
                ", startedMatchID=" + startedMatchID +
                ", loadMatch=" + loadMatch +
                ", filename='" + savedMatchID + '\'' +
                ", WaitingMatches=" + WaitingMatches +
                ", runningMatches=" + runningMatches +
                ", savedMatches=" + savedMatches +
                '}';
    }
}

