package it.polimi.ingsw.app;
import it.polimi.ingsw.protocol.client.view.GUI.controller.SceneManager;
import it.polimi.ingsw.protocol.client.ClientGUI;
import it.polimi.ingsw.protocol.client.view.View;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppGUI extends Application{
    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);
        ViewGUI view = new ViewGUI();
        ClientGUI clientGUI = new ClientGUI(view);

        new Thread(clientGUI).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
