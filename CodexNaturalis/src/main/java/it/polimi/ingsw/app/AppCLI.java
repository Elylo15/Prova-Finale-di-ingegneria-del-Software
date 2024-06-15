package it.polimi.ingsw.app;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.ViewCLI;

/**
 * Main class for the CLI version of the application
 */
public class AppCLI {

    /**
     * Main method for the CLI version of the application
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        ViewCLI view = new ViewCLI();
        Client client = new Client(view);
        client.run();
    }
}
