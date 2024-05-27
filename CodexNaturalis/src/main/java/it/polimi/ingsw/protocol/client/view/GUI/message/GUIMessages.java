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
        // the controllers of the fxml files can call this method to serialize the user input
        try {
            // Serialize the message
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(message);
            //serialize the Object message transforming it in a stream of bytes
            byte[] serializedMessage = byteArrayOutputStream.toByteArray();
            // Send the message
            storedObjectsToClient.put(serializedMessage); //insert the serialized object in the queue storedObjectsToClient
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
        //viewGUI can call this method to serialize the objects the graphic interface need
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
        //viewGUI can call this method to deserialize the input from the user interface sent by the methods in fxml files controller
        // so that the client is able to have the object needed
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
        //the controllers of the fxml files can call this method to deserialize the object sent by the methods of viewGUI
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

    public static void clearQueue(){
        storedObjectsToClient.clear();
        storedObjectsToGUI.clear();
    }


}
