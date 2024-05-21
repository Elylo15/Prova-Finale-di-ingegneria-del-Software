package it.polimi.ingsw.protocol.client.view.GUI;


import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AvailableColorsController {
    public Label label;
    public TextField colorToChoose;
    public Button submit;

    private Stage primaryStage;
    private Client client;

    private WaitingController waitingController;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public WaitingController getWaitingController() {
        return waitingController;
    }

    public void setUp(String string){
        label.setText("These are the color that are available: " + string);
    }

    public String getColor(){
        return colorToChoose.getText();
    }

    @FXML
    private void handleSubmit() throws IOException {
        client.color();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/availableColors.fxml"));
            Parent root = loader.load();
            waitingController = loader.getController();
            waitingController.setPrimaryStage(primaryStage);
            waitingController.setClient(client);

            client.setWaitingController(waitingController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
