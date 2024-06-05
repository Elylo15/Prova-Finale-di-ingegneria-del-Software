package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Objects;

public class AvailableColorsController {
    @FXML
    public Pane mainPane;
    availableColorsMessage message;

    private final double[][] positions = {
              {496.0, 543.0},  {922.0, 543.0}, {-18.0, 543.0},  {-1434.0, 543.0},
    };

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When a button is clicked, it sends the color to the client.
     *///TODO MAKE THEM APPEAR GRADUALLY
    @FXML
    public void initialize() {
        this.message = (availableColorsMessage) GUIMessages.readToGUI();

        for (int i = 0; i < message.getColors().size(); i++) {
            double fitHeight = 540;
            double fitWidth = 701;
            if (message.getColors().get(i).equalsIgnoreCase("purple")) {
                Image image = new Image(Objects.requireNonNull(SceneManager.class.getResource("/Images/Colors/purple.png")).toString());
                ImageView imageView = new ImageView(image);
                placeInFirstFree(imageView, fitWidth, fitHeight);
                imageView.setOnMouseClicked(event -> GUIMessages.writeToClient("purple"));
            } else if (message.getColors().get(i).equalsIgnoreCase("green")) {
                Image image = new Image(Objects.requireNonNull(SceneManager.class.getResource("/Images/Colors/green.png")).toString());
                ImageView imageView = new ImageView(image);
                placeInFirstFree(imageView, fitWidth, fitHeight);
                imageView.setOnMouseClicked(event -> GUIMessages.writeToClient("green"));
            } else if (message.getColors().get(i).equalsIgnoreCase("blue")) {
                Image image = new Image(Objects.requireNonNull(SceneManager.class.getResource("/Images/Colors/blue.png")).toString());
                ImageView imageView = new ImageView(image);
                placeInFirstFree(imageView, fitWidth, fitHeight);
                imageView.setOnMouseClicked(event -> GUIMessages.writeToClient("blue"));
            } else if (message.getColors().get(i).equalsIgnoreCase("red")) {
                Image image = new Image(Objects.requireNonNull(SceneManager.class.getResource("/Images/Colors/red.png")).toString());
                ImageView imageView = new ImageView(image);
                placeInFirstFree(imageView, fitWidth, fitHeight);
                imageView.setOnMouseClicked(event -> GUIMessages.writeToClient("red"));
            }
        }
    }

    private void placeInFirstFree(ImageView imageView, double fitWidth, double fitHeight) {
        double[] positions = getFirstFreeLayout();
        imageView.setLayoutX(positions[0]);
        imageView.setLayoutY(positions[1]);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.preserveRatioProperty().setValue(true);
        mainPane.getChildren().add(imageView);
        initializeHoverEffect(imageView);
    }

    @FXML
    private void onHover(MouseEvent event) {
        Node source = (Node) event.getSource();

        ScaleTransition enlargeTransition = new ScaleTransition(Duration.millis(200), source);
        enlargeTransition.setToX(1.1);
        enlargeTransition.setToY(1.1);

        ScaleTransition shrinkTransition = new ScaleTransition(Duration.millis(200), source);
        shrinkTransition.setToX(1.0);
        shrinkTransition.setToY(1.0);

        source.setOnMouseEntered(e -> enlargeTransition.playFromStart());
        source.setOnMouseExited(e -> shrinkTransition.playFromStart());
    }


    public void initializeHoverEffect(Node node) {
        node.setOnMouseEntered(this::onHover);
        node.setOnMouseExited(this::onHover);
    }

    public double[] getFirstFreeLayout() {
        for (double[] position : positions) {
            if (isFree(position[0], position[1])) {
                return position;
            }
        }
        return null;
    }


    public boolean isFree(double x, double y) {
        for (Node node : mainPane.getChildren()) {
            if (node.getLayoutX() == x && node.getLayoutY() == y) {
                return false;
            }
        }
        return true;
    }

}
