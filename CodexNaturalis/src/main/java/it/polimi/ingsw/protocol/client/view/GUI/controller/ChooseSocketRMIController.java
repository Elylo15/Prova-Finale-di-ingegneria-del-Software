package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * This class is a controller for the GUI scene where the user chooses between Socket and RMI for communication.
 * It contains two buttons, one for Socket and one for RMI.
 * When a button is clicked, it sends the user's choice to the client and prints the choice to the console.
 */
public class ChooseSocketRMIController {
    @FXML
    private ImageView socketButton;
    @FXML
    private ImageView rmiButton;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When the Socket button is clicked, it sends true to the client.
     * When the RMI button is clicked, it sends false to the client.
     */
    @FXML
    private void initialize() {
        initializeHoverEffect(socketButton);
        initializeHoverEffect(rmiButton);

        socketButton.setOnMouseClicked(event -> {
            //System.out.println("Socket");
            GUIMessages.writeToClient(true);
        });

        rmiButton.setOnMouseClicked(event -> {
            //System.out.println("RMI");
            GUIMessages.writeToClient(false);
        });
    }

    @FXML
    private void onHover(MouseEvent event) {
        Node source = (Node) event.getSource();

        ScaleTransition enlargeTransition = new ScaleTransition(Duration.millis(200), source);
        enlargeTransition.setToX(1.05);
        enlargeTransition.setToY(1.05);

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