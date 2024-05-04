package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.server.FSM.PlayerFSM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class ClientManager implements Runnable{
    private MatchInfo matchInfo;
    private ArrayList<PlayerInfo> playersInfo; // Only online players

    private boolean lastTurn;

    private ThreadPoolExecutor executor;

    /**
     * Standard constructor for ClientManager
     * @param match object representing the model and data related to the server
     */
    public ClientManager(MatchInfo match) {
        this.matchInfo = match;

        playersInfo = new ArrayList<>();
        this.lastTurn = false;

        int corePoolSize = 15;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

    }



    // maybe remove this method
    public void setStatus(Player player) {

    }

    public synchronized void addPlayerInfo(PlayerInfo playerInfo) throws Exception {
        if(playerInfo != null && matchInfo.getExpectedPlayers() > this.playersInfo.size())
        {
            this.playersInfo.add(playerInfo);
        } else {
            throw new Exception();
        }
    }

    public void kickPlayer(String name) {

    }

    /**
     * Checks if new players can join. If the number of player meets with expectedPlayers, the eventually new player is kicked
     * @param connection
     */
    public synchronized void checkAvailability(ClientConnection connection) {
        if(this.matchInfo.getExpectedPlayers() <= this.playersInfo.size())
            connection.closeConnection();
    }

    public MatchInfo getMatchInfo() {return this.matchInfo;}
    public ArrayList<PlayerInfo> getPlayersInfo() {return this.playersInfo;}
    public Match getMatch() {return this.matchInfo.getMatch();}


    public void saveMatch() {

    }


    /**
     * Hosts a session for the current game
     */
    @Override
    public void run() {

    }



}
