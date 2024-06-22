package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is a controller for the gui scene where the user joins a running match.
 * It contains a ListView for the matches and a list of buttons for each match.
 * The ListView is populated with the matches that are currently running.
 * Each match in the ListView is represented by a button.
 * When a button is clicked, it sends a message to the client with the match ID and disables all other buttons.
 */
public class JoinRunningMatchController {
    @FXML
    private ImageView back;
    @FXML
    private ImageView rotate1;
    @FXML
    private ImageView rotate2;
    @FXML
    private ImageView rotate3;
    @FXML
    private ImageView rotate4;
    @FXML
    private ListView<String> JoinMatchList; // ListView for the matches

    /**
     * This method is called when the scene is loaded.
     * It reads the serverOptionMessage from the gui and gets the list of running matches.
     * Then, it populates the ListView with the matches and sets the cell factory to use buttons as cells.
     */
    @FXML
    private void initialize() {
        JoinMatchList.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        serverOptionMessage serverOptionMessage = (serverOptionMessage) GUIMessages.readToGUI();
        ArrayList<Integer> matchList = serverOptionMessage.getRunningMatches();

        Utilities.rotateEffect(rotate1, 3);
        Utilities.rotateEffect(rotate2, 2);
        Utilities.rotateEffect(rotate3, 4);
        Utilities.rotateEffect(rotate4, 5);
        Utilities.hooverEffect(back, 1.05);
        Utilities.hooverEffect(rotate1, 1.2);
        Utilities.hooverEffect(rotate2, 1.2);
        Utilities.hooverEffect(rotate3, 1.2);
        Utilities.hooverEffect(rotate4, 1.2);

        // Convert the integers to strings and add them to the ListView
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Integer match : matchList) {
            items.add(match.toString());
        }
        JoinMatchList.setItems(items);

        JoinMatchList.setCellFactory(listView -> new ClickableListCell());
    }

    /**
     * This method is called when the user clicks the "Back" button.
     * It goes back to the server option scene.
     */
    @FXML
    public void goBack() {
        Platform.runLater(SceneManager::InsertServerOption);
    }


    /**
     * This class is a custom cell for the ListView that handles clicks on the cell values.
     * Each cell represents a match ID.
     * When a cell is clicked, it sends a message to the client with the match ID.
     */
    class ClickableListCell extends ListCell<String> {
        public ClickableListCell() {
            setOnMouseClicked(event -> {
                String item = getItem();
                if (item != null) {
                    GUIMessages.writeToClient(new serverOptionMessage(false, null, Integer.parseInt(item), false, null));
                    JoinMatchList.setDisable(true);
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

