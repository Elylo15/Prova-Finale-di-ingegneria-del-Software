package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class is a controller for the gui scene where the user chooses the server option.
 * It contains four buttons: new_match, join_match, load_match, and join_running_match.
 * Each button corresponds to a different server option.
 * When a button is clicked, it performs the corresponding action and sends a message to the client.
 */
public class InsertServerOptionController {
    @FXML
    private ImageView reload0;
    @FXML
    private ImageView reload1;
    @FXML
    private ImageView reload2;
    @FXML
    private ImageView reload3;
    @FXML
    private ImageView reload4;
    @FXML
    private ImageView reload5;
    @FXML
    private ImageView reload6;
    @FXML
    private ImageView reload7;
    @FXML
    private ImageView reload8;
    @FXML
    private ImageView reload9;
    @FXML
    private ImageView reload10;
    @FXML
    private ImageView reload11;
    @FXML
    private Text new_match;// Button to start a new match
    @FXML
    private Text join_match;// Button to join a match
    @FXML
    private Text load_match;// Button to load a match
    @FXML
    private Text join_running_match;// Button to join a running match
    private serverOptionMessage serverOptionMessage;// Message to be sent to the client

    /**
     * This method is called when the scene is loaded.
     * It sets the action for each button.
     * When the new_match button is clicked, it creates a new serverOptionMessage and sends it to the client.
     * When the join_match, join_running_match or load_match button is clicked, it updates the scene to JoinMatch and sends the serverOptionMessage to the gui.
     */
    @FXML
    private void initialize() throws NoSuchFieldException, IllegalAccessException {
        this.serverOptionMessage = (serverOptionMessage) GUIMessages.readToGUI();
        GUIMessages.writeToGUI(serverOptionMessage);

        join_match.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 96));
        load_match.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 96));
        join_running_match.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 96));
        new_match.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 96));

        Utilities.hooverEffect(new_match, 1.05);
        Utilities.hooverEffect(join_match, 1.05);
        Utilities.hooverEffect(load_match, 1.05);
        Utilities.hooverEffect(join_running_match, 1.05);
        Utilities.hooverEffect(reload0, 1.2);
        Utilities.hooverEffect(reload1, 1.2);
        Utilities.hooverEffect(reload2, 1.2);
        Utilities.hooverEffect(reload3, 1.2);
        Utilities.hooverEffect(reload4, 1.2);
        Utilities.hooverEffect(reload5, 1.2);
        Utilities.hooverEffect(reload6, 1.2);
        Utilities.hooverEffect(reload7, 1.2);
        Utilities.hooverEffect(reload8, 1.2);
        Utilities.hooverEffect(reload9, 1.2);
        Utilities.hooverEffect(reload10, 1.2);
        Utilities.hooverEffect(reload11, 1.2);

        Utilities.rotateEffect(reload0, 4);
        Utilities.rotateEffectArrow(reload1, 5);
        Utilities.rotateEffect(reload2, 2);
        Utilities.rotateEffectArrow(reload3, 3);
        Utilities.rotateEffect(reload4, 3);
        Utilities.rotateEffectArrow(reload5, 3);
        Utilities.rotateEffect(reload6, 2);
        Utilities.rotateEffectArrow(reload7, 5);
        Utilities.rotateEffectArrow(reload8, 3);
        Utilities.rotateEffect(reload9, 2);
        Utilities.rotateEffect(reload10, 4);
        Utilities.rotateEffectArrow(reload11, 4);


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
    public void reloadOptions() {
        GUIMessages.writeToClient(serverOptionMessage);
    }
}
