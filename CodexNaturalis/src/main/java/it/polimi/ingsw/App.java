package it.polimi.ingsw;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class App {
    public static void main(String[] args) {
        Application.launch(application.class, args);
    }

    public static class application extends Application {
        @Override
        public void start(Stage stage) {
            ViewGUI view = new ViewGUI();
            Client client = new Client(view);
            client.run();
        }
    }
}
