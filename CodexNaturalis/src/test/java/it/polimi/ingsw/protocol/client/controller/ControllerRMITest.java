package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.server.ClientConnection;
import it.polimi.ingsw.protocol.server.RMI.MainRemoteServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

class ControllerRMITest {


    private ThreadPoolExecutor executor;
    private ControllerRMI controller;
    private ClientConnection connection;

    private ClientConnection mockServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            MainRemoteServer server = new MainRemoteServer();
            registry.rebind("MainServer", server);
            return server.clientConnected(registry);
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        controller = new ControllerRMI("localhost", "1099");

        int corePoolSize = 8;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());


        Future<ClientConnection> connectionFuture = executor.submit(this::mockServer);
        controller.connectToServer("localhost", "1099");
        ClientConnection connection = connectionFuture.get();
    }

    @AfterEach
    void tearDown() {
        executor.shutdown();
    }

    @Test
    @DisplayName("Connection to server and receiving an answer")
    void connectToServer() throws ExecutionException, InterruptedException {
        connection.sendAnswerToConnection(new connectionResponseMessage(true));
        assertTrue(controller.answerConnection().getCorrect());
    }


    @Test
    void getCurrent() {
    }

    @Test
    void serverOptions() {
    }

    @Test
    void correctAnswer() {
    }

    @Test
    void sendOptions() {
    }

    @Test
    void getUnavailableName() {
    }

    @Test
    void chooseName() {
    }

    @Test
    void getAvailableColor() {
    }

    @Test
    void chooseColor() {
    }

    @Test
    void newHost() {
    }

    @Test
    void expectedPlayers() {
    }

    @Test
    void placeStarter() {
    }

    @Test
    void getObjectiveCards() {
    }

    @Test
    void chooseObjective() {
    }

    @Test
    void placeCard() {
    }

    @Test
    void pickCard() {
    }

    @Test
    void updatePlayer() {
    }

    @Test
    void endGame() {
    }

    @Test
    void sendAnswerToPing() {
    }
}