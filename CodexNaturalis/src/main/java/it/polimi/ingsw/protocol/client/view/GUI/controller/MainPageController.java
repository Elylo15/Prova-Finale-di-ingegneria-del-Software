package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MainPageController {

    @FXML
    public Button playBtn;
    @FXML
    public Button rulesBtn;
    private int counter = 0;

    /**
     * This method is called when the player presses the play button.
     * It sends a message to start the game.
     */
    @FXML
    public void start(MouseEvent mouseEvent) {
        GUIMessages.writeToClient(true);
    }

    /**
     * This method is called when the player presses the rules button.
     * It loads the rules images, and the player can see them by pressing the button again.
     */
    @FXML
    public void loadRules(MouseEvent mouseEvent) {
        playBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::start);
        playBtn.setVisible(false);

        rulesBtn.setText("Next");
        rulesBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::loadRules);
        rulesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::nextImg);

        new ImageView().setImage(new Image("img/rules0.png"));
    }

    /**
     * This method is called when the player presses the next button.
     * It loads the next rules image, and after the last one, it goes back to the main page.
     */
    public void nextImg(MouseEvent mouseEvent) {
        counter++;
        if(counter == 1)        //TODO set the correct images and positions (and btn font)
            new ImageView().setImage(new Image("img/rules1.png"));
        else if(counter == 2)
            new ImageView().setImage(new Image("img/rules2.png"));
        else if(counter == 3)
            new ImageView().setImage(new Image("img/rules3.png"));
        else if(counter == 4) {
            new ImageView().setImage(new Image("img/mainPage.png"));
            counter = 0;
            playBtn.setVisible(true);
            playBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::start);
            rulesBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::nextImg);
            rulesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::loadRules);
        }
    }
}
