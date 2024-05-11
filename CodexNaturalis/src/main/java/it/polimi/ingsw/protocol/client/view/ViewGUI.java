package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.GUI.InsertIPPortController;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionResponseMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


public class ViewGUI extends View {
    private final Stage stage;
    private Scene scene;

    public ViewGUI() {
        this.stage = new Stage();
    }

    @Override
    public void playerDisconnected() {

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

    @Override
    public void updatePlayer(currentStateMessage message) {

    }

    @Override
    public void answerToConnection(connectionResponseMessage message) {

    }

    @Override
    public serverOptionMessage serverOptions(serverOptionMessage message) {
        return null;
    }

    @Override
    public void answerToOption(serverOptionResponseMessage message) {

    }

    @Override
    public String unavailableNames(unavailableNamesMessage message) {
        return "";
    }

    @Override
    public void answer(responseMessage message) {

    }

    @Override
    public String availableColors(availableColorsMessage message) {
        return "";
    }

    @Override
    public int placeStarter() {
        return 0;
    }

    @Override
    public int expectedPlayers() {
        return 0;
    }

    @Override
    public int chooseObjective() {
        return 0;
    }

    @Override
    public int[] placeCard() {
        return new int[0];
    }

    @Override
    public void update(updatePlayerMessage update) {

    }

    @Override
    public int pickCard() {
        return 0;
    }

    @Override
    public void endGame(declareWinnerMessage message) {

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

    @Override
    public String[] askPortIP(){
        String[] server = new String[2];
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/GUI/Insert_IP_PORT.fxml"));
            Parent root = loader.load();

            InsertIPPortController controller = loader.getController();
            controller.setViewGUI(this);

            server[0] = controller.getIp();
            server[1] = controller.getPort();

        }catch (IOException e){
            e.printStackTrace();
        }
        return server;
    }

    @Override
    public void showPlayerHand(PlayerHand playerHand) {

    }

    @Override
    public void showCommonArea(CommonArea commonArea) {

    }
}
