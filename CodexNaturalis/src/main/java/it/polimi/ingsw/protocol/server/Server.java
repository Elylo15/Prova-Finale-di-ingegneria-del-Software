package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    private HashMap<Match, ClientManager> clientManagers;
    private int portSocket;
    private int portRMI;

    private ThreadPoolExecutor executor;


    public Server() {
        portSocket = 30000;
        portRMI = 40000;
        clientManagers = new HashMap<>();

        int corePoolSize = 2;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    public Server(int portSocket, int portRMI, int maximumPoolSize) {
        this.portSocket = portSocket;
        this.portRMI = portRMI;
        this.clientManagers = new HashMap<>();

        if(maximumPoolSize < 2)
            maximumPoolSize = 2;

        int corePoolSize = 2;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    public void createMatch() {
        Match match = new Match();
        // TODO properly set up the correct path
        ClientManager clientManager = new ClientManager(match, 4, "./savedGames/");
        clientManager.setPorts(Integer.toString(portSocket), Integer.toString(portRMI));
        this.portSocket += 1;
        this.portRMI += 1;
        clientManagers.put(match, clientManager);
        executor.execute(clientManager);
    }

    public void closeMatch() {
        //TODO find a way to close a match at any momenta, possibly involve executors
    }

    public void loadMatch(String fileName) {

    }
}
