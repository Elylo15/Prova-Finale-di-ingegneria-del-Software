package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.server.ClientRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

/**
 * The MainRemoteServer class represents the RMI server that manages client connections
 * and provides methods for clients to interact with the server.
 */
public class MainRemoteServer extends UnicastRemoteObject implements MainRemoteServerInterface, Runnable {
    private ArrayList<String> connectionNames;
    private boolean setUpClient;
    private int clientCounter;

    /**
     * The MainRemoteServer class represents the RMI server that manages client connections
     * and provides methods for clients to interact with the server.
     */
    public MainRemoteServer() throws RemoteException {
        this.connectionNames = new ArrayList<>();
        this.setUpClient = false;
        this.clientCounter = 0;
    }

    /**
     * Invoked by a new client that wants to connect to the server.
     * Registers the new client and generates a unique name for the client connection.
     *
     * @return Name of the new object that the client has to lookup for the new connection
     * @throws RemoteException if a remote exception occurs
     */
    @Override
    public synchronized String helloFromClient() throws RemoteException {
        String newClientName = "MessageExchanger_" + LocalDateTime.now() + "_" + (new Random()).nextInt(100000);
        this.connectionNames.add(newClientName);
        this.clientCounter += 1;
        this.setUpClient = true;

        // Notifies the waiting server that a new client connected
        this.notifyAll();

        // Waits for the setup of the new client connection to finish
        while (this.setUpClient) {
            try {
                this.wait();
            } catch (InterruptedException ignore) {}
        }

        return newClientName;
    }

    /**
     * Waits for a new client to connect and sets up the new ClientRMI connection.
     *
     * @return the ClientRMI object
     */
    public synchronized ClientRMI clientConnected() {

        // Waits a new client ro connect
        while (!this.setUpClient) {
            try {
                this.wait();
            } catch (InterruptedException ignore) {}
        }

        // Sets up the new ClientConnection
        ClientRMI connection = new ClientRMI(this.clientCounter, connectionNames.getLast());
        this.setUpClient = false;

        // Wakes up helloFromClient
        this.notifyAll();

        return connection;
    }

    /**
     * Empty run method, needed for executors to work.
     */
    @Override
    public void run() {

    }
}
