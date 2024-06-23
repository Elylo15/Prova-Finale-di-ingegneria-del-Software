package it.polimi.ingsw.app;

import it.polimi.ingsw.client.view.gui.MainGUI;

/**
 * This is the main class for the client side of the application.
 * It is responsible for starting the GUI of the application.
 */
public class AppFX {
    /**
     * The main method of the application.
     * It calls the main method of the MainGUI class, passing the command line arguments to it.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        MainGUI.main(args);
    }
}