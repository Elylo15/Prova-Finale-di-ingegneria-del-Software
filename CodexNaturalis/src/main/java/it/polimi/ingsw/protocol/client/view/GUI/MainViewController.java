package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class MainViewController {
    private Stage primaryStage;
    private Client client;
    private InsertIPPortController insertIPPortController;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public InsertIPPortController getInsertIPPortController() {
        return insertIPPortController;
    }


    // Gestore di eventi per il clic del mouse
    @FXML
    public void loadNextPage(MouseEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insert_IP_PORT.fxml"));
            Parent root = loader.load();

            //passo lo stage al controller successivo
            insertIPPortController = loader.getController();
            insertIPPortController.setPrimaryStage(primaryStage);

            //passo il client al controller successivo
            insertIPPortController.setClient(client);

            //passo il controller al client
            client.setInsertIPPortController(insertIPPortController);

            primaryStage.getScene().setRoot(root);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}