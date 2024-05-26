package it.polimi.ingsw.protocol.client.view.GUI.message;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUIMessages {
    private static BlockingQueue<byte[]> storedObjectsToClient;
    private static BlockingQueue<byte[]> storedObjectsToGUI;


    /**
     * Constructor for MessageExchanger
     */
    public GUIMessages() {
        storedObjectsToGUI = new LinkedBlockingQueue<>();
        storedObjectsToClient = new LinkedBlockingQueue<>();
    }

    /**
     * Sends a message to Client.
     *
     * @param message Message to be sent.
     */
    public static void writeToClient(Object message) {
        try {
            // Serialize the message
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(message);
            byte[] serializedMessage = byteArrayOutputStream.toByteArray();
            // Send the message
            storedObjectsToClient.put(serializedMessage);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Sends a message to GUI.
     *
     * @param message Message to be sent.
     */
    public static void writeToGUI(Object message) {
        try {
            // Serialize the message
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(message);
            byte[] serializedMessage = byteArrayOutputStream.toByteArray();
            // Send the message
            storedObjectsToGUI.put(serializedMessage);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



    /**
     * Returns the last message received for Client.
     *
     * @return Message received.
     */
    public static Object readToClient() {
        try {
            // Deserialize the message
            byte[] serializedMessage = storedObjectsToClient.take();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedMessage);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            // Return the message
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Returns the last message received for GUI.
     *
     * @return Message received.
     */
    public static Object readToGUI() {
        try {
            // Deserialize the message
            byte[] serializedMessage = storedObjectsToGUI.take();
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
