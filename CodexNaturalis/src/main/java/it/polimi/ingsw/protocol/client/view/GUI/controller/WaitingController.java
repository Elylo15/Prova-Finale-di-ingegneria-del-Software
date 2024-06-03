package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitingController implements Initializable {



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String color = (String) GUIMessages.readToGUI();
        ImageView imageView = new ImageView();
        imageView.setLayoutX(100); //TODO set correct position
        imageView.setLayoutY(100); //TODO

        switch (color) {
            case "RED" -> {
                Image image = new Image("/img/Background/WaitingRed.png");
                imageView.setImage(image);
            }
            case "BLUE" -> {
                Image image = new Image("/img/Background/WaitingBlue.png");
                imageView.setImage(image);
            }
            case "GREEN" -> {
                Image image = new Image("/img/Background/WaitingGreen.png");
                imageView.setImage(image);
            }
            case "PURPLE" -> {
                Image image = new Image("/img/Background/WaitingPurple.png");
                imageView.setImage(image);
            }
        }
    }
}
