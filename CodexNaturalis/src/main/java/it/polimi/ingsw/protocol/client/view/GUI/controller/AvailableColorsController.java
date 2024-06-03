package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

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
    availableColorsMessage message;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When a button is clicked, it sends the color to the client.
     */
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
        red.setOnAction(event -> GUIMessages.writeToClient("red"));
        purple.setOnAction(event -> GUIMessages.writeToClient("purple"));
        green.setOnAction(event -> GUIMessages.writeToClient("green"));
        blue.setOnAction(event -> GUIMessages.writeToClient("blue"));
    }

//    private void onHoover(mouseEvent e) {
//        ScaleTransition enlargeTransition = new ScaleTransition(Duration.millis(200), clickedCard);
//        enlargeTransition.setToX(1.5);
//        enlargeTransition.setToY(1.5);
//
//        ScaleTransition shrinkTransition = new ScaleTransition(Duration.millis(200), clickedCard);
//        shrinkTransition.setToX(1.0);
//        shrinkTransition.setToY(1.0);
//
//        // Set mouse event handlers for transitions
//        clickedCard.setOnMouseEntered(e -> enlargeTransition.playFromStart());
//        clickedCard.setOnMouseExited(e -> shrinkTransition.playFromStart());
//    }

    //TODO: image with the four people, each image has a color attached, when the user clicks on the image, the color is sent to the server
    //onHoover it became bigger

}
