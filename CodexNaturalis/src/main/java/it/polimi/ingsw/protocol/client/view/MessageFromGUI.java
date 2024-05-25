package it.polimi.ingsw.protocol.client.view;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageFromGUI {
    private static BlockingQueue<byte[]> storedObjects;


    /**
     * Constructor for MessageExchanger
     */
    public MessageFromGUI() {
        storedObjects = new LinkedBlockingQueue<>();
    }

    /**
     * Sends a message to the other side.
     *
     * @param message Message to be sent.
     */
    public static void write(Object message) {
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
        }
    }

    /**
     * Returns the last message received.
     *
     * @return Message received.
     */
    public static Object read() {
        try {
            // Deserialize the message
            byte[] serializedMessage = storedObjects.take();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedMessage);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            // Return the message
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

}
