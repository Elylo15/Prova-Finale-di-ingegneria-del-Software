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


    private ThreadPoolExecutor executor;

    private PlayerFSM fsm;

    private boolean lastTurn;


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


    public void setStatus(Player player) {

    }

    public void addPlayerInfo(PlayerInfo playerInfo) throws Exception {
        if(playerInfo != null && matchInfo.getExpectedPlayers() > this.playersInfo.size())
        {
            this.playersInfo.add(playerInfo);
        } else {
            throw new Exception();
        }
    }

    public void kickPlayer(String name) {

    }

    public MatchInfo getMatchInfo() {return this.matchInfo;}
    public ArrayList<PlayerInfo> getPlayersInfo() {return this.playersInfo;}
    public Match getMatch() {return this.matchInfo.getMatch();}





    public void saveMatch() {

    }


    /**
     * Runs the host session for the game
     */
    @Override
    public void run() {
    }



}
