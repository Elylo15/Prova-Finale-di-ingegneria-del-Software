package it.polimi.ingsw.app;

import it.polimi.ingsw.protocol.server.Server;

/**
 * Main class for the server
 */
public class AppServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
