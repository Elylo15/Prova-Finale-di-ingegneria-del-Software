package it.polimi.ingsw.protocol.client.view.GUI;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class availableColorsController {
    public Label available;
    public TextField color;
    public void setUp(String string){
        available.setText("these are the colors that are available " + string);
    }
    public String chooseColor(){
        return color.getText();
    }
}
