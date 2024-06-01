package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a controller for the GUI scene where the user loads a match.
 * It contains a ListView for the matches and a list of buttons for each match.
 * The ListView is populated with the matches that are waiting for players.
 * Each match in the ListView is represented by a button.
 * When a button is clicked, it sends a message to the client with the match ID and disables all other buttons.
 */
public class LoadMatchController {
    @FXML
    private ListView<String> LoadMatchList;// ListView for the matches

    private serverOptionMessage serverOptionMessage; // Message to be sent to the client
    private ArrayList<Integer> MatchList;// List of matches waiting for players

    private List<Button> buttons = new ArrayList<>(); // List of buttons for each match

    /**
     * This method is called when the scene is loaded.
     * It reads the serverOptionMessage from the GUI and gets the list of matches.
     * Then, it populates the ListView with the matches and sets the cell factory to use buttons as cells.
     */
    private void initialize() {
        serverOptionMessage = (serverOptionMessage) GUIMessages.readToGUI();
        MatchList  = serverOptionMessage.getWaitingMatches();


        // Convert the integers to strings and add them to the ListView
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Integer match : MatchList) {
            items.add(match.toString());
        }
        LoadMatchList.setItems(items);



        LoadMatchList.getItems().addAll(items);

        // Set the cell factory to use buttons as cells
        LoadMatchList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new LoadMatchController.ButtonListCell();
            }
        });


    }

    /**
     * This class is a custom cell for the ListView that uses buttons.
     * Each button represents a match.
     * When a button is clicked, it sends a message to the client with the match ID and disables all other buttons.
     */
    class ButtonListCell extends ListCell<String> {
        private final Button button;// Button for the match

        public ButtonListCell() {
            button = new Button();
            button.setOnAction(event -> {
                String item = getItem();
                if (item != null) {
                    // Send a message to the client with the match ID and disable all other buttons
                    GUIMessages.writeToClient(new serverOptionMessage(false, null, null, true, Integer.parseInt(item)));
                    disableOtherButtons(button);
                }
            });
            buttons.add(button);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Set the text of the button to the match ID and set the graphic of the cell to the button
                button.setText(item);
                setGraphic(button);
            }
        }

        /**
         * This method disables all buttons except the one that was clicked.
         * @param clickedButton The button that was clicked.
         */
        private void disableOtherButtons(Button clickedButton) {
            for (Button btn : buttons) {
                if (btn != clickedButton) {
                    btn.setDisable(true);
                } else {
                    btn.setDisable(false);
                }
            }
        }
    }


}
