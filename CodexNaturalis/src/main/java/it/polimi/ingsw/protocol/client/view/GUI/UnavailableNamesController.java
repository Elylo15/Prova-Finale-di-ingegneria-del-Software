package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UnavailableNamesController {
    public Label unavailableNames;
    public Label label2;

    public TextField nameToChoose;

    public Button submitButton;
    private ViewGUI viewGUI;

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }
    public void setUp(String string){
        unavailableNames.setText("These are the names that are not available: " + string);
    }
    public void setUpNoNames(){
        unavailableNames.setText("All the nicknames are available");
    }
    @FXML
    private void handleSubmit() throws IOException {
        Stage stage = (Stage) nameToChoose.getScene().getWindow();
        stage.close();
    }
    public String getName(){
        return nameToChoose.getText();
    }




}

