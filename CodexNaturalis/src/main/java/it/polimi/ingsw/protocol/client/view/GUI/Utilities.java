package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;
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
     * Displays the cards placeholders that can be clicked to select the position where to place the card
     * in the playground and to place the objective in the objective area
     *
     * @param pane         the pane where to display the placeholders
     * @param layoutX      the x position of the placeholder
     * @param layoutY      the y position of the placeholder
     * @param fitHeight    the height of the placeholder
     * @param fitWidth     the width of the placeholder
     * @param eventHandler the event handler to call when the placeholder is clicked
     */
    public static void addClickablePlaceholder(Pane pane, double layoutX, double layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler) {
        Random random = new Random();
        int color = random.nextInt(5);

        Image image = switch (color) {
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

        // Check if image with the same position and size already exists
        boolean exists = pane.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .anyMatch(node -> {
                    ImageView imageView = (ImageView) node;
                    return (int) imageView.getLayoutX() == layoutX && (int) imageView.getLayoutY() == layoutY &&
                            (int) imageView.getFitWidth() == fitWidth && (int) imageView.getFitHeight() == fitHeight;
                });

        if (!exists) {
            ImageView imageView = new ImageView(image);
            imageView.setLayoutX(layoutX);
            imageView.setLayoutY(layoutY);
            imageView.setFitHeight(fitHeight);
            imageView.setFitWidth(fitWidth);
            imageView.getStyleClass().add("placeholder");
            imageView.setOnMouseClicked(eventHandler);
            pane.getChildren().add(imageView);
            fadeInTransition(imageView, 0.5);
        }
    }

    /**
     * Adds a new card to the pane if it doesn't exist already in the same position and with the same ID
     *
     * @param pane         the pane where to add the card
     * @param mainPane     the main pane
     * @param clickCounter the number of clicks (representing the player visualized)
     * @param selectedCard the card previously selected, or null
     * @param cardID       the ID of the card
     * @param front        if the card is front up
     * @param card         the card to add
     * @param layoutX      the x position of the card
     * @param layoutY      the y position of the card
     * @param fitHeight    the height of the card
     * @param fitWidth     the width of the card
     * @param eventHandler the event handler to call when the card is clicked
     */
    public static void addNewCardToPane(Pane pane, Pane mainPane, int clickCounter, ImageView selectedCard, int cardID, boolean front, Card card,
                                        double layoutX, double layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler) {
        ImageView existingCard = pane.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .map(node -> (ImageView) node)
                .filter(imageView -> {
                    Card existing = (Card) imageView.getUserData();
                    return existing != null && existing.getID() == cardID &&
                            (int) imageView.getLayoutX() == layoutX && (int) imageView.getLayoutY() == layoutY;
                })
                .findFirst()
                .orElse(null);

        if (existingCard == null) {
            // Card with the same ID and position doesn't exist, so remove the existing card and load the new one
            removeCardFromPosition(layoutX, layoutY, pane);
            ImageView newCard = createCardImageView(cardID, front, card, layoutX, layoutY, fitHeight, fitWidth);
            if (eventHandler != null) {
                newCard.setOnMouseClicked(eventHandler);
            }
            if (card instanceof ObjectiveCard && pane == mainPane && clickCounter != -1)
                hooverEffect(newCard, selectedCard, 1.05);
            else if (card instanceof ObjectiveCard || card instanceof PlaceableCard && pane == mainPane && clickCounter == -1)
                hooverEffect(newCard, selectedCard, 1.05);

            Platform.runLater(() -> {
                pane.getChildren().add(newCard);
                fadeInTransition(newCard, 1.0);
            });
            // If Card with the same ID and position exists, do nothing
        }
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
     * Removes a card from the pane.
     *
     * @param layoutX  the x position of the card
     * @param layoutY  the y position of the card
     * @param mainPane the main pane
     */
    public static void removeCardFromPosition(double layoutX, double layoutY, Pane mainPane) {
        Platform.runLater(() -> {
            List<Node> nodesToRemove = mainPane.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .filter(node -> {
                        ImageView imageView = (ImageView) node;
                        return imageView.getLayoutX() == layoutX && imageView.getLayoutY() == layoutY;
                    })
                    .toList();

            // Remove nodes after iteration
            for (Node node : nodesToRemove) {
                ImageView imageView = (ImageView) node;
//                imageView.toFront(); // Card to the front
                if (imageView.getUserData() instanceof Card)
                    fadeOutTransition(mainPane, imageView, 1);
                else
                    fadeOutTransition(mainPane, imageView, 0.5);
            }
        });
    }

    /**
     * When the mouse enters the imageView, it makes it bigger
     * When the mouse exits the imageView, it makes it smaller
     *
     * @param node         the node to apply the effect to
     * @param selectedCard the card previously selected
     * @param scale        the scale factor
     */
    public static void hooverEffect(Node node, ImageView selectedCard, double scale) {
        node.setOnMouseEntered(e -> makeBiggerTransition(node, scale));
        node.setOnMouseExited(e -> {
            if (selectedCard != node)
                makeSmallerTransition(node);
        });
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
     * Fades in the image
     *
     * @param image      the image to fade in
     * @param maxOpacity the maximum opacity
     */
    public static void fadeInTransition(ImageView image, double maxOpacity) {
        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(maxOpacity);

        fadeTransitionIn.play();
    }

    /**
     * Fades out the image and removes it
     *
     * @param pane       the pane where the image is
     * @param image      the image to fade out
     * @param maxOpacity the maximum opacity
     */
    public static void fadeOutTransition(Pane pane, ImageView image, double maxOpacity) {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionOut.setFromValue(maxOpacity);
        fadeTransitionOut.setToValue(0.0);

        fadeTransitionOut.setOnFinished(event -> pane.getChildren().remove(image));

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

}
