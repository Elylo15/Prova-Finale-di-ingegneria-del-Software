package it.polimi.ingsw.protocol.client.view.GUI.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;

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
        Media media = new Media(audioFile);
        MediaPlayer backgroundMediaPlayer = new MediaPlayer(media);

        backgroundMediaPlayer.setOnEndOfMedia(() -> {
            backgroundMediaPlayer.seek(backgroundMediaPlayer.getStartTime()); // Restart from the beginning
            backgroundMediaPlayer.play();
        });

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/availableColors.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene to see message after color choice
     */
    public static void answer() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/answer.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene to choose the socket type
     */
    public static void Choose_Socket_RMI() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/choose_Socket_RMI.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene for logging out
     */
    public static void Disconnect() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/disconnect.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene for ending the game
     */
    public static void EndGame() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/endGame.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene for expected players
     */
    public static void expectedPlayers() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/expectedPlayers.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene for inserting the IP
     */
    public static void InsertIP() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/insertIP.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the game selection scene
     */
    public static void InsertServerOption() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/insertServerOption.fxml")));
            primaryStage.setScene(new Scene(root));
            if (!primaryStage.isFullScreen()) {
                primaryStage.setFullScreen(true);
            }
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene for joining a match
     */
    public static void JoinMatch() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/joinMatch.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene for joining a running match
     */
    public static void JoinRunningMatch() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/joinRunningMatch.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene for loading a match
     */
    public static void LoadMatch() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/loadMatch.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the opening scene
     */
    public static void MainView() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/mainView.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/styles.css")).toExternalForm());
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Load and view the scene to place the StarterCard
     */
    public static void starterPage() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/gamePage.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/styles.css")).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene to choose the name
     */
    public static void unavailableNames() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/unavailableNames.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load and view the scene to wait for other players to connect
     */
    public static void waiting() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/waiting.fxml")));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
