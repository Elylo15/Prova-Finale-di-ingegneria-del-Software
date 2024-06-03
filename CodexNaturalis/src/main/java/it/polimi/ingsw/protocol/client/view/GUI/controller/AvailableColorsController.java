package it.polimi.ingsw.protocol.client.view.GUI.controller;


import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
    Button purple;

    private Stage primaryStage;
    private Client client;
    private WaitingController waitingController;

    availableColorsMessage message;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(Client clientCLI) {
        this.client = clientCLI;
    }

    public WaitingController getWaitingController() {
        return waitingController;
    }


    @FXML
    public void initialize() {
        //initialize
        this.message = (availableColorsMessage) GUIMessages.readToGUI();
        red.setDisable(true);
        purple.setDisable(true);
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
            if (message.getColors().get(i).equalsIgnoreCase("purple")) {
                purple.setDisable(false);
            }
        }
        //send the result
        red.setOnAction(event -> {
            GUIMessages.writeToClient("red");
        });

        purple.setOnAction(event -> {
            GUIMessages.writeToClient("purple");
        });
        green.setOnAction(event -> {
            GUIMessages.writeToClient("green");
        });
        blue.setOnAction(event -> {
            GUIMessages.writeToClient("blue");
        });
    }



}
