package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UnavailableNamesController {
    @FXML
    public Label unavailableNames;
    @FXML
    public TextField nameToChoose;
    @FXML
    public Button submitButton;

    private Stage primaryStage;
    private Client client;

    private AvailableColorsController availableColorsController;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AvailableColorsController getAvailableColorsController() {
        return availableColorsController;
    }

    public void setUp(String string){
        unavailableNames.setText("These are the names that are not available: " + string);
    }
    public String getName(){
        return nameToChoose.getText();
    }

    public void setUpNoNames(){
        unavailableNames.setText("All the nicknames are available");
    }


    @FXML
    private void handleSubmit() throws IOException {
        client.name();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/availableColors.fxml"));
            Parent root = loader.load();
            availableColorsController = loader.getController();
            availableColorsController.setPrimaryStage(primaryStage);
            availableColorsController.setClient(client);

            client.setAvailableColorsController(availableColorsController);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

