package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.client.view.ViewCLI;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.server.ClientConnection;
import it.polimi.ingsw.protocol.server.RMI.MainRemoteServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;
    private ViewCLI view;
    private Registry registry;
    private MainRemoteServer server;
    private ClientConnection connection;
    ThreadPoolExecutor executor;

    @BeforeEach
    void setUp() throws RemoteException {
        view = new ViewCLI();
        client = new Client(view);

        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

        server = new MainRemoteServer();
        Future<ClientConnection> connectionFuture = executor.submit(() -> {
            try {
                registry = LocateRegistry.createRegistry(1099);
                registry.rebind("MainServer", server);
                System.out.println("Server ready");
                connection = server.clientConnected(registry);
                connection.sendAnswerToConnection(new connectionResponseMessage(true));
                return connection;
            } catch (RemoteException | AlreadyBoundException e) {
                e.printStackTrace();
                return null;
            }
        });

        //client.setController("localhost", false);
        //client.connection(false);


        System.setIn(new ByteArrayInputStream("localhost\nrmi\n".getBytes()));

        System.out.println(view.askIP());
        System.out.println(view.askSocket());

        System.setIn(System.in);
        try {
            connection = connectionFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() throws NotBoundException, RemoteException {
        registry.unbind("MainServer");
        UnicastRemoteObject.unexportObject(server, true);
        UnicastRemoteObject.unexportObject(registry, true);
        executor.shutdown();

    }

    private void setInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @Test
    public void runTest() {




    }

}