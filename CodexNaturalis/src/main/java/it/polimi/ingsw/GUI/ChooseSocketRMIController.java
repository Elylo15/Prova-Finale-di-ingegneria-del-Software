 package it.polimi.ingsw.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ChooseSocketRMIController {
    @FXML
    private Button socketButton;

    @FXML
    private Button rmiButton;

    private ViewGUI viewGUI;

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }

    @FXML
    public void initialize() {
        viewGUI.askSocket();

    }

    public boolean useSocket() {
        return socketButton.isPressed(); // Return true if the socket button is pressed
    }
}

