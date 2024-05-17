package it.polimi.ingsw.protocol.server.RMI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageExchangerTest {
    private MessageExchanger messageExchanger;

    @BeforeEach
    public void setup() throws RemoteException {
        messageExchanger = new MessageExchanger();
    }

    @Test
    public void testWriteAndRead() throws RemoteException {
        String message = "Test message";
        messageExchanger.write(message);
        Object retrievedMessage = messageExchanger.read();
        assertEquals(message, retrievedMessage);
    }

    @Test
    public void testMultipleWritesAndReads() throws RemoteException {
        String message1 = "Test message 1";
        String message2 = "Test message 2";
        messageExchanger.write(message1);
        messageExchanger.write(message2);
        Object retrievedMessage1 = messageExchanger.read();
        Object retrievedMessage2 = messageExchanger.read();
        assertEquals(message1, retrievedMessage1);
        assertEquals(message2, retrievedMessage2);
    }

    @Test
    public void testInterruptedWrite() {
        Thread.currentThread().interrupt();
        assertThrows(RemoteException.class, () -> messageExchanger.write("Test message"));
    }

    @Test
    public void testInterruptedRead() {
        Thread.currentThread().interrupt();
        assertThrows(RemoteException.class, () -> messageExchanger.read());
    }
}