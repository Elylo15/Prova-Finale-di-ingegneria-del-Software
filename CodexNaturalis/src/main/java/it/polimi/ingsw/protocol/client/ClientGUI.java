package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.view.ViewGUI;

public class ClientGUI extends Client implements Runnable {

    /**
     * method {@code Client}: constructs a new Client
     *
     * @param view: default ViewGUI
     */
    public ClientGUI(ViewGUI view) {
        super(view);
    }

    /**
     * method {@code run}: invocations of controller methods to send and receive messages to and from the server.
     * Invocations of view methods to display and receive player's info.
     */
    @Override
    public void run() {
        ((ViewGUI) getView()).startMain();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        whileRun();
    }
}
