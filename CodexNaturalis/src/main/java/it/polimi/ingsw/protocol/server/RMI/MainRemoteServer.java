package it.polimi.ingsw.protocol.server.RMI;

import it.polimi.ingsw.protocol.server.ClientConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MainRemoteServer extends UnicastRemoteObject implements MainRemoteServerInterface, Runnable {
    private ArrayList<RemoteServerInterface> servers = new ArrayList<RemoteServerInterface>();
    private boolean newClient;

    public MainRemoteServer() throws RemoteException {
        this.servers = new ArrayList<>();
        this.newClient = false;
    }

    /**
     * It is invoked by a new client that wants to connect to the server.
     *
     * @return Name (String) of the new object that the client has to lookup for the new connection.
     * @throws RemoteException
     */
    @Override
    public synchronized String helloFromClient() throws RemoteException {
        return "";
    }

//    public ClientConnection newClientConnection(){
//
//    }

    /**
     *
     */
    @Override
    public void run() {

    }
}
