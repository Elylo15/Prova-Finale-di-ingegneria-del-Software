package it.polimi.ingsw.protocol.client.view.GUI;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class unavailableNamesController {

    public TextField name;
    public TextField unavailable;

    public void setNames(String string){
        unavailable.setText(string);
    }

    public String chooseName(){
        return name.getText();
    }

}

