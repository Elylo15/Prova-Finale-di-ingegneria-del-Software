package it.polimi.ingsw.protocol.client.view.GUI.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.resize;

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
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/availableColors.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to choose the socket type
     */
    public static void Choose_Socket_RMI() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/choose_Socket_RMI.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for logging out
     */
    public static void disconnect() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/disconnect.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for expected players
     */
    public static void expectedPlayers() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/expectedPlayers.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for picking the name
     */
    public static void pickNameFA() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/pickNameFA.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for inserting the IP
     */
    public static void InsertIP() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/insertIP.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the game selection scene
     */
    public static void InsertServerOption() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/insertServerOption.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for joining a match
     */
    public static void JoinMatch() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/joinMatch.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for joining a running match
     */
    public static void JoinRunningMatch() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/joinRunningMatch.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene for loading a match
     */
    public static void LoadMatch() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/loadMatch.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the opening scene
     */
    public static void MainView() {
        try {

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/mainView.fxml"));
            Pane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/styles.css")).toExternalForm());
            primaryStage.show();

            resize(scene, root);

        } catch (IOException ignore) {
        }
    }



    public static void learToPlay() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/learnToPlay.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }


    /**
     * Load and view the scene to place the StarterCard
     */
    public static void starterPage() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/gamePage.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/styles.css")).toExternalForm());
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to choose the name
     */
    public static void unavailableNames() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/unavailableNames.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

    /**
     * Load and view the scene to wait for other players to connect
     */
    public static void waiting() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/waiting.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            resize(scene, root);
        } catch (IOException ignore) {
        }
    }

}
