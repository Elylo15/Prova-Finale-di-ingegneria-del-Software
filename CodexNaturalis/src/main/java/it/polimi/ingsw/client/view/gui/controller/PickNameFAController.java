package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import it.polimi.ingsw.messages.connectionState.unavailableNamesMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class PickNameFAController {
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label player4;


    /**
     * This method is called when the scene is loaded.
     * It reads the unavailableNamesMessage from the gui and sets the text of the unavailableNames label to the names that are already taken.
     * Then, it sets the action of the submit button to send the name chosen by the user to the client.
     */
    @FXML
    public void initialize() {
        unavailableNamesMessage message = (unavailableNamesMessage) GUIMessages.readToGUI();
        // List of names
        ArrayList<String> names = message.getNames();

        if (!names.isEmpty())
            player1.setText(names.getFirst());

        if (names.size() > 1) {
            player2.setText(names.get(1));
        }
        if (names.size() > 2) {
            player3.setText(names.get(2));
        }
        if (names.size() > 3) {
            player4.setText(names.get(3));
        }

        Utilities.hooverEffect(player1, 1.05);
        Utilities.hooverEffect(player2, 1.05);
        Utilities.hooverEffect(player3, 1.05);
        Utilities.hooverEffect(player4, 1.05);
    }

    @FXML
    public void onCLickSend(MouseEvent event) {
        Label player = (Label) event.getSource();
        GUIMessages.writeToClient(player.getText());
    }

}
