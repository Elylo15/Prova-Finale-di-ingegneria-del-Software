package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;
/**
 * This class is a controller for the background music.
 * It contains a media player that plays the background music.
 */
public class BackgroundMusic {
    private static BackgroundMusic instance;
    private MediaPlayer backgroundMediaPlayer;

    /**
     * Constructor for the background music
     */
    private BackgroundMusic() {
        initializeBackgroundMusic();
    }

    /**
     * Gets the instance of the background music
     */
    public static void getInstance() {
        if (instance == null) {
            instance = new BackgroundMusic();
        }
    }

    /**
     * Initializes the background music
     */
    private void initializeBackgroundMusic() {
        Platform.runLater(() -> {
            String audioFile = Objects.requireNonNull(BackgroundMusic.class.getResource("/Audio/song.mp3")).toString();
            Media media = new Media(audioFile);
            backgroundMediaPlayer = new MediaPlayer(media);

            backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repeat indefinitely

            backgroundMediaPlayer.setVolume(0.3);

            backgroundMediaPlayer.play();
        });
    }

}

