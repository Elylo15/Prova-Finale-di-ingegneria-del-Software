package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ExpectedPlayersController {

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button four;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When a button is clicked, it sends the number of expected players to the client.
     */
    @FXML
    private void initialize() {
        //serialize the number to send corresponding on the button clicked
        two.setOnAction(event -> GUIMessages.writeToClient(2));
        three.setOnAction(event -> GUIMessages.writeToClient(3));
        four.setOnAction(event -> GUIMessages.writeToClient(4));
    }

}
