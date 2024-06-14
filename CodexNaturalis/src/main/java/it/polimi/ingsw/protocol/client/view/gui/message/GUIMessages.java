package it.polimi.ingsw.protocol.client.view.gui.message;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class used to exchange messages between gui and Client.
 */
public class GUIMessages {
    private static BlockingQueue<byte[]> storedObjectsToClient;
    private static BlockingQueue<byte[]> storedObjectsToGUI;

    public static void initialize(){
        storedObjectsToGUI = new LinkedBlockingQueue<>();
        storedObjectsToClient = new LinkedBlockingQueue<>();
    }

    /**
     * Sends a message to Client.
     *
     * @param message Message to be sent.
     */
    public static void writeToClient(Object message) {
        // the controllers of the fxml files can call this method to serialize the user input
        write(message, storedObjectsToClient);
    }

    /**
     * Writes the message to the queue.
     *
     * @param message       Message to be sent.
     * @param storedObjects Queue in which the message is written.
     */
    private static void write(Object message, BlockingQueue<byte[]> storedObjects) {
        try {
            // Serialize the message
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(message);
            //serialize the Object message transforming it in a stream of bytes
            byte[] serializedMessage = byteArrayOutputStream.toByteArray();
            // Send the message
            storedObjects.put(serializedMessage); //insert the serialized object in the queue storedObjectsToClient
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Sends a message to gui.
     *
     * @param message Message to be sent.
     */
    public static void writeToGUI(Object message) {
        //viewGUI can call this method to serialize the objects the graphic interface need
        write(message, storedObjectsToGUI);
    }

    /**
     * Returns the last message received for Client.
     *
     * @return Message received.
     */
    public static Object readToClient() {
        //viewGUI can call this method to deserialize the input from the user interface sent by the methods in fxml files controller
        // so that the client is able to have the object needed
        return read(storedObjectsToClient);
    }

    /**
     * Returns the last message received for gui.
     *
     * @return Message received.
     */
    public static Object readToGUI() {
        //the controllers of the fxml files can call this method to deserialize the object sent by the methods of viewGUI
        return read(storedObjectsToGUI);
    }

    /**
     * Reads the message from the queue.
     *
     * @param storedObjects Queue from which the message is read.
     * @return Message read.
     */
    private static Object read(BlockingQueue<byte[]> storedObjects) {
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

    /**
     * Clears the queues.
     */
    public static void clearQueue() {
        storedObjectsToClient.clear();
        storedObjectsToGUI.clear();
    }

}
