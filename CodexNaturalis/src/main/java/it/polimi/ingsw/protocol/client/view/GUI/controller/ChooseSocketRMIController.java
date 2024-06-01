package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.ClientCLI;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is a controller for the GUI scene where the user chooses between Socket and RMI for communication.
 * It contains two buttons, one for Socket and one for RMI.
 * When a button is clicked, it sends the user's choice to the client and prints the choice to the console.
 */
public class ChooseSocketRMIController {
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When the Socket button is clicked, it sends true to the client.
     * When the RMI button is clicked, it sends false to the client.
     */
    @FXML
    private void initialize() {
        socketButton.setOnAction(event -> {
            //System.out.println("Socket");
            GUIMessages.writeToClient(true);
        });

        rmiButton.setOnAction(event -> {
            //System.out.println("RMI");
            GUIMessages.writeToClient(false);
        });
    }
}