package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.client.view.ViewCLI;
import it.polimi.ingsw.protocol.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientCLITest {

    private Client client;
    private InputStream in;
    private ViewCLI viewCLI;
    private ThreadPoolExecutor executor;


    private void setInput(String input) {
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @BeforeEach
    public void setUp() {
        viewCLI = new ViewCLI();
        client = new ClientCLI(viewCLI);
        int corePoolSize = 15;
        int maximumPoolSize = 100;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

    }

    @Test
    void getServerIP() {
        String serverIp = "test";
        client.setIP(serverIp);
        assertEquals(serverIp, client.getServerIP());
    }

    @Test
    void getView() {
        assertEquals(viewCLI, client.getView());
    }

    @Test
    void connection() {
        Server server = new Server();
        executor.submit(server);

        client.setController("localhost", true);
        client.getController().connectToServer("localhost", "1024");
        assertTrue(client.getController().answerConnection().getCorrect());
        client.setController("localhost", false);
        client.getController().connectToServer("localhost", "1099");
        assertTrue(client.getController().answerConnection().getCorrect());
        executor.shutdown();
    }



    @Test
    void serverOptions() {

    }

    @Test
    void name() {
    }

    @Test
    void color() {
    }

    @Test
    void waitingPlayer() {
    }

    @Test
    void starter() {
    }

    @Test
    void pickObjective() {
    }

    @Test
    void pickCard() {
    }

    @Test
    void placeCard() {
    }

    @Test
    void setIP() {
    }

    @Test
    void setController() {
    }

    @Test
    void pickNameFA() {
    }
}