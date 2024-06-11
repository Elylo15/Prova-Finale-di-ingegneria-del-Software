package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.ArrayList;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.hooverEffect;
import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.submitName;

public class PickNameFAController {
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
     * It reads the unavailableNamesMessage from the GUI and sets the text of the unavailableNames label to the names that are already taken.
     * Then, it sets the action of the submit button to send the name chosen by the user to the client.
     */
    @FXML
    public void initialize() {
        unavailableNamesMessage message = (unavailableNamesMessage) GUIMessages.readToGUI();
        ArrayList<String> names = message.getNames();

        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 42);
        String namesWithNewLines = String.join("\n", names);
        unavailableNames.setFont(font);
        unavailableNames.setText(namesWithNewLines);

        hooverEffect(submitButton, 1.05);

        submitName(submitButton, nameToChoose);
    }
}
