package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class InsertIPPortController {
    public TextField ip_server;
    public TextField port_protocol;
    public Button submit_ip;
    private ViewGUI viewGUI;


    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }

    @FXML
    private void handleSubmit() throws IOException {
        Stage stage = (Stage) ip_server.getScene().getWindow();
        stage.close();
    }

    public String getIP(){
        return ip_server.getText();
    }

    public String getPort(){
        return port_protocol.getText();
    }


}

