package it.polimi.ingsw.protocol.server;

import java.util.Timer;
import java.util.TimerTask;

public abstract class CheckConnection {
    private final int timeOut;
    private final boolean isAlive;

    /**
     * constructs a new method {@code checkConnection}.
     * @param timeOut time after which, if not responding, the client will be disconnected.
     */
    public CheckConnection(int timeOut){
        this.timeOut = timeOut;
        this.isAlive = true;
    }

    /**
     * method {@code startCheckConnection}: starts to ping the client.
     */
    public void startConnectionCheck() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isAlive) {
                    checkIsAlive();
                } else {
                    timer.cancel();
                }
            }
        }, 0, timeOut);
    }

    /**
     * @return boolean isAlive.
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * method {@code checkIsAlive}: pings the client to check if alive.Changes the state
     * of isAlive to true if the client responds, false elsewhere.
     */
    public abstract void checkIsAlive();

}
