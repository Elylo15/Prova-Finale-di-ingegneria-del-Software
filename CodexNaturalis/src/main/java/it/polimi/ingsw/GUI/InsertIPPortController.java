package it.polimi.ingsw.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;

import java.awt.*;

public class InsertIPPortController {
    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    private ViewGUI viewGUI;

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }

    @FXML
    private void handleSubmit() {
        viewGUI.askPortIP();
    }

    public String getIp() {
        return ipField.getText();
    }

    public String getPort() {
        return portField.getText();
    }
}

