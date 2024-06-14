package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;


public class MainPageController implements Initializable {
    @FXML
    private ImageView playBtn;
    @FXML
    private ImageView rulesBtn;

    /**
     * This method is called when the player presses the play image-button.
     * It sends a message to start the game.
     */
    @FXML
    private void start() {
        GUIMessages.writeToClient(true);
    }

    /**
     * This method is called when the player presses the rules image-button.
     * It loads the rules that teaches the player how to play.
     */
    @FXML
    private void loadRules() {
        Platform.runLater(SceneManager::learToPlay);
    }

    /**
     * This method is called when the scene is loaded.
     * It sets the hoover effect for the play and rules image-buttons.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Utilities.hooverEffect(playBtn, 1.05);
        Utilities.hooverEffect(rulesBtn, 1.05);
    }
}