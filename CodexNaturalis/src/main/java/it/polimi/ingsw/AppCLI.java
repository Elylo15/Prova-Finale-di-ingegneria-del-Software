package it.polimi.ingsw;

import it.polimi.ingsw.protocol.client.Client;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.client.view.View;
import it.polimi.ingsw.protocol.client.view.ViewCLI;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class AppCLI {
    public static void main(String[] args) {
        Client client;
        Controller controller = null;
        ViewCLI view = new ViewCLI();
        boolean isSocket = false;

        String[] server = view.askPortIP();

        if (view.askSocket()) {
            try {
                controller = new ControllerSocket(server[0], server[1]);
                isSocket = true;
            } catch (Exception e) {
                view.playerDisconnected();
            }
        } else {
            try {
                controller = new ControllerRMI(server[0], server[1]);
            } catch (Exception e) {
                view.playerDisconnected();
            }
        }

        client = new Client(view, controller);
        client.setIP(server[0]);
        client.setPort(server[1]);
        client.connection(isSocket);
        client.run();
    }
}
