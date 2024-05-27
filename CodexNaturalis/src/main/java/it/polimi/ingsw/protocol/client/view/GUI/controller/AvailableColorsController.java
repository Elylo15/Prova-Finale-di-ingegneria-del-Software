package it.polimi.ingsw.protocol.client.view.GUI.controller;


import it.polimi.ingsw.protocol.client.ClientCLI;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AvailableColorsController {
    @FXML
    Label availableColors;
    @FXML
    Button blue;
    @FXML
    Button green;
    @FXML
    Button red;
    @FXML
    Button yellow;

    private Stage primaryStage;
    private ClientCLI clientCLI;
    private WaitingController waitingController;

    availableColorsMessage message;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(ClientCLI clientCLI) {
        this.clientCLI = clientCLI;
    }

    public WaitingController getWaitingController() {
        return waitingController;
    }


    @FXML
    public void initialize() {
        //initialize
        this.message = (availableColorsMessage) GUIMessages.readToGUI();
        red.setDisable(true);
        yellow.setDisable(true);
        blue.setDisable(true);
        green.setDisable(true);
        availableColors.setText("These colors are available: " + message.getColors());
        for (int i = 0; i < message.getColors().size(); i++) {
            //scroll through the array of available colors, if a color is present we enable its button,
            //otherwise it will remain disabled
            if (message.getColors().get(i).equalsIgnoreCase("red")) {
                red.setDisable(false);
            }
            if (message.getColors().get(i).equalsIgnoreCase("green")) {
                green.setDisable(false);
            }
            if (message.getColors().get(i).equalsIgnoreCase("blue")) {
                blue.setDisable(false);
            }
            if (message.getColors().get(i).equalsIgnoreCase("yellow")) {
                yellow.setDisable(false);
            }
        }
    }

    public void getColor(){
        //send the result
        red.setOnAction(event -> {
            GUIMessages.writeToClient("red");
        });

        yellow.setOnAction(event -> {
            GUIMessages.writeToClient("yellow");
        });
        green.setOnAction(event -> {
            GUIMessages.writeToClient("green");
        });
        blue.setOnAction(event -> {
            GUIMessages.writeToClient("blue");
        });
    }




}
