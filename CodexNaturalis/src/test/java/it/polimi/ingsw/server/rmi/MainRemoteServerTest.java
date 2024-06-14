package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.controller.ControllerRMI;
import it.polimi.ingsw.messages.connectionState.connectionResponseMessage;
import it.polimi.ingsw.server.ClientConnection;
import org.junit.jupiter.api.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class MainRemoteServerTest {

    private MainRemoteServer mainRemoteServer;
    private Registry registry;
    private ThreadPoolExecutor executor;

    @BeforeEach
    void setup() throws RemoteException, AlreadyBoundException {
        registry = LocateRegistry.createRegistry(1099);
        mainRemoteServer = new MainRemoteServer();
        registry.bind("MainServer", mainRemoteServer);
        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());
        executor.submit(() -> {
            Registry registry = null;
            try {

                // Listens for new clients and returns a ClientConnection to them
                ClientConnection connection = mainRemoteServer.clientConnected(registry);
                connection.sendAnswerToConnection(new connectionResponseMessage(true));
            } catch (RemoteException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @AfterEach
    void tearDown() throws RemoteException, NotBoundException {
        registry.unbind("MainServer");
        UnicastRemoteObject.unexportObject(mainRemoteServer, true);
        UnicastRemoteObject.unexportObject(registry, true);
    }

    @Test
    @DisplayName("New client is correctly registered")
    void newClientConnection() {
        executor.submit(() -> {
            ControllerRMI controllerRMI = new ControllerRMI("localhost", "1099");
            controllerRMI.connectToServer("localhost", "1099");
            boolean answer = controllerRMI.answerConnection().getCorrect();
            Assertions.assertTrue(answer);
        });

    }


}