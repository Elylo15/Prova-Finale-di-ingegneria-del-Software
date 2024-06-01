package it.polimi.ingsw.app;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.ViewCLI;

/**
 * Main class for the CLI version of the application
 */
public class AppCLI {
    public static void main(String[] args) {
        ViewCLI view = new ViewCLI();
        Client client = new Client(view);
        client.run();
    }
}
