package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.view.GUI.*;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;


public class ViewGUI extends View {
    private Stage mainstage;


    public ViewGUI() {
        this.mainstage = new Stage(); //finestra
    }

    public void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root); //contenuto
            mainstage.setScene(scene);
            mainstage.setMaximized(true);
            if (!mainstage.isShowing()) {
                mainstage.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String[] askPortIP(){

        String[] server = new String[2];

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insert_IP_PORT.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InsertIPPortController controller = loader.getController();
        controller.setViewGUI(this);

        Scene scene = new Scene(root);
        mainstage.setScene(scene);
        mainstage.setMaximized(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }
        server[0] = controller.getIP();
        server[1] = controller.getPort();

        return server;
    }

        @Override
    public boolean askSocket() {// true = socket, false = rmi
        boolean useSocket = true;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Choose_Socket_RMI.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChooseSocketRMIController controller = loader.getController();
        controller.setViewGUI(this);

        Scene scene = new Scene(root);
        mainstage.setScene(scene);
        mainstage.setMaximized(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }

        useSocket = controller.useSocket();

        // Restituisci il valore di useSocket
        return useSocket;
    }


    //newMatch sei host di una nuova partita
    //joinMatch ti unisci ad una nuova partita
    //loadMatch carichi una partita da file
    //joinRunningMatch ti unisci ad una partita in corso

     @Override
    public serverOptionMessage serverOptions(serverOptionMessage message) {
        serverOptionMessage newMessage = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insert_ServerOption.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InsertServerOptionController controller = loader.getController();
        controller.setViewGUI(this);

        Scene scene = new Scene(root);
        mainstage.setScene(scene);
        mainstage.setMaximized(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }

        newMessage = controller.getServerOptionMessage();
        return newMessage;
    }

    @Override
    public void answerToOption(serverOptionResponseMessage message) {

    }

    @Override
    public void playerDisconnected() {
        loadFXML("/Disconnect.fxml");
    }

    @Override
    public void answerToConnection(connectionResponseMessage message) {

    }


    @Override
    public String unavailableNames(unavailableNamesMessage message) {
        String name = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/unavailableNames.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UnavailableNamesController controller = loader.getController();
        controller.setViewGUI(this);

        Scene scene = new Scene(root);
        mainstage.setScene(scene);
        mainstage.setMaximized(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }

        controller.setNames(message.toString());
        name = controller.chooseName();

        return name;
    }

    /**
     * visualize the response about the value entered
     * @param message
     */
    @Override
    public void answer(responseMessage message) {
    }

    @Override
    public String availableColors(availableColorsMessage message) {
        String color=null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/availableColors.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        availableColorsController controller = loader.getController();
        controller.setViewGUI(this);

        Scene scene = new Scene(root);
        mainstage.setScene(scene);
        mainstage.setMaximized(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }

        controller.setUp(message.toString());
        color = controller.chooseColor();
        return color;
    }

    /**
     * allow the user to choose how many players will play
     * @return number of expected players
     */
    @Override
    public int expectedPlayers() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/expectedPlayers.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        expectedPlayersController controller = loader.getController();
        controller.setViewGUI(this);

        Scene scene = new Scene(root);
        mainstage.setScene(scene);
        mainstage.setMaximized(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }

        //int num = controller.getNumber();

        //return num;
        return 0;
    }

    @Override
    public void updatePlayer(currentStateMessage message) {
        //load a mainGamePage with a backGround, scoreBoard etc.
        //load the playerCards in the correct positions and visualize the playerArea
        //and the general starting turn situation

        //get playerHand, load card images based on id in correct place, when selected, select the id
        //playerHand is an array of 3 cards, get id and place (placeHolder?) use imageBinder with Card

        //playerArea like a grid

        //scoreboard -> based on points, color goes to specific position (placeholder?)

        //decks -> load back of card on top, load front of card on table (imageBinder)

        //if notYourTurn button -> onClick back to your page
        //if notYourTurn back of currentPlayer hand and of objective cards
    }

    @Override
    public int placeStarter() {
        //animations and things happen in a mainGamePage
        //addEventListener -> onClick on border turn around the card
        //addEventListener -> onClick select, on click place where clicked and click place button(o doubleClick)
        return 0;
    }

    @Override
    public int chooseObjective(ArrayList<ObjectiveCard> objectives) {
        //animations and things happen in a mainGamePage
        //addEventListener -> onClick select and click button choose (or double click is definitive)
        return 0;
    }

    @Override
    public int[] placeCard() {
        //animations and things happen in a mainGamePage
        //addEventListener -> onClick on border turn around the card
        //addEventListener -> onClick select, on double click place where clicked and click place button (or double click is definitive)
        return new int[0];
    }

    @Override
    public int pickCard() {
        //animations and things happen in a mainGamePage
        //addEventListener -> onClick select and click button choose (or first click is definitive)
        //update what is seen (load back of card on top, load front of card on table)
        return 0;
    }

    @Override
    public void update(updatePlayerMessage update) {
        //load the mainGamePage with the general ending turn situation
    }


    @Override
    public String pickNameFA(unavailableNamesMessage message) {
        return "";
    }

    @Override
    public void endGame(declareWinnerMessage message) {
    }

}

