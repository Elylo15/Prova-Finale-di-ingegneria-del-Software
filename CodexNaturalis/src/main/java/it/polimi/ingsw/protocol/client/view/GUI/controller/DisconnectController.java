package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DisconnectController {

    @FXML
    private Button closeButton;

    private ViewGUI viewGUI;

    public void setTimeWindow() {
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished( event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        delay.play();
    }

    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }
}