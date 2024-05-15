package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ChooseSocketRMIController {
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;

    private BooleanProperty useSocket = new SimpleBooleanProperty();

    private ViewGUI viewGUI;

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }

    public void initialize() {
        socketButton.setOnAction(event -> useSocket.set(true));
        rmiButton.setOnAction(event -> useSocket.set(false));
    }

    public BooleanProperty useSocketProperty() {
        return useSocket;
    }
}