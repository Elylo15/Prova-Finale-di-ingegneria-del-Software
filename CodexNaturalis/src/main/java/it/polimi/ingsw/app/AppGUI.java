package it.polimi.ingsw.app;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.GUI.controller.SceneManager;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main class for the GUI version of the application
 */
public class AppGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);
        //Font customFont = Font.loadFont(SceneManager.class.getResourceAsStream("/Fonts/FantasyScript.ttf"), 20);

        SceneManager.initializeBackgroundMusic();

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (!primaryStage.isFullScreen()) {
                primaryStage.setFullScreen(true);
            }
        });

        ViewGUI view = new ViewGUI();
        Client client = new Client(view);
        new Thread(client).start();
    }
}
