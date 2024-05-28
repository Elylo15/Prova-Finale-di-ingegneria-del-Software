package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.server.RMI.MainRemoteServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientRMITest {

    private MainRemoteServer mainRemoteServer;
    private Registry registry;
    private ThreadPoolExecutor executor;
    ClientConnection connection;

    @BeforeEach
    void setup() throws RemoteException, AlreadyBoundException {
        registry = LocateRegistry.createRegistry(1099);
        mainRemoteServer = new MainRemoteServer();
        registry.bind("MainServer", mainRemoteServer);
        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
        executor.submit(() -> {
            Registry registry = null;
            try {

                // Listens for new clients and returns a ClientConnection to them
                connection = mainRemoteServer.clientConnected(registry);
                connection.sendAnswerToConnection(new connectionResponseMessage(true));
            } catch (RemoteException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    @DisplayName("Successful sendAnswerToConnection")
    void sendAnswerToConnectionSuccessfulTest() {
        executor.submit(() -> {
            ControllerRMI controllerRMI = new ControllerRMI("localhost", "1099");
            controllerRMI.connectToServer("localhost", "1099");
            boolean answer = controllerRMI.answerConnection().getCorrect();
            Assertions.assertTrue(answer);
        });
    }

    @Test
    @DisplayName("Failed sendAnswerToConnection")
    void sendAnswerToConnectionFailedTest() {
        executor.submit(() -> {
            ControllerRMI controllerRMI = new ControllerRMI("localhost", "1099");
            controllerRMI.connectToServer("localhost", "1099");
            connection.closeConnection();
            Future<connectionResponseMessage> actualMessage = executor.submit(controllerRMI::answerConnection);
            assertThrows(ExecutionException.class, () -> {
                actualMessage.get(1, TimeUnit.SECONDS);
            });
        });
    }
}