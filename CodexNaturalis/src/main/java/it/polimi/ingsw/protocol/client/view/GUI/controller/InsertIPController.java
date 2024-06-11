package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.hooverEffect;
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
    public ImageView backBtn;
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
        hooverEffect(rotate1, 1.1);
        rotateEffect(rotate2, 4);
        rotateEffect(rotate3, 3);
        hooverEffect(rotate3, 1.1);
        rotateEffect(rotate4, 4);
        rotateEffect(rotate5, 5);
        hooverEffect(rotate5, 1.1);
        rotateEffect(rotate6, 2);
        rotateEffect(rotate7, 3);
        rotateEffect(rotate8, 4);
        hooverEffect(rotate8, 1.1);
        rotateEffect(rotate9, 3);
        rotateEffect(rotate10, 2);
        hooverEffect(rotate10, 1.1);

        hooverEffect(submit_ip, 1.05);
        hooverEffect(backBtn, 1.05);

        submit_ip.setOnMouseClicked(event -> {
            GUIMessages.writeToClient(ip.getText());// Send the IP to the client
        });
    }

    /**
     * This method is called when the user clicks on the back button
     * It will go back to the main view
     */
    @FXML
    public void back() {
        Platform.runLater(SceneManager::MainView);
    }


}
