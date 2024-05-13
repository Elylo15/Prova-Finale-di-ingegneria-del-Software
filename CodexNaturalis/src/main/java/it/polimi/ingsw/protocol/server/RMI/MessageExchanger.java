package it.polimi.ingsw.protocol.server.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

public class MessageExchanger extends UnicastRemoteObject implements MessageExchangerInterface {
    private ArrayList<Object> storedObjects;


    /**
     * Constructor for MessageExchanger
      * @throws RemoteException
     */
    public MessageExchanger() throws RemoteException {
        storedObjects =  new ArrayList<>();
    }


    /**
     * Sends a message to the other side.
     *
     * @param message Message to be sent.
     * @throws RemoteException
     */
    @Override
    public synchronized void write(Object message) throws RemoteException {

        // Adds a new message to the list
        this.storedObjects.add(message);

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
        while (storedObjects.isEmpty())  {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }

        // Obtains the last message
        return this.storedObjects.removeFirst();
    }

    /**
     * Retrieves last message. Created only for the tests.
     * @return last stored message.
     */
    protected ArrayList<Object> getStoredObject() {return this.storedObjects;}
}
