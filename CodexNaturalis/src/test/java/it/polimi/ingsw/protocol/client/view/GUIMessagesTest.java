package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.protocol.client.view.gui.message.GUIMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class GUIMessagesTest {

    @BeforeEach
    public void setUp() {
        // Create a new instance of GUIMessages to initialize the queues
        new GUIMessages();
    }

    @Test
    @DisplayName("Test writeToClient and readToClient")
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
    @DisplayName("Test writeToGUI and readToGUI")
    public void testWriteToGUIAndReadToGUI() {
        String message = "Test message to gui";

        // Write the message to the gui
        GUIMessages.writeToGUI(message);

        // Read the message from the gui
        Object receivedMessage = GUIMessages.readToGUI();

        // Assert that the received message is equal to the sent message
        assertEquals(message, receivedMessage);
    }


    @Test
    @DisplayName("Test concurrent access")
    public void testConcurrentAccess() {
        // Define two different messages for client and gui
        String clientMessage = "Client message";
        String guiMessage = "gui message";

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


    @Test
    @DisplayName("Test clearQueue")
    void shouldClearQueue() {
        String message = "Test message";
        GUIMessages.writeToClient(message);
        GUIMessages.writeToGUI(message);
        GUIMessages.clearQueue();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Object> clientMessage = executor.submit(() -> GUIMessages.readToClient());
        Future<Object> guiMessage = executor.submit(() -> GUIMessages.readToGUI());

        try {
            assertNull(clientMessage.get(1, TimeUnit.SECONDS));
            assertNull(guiMessage.get(1, TimeUnit.SECONDS));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            assertTrue(e.getClass().equals(TimeoutException.class));
        }
    }

}
