package it.polimi.ingsw.protocol.server;

public class CheckConnectionSocket extends CheckConnection{
    /**
     * constructs a new method {@code checkConnection}.
     *
     * @param timeOut time after which, if not responding, the client will be disconnected.
     */
    public CheckConnectionSocket(int timeOut) {
        super(timeOut);
    }

    /**
     * method {@code checkIsAlive}: pings the client to check if alive.Changes the state
     * of isAlive to true if the client responds, false elsewhere.
     */
    @Override
    public void checkIsAlive() {

    }
}
