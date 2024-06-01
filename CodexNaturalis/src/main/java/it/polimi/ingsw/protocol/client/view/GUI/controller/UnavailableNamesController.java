package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UnavailableNamesController {
    @FXML
    public Label unavailableNames;
    @FXML
    public TextField nameToChoose;
    @FXML
    public Button submitButton;


    private unavailableNamesMessage message;

    @FXML
    public void initialize() {
        //deserialize the unavailableNamesMessage
        this.message = (unavailableNamesMessage) GUIMessages.readToGUI();
        // set the label to the anavailable names
        if(message.toString().equals("[]")){
            unavailableNames.setText("All nicknames are available");
        }
        else {
            unavailableNames.setText("These nicknames are not available: " + message.getNames());
        }
        submitButton.setOnAction(event -> {
            if (nameToChoose.getText().isEmpty()) {
                return;
            }
            //serialize the name chosen by the user. The method unavailable names in viewGUI can than deserialize it
            GUIMessages.writeToClient(nameToChoose.getText());
        });

    }
}

