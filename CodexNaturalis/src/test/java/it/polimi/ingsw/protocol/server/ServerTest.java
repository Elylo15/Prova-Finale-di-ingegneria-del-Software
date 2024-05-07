package it.polimi.ingsw.protocol.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server;
    ThreadPoolExecutor executor;

    @BeforeEach
    void setUp() {
        server = new Server();
        int corePoolSize = 15;
        int maximumPoolSize = 100;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }
    @Test
    void acceptConnectionSocket() {
    }

    @Test
    void acceptConnectionRMI() {
    }

    @Test
    void closeMatch() {
    }

    @Test
    void loadMatch() {
    }

    @Test
    void run() {
        executor.submit(server);

    }
}