package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChooseSocketRMIController {
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;

    private boolean useSocket;

    private ViewGUI viewGUI;

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }

    public void isSocket() {
            useSocket = true;
            Stage stage = (Stage) socketButton.getScene().getWindow();
            stage.close();
    }

    public void isRMI() {
            useSocket = false;
            Stage stage = (Stage) rmiButton.getScene().getWindow();
            stage.close();
    }

    public boolean useSocket() {
        return useSocket;
    }
}