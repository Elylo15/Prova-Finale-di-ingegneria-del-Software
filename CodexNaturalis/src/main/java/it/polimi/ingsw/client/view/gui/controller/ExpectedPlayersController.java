package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class ExpectedPlayersController {
    @FXML
    private ImageView two;
    @FXML
    private ImageView three;
    @FXML
    private ImageView four;

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each image-button.
     * When a button is clicked, it sends the number of expected players to the client.
     */
    @FXML
    private void initialize() {

        Utilities.hooverEffect(two, 1.1);
        Utilities.hooverEffect(three, 1.1);
        Utilities.hooverEffect(four, 1.1);

        //serialize the number to send corresponding on the button clicked
        two.setOnMouseClicked(event -> GUIMessages.writeToClient(2));
        three.setOnMouseClicked(event -> GUIMessages.writeToClient(3));
        four.setOnMouseClicked(event -> GUIMessages.writeToClient(4));
    }

}
