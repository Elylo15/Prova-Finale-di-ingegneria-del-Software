package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.currentStateMessage;

import java.util.ArrayList;
import java.util.concurrent.*;

public class ClientManager implements Runnable{
    private MatchInfo matchInfo;
    private ArrayList<PlayerInfo> playersInfo; // Only online players

    private ThreadPoolExecutor executor;

    /**
     * Standard constructor for ClientManager
     * @param match object representing the model and data related to the server
     */
    public ClientManager(MatchInfo match) {
        this.matchInfo = match;
        this.matchInfo.setLastTurn(false);

        playersInfo = new ArrayList<>();


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
        boolean gameOver = false;
        while(!gameOver) {
            switch (this.matchInfo.getStatus()) {
                case Waiting -> {
                    this.waiting();
                    this.saveMatch();
                }
                case Player1 -> {
                    this.player(matchInfo.getMatch().getPlayers().get(0));
                    this.saveMatch();
                }
                case Player2 -> {
                    this.player(matchInfo.getMatch().getPlayers().get(1));
                    this.saveMatch();
                }
                case Player3 -> {
                    this.player(matchInfo.getMatch().getPlayers().get(2));
                    this.saveMatch();
                }
                case Player4 -> {
                    this.player(matchInfo.getMatch().getPlayers().get(3));
                    this.saveMatch();
                }
                case Endgame -> {
                    this.endgame();
                    this.saveMatch();
                }
                case KickingPlayers -> {
                    this.kickPlayer();
                    gameOver = true;
                }
            }
        }

    }


    private void waiting() {
        int previousConnectedPlayers = this.playersInfo.size();
        currentStateMessage currState = new currentStateMessage(null, null,"WaitingForPlayersState",false);

        // Obtains the number of expected players for this match
        while(this.matchInfo.getExpectedPlayers() == null ) {
            /*
            1. Mandare messaggi stato ad ogni player che si connette
            2. Mandare messaggi host ad ogni player
            3. Rispondere

            4. Kickare player che non rispondono
            5. Kickare player che si disconnettono
             */



        }

        // Wait for the specified number of expected players
        while(this.playersInfo.size() < this.matchInfo.getExpectedPlayers()) {

        }



        // Adds all players to the match (match of the model)
    }

    private void player(Player player) {

    }

    private void endgame() {

    }

    private void kickPlayer() {

    }



}
