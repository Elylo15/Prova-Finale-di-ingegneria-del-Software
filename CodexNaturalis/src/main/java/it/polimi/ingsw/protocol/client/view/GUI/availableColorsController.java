package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class availableColorsController {
   public Label label;
   public TextField colorToChoose;
    public Button submit;

   private ViewGUI viewGUI;


    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }
    public void setUp(String string){
        label.setText("These are the color that are available: " + string);
    }

    @FXML
    private void handleSubmit() throws IOException {
        Stage stage = (Stage) colorToChoose.getScene().getWindow();
        stage.close();
    }

    public String getColor(){
        return colorToChoose.getText();
    }




}
