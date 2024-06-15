package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.Utilities.*;


public class MainPageController implements Initializable {
    @FXML
    public Pane pane;
    @FXML
    public ImageView redRotate;
    @FXML
    public ImageView blueRotate;
    @FXML
    public ImageView greenRotate;
    @FXML
    public ImageView purRotate;
    @FXML
    public ImageView devsR;
    @FXML
    public ImageView devsP;
    @FXML
    public ImageView devsG;
    @FXML
    public ImageView devsB;
    @FXML
    public ImageView devs;
    @FXML
    public ImageView cranio;
    @FXML
    public ImageView bia;
    @FXML
    public ImageView agnes;
    @FXML
    public ImageView nico;
    @FXML
    public ImageView ely;
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
        rotateEffect(blueRotate, 3);
        rotateEffect(redRotate, 3);
        rotateEffect(greenRotate, 3);
        rotateEffect(purRotate, 3);

        playBtn.toFront();
        rulesBtn.toFront();
        devsP.toFront();

        hooverEffect(cranio, 1.05);
        hooverEffect(playBtn, 1.05);
        hooverEffect(rulesBtn, 1.05);
    }

    @FXML
    private void surprise() {
        if(devs.getOpacity() != 0 ) {
            fadeOutTransition(pane, devs, 1, false);
            fadeOutTransition(pane, bia, 1, false);
            fadeOutTransition(pane, agnes, 1, false);
            fadeOutTransition(pane, nico, 1, false);
            fadeOutTransition(pane, ely, 1, false);
            removeHooverEffect(devsR);
            removeHooverEffect(devsP);
            removeHooverEffect(devsG);
            removeHooverEffect(devsB);
            removeHooverEffect(bia);
            removeHooverEffect(agnes);
            removeHooverEffect(nico);
            removeHooverEffect(ely);
            rotateEffect(blueRotate, 3);
            rotateEffect(redRotate, 3);
            rotateEffect(greenRotate, 3);
            rotateEffect(purRotate, 3);

            fadeInTransition(playBtn, 1);
            fadeInTransition(rulesBtn, 1);
            playBtn.setDisable(false);
            rulesBtn.setDisable(false);
        } else {
            fadeInTransition(devs, 1);
            fadeInTransition(bia, 1);
            fadeInTransition(agnes, 1);
            fadeInTransition(nico, 1);
            fadeInTransition(ely, 1);
            hooverEffect(devsR, 1.05);
            hooverEffect(devsP, 1.05);
            hooverEffect(devsG, 1.05);
            hooverEffect(devsB, 1.05);
            hooverEffect(bia, 1.05);
            hooverEffect(agnes, 1.05);
            hooverEffect(nico, 1.05);
            hooverEffect(ely, 1.05);

           fadeOutTransition(pane, playBtn, 1, false);
           fadeOutTransition(pane, rulesBtn, 1, false);
           playBtn.setDisable(true);
           rulesBtn.setDisable(true);
        }
    }
}