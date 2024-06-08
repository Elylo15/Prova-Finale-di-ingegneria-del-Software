package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class MainPageController {

    @FXML
    private BorderPane root;

    @FXML
    public ImageView playBtn;
    @FXML
    public ImageView rulesBtn;
    @FXML
    public Pane pane;
    @FXML
    public ImageView image;

    private int counter = 0;

    public void initialize() {
//        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
//            if (newScene != null) {
//                // Bind BorderPane size to the scene size
//                root.prefWidthProperty().bind(newScene.widthProperty());
//                root.prefHeightProperty().bind(newScene.heightProperty());
//
//                // Bind Pane size to the BorderPane size
//                pane.prefWidthProperty().bind(root.widthProperty());
//                pane.prefHeightProperty().bind(root.heightProperty());
//
//                // Bind the ImageView size to the Pane size to maintain the ratio
//                image.fitWidthProperty().bind(pane.widthProperty());
//                image.fitHeightProperty().bind(pane.heightProperty());
//
//                // Add listeners to adjust button positions and sizes dynamically
//                pane.widthProperty().addListener((widthObs, oldWidth, newWidth) -> adjustLayout());
//                pane.heightProperty().addListener((heightObs, oldHeight, newHeight) -> adjustLayout());
//
//                // Initial adjustment
//                adjustLayout();
//            }
//        });
    }

//    private void adjustLayout() {
//        double paneWidth = pane.getWidth();
//        double paneHeight = pane.getHeight();
//
//        // Set button sizes
//        playBtn.setPrefWidth(paneWidth * 210.0 / 1920.0);
//        playBtn.setPrefHeight(paneHeight * 0.0 / 1080.0);
//        rulesBtn.setPrefWidth(paneWidth * 210.0 / 1920.0);
//        rulesBtn.setPrefHeight(paneHeight * 65.0 / 1080.0);
//
//        // Set button positions
//        playBtn.setLayoutX(paneWidth * 128.0 / 1920.0);
//        playBtn.setLayoutY(paneHeight * 892.0 / 1080.0);
//        rulesBtn.setLayoutX(paneWidth * 1528.0 / 1920.0);
//        rulesBtn.setLayoutY(paneHeight * 900.0 / 1080.0);
//
//        // Adjust font sizes
//        playBtn.setFont(new Font("Curlz MT", paneHeight * 34.0 / 1080.0));
//        rulesBtn.setFont(new Font("Curlz MT", paneHeight * 34.0 / 1080.0));
//
//        playBtn.setTextAlignment(TextAlignment.CENTER);
//        rulesBtn.setTextAlignment(TextAlignment.CENTER);
//    }


    /**
     * This method is called when the player presses the play button.
     * It sends a message to start the game.
     */
    @FXML
    public void start() {
        GUIMessages.writeToClient(true);
    }

    /**
     * This method is called when the player presses the rules button.
     * It loads the rules that teaches to player how to play.
     */
    @FXML
    public void loadRules() {
        Platform.runLater(SceneManager::learToPlay);
    }


}