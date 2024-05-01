package it.polimi.ingsw.protocol.client.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class ViewGUI extends View {
    private Stage stage;
    private Scene scene;

    public ViewGUI(Stage stage) {
        this.stage = stage;
    }

    public boolean askSocket(){
        AtomicBoolean useSocket = new AtomicBoolean(false);
        Button socketButton = new Button("Use Sockets");
        Button rmiButton = new Button("Use RMI");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(socketButton, rmiButton);

        Scene scene = new Scene(root, 300, 200);

        stage.setTitle("Choose Connection Method");
        stage.setScene(scene);
        stage.show();

        socketButton.setOnAction(e -> {
            useSocket.set(true);
            stage.close();

        });
        rmiButton.setOnAction(e -> {
            useSocket.set(false);
            stage.close();
        });
        return useSocket.get();
    }

    public boolean askGUI(){
        AtomicBoolean useGUI = new AtomicBoolean(false);
        Button guiButton = new Button("Use Graphic Interface");
        Button cliButton = new Button("Use CommandLine Interface");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(guiButton, cliButton);

        Scene scene = new Scene(root, 300, 200);

        stage.setTitle("Choose Connection Method");
        stage.setScene(scene);
        stage.show();

        guiButton.setOnAction(e -> {
            useGUI.set(true);
            stage.close();

        });
        cliButton.setOnAction(e -> {
            useGUI.set(false);
            stage.close();
        });
        return useGUI.get();
    }
}
