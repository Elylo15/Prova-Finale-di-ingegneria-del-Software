package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.model.cards.Card;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Random;

public class Utilities {

    /**
     * Turns the card around when clicked
     *
     * @param event the mouse event
     */
    public static void turnAround(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        Card card = (Card) clickedCard.getUserData();

        if (card != null) {
            clickedCard.setDisable(true);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), clickedCard);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                ImageBinder imageBinder = new ImageBinder();
                Image newImage = imageBinder.getOppositeImage(card.getID(), card.isFront());
                clickedCard.setImage(newImage);

                card.setFront(!card.isFront());

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), clickedCard);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);

                fadeIn.setOnFinished(e2 -> clickedCard.setDisable(false));

                fadeIn.play();
            });

            fadeOut.play();
        }
    }

    /**
     * Rotates the image
     *
     * @param imageView the image to rotate
     * @param seconds   the duration of the rotation
     */
    public static void rotateEffect(ImageView imageView, int seconds) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(seconds), imageView);
        rotateTransition.setByAngle(360); // Rotate 360 degrees
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely
        rotateTransition.setAutoReverse(true); // Do not reverse the direction

        // Start the rotation
        rotateTransition.play();
    }

    /**
     * Rotates the arrow Image
     *
     * @param imageView the image to rotate
     * @param seconds   the duration of the rotation
     */
    public static void rotateEffectArrow(ImageView imageView, int seconds) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(seconds), imageView);
        rotateTransition.setByAngle(-720); // Rotate 500 degrees
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely
        rotateTransition.setAutoReverse(false); // Do not reverse the direction

        // Start the rotation
        rotateTransition.play();
    }

    /**
     * Bind the image to the card and set the card to the imageView
     *
     * @param imageView the imageView to set the card to
     * @param cardID    the ID of the card
     * @param front     if the card is front up
     * @param card      the card to set to the imageView
     */
    public static void setupCard(ImageView imageView, int cardID, boolean front, Card card) {
        ImageBinder imageBinder = new ImageBinder();
        ImageView cardImageView = imageBinder.bindImage(cardID, front);
        imageView.setImage(cardImageView.getImage());
        imageView.setUserData(card); // Store card ID and state
    }

    /**
     * Creates an imageView with the card and sets the card to the imageView
     *
     * @param cardID    the ID of the card
     * @param front     if the card is front up
     * @param card      the card to set to the imageView
     * @param layoutX   the x position of the imageView
     * @param layoutY   the y position of the imageView
     * @param fitHeight the height of the imageView
     * @param fitWidth  the width of the imageView
     * @return the imageView with the card set
     */
    public static ImageView createCardImageView(int cardID, boolean front, Card card, double layoutX, double layoutY, double fitHeight, double fitWidth) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPreserveRatio(true);


        setupCard(imageView, cardID, front, card);
        return imageView;
    }


    /**
     * When the mouse enters the imageView, it makes it bigger
     * When the mouse exits the imageView, it makes it smaller
     *
     * @param node  the node to apply the effect to
     * @param scale the scale factor
     */
    public static void hooverEffect(Node node, double scale) {
        node.setOnMouseEntered(e -> makeBiggerTransition(node, scale));
        node.setOnMouseExited(e -> makeSmallerTransition(node));
    }

    /**
     * Removes the hoover effect from the node
     *
     * @param node the node to remove the effect from
     */
    public static void removeHooverEffect(Node node) {
        node.setOnMouseEntered(null);
        node.setOnMouseExited(null);
    }

    /**
     * When the mouse enters the imageView, it makes it bigger
     *
     * @param node  the node to apply the effect to
     * @param scale the scale factor
     */
    public static void makeBiggerTransition(Node node, double scale) {
        ScaleTransition makeBigger = new ScaleTransition(Duration.millis(200), node);
        makeBigger.setToX(scale);
        makeBigger.setToY(scale);
        makeBigger.play();
    }

    /**
     * When the mouse exits the imageView, it makes it smaller
     *
     * @param node the node to apply the effect to
     */
    public static void makeSmallerTransition(Node node) {
        ScaleTransition makeSmaller = new ScaleTransition(Duration.millis(200), node);
        makeSmaller.setToX(1.0);
        makeSmaller.setToY(1.0);
        makeSmaller.play();
    }

    /**
     * Displays the popup with the image
     *
     * @param imagePath the path of the image
     * @param mainPane  the main pane
     */
    public static void showImagePopup(String imagePath, Pane mainPane) {
        Platform.runLater(() -> {
            Image imageLoad = new Image(Objects.requireNonNull(Utilities.class.getResourceAsStream(imagePath)));
            ImageView image = new ImageView(imageLoad);

            image.preserveRatioProperty().setValue(true);
            image.setFitWidth(646);
            image.setFitHeight(167);
            image.setLayoutX(957);
            image.setLayoutY(608);
            image.setOpacity(0.0); // Initially invisible

            mainPane.getChildren().add(image);

            fadeTransitionForPopUP(image, mainPane);
        });
    }

    /**
     * Gets the card from the position
     *
     * @param layoutX  the x position of the card
     * @param layoutY  the y position of the card
     * @param mainPane the main pane
     * @return the imageView of the card
     */
    public static ImageView getCardFromPosition(double layoutX, double layoutY, Pane mainPane) {
        ImageView imageView = null;
        for (Node node : mainPane.getChildren()) {
            if (node instanceof ImageView currentImageView) {
                if (currentImageView.getLayoutX() == layoutX && currentImageView.getLayoutY() == layoutY) {
                    imageView = currentImageView;
                    break;
                }
            }
        }
        return imageView;
    }

    /**
     * Fades in the image
     *
     * @param node       the node to fade in (image, or label)
     * @param maxOpacity the maximum opacity
     */
    public static void fadeInTransition(Node node, double maxOpacity) {
        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), node);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(maxOpacity);
        node.setDisable(false);

        fadeTransitionIn.play();
    }

    /**
     * Fades out the image and removes it
     *
     * @param pane            the pane where the image is
     * @param node            the node to fade out (image, or label)
     * @param maxOpacity      the maximum opacity
     * @param removeAfterFade if true, remove the node after the fade out
     */
    public static void fadeOutTransition(Pane pane, Node node, double maxOpacity, boolean removeAfterFade) {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), node);
        fadeTransitionOut.setFromValue(maxOpacity);
        fadeTransitionOut.setToValue(0.0);
        node.setDisable(true);
        if (removeAfterFade)
            fadeTransitionOut.setOnFinished(event -> pane.getChildren().remove(node));

        fadeTransitionOut.play();
    }

    /**
     * Fades in the image and then fades it out
     *
     * @param image    the image to fade in and out
     * @param mainPane the main pane
     */
    public static void fadeTransitionForPopUP(ImageView image, Pane mainPane) {
        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(1.0);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionOut.setFromValue(1.0);
        fadeTransitionOut.setToValue(0.0);

        fadeTransitionOut.setOnFinished(event -> mainPane.getChildren().remove(image));

        SequentialTransition sequentialTransition = new SequentialTransition(
                fadeTransitionIn,
                pauseTransition,
                fadeTransitionOut
        );

        sequentialTransition.play();
    }

    /**
     * Creates a clickable pane
     *
     * @param pane         the pane to add the clickable pane to
     * @param layoutX      the x position of the clickable pane
     * @param layoutY      the y position of the clickable pane
     * @param fitHeight    the height of the clickable pane
     * @param fitWidth     the width of the clickable pane
     * @param eventHandler the event handler to add to the clickable pane
     * @param image        the image of the clickable pane
     */
    public static void createClickablePane(Pane pane, double layoutX, double layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitHeight(fitHeight);
        imageView.setFitWidth(fitWidth);
        imageView.setOnMouseClicked(eventHandler);
        pane.getChildren().add(imageView);
        fadeInTransition(imageView, 0.5);
    }

    /**
     * Creates a random color image for the placeholder
     *
     * @return the image with the random color
     */
    public static Image randomColorForPlaceholder() {
        Random random = new Random();
        int color = random.nextInt(5);

        return switch (color) {
            case 0 ->
                    new Image(Objects.requireNonNull(Utilities.class.getResourceAsStream("/Images/Placeholders/blue.png")));
            case 1 ->
                    new Image(Objects.requireNonNull(Utilities.class.getResourceAsStream("/Images/Placeholders/green.png")));
            case 2 ->
                    new Image(Objects.requireNonNull(Utilities.class.getResourceAsStream("/Images/Placeholders/red.png")));
            case 3 ->
                    new Image(Objects.requireNonNull(Utilities.class.getResourceAsStream("/Images/Placeholders/yellow.png")));
            case 4 ->
                    new Image(Objects.requireNonNull(Utilities.class.getResourceAsStream("/Images/Placeholders/purple.png")));
            default -> null;
        };
    }


    /**
     * Plays the sound effect
     *
     * @param soundFile the sound file to play
     */
    public static void playSoundEffect(String soundFile) {
        Platform.runLater(() -> {
            String audioFile = Objects.requireNonNull(Utilities.class.getResource(soundFile)).toString();
            Media media = new Media(audioFile);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        });
    }

}
