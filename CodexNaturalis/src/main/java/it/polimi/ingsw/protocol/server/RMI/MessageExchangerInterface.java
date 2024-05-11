package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageExchangerInterface extends Remote {
    /**
     * Sends a message to the other side.
     *
     * @param message Message to be sent.
     * @throws RemoteException
     */
    void sendMessage(Message message) throws RemoteException;

    /**
     * Returns the last message received.
     *
     * @return Message received.
     * @throws RemoteException
     */
    Message receiveMessage() throws RemoteException;
}
