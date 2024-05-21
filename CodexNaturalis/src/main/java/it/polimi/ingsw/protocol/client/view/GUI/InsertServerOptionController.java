package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InsertServerOptionController {
    @FXML
    public Button new_match;
    @FXML
    public Button join_match;
    @FXML
    public Button load_match;
    @FXML
    public Button join_running_match;

    private serverOptionMessage serverOptionMessage;

    private Stage primaryStage;
    private Client client;

    private UnavailableNamesController unavailableNamesController;
    private JoinMatchController joinMatchController;
    private LoadMatchController loadMatchController;
    private JoinRunningMatchController joinRunningMatchController;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public UnavailableNamesController getUnavailableNamesController() {
        return unavailableNamesController;
    }

    public JoinMatchController getJoinMatchController() {
        return joinMatchController;
    }

    public LoadMatchController getLoadMatchController() {
        return loadMatchController;
    }

    public JoinRunningMatchController getJoinRunningMatchController() {
        return joinRunningMatchController;
    }


    public void newMatchPressed() {
        client.serverOptions();
        serverOptionMessage = new serverOptionMessage(true, null, null, false, null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/unvailableNames.fxml"));
            Parent root = loader.load();
            unavailableNamesController = loader.getController();
            unavailableNamesController.setPrimaryStage(primaryStage);
            unavailableNamesController.setClient(client);

            client.setUnavailableNamesController(unavailableNamesController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void JoinMatchPressed() {
        client.serverOptions();
        //da modificare
        serverOptionMessage = new serverOptionMessage(true, null, null, false, null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JoinMatch.fxml"));
            Parent root = loader.load();
            joinMatchController = loader.getController();
            joinMatchController.setPrimaryStage(primaryStage);
            joinMatchController.setClient(client);

            client.setJoinMatchController(joinMatchController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMatchPressed() {
        client.serverOptions();
        //da modficare
        serverOptionMessage = new serverOptionMessage(true, null, null, false, null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadMatch.fxml"));
            Parent root = loader.load();
            loadMatchController = loader.getController();
            loadMatchController.setPrimaryStage(primaryStage);
            loadMatchController.setClient(client);

            client.setLoadMatchController(loadMatchController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void JoinRunningMatchPressed() {
        client.serverOptions();
        //da modificare
        serverOptionMessage = new serverOptionMessage(true, null, null, false, null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JoinRunningMatch.fxml"));
            Parent root = loader.load();
            joinRunningMatchController = loader.getController();
            joinRunningMatchController.setPrimaryStage(primaryStage);
            joinRunningMatchController.setClient(client);

            client.setJoinRunningMatchController(joinRunningMatchController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
      }

    public serverOptionMessage getServerOptionMessage() {
        return serverOptionMessage;
    }
}
