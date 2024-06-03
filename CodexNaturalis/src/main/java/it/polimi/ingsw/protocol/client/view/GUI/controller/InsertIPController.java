package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


/**
 * This class is a controller for the GUI scene where the user enters the IP address.
 * It contains a TextField for the IP address and a Button to submit the IP address.
 * When the submit button is clicked, it sends the IP address to the client.
 */
public class InsertIPController {

    @FXML
    private TextField ip;

    @FXML
    private Button submit_ip;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for the submit button.
     * When the submit button is clicked, it sends the IP address entered in the TextField to the client.
     */
    @FXML
    private void initialize() {
        submit_ip.setOnAction(event -> {

            GUIMessages.writeToClient(ip.getText());// Send the IP to the client
        });
    }
}
