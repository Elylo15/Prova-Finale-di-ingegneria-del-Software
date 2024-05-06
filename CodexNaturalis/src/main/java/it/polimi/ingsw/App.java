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

import java.rmi.RemoteException;

public class App {
    public static void main(String[] args) {
        Application.launch(application.class, args);
    }

    public static class application extends Application {
        @Override
        public void start(Stage stage) {
            Controller controller;
            Client client;
            View view;
            boolean isSocket;

            ViewGUI initialView = new ViewGUI();

            String[] server = initialView.askPortIP();

            if(initialView.askSocket()) {
                controller = new ControllerSocket(server[0], server[1]);
                isSocket = true;
            } else {
                try {
                    controller = new ControllerRMI(server[0], server[1]);
                    isSocket = false;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            if (initialView.askGUI())
                view = new ViewGUI();
            else {
                view = new ViewCLI();
            }

            client = new Client(view, controller);
            client.setIP(server[0]);
            client.setPort(server[1]);
            client.connection(isSocket);
            client.run();
        }
    }
}
