package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

public class ChooseSocketRMIController {
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;

    private boolean useSocket;

    private Stage primaryStage;
    private Client client;
    private String[] server;
    private InsertServerOptionController insertServerOptionController;

    private Controller controller;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setServer(String[] server) {
        this.server = server;

    }

    public InsertServerOptionController getInsertServerOptionController() {
        return insertServerOptionController;
    }

    public void setConnection() {
        boolean isSocket = client.getView().askSocket();
        client.setController(this.server, isSocket);
        client.connection(isSocket);
    }

    public void isSocket() {
        setConnection();
        useSocket = true;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insert_ServerOption.fxml"));
            Parent root = loader.load();
            insertServerOptionController = loader.getController();
            insertServerOptionController.setPrimaryStage(primaryStage);
            insertServerOptionController.setClient(client);

            client.setInsertServerOptionController(insertServerOptionController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void isRMI() {
        setConnection();
        useSocket = false;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insert_ServerOption.fxml"));
            Parent root = loader.load();
            InsertIPPortController nextController = loader.getController();
            nextController.setPrimaryStage(primaryStage);
            nextController.setClient(client);
            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean useSocket() {
        return useSocket;
    }
}