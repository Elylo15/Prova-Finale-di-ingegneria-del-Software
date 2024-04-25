package it.polimi.ingsw.protocol.server;

import java.util.Timer;
import java.util.TimerTask;

public abstract class CheckConnection {
    private int timeOut;
    private boolean isAlive;

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
     * @param timeOut time after which, if not responding, the player will be disconnected.
     */
    public void startConnectionCheck(int timeOut, boolean isAlive) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isAlive) {
                    checkIsAlive();
                } else {
                    timer.cancel();
                    aliveResponse();
                }
            }
        }, 0, timeOut);
    }

    /**
     *
     * @param timeOut: new timeOut.
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * @return timeOut.
     */
    public int getTimeOut() {
        return timeOut;
    }

    /**
     * Sends a response to let the client know if he has been disconnected.
     */
    public void aliveResponse(){}

    /**
     * method {@code checkIsAlive}: pings the client to check if alive.Changes the state
     * of isAlive to true if the client responds, false elsewhere.
     */
    public void checkIsAlive() {}

}
