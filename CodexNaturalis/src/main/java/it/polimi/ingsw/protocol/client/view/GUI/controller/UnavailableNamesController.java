package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.ClientCLI;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UnavailableNamesController {
    @FXML
    public Label unavailableNames;
    @FXML
    public TextField nameToChoose;
    @FXML
    public Button submitButton;


    private unavailableNamesMessage message;

    public void initialize() {
        this.message = (unavailableNamesMessage) GUIMessages.readToGUI();
        // Converti la lista di nomi non disponibili in una string
        String names = String.join("Already taken names: ", message.getNames());

        // Imposta il testo della label con la stringa dei nomi
        unavailableNames.setText(names);

        submitButton.setOnAction(event -> {
            if (nameToChoose.getText().isEmpty()) {
                return;
            }
            GUIMessages.writeToClient(nameToChoose.getText());
        });

    }
}

