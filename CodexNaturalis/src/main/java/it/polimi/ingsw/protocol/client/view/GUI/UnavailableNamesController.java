package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.scene.control.TextField;

public class UnavailableNamesController {

    public TextField name;
    public TextField unavailable;
    private ViewGUI viewGUI;


    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }

    public void setNames(String string){
        unavailable.setText(string);
    }

    public String chooseName(){
        return name.getText();
    }


}

