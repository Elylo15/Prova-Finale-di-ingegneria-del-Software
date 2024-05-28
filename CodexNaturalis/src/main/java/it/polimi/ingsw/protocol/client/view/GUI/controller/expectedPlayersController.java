package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class expectedPlayersController {


    @FXML
    private Button four;

    @FXML
    private Button three;

    @FXML
    private Button two;


    @FXML
    private void getNumber() {
        //serialize the number to send corresponding on the button clicked
        two.setOnAction(event -> {
            GUIMessages.writeToClient(2);
        });
        three.setOnAction(event -> {
            GUIMessages.writeToClient(3);
        });
        four.setOnAction(event -> {
            GUIMessages.writeToClient(4);
        });
    }

}
