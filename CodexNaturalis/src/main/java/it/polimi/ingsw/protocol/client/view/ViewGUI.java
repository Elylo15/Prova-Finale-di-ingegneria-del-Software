package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.view.GUI.ChooseSocketRMIController;
import it.polimi.ingsw.protocol.client.view.GUI.InsertIPPortController;
import it.polimi.ingsw.protocol.client.view.GUI.InsertServerOptionController;
import it.polimi.ingsw.protocol.client.view.GUI.expectedPlayersController;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;


public class ViewGUI extends View {
    private final Stage stage;
    private Scene scene;


    public ViewGUI() {
        this.stage = new Stage();
    }

    @Override
    public String[] askPortIP(){
        String[] server = new String[2];
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insert_IP_PORT.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            InsertIPPortController controller = loader.getController();
            controller.setViewGUI(this);

            server[0] = controller.getIP();
            server[1] = controller.getPort();

        }catch (IOException e){
            e.printStackTrace();
        }
        return server;
    }

    @Override
    public boolean askSocket() {// true = socket, false = rmi
        boolean useSocket = true;
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Choose_Socket_RMI.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.showAndWait();

                ChooseSocketRMIController controller = loader.getController();
                controller.setViewGUI(this);
                
                useSocket = controller.useSocket();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Restituisci il valore di useSocket
            return useSocket;
    }



    @Override
    public serverOptionMessage serverOptions(serverOptionMessage message) {
        serverOptionMessage newMessage = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Insert_ServerOption.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            InsertServerOptionController controller = loader.getController();
            controller.setViewGUI(this);
            controller.setServerOptionMessage(message);


            newMessage = controller.getServerOptionMessage();
        }catch(IOException e){
                e.printStackTrace();
        }
        return newMessage;
    }

    @Override
    public void answerToOption(serverOptionResponseMessage message) {

    }

    @Override
    public void playerDisconnected() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Disconnect.fxml"));
            Parent root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void answerToConnection(connectionResponseMessage message) {

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

    /**
     * allow the user to choose how many players will play
     * @return number of expected players
     */
    @Override
    public int expectedPlayers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/expectedPlayers.fxml"));
            Parent root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            expectedPlayersController controller = loader.getController();
            controller.setViewGUI(this);
            //int num = controller.getNumber();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void endGame(declareWinnerMessage message) {
    }

}

