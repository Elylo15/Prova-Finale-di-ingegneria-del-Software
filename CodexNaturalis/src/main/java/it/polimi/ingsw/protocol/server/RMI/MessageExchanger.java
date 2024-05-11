package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MessageExchanger extends UnicastRemoteObject implements MessageExchangerInterface {
    private Message lastMessage;

    private boolean receiverCanRead;
    private boolean senderCanWrite;


    /**
     * Constructor for MessageExchanger
      * @throws RemoteException
     */
    public MessageExchanger() throws RemoteException {
        lastMessage = null;
        this.receiverCanRead = false;
        this.senderCanWrite = true;
    }


    /**
     * Sends a message to the other side.
     *
     * @param message Message to be sent.
     * @throws RemoteException
     */
    @Override
    public synchronized void sendMessage(Message message) throws RemoteException {

        // Wait until the receiver has read
        while(!senderCanWrite) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }

        // Blocks the receiver from reading
        this.receiverCanRead = false;

        // Overwrites the stored message
        this.lastMessage = message;

        // Allows the receiver to read
        this.senderCanWrite = false;
        this.receiverCanRead = true;

        // Notify the receiver
        this.notifyAll();

    }

    /**
     * Returns the last message received.
     *
     * @return Message received.
     * @throws RemoteException
     */
    @Override
    public synchronized Message receiveMessage() throws RemoteException {

        // Wait until the sender has written
        while (!receiverCanRead) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }

        // Block the sender from writing
        this.senderCanWrite = false;

        // Obtains the last message
        Message message = this.lastMessage;

        // Allows the sender to write
        this.receiverCanRead = false;
        this.senderCanWrite = true;

        // Notify the sender
        this.notifyAll();

        return message;
    }

    /**
     * Retrieves last message. Created only for the tests.
     * @return last stored message.
     */
    protected Message getLastMessage() {return lastMessage;}
}
