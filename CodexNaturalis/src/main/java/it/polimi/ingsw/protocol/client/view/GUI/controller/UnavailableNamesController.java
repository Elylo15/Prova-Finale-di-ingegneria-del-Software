package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class UnavailableNamesController {
    @FXML
    public Label unavailableNames;
    @FXML
    public TextField nameToChoose;
    @FXML
    public Button submitButton;
    private unavailableNamesMessage message;


    /**
     * This method is called when the scene is loaded.
     * It reads the unavailableNamesMessage from the GUI and sets the label to the unavailable names.
     * Then, it sets the action of the submit button to send the name chosen by the user to the client.
     */
    @FXML
    public void initialize() {
        //deserialize the unavailableNamesMessage
        this.message = (unavailableNamesMessage) GUIMessages.readToGUI();
        // set the label to the unavailable names
        ArrayList<String> names = message.getNames();

        if(names.isEmpty()) {
            unavailableNames.setText("All nicknames are available");
        } else {
            unavailableNames.setText("These nicknames are not available: ");
            for (int i = 0; i < names.size(); i++)
                unavailableNames.setText(names.get(i) + "\n");
        }

        submitButton.setOnAction(event -> {
            if (nameToChoose.getText().isEmpty()) {
                return;
            }
            if (nameToChoose.getText().length() > 10) {
                Label errorLabel = new Label();
                errorLabel.setStyle("-fx-text-fill: red;");     //TODO see if color and position is ok
                errorLabel.setText("Your nickname is too long! Use at most 10 characters.");
                errorLabel.setVisible(true);
            }

            //serialize the name chosen by the user. The method unavailable names in viewGUI can than deserialize it
            GUIMessages.writeToClient(nameToChoose.getText());
        });

    }
}

