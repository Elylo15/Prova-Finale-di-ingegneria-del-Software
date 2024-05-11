package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MessageExchanger extends UnicastRemoteObject implements MessageExchangerInterface {
    private Object storedObject;

    private boolean receiverCanRead;
    private boolean senderCanWrite;


    /**
     * Constructor for MessageExchanger
      * @throws RemoteException
     */
    public MessageExchanger() throws RemoteException {
        storedObject = null;
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
    public synchronized void write(Object message) throws RemoteException {

        // Wait until the receiver has read
        while(!senderCanWrite) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }

        // Blocks the receiver from reading
        this.receiverCanRead = false;

        // Overwrites the stored message
        this.storedObject = message;

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
    public synchronized Object read() throws RemoteException {

        // Wait until the sender has written
        while (!receiverCanRead) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }

        // Block the sender from writing
        this.senderCanWrite = false;

        // Obtains the last message
        Object message = this.storedObject;

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
    protected Object getStoredObject() {return storedObject;}
}
