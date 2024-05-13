package it.polimi.ingsw.GUI.ServerOption;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class InsertServerOptionController {

    @FXML
    private Button newMatchButton;

    @FXML
    private Button runMatchButton;

    @FXML
    private Button loadMatchButton;

    private ViewGUI viewGUI;
    private boolean newMatchPressed = false;
    private boolean runMatchPressed = false;
    private boolean loadMatchPressed = false;

    public InsertServerOptionController(Button newMatchButton) {
        this.newMatchButton = newMatchButton;
    }

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }

    @FXML
    public void initialize() {
        newMatchButton.setOnAction(e ->{
            newMatchPressed=true;
            runMatchButton.setDisable(true);
            loadMatchButton.setDisable(true);
            serverOptionMessage message = new serverOptionMessage(true, null, null, false, null);
            viewGUI.serverOptions(message);
        } );

        runMatchButton.setOnAction(e ->{
            runMatchPressed=true;
            newMatchButton.setDisable(true);
            loadMatchButton.setDisable(true);
            //manda ad un'altra pagina

        } );

        loadMatchButton.setOnAction(e ->{
            loadMatchPressed=true;
            newMatchButton.setDisable(true);
            runMatchButton.setDisable(true);
            //manda ad un'altra pagina
        } );
    }

    public boolean newMatchButtonPressed() {
        return newMatchPressed; // Return true if the new match button is pressed
    }
}
