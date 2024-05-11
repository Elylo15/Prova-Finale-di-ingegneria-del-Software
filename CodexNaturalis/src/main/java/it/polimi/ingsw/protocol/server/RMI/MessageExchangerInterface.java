package it.polimi.ingsw.protocol.server.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageExchangerInterface extends Remote {
    /**
     * Sends a message to the other side.
     *
     * @param message Message to be sent.
     * @throws RemoteException
     */
    void write(Object message) throws RemoteException;

    /**
     * Returns the last message received.
     *
     * @return Message received.
     * @throws RemoteException
     */
    Object read() throws RemoteException;
}
