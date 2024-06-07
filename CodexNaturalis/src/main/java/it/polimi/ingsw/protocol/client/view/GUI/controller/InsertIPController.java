package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


/**
 * This class is a controller for the GUI scene where the user enters the IP address.
 * It contains a TextField for the IP address and a Button to submit the IP address.
 * When the submit button is clicked, it sends the IP address to the client.
 */
public class InsertIPController {
    @FXML
    public ImageView rotate;
    @FXML
    public ImageView rotate1;
    @FXML
    public ImageView rotate2;
    @FXML
    public ImageView rotate3;
    @FXML
    public ImageView rotate4;
    @FXML
    public ImageView rotate5;
    @FXML
    public ImageView rotate6;
    @FXML
    public ImageView rotate7;
    @FXML
    public ImageView rotate8;
    @FXML
    public ImageView rotate9;
    @FXML
    public ImageView rotate10;

    @FXML
    private TextField ip;

    @FXML
    private Button submit_ip;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for the submit button.
     * When the submit button is clicked, it sends the IP address to the client.
     */
    @FXML
    private void initialize() {
        rotateEffect(rotate, 5);
        rotateEffect(rotate1, 2);
        rotateEffect(rotate2, 4);
        rotateEffect(rotate3, 3);
        rotateEffect(rotate4, 4);
        rotateEffect(rotate5, 5);
        rotateEffect(rotate6, 2);
        rotateEffect(rotate7, 3);
        rotateEffect(rotate8, 4);
        rotateEffect(rotate9, 3);
        rotateEffect(rotate10, 2);

        submit_ip.setOnAction(event -> {

            GUIMessages.writeToClient(ip.getText());// Send the IP to the client
        });
    }

    public void rotateEffect(ImageView imageView, int seconds){
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(seconds), imageView);
        rotateTransition.setByAngle(360); // Rotate 360 degrees
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely
        rotateTransition.setAutoReverse(true); // Do not reverse the direction

        // Start the rotation
        rotateTransition.play();
    }
}
