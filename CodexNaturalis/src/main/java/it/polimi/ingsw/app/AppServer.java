package it.polimi.ingsw.app;

import it.polimi.ingsw.server.Server;

/**
 * Main class for the server
 */
public class AppServer {
    /**
     * Main method for the server
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
