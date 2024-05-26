package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.ClientCLI;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InsertServerOptionController {
    @FXML
    public Button new_match;
    @FXML
    public Button join_match;
    @FXML
    public Button load_match;
    @FXML
    public Button join_running_match;

    private serverOptionMessage serverOptionMessage;

    private void initialize() {
        this.serverOptionMessage = (serverOptionMessage) GUIMessages.readToGUI();

        new_match.setOnAction(event -> {
            serverOptionMessage = new serverOptionMessage(true, null, null, false, null);
            GUIMessages.writeToClient(serverOptionMessage);
        });

        join_match.setOnAction(event -> {
            Platform.runLater(SceneManager::JoinMatch);
            GUIMessages.writeToGUI(serverOptionMessage);
        });

        load_match.setOnAction(event -> {
            Platform.runLater(SceneManager::LoadMatch);
            GUIMessages.writeToGUI(serverOptionMessage);
        });

        join_running_match.setOnAction(event -> {
            Platform.runLater(SceneManager::JoinRunningMatch);
            GUIMessages.writeToGUI(serverOptionMessage);
        });
    }



}
