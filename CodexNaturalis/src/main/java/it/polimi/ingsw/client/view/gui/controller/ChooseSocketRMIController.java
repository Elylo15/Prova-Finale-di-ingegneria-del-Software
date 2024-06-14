package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * This class is a controller for the gui scene where the user chooses between Socket and rmi for communication.
 * It contains two image-buttons, one for Socket and one for rmi.
 * When a button is clicked, it sends the user's choice to the client and prints the choice to the console.
 */
public class ChooseSocketRMIController {
    @FXML
    private ImageView socketButton;
    @FXML
    private ImageView rmiButton;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each image-button.
     * When the Socket button is clicked, it sends true to the client.
     * When the rmi button is clicked, it sends false to the client.
     */
    @FXML
    private void initialize() {
        Utilities.hooverEffect(socketButton, 1.05);
        Utilities.hooverEffect(rmiButton, 1.05);

        socketButton.setOnMouseClicked(event -> {
            //System.out.println("Socket");
            GUIMessages.writeToClient(true);
        });

        rmiButton.setOnMouseClicked(event -> {
            //System.out.println("rmi");
            GUIMessages.writeToClient(false);
        });
    }


}