package it.polimi.ingsw;

import it.polimi.ingsw.protocol.server.*;

public class AppServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
