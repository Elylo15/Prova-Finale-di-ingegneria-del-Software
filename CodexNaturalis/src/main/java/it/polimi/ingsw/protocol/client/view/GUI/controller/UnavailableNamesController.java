package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.*;

public class UnavailableNamesController implements Initializable {
    @FXML
    private Label unavailableNames;
    @FXML
    private TextField nameToChoose;
    @FXML
    private ImageView submitButton;
    @FXML
    private ImageView back;
    @FXML
    private ImageView field;
    @FXML
    private Label retry;
    private ArrayList<String> names;

    /**
     * This method is called when the scene is loaded.
     * It reads the unavailableNamesMessage from the GUI and sets the label to the unavailable names.
     * Then, it sets the action of the submit button to send the name chosen by the user to the client.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unavailableNamesMessage message = (unavailableNamesMessage) GUIMessages.readToGUI();

        hooverEffect(submitButton, 1.05);

        submitButton.setOnMouseClicked(event -> {
            if (nameToChoose.getText().isEmpty()) {
                retry.setText("Try Again!");
                retry.setVisible(true);
            } else if (nameToChoose.getText().length() >= 10) {
                retry.setText("Too Long!");
                retry.setVisible(true);
            } else if (names.contains(nameToChoose.getText())) {
                retry.setText("Name Taken!");
                retry.setVisible(true);
            } else
                GUIMessages.writeToClient(nameToChoose.getText());
        });

        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 42);

        names = message.getNames();
        unavailableNames.setFont(font);
        if (names.isEmpty()) {
            back.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/ChooseName/allName.png"))));
            nameToChoose.setLayoutX(753);
            nameToChoose.setLayoutY(502);
            nameToChoose.setFont(font);
            submitButton.setLayoutX(1357);
            submitButton.setLayoutY(496);
            field.setLayoutX(725);
            field.setLayoutY(475);
        } else {
            String namesWithNewLines = String.join("\n", names);
            unavailableNames.setFont(font);
            unavailableNames.setText(namesWithNewLines);
        }
    }
}