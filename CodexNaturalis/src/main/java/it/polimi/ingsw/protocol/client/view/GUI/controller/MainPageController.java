package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class MainPageController {
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

}