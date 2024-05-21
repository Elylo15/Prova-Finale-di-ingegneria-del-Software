package it.polimi.ingsw.protocol.client.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class gamePageController {

    @FXML
    private VBox cardContainer;

    @FXML
    private void switchToGamePage() throws IOException {
        Parent gamePage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game_page.fxml")));
        Scene scene = new Scene(gamePage);
        Stage stage = (Stage) cardContainer.getScene().getWindow();
        stage.setScene(scene);
    }
}
