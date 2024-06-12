package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.ArrayList;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.hooverEffect;
import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.submitName;

public class PickNameFAController {
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
    private ListView<String> nameList; // ListView for the matches


    /**
     * This method is called when the scene is loaded.
     * It reads the unavailableNamesMessage from the GUI and sets the text of the unavailableNames label to the names that are already taken.
     * Then, it sets the action of the submit button to send the name chosen by the user to the client.
     */
    @FXML
    public void initialize() {



        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 42);
        //String namesWithNewLines = String.join("\n", names);
        //unavailableNames.setFont(font);
        //unavailableNames.setText(namesWithNewLines);

        hooverEffect(submitButton, 1.05);

        //submitName(submitButton, nameToChoose);

        // Read the serverOptionMessage from the GUI and get the list of matches
        // Message to be sent to the client
        unavailableNamesMessage message = (unavailableNamesMessage) GUIMessages.readToGUI();
        // List of names
        ArrayList<String> names = message.getNames();

        // Convert the integers to strings and add them to the ListView
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(names);
        nameList.setItems(items);

        nameList.setCellFactory(listView -> new PickNameFAController.ClickableListCell());
    }


    class ClickableListCell extends ListCell<String> {
        public ClickableListCell() {
            setOnMouseClicked(event -> {
                String item = getItem();
                if (item != null) {
                    GUIMessages.writeToClient(item);
                    nameList.setDisable(true);
                }
            });
        }

        /**
         * This method is called when the cell is updated.
         * It sets the text of the cell to the match ID.
         *
         * @param item  the match ID
         * @param empty true if the cell is empty, false otherwise
         */
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Set the text of the cell to the match ID
                setText(item);
                setGraphic(null);
            }
        }
    }
}
