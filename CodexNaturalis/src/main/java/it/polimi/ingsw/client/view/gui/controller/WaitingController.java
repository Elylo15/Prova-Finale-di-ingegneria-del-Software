package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is a controller for the gui scene where the user waits for the other players to choose their colors.
 * It contains an image that changes based on the color chosen by the user.
 */
public class WaitingController implements Initializable {

    @FXML
    private BorderPane pane;

    /**
     * This method is called when the scene is loaded.
     * It reads the color from the gui and sets the image to the color chosen by the user.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String color = (String) GUIMessages.readToGUI();
        Image img = null;

        switch (color) {
            case "red" ->
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Waiting/WaitingRed.png")));
            case "blue" ->
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Waiting/WaitingBlue.png")));
            case "green" ->
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Waiting/WaitingGreen.png")));
            case "purple" ->
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Waiting/WaitingPurple.png")));
            default ->
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Waiting/WaitingRand.png")));
        }

        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);
        imageView.setPreserveRatio(true);
        pane.getChildren().add(imageView);
    }
}
