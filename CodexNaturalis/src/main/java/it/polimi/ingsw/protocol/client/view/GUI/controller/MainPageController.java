package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.Objects;

public class MainPageController {

    @FXML
    public Button playBtn;
    @FXML
    public Button rulesBtn;
    @FXML
    public Pane pane;
    @FXML
    public ImageView image;

    private int counter = 0;

    public void initialize() {
        image.fitWidthProperty().bind(pane.widthProperty());
        image.fitHeightProperty().bind(pane.heightProperty());


        pane.widthProperty().addListener((obs, oldVal, newVal) -> adjustLayout());
        pane.heightProperty().addListener((obs, oldVal, newVal) -> adjustLayout());

        adjustLayout();
    }

    private void adjustLayout() {
        double paneWidth = pane.getWidth();
        double paneHeight = pane.getHeight();

        // Set button sizes
        playBtn.setPrefWidth(paneWidth * 210.0 / 1920.0);
        playBtn.setPrefHeight(paneHeight * 72.0 / 1080.0);
        rulesBtn.setPrefWidth(paneWidth * 210.0 / 1920.0);
        rulesBtn.setPrefHeight(paneHeight * 72.0 / 1080.0);

        // Set button positions
        playBtn.setLayoutX(paneWidth * 128.0 / 1920.0);
        playBtn.setLayoutY(paneHeight * 892.0 / 1080.0);
        rulesBtn.setLayoutX(paneWidth * 1528.0 / 1920.0);
        rulesBtn.setLayoutY(paneHeight * 900.0 / 1080.0);

        // Adjust font sizes
        playBtn.setFont(new Font("Curlz MT", paneHeight * 34.0 / 1080.0));
        rulesBtn.setFont(new Font("Curlz MT", paneHeight * 34.0 / 1080.0));
    }
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
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Rules/Rules1.png")));
        }if(counter == 2) {

            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Rules/Rules2.png")));
        }else if(counter == 3) {

            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Rules/Rules3.png")));
        }else if(counter == 4) {

            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Rules/Rules4.png")));
        }else if(counter == 5) {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Rules/Rules5.png")));
        } else if (counter == 0) {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/Background.png")));
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