package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.ObjectiveCard;

import it.polimi.ingsw.protocol.client.view.GUI.controller.*;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionResponseMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.application.Platform;

import java.util.*;

public class ViewGUI extends View {

    public ViewGUI() {
        new GUIMessages();
    }

    public void startMain() {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::MainView);
    }


    /**
     * allow the user to enter the ip of the server he wants to connect to
     *
     * @return array with ip and port of the server
     */
    public String askIP() {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        String ip = "";
        Platform.runLater(SceneManager::InsertIP); //update the scene
        ip = (String) GUIMessages.readToClient(); //read the ip entered by the user
        System.out.println(ip);
        return ip;
    }

    /**
     * allow the user to choose if he wants to use socket or rmi
     *
     * @return boolean
     */
    @Override
    public boolean askSocket() {// true = socket, false = rmi
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        boolean useSocket = true;
        Platform.runLater(SceneManager::Choose_Socket_RMI);
        useSocket = (boolean) GUIMessages.readToClient();
        System.out.println(useSocket);
        return useSocket;
    }


    @Override
    public serverOptionMessage serverOptions(serverOptionMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        serverOptionMessage newMessage = null;
        GUIMessages.writeToGUI(message);
        Platform.runLater(SceneManager::InsertServerOption);
        newMessage = (serverOptionMessage) GUIMessages.readToClient();
        System.out.println(newMessage);
        return newMessage;
    }

    @Override
    public void answerToOption(serverOptionResponseMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        //da vedere poi
    }

    @Override
    public void playerDisconnected() {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::Disconnect);
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void answerToConnection(connectionResponseMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        //da vedere poi
    }


    @Override
    public String unavailableNames(unavailableNamesMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();  //remove all elements that could be in the queue
        String name = null;
        GUIMessages.writeToGUI(message);  //serialize the message and send it to the gui
        Platform.runLater(SceneManager::unavailableNames); //run the method unavailableNames in SceneManager
        name = (String) GUIMessages.readToClient(); //deserialize the string the method must return
        System.out.println(name);
        return name;
    }

    /**
     * visualize the response about the value entered
     *
     * @param message
     */
    @Override
    public void answer(responseMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        //da vedere successivamente
    }

    /**
     * allow the user to choose his color
     *
     * @param message
     * @return color
     */
    @Override
    public String availableColors(availableColorsMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        String color = null;
        GUIMessages.writeToGUI(message); //in AvailableColorsController we call GUImessages.readToGui() to receive this message
        Platform.runLater(SceneManager::availableColors); //run the method availableColors in SceneManager
        color = (String) GUIMessages.readToClient(); //read the object we sent calling GUImessages.writeToClient() in AvailableColorsController
        System.out.println(color);
        return color;
    }

    /**
     * allow the user to choose how many players will play
     *
     * @return number of expected players
     */
    @Override
    public int expectedPlayers() {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        int number = 0;
        Platform.runLater(SceneManager::expectedPlayers); //run the method expectedPlayers in SceneManager
        number = (int) GUIMessages.readToClient();
        System.out.println(number);

        return number;
    }

    /**
     * visualize the waiting status of the match.
     * The first player to connect to the server is the host and will wait for other players to be ready after choosing the number of players.
     * The other players after choosing name and color will wait for the other players to choose to.
     */
    public void waiting() {
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::waiting);
    }

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

    /**
     * Method {@code updatePlayer} Visualize the current state of the game
     * @param message: currentStateMessage
     */
    @Override
    public void updatePlayer(currentStateMessage message) {
        GUIMessages.clearQueue();
        if (Objects.equals(message.getStateName(), "StarterCardState")) {
            Platform.runLater(SceneManager::starterPage);
        } else if (Objects.equals(message.getStateName(), "ObjectiveState")) {
            Platform.runLater(SceneManager::objectivePage);
        } else {
            Platform.runLater(SceneManager::myselfGamePage);
        }
        GUIMessages.writeToGUI(message);
    }

    /**
     * Method {@code placeStarter}: Places the starter card
     */
    @Override
    public int placeStarter() {
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::starterPage);
        return (int) GUIMessages.readToClient();

    }

    /**
     * Method {@code chooseObjective}: Allows the player to choose an objective card
     * @param objectives: the list of objective cards
     * @return the index of the chosen objective card
     */
    @Override
    public int chooseObjective(ArrayList<ObjectiveCard> objectives) {
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::objectivePage);
        return (int) GUIMessages.readToClient();

    }

    /**
     * Method {@code placeCard}: Allows the player to place a card
     * @return the index of the chosen card, and coordinates of the chosen cell
     */
    @Override
    public int[] placeCard() {
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::myselfGamePage);
        return (int[]) GUIMessages.readToClient();
    }

    /**
     * Method {@code pickCard}: Allows the player to pick a card to draw
     * @return the index of the chosen card
     */
    @Override
    public int pickCard() {
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::myselfGamePage);
        return (int) GUIMessages.readToClient();
    }

    /**
     * Method {@code update}: Updates the parameters of the player that concluded his turn
     * @param update: updatePlayerMessage
     */
    @Override
    public void update(updatePlayerMessage update) {
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::myselfGamePage);
        GUIMessages.writeToGUI(update);
    }


    @Override
    public String pickNameFA(unavailableNamesMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        return "";
    }

    /**
     * visualize the final information about the game, the number of objectives achieved by each player and then
     * show the scoreboard with the personal points of the players
     *
     * @param message
     */
    @Override
    public void endGame(declareWinnerMessage message) {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();

    }

}

