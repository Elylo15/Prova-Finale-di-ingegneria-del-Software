package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.stage.Stage;



public class expectedPlayersController {
    public int numberOfPlayers = 0;
    @FXML
    private Button playerTwo;
    @FXML
    private Button playerThree;

    @FXML
    private Button playerFour;

    private ViewGUI viewGUI;

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }
    public void ButtonTwo() {
        numberOfPlayers = 2;
        Stage stage = (Stage) playerTwo.getScene().getWindow();
        stage.close();
    }
    public void ButtonThree() {
        numberOfPlayers = 3;
        Stage stage = (Stage) playerThree.getScene().getWindow();
        stage.close();
    }
    public void ButtonFour() {
        numberOfPlayers = 4;
        Stage stage = (Stage) playerFour.getScene().getWindow();
        stage.close();
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }


}


