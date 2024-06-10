package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.hooverEffect;

public class AvailableColorsController {
    availableColorsMessage message;
    @FXML
    ImageView blue;
    @FXML
    ImageView green;
    @FXML
    ImageView red;
    @FXML
    ImageView purple;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each image-button.
     * When a button is clicked, it sends the color to the client.
     */
    @FXML
    public void initialize() {
        red.setDisable(true);
        green.setDisable(true);
        blue.setDisable(true);
        purple.setDisable(true);

        this.message = (availableColorsMessage) GUIMessages.readToGUI();

        for (int i = 0; i < message.getColors().size(); i++) {
            if (message.getColors().get(i).equalsIgnoreCase("red")) {
                red.setDisable(false);
                red.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Colors/red.png"))));
                hooverEffect(red, null, 1.2);
            } else if (message.getColors().get(i).equalsIgnoreCase("green")) {
                green.setDisable(false);
                green.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Colors/green.png"))));
                hooverEffect(green, null, 1.2);
            } else if (message.getColors().get(i).equalsIgnoreCase("blue")) {
                blue.setDisable(false);
                blue.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Colors/blue.png"))));
                hooverEffect(blue, null, 1.2);
            } else if (message.getColors().get(i).equalsIgnoreCase("purple")) {
                purple.setDisable(false);
                purple.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Colors/purple.png"))));
                hooverEffect(purple, null, 1.2);
            }
        }
        //send the result
        red.setOnMouseClicked(event -> GUIMessages.writeToClient("red"));
        purple.setOnMouseClicked(event -> GUIMessages.writeToClient("purple"));
        green.setOnMouseClicked(event -> GUIMessages.writeToClient("green"));
        blue.setOnMouseClicked(event -> GUIMessages.writeToClient("blue"));
    }


}
