package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GUIMessagesTest {

    @BeforeEach
    public void setUp() {
        // Create a new instance of GUIMessages to initialize the queues
        new GUIMessages();
    }

    @Test
    public void testWriteToClientAndReadToClient() {
        String message = "Test message to client";

        // Write the message to the client
        GUIMessages.writeToClient(message);

        // Read the message from the client
        Object receivedMessage = GUIMessages.readToClient();

        // Assert that the received message is equal to the sent message
        assertEquals(message, receivedMessage);
    }

    @Test
    public void testWriteToGUIAndReadToGUI() {
        String message = "Test message to GUI";

        // Write the message to the GUI
        GUIMessages.writeToGUI(message);

        // Read the message from the GUI
        Object receivedMessage = GUIMessages.readToGUI();

        // Assert that the received message is equal to the sent message
        assertEquals(message, receivedMessage);
    }


    @Test
    public void testConcurrentAccess() {
        // Define two different messages for client and GUI
        String clientMessage = "Client message";
        String guiMessage = "GUI message";

        // Write the messages to the respective queues
        GUIMessages.writeToClient(clientMessage);
        GUIMessages.writeToGUI(guiMessage);

        // Read the messages from the respective queues
        Object receivedClientMessage = GUIMessages.readToClient();
        Object receivedGuiMessage = GUIMessages.readToGUI();

        // Assert that the received messages match the sent messages
        assertEquals(clientMessage, receivedClientMessage);
        assertEquals(guiMessage, receivedGuiMessage);
    }

}
