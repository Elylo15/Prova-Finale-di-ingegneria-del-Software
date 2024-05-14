package it.polimi.ingsw.protocol.client.view.GUI.expectedPlayers;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class expectedPlayersController implements Initializable {

    private ViewGUI viewGUI;

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }
    @FXML
    private Label label;

    @FXML
    private ChoiceBox<Integer> choice;

    private Integer[] numPlayers = {1,2,3,4,5};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        choice.getItems().addAll(numPlayers); //populates the choice box
        choice.setOnAction(this::getNumber);

    }

    /**
     *
     * @param event
     * @return the number the user chose
     */
    public int getNumber(ActionEvent event) {

        Integer result = choice.getValue();
        label.setText("You selected " + result.toString());
        return result;
    }

}


