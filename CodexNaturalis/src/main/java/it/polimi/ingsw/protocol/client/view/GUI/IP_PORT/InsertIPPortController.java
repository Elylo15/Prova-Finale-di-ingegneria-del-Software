package it.polimi.ingsw.protocol.client.view.GUI.IP_PORT;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;

import java.awt.*;

public class InsertIPPortController {
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;

    private ViewGUI viewGUI;

    public InsertIPPortController(TextField ipField, TextField portField) {
        this.ipField = ipField;
        this.portField = portField;
    }

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

