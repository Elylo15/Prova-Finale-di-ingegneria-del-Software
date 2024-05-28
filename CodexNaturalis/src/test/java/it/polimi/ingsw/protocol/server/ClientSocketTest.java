package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientSocketTest {
    private ClientSocket clientSocket;
    private ServerSocket serverSocket;
    private Socket socket;
    private ThreadPoolExecutor executor;
    private ControllerSocket controllerSocket;

    @BeforeEach
    void setUp() throws IOException {
        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        serverSocket = new ServerSocket(1024);
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

        executor.submit(() -> {
            try {
                socket = serverSocket.accept();
                clientSocket = new ClientSocket("127.0.0.1", String.format("%d",socket.getPort()), socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        controllerSocket = new ControllerSocket("localhost", "1024");
        controllerSocket.connectToServer("localhost", "1024");

    }



    @Test
    @DisplayName("Successful sendAnswerToConnection")
    void sendAnswerToConnectionSuccessfulTest() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        connectionResponseMessage expectedMessage = new connectionResponseMessage(true);
        executor.submit(() -> clientSocket.sendAnswerToConnection(expectedMessage));
        connectionResponseMessage response = controllerSocket.answerConnection();
        assertEquals(expectedMessage, response);

    }

    @Test
    @DisplayName("Failed sendAnswerToConnection")
    void sendAnswerToConnectionFailedTest() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException, TimeoutException {
        clientSocket.closeConnection();
        connectionResponseMessage expectedMessage = new connectionResponseMessage(true);
        executor.submit(() -> clientSocket.sendAnswerToConnection(expectedMessage));
        Future<connectionResponseMessage> actualMessage = executor.submit(() -> controllerSocket.answerConnection());
        assertThrows(ExecutionException.class, () -> {
            actualMessage.get(1, TimeUnit.SECONDS);
        });

    }




}