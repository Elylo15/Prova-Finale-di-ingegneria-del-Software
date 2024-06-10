package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.submitName;

public class UnavailableNamesController {
    @FXML
    public Label unavailableNames;
    @FXML
    public TextField nameToChoose;
    @FXML
    public ImageView submitButton;
    @FXML
    public ImageView back;
    @FXML
    public ImageView field;


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

        if (names.isEmpty()) {
            back.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/ChooseName/allName.png"))));
            nameToChoose.setLayoutX(753);
            nameToChoose.setLayoutY(502);
            submitButton.setLayoutX(1357);
            submitButton.setLayoutY(496);
            field.setLayoutX(725);
            field.setLayoutY(475);
        } else {
            unavailableNames.setText("These nicknames are not available: ");
            for (String name : names) unavailableNames.setText(name + "\n");
        }

        submitName(submitButton, nameToChoose);
    }
}

