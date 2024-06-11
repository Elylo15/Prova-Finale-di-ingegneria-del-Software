package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.hooverEffect;


public class MainPageController implements Initializable {
    @FXML
    public ImageView playBtn;
    @FXML
    public ImageView rulesBtn;
    @FXML
    public Pane pane;
    @FXML
    public ImageView image;


    /**
     * This method is called when the player presses the play image-button.
     * It sends a message to start the game.
     */
    @FXML
    public void start() {
        GUIMessages.writeToClient(true);
    }

    /**
     * This method is called when the player presses the rules image-button.
     * It loads the rules that teaches the player how to play.
     */
    @FXML
    public void loadRules() {
        Platform.runLater(SceneManager::learToPlay);
    }

    /**
     * This method is called when the scene is loaded.
     * It sets the hoover effect for the play and rules image-buttons.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       hooverEffect(playBtn, 1.05);
       hooverEffect(rulesBtn, 1.05);
    }
}