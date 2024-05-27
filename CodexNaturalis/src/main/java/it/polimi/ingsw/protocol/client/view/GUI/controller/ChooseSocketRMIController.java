package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.ClientCLI;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChooseSocketRMIController {
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;
    @FXML
    private void initialize() {
        socketButton.setOnAction(event -> {
            System.out.println("Socket");
            GUIMessages.writeToClient(true);
        });

        rmiButton.setOnAction(event -> {
            System.out.println("RMI");
            GUIMessages.writeToClient(false);
        });
    }
}