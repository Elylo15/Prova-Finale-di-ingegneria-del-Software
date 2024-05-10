package it.polimi.ingsw.protocol.messages.ServerOptionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class serverOptionMessage implements Message, Serializable {
    private final boolean newMatch;         // true if the client wants to join a new game
    private final Integer  startedMatchID;  // ID of the started match the client wants to join
    private final String Nickname;          // Nickname of the player that the client wants to play as
    private final boolean loadMatch;        // true if the client wants to load a custom match
    private final String pathToLoad;        // path of the saved game that the client wants to load

   public serverOptionMessage(boolean newMatch, Integer startedMatchID, String Nickname, boolean loadMatch, String pathToLoad) {
       this.newMatch = newMatch;
       this.startedMatchID = startedMatchID;
       this.Nickname = Nickname;
       this.loadMatch = loadMatch;
       this.pathToLoad = pathToLoad;
   }

   public boolean isNewMatch() {
       return newMatch;
   }

   public Integer getStartedMatchID() {
       return startedMatchID;
   }

   public String getNickname() {
       return Nickname;
   }

   public boolean isLoadMatch() {
       return loadMatch;
   }

   public String getPathToLoad() {
       return pathToLoad;
   }

    @Override
    public void setLock() {}

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {}

    @Override
    public String toString() {
        return "serverOptionMessage{" +
                "newMatch=" + newMatch +
                ", startedMatchID=" + startedMatchID +
                ", Nickname='" + Nickname + '\'' +
                ", loadMatch=" + loadMatch +
                ", pathToLoad='" + pathToLoad + '\'' +
                '}';
    }
}

