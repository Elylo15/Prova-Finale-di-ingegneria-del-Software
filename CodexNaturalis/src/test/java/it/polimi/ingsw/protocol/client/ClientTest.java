package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.view.ViewCLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        client = new Client(viewCLI);
        int corePoolSize = 15;
        int maximumPoolSize = 100;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());

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