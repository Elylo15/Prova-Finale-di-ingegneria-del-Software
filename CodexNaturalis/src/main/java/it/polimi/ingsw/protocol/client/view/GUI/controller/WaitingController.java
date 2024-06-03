package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WaitingController implements Initializable {

    @FXML
    public BorderPane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String color = (String) GUIMessages.readToGUI();
        Image img = null;

        switch (color) {
            case "red" ->
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Waiting/WaitingRed.png")));
            case "blue" ->
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Waiting/WaitingBlue.png")));
            case "green" ->
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Waiting/WaitingGreen.png")));
            case "purple" ->
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Waiting/WaitingPurple.png")));
        }

        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);
        imageView.setPreserveRatio(true);
        pane.getChildren().add(imageView);
    }
}
