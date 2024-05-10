package it.polimi.ingsw.protocol.server.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainRemoteServerInterface extends Remote {
    /**
     * It is invoked by a new client that wants to connect to the server.
     * @return Name (String) of the new object that the client has to lookup for the new connection.
     * @throws RemoteException
     */
    String helloFromClient() throws RemoteException;
}
