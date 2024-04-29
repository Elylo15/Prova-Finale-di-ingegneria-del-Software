package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.protocol.server.FSM.MatchState;

import java.io.Serializable;

public class MatchInfo implements Serializable {
    private Match match;
    private Integer ID;
    private String path;
    private int expectedPlayers;
    private MatchState status;

    public MatchInfo(Match match, int ID, String path, int expectedPlayers, MatchState status) {
        this.match = match;
        this.ID = ID;
        this.path = path;
        this.expectedPlayers = expectedPlayers;
        this.status = status;
    }

    public void setExpectedPlayers(int expectedPlayers) {this.expectedPlayers = expectedPlayers;}
    public void setID(Integer ID) {this.ID = ID;}
    public void setPath(String path) {this.path = path;}
    public void setStatus(MatchState status) {this.status = status;}

    public Match getMatch() {return match;}
    public Integer getID() {return ID;}
    public String getPath() {return path;}
    public int getExpectedPlayers() {return expectedPlayers;}
    public MatchState getStatus() {return status;}

}
