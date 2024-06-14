package it.polimi.ingsw.protocol.server.rmi;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageExchanger extends UnicastRemoteObject implements MessageExchangerInterface {
    private final BlockingQueue<byte[]> storedObjects;


    /**
     * Constructor for MessageExchanger
     *
     * @throws RemoteException if a remote exception occurs
     */
    public MessageExchanger() throws RemoteException {
        storedObjects = new LinkedBlockingQueue<>();
    }


    /**
     * Sends a message to the other side.
     *
     * @param message Message to be sent.
     * @throws RemoteException if a remote exception occurs
     */
    @Override
    public void write(Object message) throws RemoteException {
        try {
            // Serialize the message
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(message);
            byte[] serializedMessage = byteArrayOutputStream.toByteArray();
            // Send the message
            storedObjects.put(serializedMessage);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Error writing object.", e);
        }
    }

    /**
     * Returns the last message received.
     *
     * @return Message received.
     * @throws RemoteException if a remote exception occurs
     */
    @Override
    public Object read() throws RemoteException {
        try {
            // Deserialize the message
            byte[] serializedMessage = storedObjects.take();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedMessage);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            // Return the message
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Error reading object.", e);
        }
    }
}
