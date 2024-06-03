package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class MainPageController {

    @FXML
    public Button playBtn;
    @FXML
    public Button rulesBtn;
    @FXML
    public Pane pane;

    private int counter = 0;

    /**
     * This method is called when the player presses the play button.
     * It sends a message to start the game.
     */
    @FXML
    public void start() {
        GUIMessages.writeToClient(true);
    }

    /**
     * This method is called when the player presses the rules button.
     * It loads the rules images, and the player can see them by pressing the button again.
     */
    //TODO create more quality images
    @FXML
    public void loadRules() {
        Image img = null;

        if(counter== 5)
            counter = 0;
        else
            counter++;

        if(counter == 1 ) {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Rules/Rules1.png")));
        }if(counter == 2) {

            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Rules/Rules2.png")));
        }else if(counter == 3) {

            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Rules/Rules3.png")));
        }else if(counter == 4) {

            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Rules/Rules4.png")));
        }else if(counter == 5) {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Rules/Rules5.png")));
        } else if (counter == 0) {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Background/Background.png")));
        }

        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);
        imageView.setPreserveRatio(true);

        pane.getChildren().clear();
        pane.getChildren().add(imageView);

        pane.getChildren().add(rulesBtn);

        if(counter == 0) {
            rulesBtn.setText("Read Rules");
            pane.getChildren().add(playBtn);
        } else
            rulesBtn.setText("Next");

    }

}