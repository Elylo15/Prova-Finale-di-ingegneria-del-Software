package it.polimi.ingsw.protocol.client.view.GUI.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitingController implements Initializable {
    @FXML
    ImageView red;
    @FXML
    ImageView blue;
    @FXML
    ImageView green;
    @FXML
    ImageView yellow;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set the images of the players based on color chosen
    }
}
