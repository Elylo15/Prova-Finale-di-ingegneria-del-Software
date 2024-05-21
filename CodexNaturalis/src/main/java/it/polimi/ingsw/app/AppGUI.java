package it.polimi.ingsw.app;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.GUI.MainViewController;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

public class AppGUI {

    public static void main(String[] args) {

        Application.launch(application.class, args);
    }

    public static class application extends Application {
        @Override
        public void start(Stage stage) {
            try {

                ViewGUI view = new ViewGUI();

                // Caricamento della pagina FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
                Parent root = loader.load();

                // Ottieni il controller e passa lo stage
                MainViewController controller = loader.getController();
                controller.setPrimaryStage(stage);

                // Imposta la scena e mostra lo stage
                stage.setScene(new Scene(root));
                stage.show();

                // Crea un'istanza della tua classe e chiama il metodo run dopo che la pagina è stata visualizzata
                Client client = new Client(view);
                controller.setClient(client);

                client.setMainViewController(controller); //mette il controller in client
                view.setClient(client); //il client viene impostato in ViewGUI

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
