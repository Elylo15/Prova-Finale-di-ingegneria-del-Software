package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.newHostMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.server.FSM.MatchState;
import it.polimi.ingsw.protocol.server.FSM.State;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class ClientManager implements Runnable{
    private MatchInfo matchInfo;
    private ArrayList<PlayerInfo> playersInfo; // Only online players
    private int timeout;
    private int turnNumber;

    private ThreadPoolExecutor executor;

    /**
     * Standard constructor for ClientManager
     * @param match object representing the model and data related to the server
     */
    public ClientManager(MatchInfo match) {
        this.matchInfo = match;
        this.matchInfo.setLastTurn(false);
        this.turnNumber = 0;

        playersInfo = new ArrayList<>();

        this.timeout = 300000;


        int corePoolSize = 15;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

    }


    public synchronized void addPlayerInfo(PlayerInfo playerInfo) throws Exception {
        if(playerInfo != null && matchInfo.getExpectedPlayers() > this.playersInfo.size())
        {
            this.playersInfo.add(playerInfo);
            this.notifyAll();
        } else {
            throw new Exception();
        }
    }


    private void kickPlayer(PlayerInfo playerInfo) {
        this.playersInfo.remove(playerInfo);
        playerInfo.getConnection().closeConnection();
    }


    private Timer startKickTimer(PlayerInfo playerInfo, Future<?> future) {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!future.isDone()) {
                    future.cancel(true);
                    kickPlayer(playerInfo);
                }
            }
        };
        timer.schedule(task, this.timeout);

        return timer;
    }


    /**
     * Checks if new players can join. If the number of player meets with expectedPlayers, the eventually new player is kicked.
     * @param connection connection manager of the new player.
     */
    public synchronized void checkAvailability(ClientConnection connection) {
        if(this.matchInfo.getExpectedPlayers() <= this.playersInfo.size())
            connection.closeConnection();
    }

    /**
     * Getter of MathInfo
     * @return MatchInfo
     */
    public MatchInfo getMatchInfo() {return this.matchInfo;}

    /**
     * Getter of PlayersInfo
     * @return list of PlayersInfo
     */
    public ArrayList<PlayerInfo> getPlayersInfo() {return this.playersInfo;}

    /**
     * Getter of Match (model)
     * @return Match
     */
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
                    this.kickPlayers();
                    gameOver = true;
                }
            }
        }

    }


    /**
     * Manages the waiting state for players to join the match.
     * It sends the current state data to all connected players, asks for the number of expected players for this match
     * and waits for the expected number of players to join.
     * When the number of expected players is met, it adds them to the match (model).
     * If a player takes too long to respond or provides an invalid response, they are kicked from the match.
     */
    private void waiting() {
        int previousConnectedPlayers = this.playersInfo.size();
        currentStateMessage currState = new currentStateMessage(null, null,"WaitingForPlayersState",false);

        // Obtains the number of expected players for this match
        while(this.matchInfo.getExpectedPlayers() == null ) {
            // preventing new player from joining at this moment and not getting all messages correctly
            synchronized(this) {
                PlayerInfo host = this.playersInfo.getFirst();
                if(host != null) {
                    // Sends current state data
                    this.playersInfo.stream()
                            .parallel()
                            .forEach(playerInfo -> {
                                currentStateMessage curr = new currentStateMessage(null, playerInfo.getPlayer(),"WaitingForPlayersState",false);
                                playerInfo.getConnection().sendCurrentState(curr);
                                newHostMessage hostMessage = new newHostMessage(host.getPlayer().getNickname(), false);
                            });

                    boolean correctAnswer = false;
                    while(!correctAnswer){
                        // If the timer ends the player is kicked
                        Future<expectedPlayersMessage> future = executor.submit(() -> host.getConnection().getExpectedPlayer());
                        Timer timer = this.startKickTimer(host,future);
                        expectedPlayersMessage expected = null;
                        try {
                            expected = future.get();
                            timer.cancel();
                        } catch (Exception e) {
                            // TODO add log file for the error recording
                        /*
                        catch (InterruptedException e) {
                            System.err.println("Task interrupted");
                        } catch (ExecutionException e) {
                            System.err.println("Task threw an exception: " + e.getCause().getMessage());
                        } catch (CancellationException e) {
                            System.err.println("Task was cancelled due to timeout");
                        }
                         */
                        }

                        if (expected != null) {
                            // Checks if the client has properly given a response
                            if(expected.isNoResponse()) {
                                correctAnswer = true;
                                this.kickPlayer(host);
                            } else {
                                // Checks if the response is valid and answer back
                                if(expected.getExpectedPlayers() >= 2 && expected.getExpectedPlayers() <= 4) {
                                    this.matchInfo.setExpectedPlayers(expected.getExpectedPlayers());
                                    correctAnswer = true;
                                    host.getConnection().sendAnswer(true);
                                } else {
                                    host.getConnection().sendAnswer(false);
                                }
                            }

                        }
                    }


                }

            }

        }

        // Wait for the specified number of expected players
        while(this.playersInfo.size() < this.matchInfo.getExpectedPlayers()) {
            /*
            1. Aspettare il numero di player corretto
            2. kickare i player che non si connettono
             */
            synchronized (this) {
                if(this.playersInfo.size() == this.matchInfo.getExpectedPlayers())
                    this.matchInfo.setStatus(MatchState.Player1);
            }

            // Gives a little of room to new players that want to join
            try {
                Thread.sleep(1000);
            } catch (Exception ignore) {}
        }

        // Adds all players to the match (match of the model)
        for(PlayerInfo playerInfo : this.playersInfo) {
            try {
                this.matchInfo.getMatch().addPlayer(playerInfo.getPlayer());
                playerInfo.setState(State.StarterCard);
            } catch (Exception e) {
                // TODO add logfile to write that a player has failed to join
            }
        }

        // Prepares the match.
        this.matchInfo.getMatch().start();
        this.turnNumber = 1;
    }

    private synchronized void player(Player player) {
        PlayerInfo playerInfo = this.findPlayer(player);
        if(playerInfo == null) {
            // TODO log that this player skipped
            return;
        }

        switch (playerInfo.getState()) {
            case WaitingForPlayers -> {
            }
            case StarterCard -> {
            }
            case Objective -> {
            }
            case PlayerTurn -> {
            }
            case NotPlayerTurn -> {
            }
            case EndGame -> {
            }
        }



    }

    private void endgame() {

    }

    private void kickPlayers() {

    }

    /**
     * Finds a player by their nickname.
     * @param player The player object to search for.
     * @return The player information if found, or {@code null} if not found.
     */
    private PlayerInfo findPlayer(Player player) {
        return this.playersInfo.stream()
                .filter(playerInfo -> Objects.equals(player.getNickname(), playerInfo.getPlayer().getNickname()))
                .findAny()
                .orElse(null);
    }




    // TODO check this method later
    private void checkOnlinePlayersNumber() {
        // This function must be used at the end of every turn
        if(this.playersInfo.size() == 1) {
            // Waits for a timeout.
            // Then if a player remains, he is declared the new winner
            Timer timer = new Timer();
            ClientManager manager = this;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (manager) {
                        if(manager.playersInfo.size() == 1) {
                            manager.matchInfo.setStatus(MatchState.Endgame);
                        }
                    }
                }
            };
            timer.schedule(task, this.timeout);
        }

        synchronized (this) {
            while (this.playersInfo.size() == 1 && (
                    this.matchInfo.getStatus() != MatchState.Endgame
                            || this.matchInfo.getStatus() != MatchState.KickingPlayers
            )) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }


            if (this.playersInfo.isEmpty()) {
                // Closes and saves the match
                this.saveMatch();
                this.matchInfo.setStatus(MatchState.KickingPlayers);
            }
        }
    }
}
