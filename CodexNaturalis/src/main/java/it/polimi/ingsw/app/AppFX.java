package it.polimi.ingsw.app;

import it.polimi.ingsw.client.view.gui.mainGUI;

/**
 * This is the main class for the client side of the application.
 * It is responsible for starting the GUI of the application.
 */
public class AppFX {
    /**
     * The main method of the application.
     * It calls the main method of the mainGUI class, passing the command line arguments to it.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        mainGUI.main(args);
    }
}