package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class InsertIPPortController {
    public TextField ip_server;
    public TextField port_protocol;
    public Button submit_ip;

    private Stage primaryStage;
    private Client client;
    private ChooseSocketRMIController chooseSocketRMIController;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ChooseSocketRMIController getChooseSocketRMIController() {
        return chooseSocketRMIController;
    }

    public String setIPPORT() {
        String server = client.getView().askIP();
        client.setIP(server);
        //setPort(server[1]);
        return server;
    }


    @FXML
    private void loadNextPage()  {
        String server = setIPPORT();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Choose_Socket_RMI.fxml"));
            Parent root = loader.load();

            chooseSocketRMIController = loader.getController();
            chooseSocketRMIController.setPrimaryStage(primaryStage);

            chooseSocketRMIController.setClient(client);
            chooseSocketRMIController.setServer(server);

            client.setChooseSocketRMIController(chooseSocketRMIController);

            primaryStage.getScene().setRoot(root);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIP(){
        return ip_server.getText();
    }

    public String getPort(){
        return port_protocol.getText();
    }


}

