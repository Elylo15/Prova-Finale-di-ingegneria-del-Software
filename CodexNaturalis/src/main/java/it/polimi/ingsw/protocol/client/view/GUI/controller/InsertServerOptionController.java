package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * This class is a controller for the GUI scene where the user chooses the server option.
 * It contains four buttons: new_match, join_match, load_match, and join_running_match.
 * Each button corresponds to a different server option.
 * When a button is clicked, it performs the corresponding action and sends a message to the client.
 */
public class InsertServerOptionController {
    @FXML
    public Text new_match;// Button to start a new match
    @FXML
    public Text join_match;// Button to join a match
    @FXML
    public Text load_match;// Button to load a match
    @FXML
    public Text join_running_match;// Button to join a running match
    public ImageView rotate;

    private serverOptionMessage serverOptionMessage;// Message to be sent to the client

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When the new_match button is clicked, it creates a new serverOptionMessage and sends it to the client.
     * When the join_match button is clicked, it updates the scene to JoinMatch and sends the serverOptionMessage to the GUI.
     * When the load_match button is clicked, it updates the scene to LoadMatch and sends the serverOptionMessage to the GUI.
     * When the join_running_match button is clicked, it updates the scene to JoinRunningMatch and sends the serverOptionMessage to the GUI.
     */
    @FXML
    private void initialize() {
        this.serverOptionMessage = (serverOptionMessage) GUIMessages.readToGUI();

        initializeHoverEffect(new_match);
        initializeHoverEffect(join_match);
        initializeHoverEffect(load_match);
        initializeHoverEffect(join_running_match);

        rotateEffect(rotate);

        new_match.setOnMouseClicked(event -> {
            serverOptionMessage = new serverOptionMessage(true, null, null, false, null);
            GUIMessages.writeToClient(serverOptionMessage);
        });

        join_match.setOnMouseClicked(event -> {
            Platform.runLater(SceneManager::JoinMatch);
            GUIMessages.writeToGUI(serverOptionMessage);
        });

        load_match.setOnMouseClicked(event -> {
            Platform.runLater(SceneManager::LoadMatch);
            GUIMessages.writeToGUI(serverOptionMessage);
        });

        join_running_match.setOnMouseClicked(event -> {
            Platform.runLater(SceneManager::JoinRunningMatch);
            GUIMessages.writeToGUI(serverOptionMessage);
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

    public void rotateEffect(ImageView imageView){
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), imageView);
        rotateTransition.setByAngle(360); // Rotate 360 degrees
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely
        rotateTransition.setAutoReverse(true); // Do not reverse the direction

        // Start the rotation
        rotateTransition.play();
    }


}
