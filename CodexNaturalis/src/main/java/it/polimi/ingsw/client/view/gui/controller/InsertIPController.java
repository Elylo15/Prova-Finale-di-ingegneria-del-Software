package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;


/**
 * This class is a controller for the gui scene where the user enters the IP address.
 * It contains a TextField for the IP address and an image-button to submit the IP address.
 * When the submit button is clicked, it sends the IP address to the client.
 */
public class InsertIPController {
    @FXML
    private ImageView rotate;
    @FXML
    private ImageView rotate1;
    @FXML
    private ImageView rotate2;
    @FXML
    private ImageView rotate3;
    @FXML
    private ImageView rotate4;
    @FXML
    private ImageView rotate5;
    @FXML
    private ImageView rotate6;
    @FXML
    private ImageView rotate7;
    @FXML
    private ImageView rotate8;
    @FXML
    private ImageView rotate9;
    @FXML
    private ImageView rotate10;
    @FXML
    private ImageView backBtn;
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
        Utilities.rotateEffect(rotate, 5);
        Utilities.rotateEffect(rotate1, 2);
        Utilities.hooverEffect(rotate1, 1.1);
        Utilities.rotateEffect(rotate2, 4);
        Utilities.rotateEffect(rotate3, 3);
        Utilities.hooverEffect(rotate3, 1.1);
        Utilities.rotateEffect(rotate4, 4);
        Utilities.rotateEffect(rotate5, 5);
        Utilities.hooverEffect(rotate5, 1.1);
        Utilities.rotateEffect(rotate6, 2);
        Utilities.rotateEffect(rotate7, 3);
        Utilities.rotateEffect(rotate8, 4);
        Utilities.hooverEffect(rotate8, 1.1);
        Utilities.rotateEffect(rotate9, 3);
        Utilities.rotateEffect(rotate10, 2);
        Utilities.hooverEffect(rotate10, 1.1);

        Utilities.hooverEffect(submit_ip, 1.05);
        Utilities.hooverEffect(backBtn, 1.05);

        ip.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 38));

        submit_ip.setOnMouseClicked(event -> {
            GUIMessages.writeToClient(ip.getText());// Send the IP to the client
        });
    }

    /**
     * This method is called when the user clicks on the back button
     * It will go back to the main view
     */
    @FXML
    private void back() {
        Platform.runLater(SceneManager::MainView);
    }


}
