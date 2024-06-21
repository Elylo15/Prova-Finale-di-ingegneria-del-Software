package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.Utilities.*;

/**
 * This class is a controller for the gui scene where the player can start the game or learn how to play.
 * It contains two image-buttons: play and rules.
 * When the player presses the play image-button, it sends a message to start the game.
 * When the player presses the rules image-button, it loads the rules that teaches the player how to play.
 */
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

    /**
     * This method is called when the player presses the cranio image-button.
     */
    @FXML
    private void surprise() {
        if (devs.getOpacity() != 0) {
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

            devsP.onMouseClickedProperty().setValue(null);
            devsR.onMouseClickedProperty().setValue(null);
            devsG.onMouseClickedProperty().setValue(null);
            devsB.onMouseClickedProperty().setValue(null);

            fadeInTransition(playBtn, 1);
            fadeInTransition(rulesBtn, 1);
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

            devsP.onMouseClickedProperty().setValue(this::openBia);
            devsR.onMouseClickedProperty().setValue(this::openEly);
            devsG.onMouseClickedProperty().setValue(this::openAgnes);
            devsB.onMouseClickedProperty().setValue(this::openNico);

            fadeOutTransition(pane, playBtn, 1, false);
            fadeOutTransition(pane, rulesBtn, 1, false);
        }
    }


    @FXML
    public void openBia(MouseEvent event) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/purBia.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);
        imageView.setPreserveRatio(true);
        pane.getChildren().add(imageView);
        fadeInTransition(imageView, 1);

        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(e -> fadeOutTransition(pane, imageView, 1, true));
        pause.play();
    }

    @FXML
    public void openEly(MouseEvent event) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/redEly.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);
        pane.getChildren().add(imageView);
        fadeInTransition(imageView, 1);

        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(e -> fadeOutTransition(pane, imageView, 1, true));
        pause.play();
    }

    @FXML
    public void openNico(MouseEvent event) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/blueNico.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);
        pane.getChildren().add(imageView);
        fadeInTransition(imageView, 1);

        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(e -> fadeOutTransition(pane, imageView, 1, true));
        pause.play();
    }

    @FXML
    public void openAgnes(MouseEvent event) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/greenAgnes.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);
        imageView.setPreserveRatio(true);
        pane.getChildren().add(imageView);
        fadeInTransition(imageView, 1);

        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(e -> fadeOutTransition(pane, imageView, 1, true));
        pause.play();
    }

}