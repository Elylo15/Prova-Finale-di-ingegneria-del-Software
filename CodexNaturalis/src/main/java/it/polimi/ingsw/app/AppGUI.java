package it.polimi.ingsw.app;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.ViewGUI;
import it.polimi.ingsw.client.view.gui.BackgroundMusic;
import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Main class for the gui version of the application
 */
public class AppGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);

        BackgroundMusic.getInstance();

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (!primaryStage.isFullScreen())
                primaryStage.setFullScreen(true);
        });

        ViewGUI view = new ViewGUI();
        Client client = new Client(view);
        new Thread(client).start();
    }

    /**
     * Stops the application
     */
    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

}
