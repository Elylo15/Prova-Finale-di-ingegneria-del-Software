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
            mainstage.setFullScreen(true);
            if (!mainstage.isShowing()) {
                mainstage.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * allow the user to enter the ip of the server he wants to connect to
     * @return array with ip and port of the server
     */
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
        mainstage.setFullScreen(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }
        server[0] = controller.getIP();
        server[1] = controller.getPort();

        return server;
    }

    /**
     * allow the user to choose if he wants to use socket or rmi
     * @return  boolean
     */
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
        mainstage.setFullScreen(true);
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
        mainstage.setFullScreen(true);
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
        mainstage.setFullScreen(true);

        if(!message.toString().equalsIgnoreCase("[]")){ //set the unavailable names to show to the user
            controller.setUp(message.toString());
        }
        else {
            controller.setUpNoNames();
        }


        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }
        name = controller.getName();

        return name;
    }

    /**
     * visualize the response about the value entered
     * @param message
     */
    @Override
    public void answer(responseMessage message) {
    }

    /**
     * allow the user to choose his color
     * @param message
     * @return color
     */
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
        mainstage.setFullScreen(true);

        controller.setUp(message.toString());

        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }
        color = controller.getColor();



        return color;
    }

    /**
     * allow the user to choose how many players will play
     * @return number of expected players
     */
    @Override
    public int expectedPlayers() {
        int number = 0;

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
        mainstage.setFullScreen(true);


        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }

        number = controller.getNumberOfPlayers();

        return number;
    }

    /**
     * visualize the waiting status of the match.
     * The first player to connect to the server is the host and will wait for other players to be ready after choosing the number of players.
     * The other players after choosing name and color will wait for the other players to choose to.
     */
    public void waiting(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/waiting.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        mainstage.setScene(scene);
        mainstage.setFullScreen(true);
        if (!mainstage.isShowing()) {
            mainstage.showAndWait();
        }

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

    @Override
    public void updatePlayer(currentStateMessage message) {
        try {
            FXMLLoader loader;
            Parent root;

            if (Objects.equals(message.getStateName(), "StarterCardState")) {
                loader = new FXMLLoader(getClass().getResource("/starterPage.fxml"));
            } else if (Objects.equals(message.getStateName(), "ObjectiveState")) {
                loader = new FXMLLoader(getClass().getResource("/objectivePage.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/myselfGamePage.fxml"));
            }

            root = loader.load();
            Object controller = loader.getController();

            if (controller instanceof StarterController) {
                ((StarterController) controller).set(message);
            } else if (controller instanceof ObjectiveController) {
                ((ObjectiveController) controller).set(message);
            } else if (controller instanceof MyselfPageController) {
                ((MyselfPageController) controller).set(message);
            }

            Scene scene = new Scene(root);
            mainstage.setScene(scene);
            mainstage.show();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public int placeStarter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/starterPage.fxml"));
            Parent root = loader.load();
            StarterController controller = loader.getController();

            return controller.getChoice();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 1000;
        }
    }

    @Override
    public int chooseObjective(ArrayList<ObjectiveCard> objectives) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/objectivePage.fxml"));
            Parent root = loader.load();
            ObjectiveController controller = loader.getController();

            controller.setObjectives(objectives);
            return controller.getChoice();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 1000;
        }
    }

    @Override
    public int[] placeCard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/myselfGamePage.fxml"));
            Parent root = loader.load();
            MyselfPageController controller = loader.getController();

            return controller.getPlaced();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return new int[]{1000, 1000, 1000, 1000};
        }
    }

    @Override
    public int pickCard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/myselfGamePage.fxml"));
            Parent root = loader.load();
            MyselfPageController controller = loader.getController();

            return controller.getDraw();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 1000;
        }
    }

    @Override
    public void update(updatePlayerMessage update) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/currentGamePage.fxml"));
            Parent root = loader.load();
            CurrentPageController controller = loader.getController();

            controller.update(update);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    @Override
    public String pickNameFA(unavailableNamesMessage message) {
        return "";
    }

    /**
     * visualize the final information about the game, the number of objectives achieved by each player and then
     * show the scoreboard with the personal points of the players
     * @param message
     */
    @Override
    public void endGame(declareWinnerMessage message) {

    }

}

