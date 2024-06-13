package it.polimi.ingsw.protocol.client.view.GUI.controller;

import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.loadScene;

/**
 * SceneManager is a utility class that manages the different scenes in the JavaFX application.
 * It provides static methods to load and display different scenes.
 */
public class SceneManager {
    private static Stage primaryStage;

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
//        Media media = new Media(audioFile);
//        MediaPlayer backgroundMediaPlayer = new MediaPlayer(media);
//
////        backgroundMediaPlayer.setOnEndOfMedia(() -> {
//            backgroundMediaPlayer.seek(backgroundMediaPlayer.getStartTime()); // Restart from the beginning
////            backgroundMediaPlayer.play();
////        });
//
//        backgroundMediaPlayer.play();
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
            loadScene("/availableColors.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to choose the socket type
     */
    public static void Choose_Socket_RMI() {
        try {
            loadScene("/Choose_Socket_RMI.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for logging out
     */
    public static void disconnect() {
        try {
            loadScene("/disconnect.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for expected players
     */
    public static void expectedPlayers() {
        try {
            loadScene("/expectedPlayers.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for picking the name
     */
    public static void pickNameFA() {
        try {
            loadScene("/pickNameFA.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for inserting the IP
     */
    public static void InsertIP() {
        try {
            loadScene("/insertIP.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the game selection scene
     */
    public static void InsertServerOption() {
        try {
            loadScene("/insertServerOption.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for joining a match
     */
    public static void JoinMatch() {
        try {
            loadScene("/joinMatch.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for joining a running match
     */
    public static void JoinRunningMatch() {
        try {
            loadScene("/joinRunningMatch.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for loading a match
     */
    public static void LoadMatch() {
        try {
            loadScene("/loadMatch.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the opening scene
     */
    public static void MainView() {
        try {
            loadScene("/mainView.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }


    public static void learToPlay() {
        try {
            loadScene("/learnToPlay.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to place the StarterCard
     */
    public static void starterPage() {
        try {
            Scene scene = loadScene("/gamePage.fxml", primaryStage);
            scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/styles.css")).toExternalForm());
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to choose the name
     */
    public static void unavailableNames() {
        try {
           loadScene("/unavailableNames.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to wait for other players to connect
     */
    public static void waiting() {
        try {
            loadScene("/waiting.fxml", primaryStage);
        } catch (IOException ignore) {
        }
    }

}
