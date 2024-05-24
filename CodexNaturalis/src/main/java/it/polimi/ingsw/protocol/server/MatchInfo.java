package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.protocol.server.FSM.MatchState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class MatchInfo implements Serializable {
    private Match match;
    private Integer ID;
    private String path;
    private Integer expectedPlayers;
    private MatchState status;
    private boolean lastTurn;
    private CopyOnWriteArrayList<PlayerInfo> allPlayersInfo;

    public MatchInfo(Match match, int ID, String path, Integer expectedPlayers, MatchState status) {
        this.match = match;
        this.ID = ID;
        this.path = path;
        this.expectedPlayers = expectedPlayers;
        this.status = status;
        this.lastTurn = false;
        this.allPlayersInfo = new CopyOnWriteArrayList<>();
    }

    public void addPlayer(PlayerInfo player) {
        this.allPlayersInfo.add(player);
    }

    public void setOffline(String Nickname) {
        for (PlayerInfo player : allPlayersInfo) {
            if (player.getPlayer().getNickname().equalsIgnoreCase(Nickname)) {
                player.getConnection().closeConnection();
                player.setConnection(null);
                return;
            }
        }
    }

    public void bringOnline(String Nickname, ClientConnection connection) {
        for (PlayerInfo player : allPlayersInfo) {
            if (player.getPlayer().getNickname().equalsIgnoreCase(Nickname)) {
                player.setConnection(connection);
                return;
            }
        }
    }

    public void setExpectedPlayers(Integer expectedPlayers) {this.expectedPlayers = expectedPlayers;}
    public void setID(Integer ID) {this.ID = ID;}
    public void setPath(String path) {this.path = path;}
    public void setStatus(MatchState status) {this.status = status;}
    public void setLastTurn(boolean lastTurn) {this.lastTurn = lastTurn;}

    public Match getMatch() {return match;}
    public Integer getID() {return ID;}
    public String getPath() {return path;}
    public Integer getExpectedPlayers() {return expectedPlayers;}
    public MatchState getStatus() {return status;}
    public boolean isLastTurn() {return lastTurn;}
    public ArrayList<PlayerInfo> getAllPlayersInfo() {return new ArrayList<>(this.allPlayersInfo);}

}
