package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientManager implements Runnable{
    private Match match;
    private HashMap<Player, ClientConnection> players;
    private int expectedPlayers;
    private final String matchFolderPath;
    private String portSocket;
    private String portRMI;

    private ThreadPoolExecutor executor;



    public ClientManager(Match match, int expectedPlayers, String matchFolderPath) {
        this.match = match;
        this.expectedPlayers = expectedPlayers;
        this.matchFolderPath = matchFolderPath;

        int corePoolSize = 15;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    public void setStatus(Player player) {

    }

    public void setPorts(String portSocket, String portRMI) {

    }

    public void acceptConnectionSocket() {

    }

    public void acceptConnectionRMI() {

    }

    public void kickPlayer(String name) {

    }

    public void startGame() {

    }

    public void closeGame() {

    }

    public void startLoadedGame() {

    }

    public void saveMatch() {

    }


    /**
     * Runs the host session for the game
     */
    @Override
    public void run() {

    }
}
