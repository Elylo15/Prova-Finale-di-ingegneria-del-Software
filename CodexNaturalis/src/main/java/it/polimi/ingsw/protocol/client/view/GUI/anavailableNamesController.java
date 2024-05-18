package it.polimi.ingsw.protocol.client.view.GUI;

import javafx.scene.control.TextField;

import java.util.ArrayList;

public class anavailableNamesController {

    public TextField name;
    public TextField unavailable;

    public void setNames(String string){
        unavailable.setText(string);
    }

    public String chooseName(){
        return name.getText();
    }



}
