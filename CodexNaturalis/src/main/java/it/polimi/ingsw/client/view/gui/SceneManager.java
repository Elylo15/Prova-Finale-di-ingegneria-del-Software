package it.polimi.ingsw.client.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


/**
 * SceneManager is a utility class that manages the different scenes in the JavaFX application.
 * It provides static methods to load and display different scenes.
 */
public class SceneManager {
    private static Stage primaryStage;
    private static final Pane root = new Pane();

    //viewGUI will use the methods of the class to visualize the scene needed

    /**
     * Sets the primary stage of the application.
     *
     * @param stage the primary stage
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void initializeBackgroundMusic() {
        String audioFile = Objects.requireNonNull(SceneManager.class.getResource("/Audio/song.mp3")).toString();
        Media media = new Media(audioFile);
        MediaPlayer backgroundMediaPlayer = new MediaPlayer(media);

//        backgroundMediaPlayer.setOnEndOfMedia(() -> {
        backgroundMediaPlayer.seek(backgroundMediaPlayer.getStartTime()); // Restart from the beginning
//            backgroundMediaPlayer.play();
//        });

        backgroundMediaPlayer.setVolume(0.3);

        backgroundMediaPlayer.play();
    }


    public static void playSoundEffect(String soundFile) {
        String audioFile = Objects.requireNonNull(SceneManager.class.getResource(soundFile)).toString();
        Media media = new Media(audioFile);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    /**
     * Loads and displays the scene for choosing available colors.
     */
    public static void availableColors() {
        try {
            new SceneSizeChangeListener("/availableColors.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to choose the socket type
     */
    public static void Choose_Socket_RMI() {
        try {
            new SceneSizeChangeListener("/choose_Socket_RMI.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for logging out
     */
    public static void disconnect() {
        try {
            new SceneSizeChangeListener("/disconnect.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for expected players
     */
    public static void expectedPlayers() {
        try {
            new SceneSizeChangeListener("/expectedPlayers.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for picking the name
     */
    public static void pickNameFA() {
        try {
            new SceneSizeChangeListener("/pickNameFA.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for inserting the IP
     */
    public static void InsertIP() {
        try {
            new SceneSizeChangeListener("/insertIP.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the game selection scene
     */
    public static void InsertServerOption() {
        try {
            new SceneSizeChangeListener("/insertServerOption.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for joining a match
     */
    public static void JoinMatch() {
        try {
            new SceneSizeChangeListener("/joinMatch.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for joining a running match
     */
    public static void JoinRunningMatch() {
        try {
            new SceneSizeChangeListener("/joinRunningMatch.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for loading a match
     */
    public static void LoadMatch() {
        try {
            new SceneSizeChangeListener("/loadMatch.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the opening scene
     */
    public static void MainView() {
        try {
            //TODO it may not work need to test
//            new SceneSizeChangeListener("/mainView.fxml", primaryStage);
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/mainView.fxml"));
            Pane controller = loader.load();
            controller.setPrefWidth(1920);
            controller.setPrefHeight(1080);
            root.getChildren().add(controller);

            Scale scale = new Scale(1, 1, 0, 0);
            scale.xProperty().bind(root.widthProperty().divide(1920));
            scale.yProperty().bind(root.heightProperty().divide(1080));
            root.getTransforms().add(scale);

            Scene scene = new Scene(controller, 1920, 1080);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();

            //DO I also need the Observer here? Or maybe the first time not?

        } catch (IOException ignore) {
        }
    }


    public static void learToPlay() {
        try {
            new SceneSizeChangeListener("/learnToPlay.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to place the StarterCard
     */
    public static void starterPage() {
        try {
            new SceneSizeChangeListener("/gamePage.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to choose the name
     */
    public static void unavailableNames() {
        try {
            new SceneSizeChangeListener("/unavailableNames.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to wait for other players to connect
     */
    public static void waiting() {
        try {
            new SceneSizeChangeListener("/waiting.fxml", primaryStage, root);
        } catch (IOException ignore) {
        }
    }

}
