package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InsertServerOptionController {

    public Button new_match;
    public Button running_match;
    public Button load_game;

    private serverOptionMessage serverOptionMessage;

    private ViewGUI viewGUI;


    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }


    public void newMatchPressed() {
        serverOptionMessage = new serverOptionMessage(true, null, null, false, null);
        Stage stage = (Stage) new_match.getScene().getWindow();
        stage.close();
    }

    public void runningMatchPressed() {
       // serverOptionMessage = new serverOptionMessage(false,, serverOptionMessage.getNickname(), false, "");
        Stage stage = (Stage) running_match.getScene().getWindow();
        stage.close();
    }

    public void loadGamePressed() {
      //  serverOptionMessage = new serverOptionMessage(false, serverOptionMessage.getStartedMatchID(), serverOptionMessage.getNickname(), true, serverOptionMessage.getPathToLoad());
        Stage stage = (Stage) load_game.getScene().getWindow();
        stage.close();
    }


    public void setServerOptionMessage(serverOptionMessage message) {
        this.serverOptionMessage = message;
    }

    public serverOptionMessage getServerOptionMessage() {
        return serverOptionMessage;
    }
}
