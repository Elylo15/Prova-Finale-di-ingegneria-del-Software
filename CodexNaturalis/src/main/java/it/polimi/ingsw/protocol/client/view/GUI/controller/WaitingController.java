package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.ClientCLI;
import javafx.stage.Stage;

public class WaitingController {
    private Stage primaryStage;
    private ClientCLI clientCLI;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(ClientCLI clientCLI) {
        this.clientCLI = clientCLI;
    }
}
