package it.polimi.ingsw.protocol.client.view.GUI.controller;


import it.polimi.ingsw.protocol.client.ClientCLI;
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
    private ClientCLI clientCLI;

    private WaitingController waitingController;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(ClientCLI clientCLI) {
        this.clientCLI = clientCLI;
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
        clientCLI.color();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/availableColors.fxml"));
            Parent root = loader.load();
            waitingController = loader.getController();
            waitingController.setPrimaryStage(primaryStage);
            waitingController.setClient(clientCLI);

            clientCLI.setWaitingController(waitingController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
