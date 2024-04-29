package it.polimi.ingsw.protocol.messages.ServerOptionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class serverOptionMessage implements Message, Serializable {
    private final boolean newMatch;
    private final String startedMatchID;
    private final String Nickname;
    private final boolean loadMatch;
    private final String pathToLoad;

   public serverOptionMessage(boolean newMatch, String startedMatchID, String Nickname, boolean loadMatch, String pathToLoad) {
       this.newMatch = newMatch;
       this.startedMatchID = startedMatchID;
       this.Nickname = Nickname;
       this.loadMatch = loadMatch;
       this.pathToLoad = pathToLoad;
   }

   public boolean isNewMatch() {
       return newMatch;
   }

   public String getStartedMatchID() {
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
}

