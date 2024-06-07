package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class ExpectedPlayersController {

    @FXML
    private ImageView two;

    @FXML
    private ImageView three;

    @FXML
    private ImageView four;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When a button is clicked, it sends the number of expected players to the client.
     */
    @FXML
    private void initialize() {

        initializeHoverEffect(two);
        initializeHoverEffect(three);
        initializeHoverEffect(four);

        //serialize the number to send corresponding on the button clicked
        two.setOnMouseClicked(event -> GUIMessages.writeToClient(2));
        three.setOnMouseClicked(event -> GUIMessages.writeToClient(3));
        four.setOnMouseClicked(event -> GUIMessages.writeToClient(4));
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

}
