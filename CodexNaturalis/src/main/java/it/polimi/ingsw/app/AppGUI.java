package it.polimi.ingsw.app;

import it.polimi.ingsw.protocol.client.ClientGUI;
import it.polimi.ingsw.protocol.client.view.GUI.controller.SceneManager;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class for the GUI version of the application
 */
public class AppGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);
        ViewGUI view = new ViewGUI();
        ClientGUI clientGUI = new ClientGUI(view);

        new Thread(clientGUI).start();
    }
}
