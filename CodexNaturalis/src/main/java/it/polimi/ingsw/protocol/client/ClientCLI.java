package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.view.ViewCLI;

public class ClientCLI extends Client {

    /**
     * method {@code Client}: constructs a new Client
     *
     * @param view: default ViewGUI
     */
    public ClientCLI(ViewCLI view) {
        super(view);
    }

    /**
     * method {@code run}: invocations of controller methods to send and receive messages to and from the server.
     * Invocations of view methods to display and receive player's info.
     */
    @Override
    public void run() {
        whileRun();
    }
}