package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Objects;

public class UnavailableNamesController {
    @FXML
    public Label unavailableNames;
    @FXML
    public TextField nameToChoose;
    @FXML
    public Button submitButton;
    @FXML
    public ImageView back;


    /**
     * This method is called when the scene is loaded.
     * It reads the unavailableNamesMessage from the GUI and sets the label to the unavailable names.
     * Then, it sets the action of the submit button to send the name chosen by the user to the client.
     */
    @FXML
    public void initialize() {
        //deserialize the unavailableNamesMessage
        unavailableNamesMessage message = (unavailableNamesMessage) GUIMessages.readToGUI();
        // set the label to the unavailable names
        ArrayList<String> names = message.getNames();

        if(names.isEmpty()) {
            back.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/ChooseName/allName.png"))));
            nameToChoose.setLayoutX(762);
            nameToChoose.setLayoutY(534);
            submitButton.setLayoutX(1499);
            submitButton.setLayoutY(537);
        } else {
            unavailableNames.setText("These nicknames are not available: ");
            for (int i = 0; i < names.size(); i++)
                unavailableNames.setText(names.get(i) + "\n");
        }

        submitButton.setOnAction(event -> {
            if (nameToChoose.getText().isEmpty()) {
                return;
            }
            if (nameToChoose.getText().length() >= 10) {

                if(names.isEmpty()) {
                    Label errorLabel = new Label();
                    errorLabel.setStyle("-fx-text-fill: #351f17;");
                    errorLabel.setText("Your name is too long!");
                    errorLabel.setPrefWidth(874);
                    errorLabel.setPrefHeight(217);
                    errorLabel.setLayoutX(859);
                    errorLabel.setLayoutY(740);
                    errorLabel.setVisible(true);
                } else {
                    nameToChoose.setPromptText("Your name is too long!");
                }

            }

            //serialize the name chosen by the user. The method unavailable names in viewGUI can than deserialize it
            GUIMessages.writeToClient(nameToChoose.getText());
        });

    }
}

