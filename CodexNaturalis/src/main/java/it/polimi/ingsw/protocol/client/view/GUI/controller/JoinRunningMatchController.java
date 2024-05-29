package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.ClientCLI;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class JoinRunningMatchController {
    @FXML
    private ListView<String> JoinRunningMatchList;

    private serverOptionMessage serverOptionMessage;
    private ArrayList<Integer> MatchList;

    private List<Button> buttons = new ArrayList<>();


    private void initialize() {
        serverOptionMessage = (serverOptionMessage) GUIMessages.readToGUI();
        MatchList  = serverOptionMessage.getRunningMatches();


        // Converti gli interi in stringhe e aggiungili a runningMatchList
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Integer match : MatchList) {
            items.add(match.toString());
        }
        JoinRunningMatchList.setItems(items);


        // Imposta gli elementi nella ListView
        JoinRunningMatchList.getItems().addAll(items);

        // Imposta la cell factory per usare bottoni come celle
        JoinRunningMatchList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new JoinRunningMatchController.ButtonListCell();
            }
        });


    }

    // Classe personalizzata per le celle che usa bottoni
    class ButtonListCell extends ListCell<String> {
        private final Button button;

        public ButtonListCell() {
            button = new Button();
            button.setOnAction(event -> {
                String item = getItem();
                if (item != null) {
                    GUIMessages.writeToClient(new serverOptionMessage(false,null,  Integer.parseInt(item), false, null));
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
                button.setText(item);
                setGraphic(button);
            }
        }

        // Metodo per disabilitare tutti i bottoni tranne quello cliccato
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
