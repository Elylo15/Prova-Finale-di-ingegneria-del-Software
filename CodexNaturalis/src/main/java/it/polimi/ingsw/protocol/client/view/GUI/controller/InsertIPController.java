package it.polimi.ingsw.protocol.client.view.GUI.controller;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;


import it.polimi.ingsw.protocol.client.ClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InsertIPController {

    @FXML
    private TextField ip;

    @FXML
    private Button submit_ip;


    @FXML
    private void initialize() {
        submit_ip.setOnAction(event -> {
            System.out.println("sono nel submit");
            GUIMessages.writeToClient(ip.getText());// Send the IP to the client
            System.out.println("tasto premuto");
        });
        System.out.println("sono inizializzato");

//        try{
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("dopo il sleep");
//        GUIMessages.writeToClient("ciao");
//        System.out.println("dopo il ciao ");
    }
}
