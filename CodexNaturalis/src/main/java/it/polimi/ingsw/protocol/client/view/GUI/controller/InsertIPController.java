package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.rotateEffect;


/**
 * This class is a controller for the GUI scene where the user enters the IP address.
 * It contains a TextField for the IP address and an image-button to submit the IP address.
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
    private ImageView submit_ip;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for the image-button.
     * When the button is clicked, it sends the IP address to the client.
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

        submit_ip.setOnMouseClicked(event -> {

            GUIMessages.writeToClient(ip.getText());// Send the IP to the client
        });
    }

}
