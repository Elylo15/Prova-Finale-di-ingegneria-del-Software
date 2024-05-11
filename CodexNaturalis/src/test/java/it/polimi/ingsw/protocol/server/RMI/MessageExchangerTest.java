package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.messages.currentStateMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class MessageExchangerTest {
    private MessageExchanger exchanger;
    private currentStateMessage message1;
    private currentStateMessage message2;

    @BeforeEach
    void setUp() {
        try {
            exchanger = new MessageExchanger();
            String message1 = "message1";
            String message2 = "message2";
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeTest() {
        try {
            exchanger.write(message1);


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals( ((currentStateMessage) exchanger.getStoredObject()), message1);
    }

    @Test
    void readFromClientTest() {
        try {
            exchanger.write(message1);
            currentStateMessage message = (currentStateMessage ) exchanger.read();
            Assertions.assertEquals( message1, message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void multipleMessagesTest() {
        ThreadPoolExecutor executor;
        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
        try {
            Future<Object> future = executor.submit(exchanger::read);
            executor.submit(() -> {
                try {
                    exchanger.write(message1);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

            currentStateMessage message = (currentStateMessage ) future.get();

            Assertions.assertEquals( message1, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}