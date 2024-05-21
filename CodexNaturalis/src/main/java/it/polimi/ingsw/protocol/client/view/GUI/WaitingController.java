package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.Client;
import javafx.stage.Stage;

public class WaitingController {
    private Stage primaryStage;
    private Client client;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
