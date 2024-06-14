package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AnswerController implements Initializable {
    @FXML
    private ImageView image;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for the image.
     * When the image is clicked, it sends a message to the client,
     * to notify that the user has clicked the image and wants to proceed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.setOnMouseClicked(event -> GUIMessages.writeToClient(true));
    }
}
