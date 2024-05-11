package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MessageExchanger extends UnicastRemoteObject implements MessageExchangerInterface {

    /**
     * Sends a message to the other side.
     *
     * @param message Message to be sent.
     * @throws RemoteException
     */
    @Override
    public void sendMessage(Message message) throws RemoteException {

    }

    /**
     * Returns the last message received.
     *
     * @return Message received.
     * @throws RemoteException
     */
    @Override
    public Message receiveMessage() throws RemoteException {
        return null;
    }
}
