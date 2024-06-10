package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.hooverEffect;

/**
 * This class is a controller for the GUI scene where the user chooses between Socket and RMI for communication.
 * It contains two image-buttons, one for Socket and one for RMI.
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
     * When the RMI button is clicked, it sends false to the client.
     */
    @FXML
    private void initialize() {
        hooverEffect(socketButton, 1.05);
        hooverEffect(rmiButton, 1.05);

        socketButton.setOnMouseClicked(event -> {
            //System.out.println("Socket");
            GUIMessages.writeToClient(true);
        });

        rmiButton.setOnMouseClicked(event -> {
            //System.out.println("RMI");
            GUIMessages.writeToClient(false);
        });
    }


}