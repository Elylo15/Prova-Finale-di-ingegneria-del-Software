package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class AvailableColorsController {
    availableColorsMessage message;

    @FXML
    ImageView blue;
    @FXML
    ImageView green;
    @FXML
    ImageView red;
    @FXML
    ImageView purple;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When a button is clicked, it sends the color to the client.
     */
    @FXML
    public void initialize() {
        red.setVisible(false);
        red.setDisable(true);
        green.setVisible(false);
        green.setDisable(true);
        blue.setVisible(false);
        blue.setDisable(true);
        purple.setVisible(false);
        purple.setDisable(true);

        this.message = (availableColorsMessage) GUIMessages.readToGUI();

        for (int i = 0; i < message.getColors().size(); i++) {
            if (message.getColors().get(i).equalsIgnoreCase("red")) {
                red.setVisible(true);
                red.setDisable(false);
                initializeHoverEffect(red);
            } else if (message.getColors().get(i).equalsIgnoreCase("green")) {
                green.setVisible(true);
                green.setDisable(false);
                initializeHoverEffect(green);
            } else if (message.getColors().get(i).equalsIgnoreCase("blue")) {
                blue.setVisible(true);
                blue.setDisable(false);
                initializeHoverEffect(blue);
            } else if (message.getColors().get(i).equalsIgnoreCase("purple")) {
                purple.setVisible(true);
                purple.setDisable(false);
                initializeHoverEffect(purple);
            }
        }
        //send the result
        red.setOnMouseClicked(event -> GUIMessages.writeToClient("red"));
        purple.setOnMouseClicked(event -> GUIMessages.writeToClient("purple"));
        green.setOnMouseClicked(event -> GUIMessages.writeToClient("green"));
        blue.setOnMouseClicked(event -> GUIMessages.writeToClient("blue"));
    }

    @FXML
    private void onHover(MouseEvent event) {
        Node source = (Node) event.getSource();

        ScaleTransition enlargeTransition = new ScaleTransition(Duration.millis(200), source);
        enlargeTransition.setToX(1.2);
        enlargeTransition.setToY(1.2);

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

}
