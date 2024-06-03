package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
/**
 * This class is a controller for the GUI scene where the user joins a running match.
 * It contains a ListView for the matches and a list of buttons for each match.
 * The ListView is populated with the matches that are currently running.
 * Each match in the ListView is represented by a button.
 * When a button is clicked, it sends a message to the client with the match ID and disables all other buttons.
 */
public class JoinRunningMatchController {
    private final List<Button> buttons = new ArrayList<>();// List of buttons for each match
    @FXML
    private ListView<String> JoinRunningMatchList; // ListView for the matches
    private serverOptionMessage serverOptionMessage; // Message to be sent to the client
    private ArrayList<Integer> MatchList;// List of matches that are currently running

    /**
     * This method is called when the scene is loaded.
     * It reads the serverOptionMessage from the GUI and gets the list of running matches.
     * Then, it populates the ListView with the matches and sets the cell factory to use buttons as cells.
     */
    @FXML
    private void initialize() {
        // Read the serverOptionMessage from the GUI and get the list of running matches
        serverOptionMessage = (serverOptionMessage) GUIMessages.readToGUI();
        MatchList = serverOptionMessage.getRunningMatches();


        // Convert the integers to strings and add them to the ListView
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Integer match : MatchList) {
            items.add(match.toString());
        }
        JoinRunningMatchList.setItems(items);

        // Set the cell factory to use buttons as cells
        JoinRunningMatchList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new JoinRunningMatchController.ButtonListCell();
            }
        });


    }

    /**
     * This method is called when the user clicks the "Back" button.
     * It goes back to the server option scene.
     * @param actionEvent The event triggered by the user
     */
    public void goBack(ActionEvent actionEvent) {
        Platform.runLater(SceneManager::InsertServerOption);
    }

    /**
     * This class is a custom cell for the ListView that uses buttons.
     * Each button represents a match.
     * When a button is clicked, it sends a message to the client with the match ID and disables all other buttons.
     */
    class ButtonListCell extends ListCell<String> {
        private final Button button;

        public ButtonListCell() {
            button = new Button();
            button.setOnAction(event -> {
                String item = getItem();
                if (item != null) {
                    // Send a message to the client with the match ID and disable all other buttons
                    GUIMessages.writeToClient(new serverOptionMessage(false, null, Integer.parseInt(item), false, null));
                    disableOtherButtons(button);
                    Platform.runLater(SceneManager::waiting);
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
         *
         * @param clickedButton The button that was clicked.
         */
        private void disableOtherButtons(Button clickedButton) {
            for (Button btn : buttons) {
                btn.setDisable(btn != clickedButton);
            }
        }
    }

}
