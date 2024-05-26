package it.polimi.ingsw.protocol.client.view.GUI.controller;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;


import it.polimi.ingsw.protocol.client.ClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InsertIPController {

    @FXML
    private TextField ip;

    @FXML
    private Button submit_ip;

    private GUIMessages GUIMessages = new GUIMessages();


    @FXML
    private void initialize() {
        submit_ip.setOnAction(event -> {
            GUIMessages.writeToClient(ip.getText()); // Send the IP to the client
        });
    }
}
