package it.polimi.ingsw;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.ViewCLI;

public class AppCLI {
    public static void main(String[] args) {
        ViewCLI view = new ViewCLI();
        Client client = new Client(view);
        client.run();
    }
}
