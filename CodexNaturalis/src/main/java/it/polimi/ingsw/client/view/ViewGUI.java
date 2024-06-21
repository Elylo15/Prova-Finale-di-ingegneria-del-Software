package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import it.polimi.ingsw.messages.connectionState.availableColorsMessage;
import it.polimi.ingsw.messages.connectionState.unavailableNamesMessage;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.messages.responseMessage;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents the gui view of the game.
 * It extends the View class and overrides its methods to provide a graphical user interface for the game.
 * It uses the SceneManager class to manage the different scenes of the game.
 * It uses the GUIMessages class to communicate with the client.
 */
public class ViewGUI extends View {
    private boolean firstTimeCurrent = true;
    private boolean firstTimeName = true;
    private String state;
    private String color;


    /**
     * Constructor for the ViewGUI class.
     * It initializes a new GUIMessages object.
     */
    public ViewGUI() {
        GUIMessages.initialize();
    }

    /**
     * This method starts the main view of the game.
     * It clears the message queue and updates the scene to the main view.
     */
    public boolean startMain() {
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        Platform.runLater(SceneManager::MainView);
        return (boolean) GUIMessages.readToClient();
    }


    /**
     * This method is used to ask the user for the IP address of the server they want to connect to.
     *
     * @return The IP address entered by the user.
     */
    public String askIP() {
        state = "IPState";
        // Clear the message queue to avoid processing any unexpected messages
        GUIMessages.clearQueue();

        // Initialize the IP address
        String ip;

        // Update the gui scene to allow the user to enter the IP address
        Platform.runLater(SceneManager::InsertIP);

        // Read the IP address entered by the user from the client
        ip = (String) GUIMessages.readToClient();

        // Return the IP address
        return ip;
    }

    /**
     * This method is used to ask the user whether they want to use a socket or rmi for communication.
     *
     * @return A boolean value representing the user's choice. True for socket, false for rmi.
     */
    @Override
    public boolean askSocket() {// true = socket, false = rmi
        state = "SocketState";
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        boolean useSocket;
        Platform.runLater(SceneManager::Choose_Socket_RMI);
        useSocket = (boolean) GUIMessages.readToClient();
        return useSocket;
    }

    /**
     * This method handles the server options for the user.
     *
     * @param message The server option message to be sent to the gui.
     * @return The server option message received from the client.
     */
    @Override
    public serverOptionMessage serverOptions(serverOptionMessage message) {
        state = "ServerOptionState";
        GUIMessages.clearQueue();
        serverOptionMessage newMessage;
        GUIMessages.writeToGUI(message);
        Platform.runLater(SceneManager::InsertServerOption);
        newMessage = (serverOptionMessage) GUIMessages.readToClient();
        return newMessage;
    }

    /**
     * This method is called when a player disconnects from the game.
     */
    @Override
    public void playerDisconnected(Exception e) {
        state = "disconnectedState";
        GUIMessages.clearQueue();
        if(!Objects.equals(e.getMessage(), "Game ended."))
            Platform.runLater(SceneManager::disconnect);
        else {
            GUIMessages.readToClient();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignore) {
        }
    }

    /**
     * This method is used when a user needs to choose a name
     *
     * @param message An instance of unavailableNamesMessage, which contains the list of names that are already taken.
     * @return The new name provided by the user.
     */
    @Override
    public String unavailableNames(unavailableNamesMessage message) {
        state = "UnavailableNamesState";
        GUIMessages.clearQueue();  //remove all elements that could be in the queue
        if (firstTimeName) {
            firstTimeName = false;
            Platform.runLater(SceneManager::unavailableNames);
        }
        GUIMessages.writeToGUI(message);
        return (String) GUIMessages.readToClient();
    }

    /**
     * This method visualizes the response about the value entered
     *
     * @param message: responseMessage
     */
    @Override
    public boolean answer(responseMessage message) {
        //to avoid reading unexpected messages
        boolean ok;
        GUIMessages.clearQueue();
        if (!message.getCorrect()) {
            switch (state) {
                case "PlaceTurnState", "PickTurnState", "objectiveState", "StarterCardState", "endGameState" -> {
                    GUIMessages.writeToGUI(message);
                    return true;
                }
                case "ServerOptionState" -> {
                    return true;
                }
                default -> {
                    Platform.runLater(SceneManager::disconnect);
                    ok = (boolean) GUIMessages.readToClient();
                    return ok;
                }
            }
        } else if(message.getCorrect() && Objects.equals(state, "NameFAState")) {
            GUIMessages.writeToGUI("random");
            Platform.runLater(SceneManager::waiting);
            return true;
        }

        return true;
    }

    /**
     * This method allows the user to choose his color
     *
     * @param message: availableColorsMessage, which contains the list of available colors.
     * @return color chosen by the user
     */
    @Override
    public String availableColors(availableColorsMessage message) {
        state = "AvailableColorsState";
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        GUIMessages.writeToGUI(message); //in AvailableColorsController we call GUIMessages.readToGui() to receive this message
        Platform.runLater(SceneManager::availableColors); //run the method availableColors in SceneManager
        color = (String) GUIMessages.readToClient(); //read the object we sent calling GUIMessages.writeToClient() in AvailableColorsController
        return color;
    }

    /**
     * This method allows the user to choose how many players will play
     *
     * @return number of expected players
     */
    @Override
    public int expectedPlayers() {
        state = "ExpectedPlayersState";
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();
        int number;
        Platform.runLater(SceneManager::expectedPlayers); //run the method expectedPlayers in SceneManager
        number = (int) GUIMessages.readToClient();

        return number;
    }

    /**
     * This method visualizes the waiting status of the match.
     * The first player to connect to the server is the host and will wait for other players to be ready
     * after choosing the number of players.
     * The other players after choosing name and color will wait for the other players to choose too.
     */
    @Override
    public void waiting() {
        state = "WaitingState";
        GUIMessages.clearQueue();
        GUIMessages.writeToGUI(color);
        Platform.runLater(SceneManager::waiting);
    }


    /**
     * Method {@code updatePlayer} loads the main page of the game and updates the player's state
     *
     * @param message: currentStateMessage
     */
    @Override
    public void updatePlayer(currentStateMessage message) {
        GUIMessages.clearQueue();
        state = message.getStateName();
        GUIMessages.writeToGUI(message);
        if (firstTimeCurrent) {
            firstTimeCurrent = false; //Load this page only the first time the method is called
            Platform.runLater(SceneManager::starterPage);
        }
    }

    /**
     * Method {@code placeStarter}: Places the starter card
     *
     * @return int: the side of the card
     */
    @Override
    public int placeStarter() {
        return (int) GUIMessages.readToClient();

    }

    /**
     * Method {@code chooseObjective}: Allows the player to choose an objective card
     *
     * @param objectives: the list of objective cards
     * @return the index of the chosen objective card
     */
    @Override
    public int chooseObjective(ArrayList<ObjectiveCard> objectives) {
        GUIMessages.writeToGUI(objectives);
        return (int) GUIMessages.readToClient();

    }

    /**
     * Method {@code placeCard}: Allows the player to place a card
     *
     * @return the index of the chosen card, front or back, and coordinates of the chosen cell
     */
    @Override
    public int[] placeCard() {
        return (int[]) GUIMessages.readToClient();
    }

    /**
     * Method {@code pickCard}: Allows the player to pick a card to draw
     *
     * @return the index of the chosen card
     */
    @Override
    public int pickCard() {
        return (int) GUIMessages.readToClient();
    }

    /**
     * Method {@code update}: Updates the parameters of the player that concluded his turn
     *
     * @param updateMsg: updatePlayerMessage
     */
    @Override
    public void update(updatePlayerMessage updateMsg) {
        GUIMessages.writeToGUI(updateMsg);
    }

    /**
     * This method allows the user to choose a nickname from the list of available names
     *
     * @param message the message containing the list of available names
     * @return the chosen nickname
     */
    @Override
    public String pickNameFA(unavailableNamesMessage message) {
        state = "NameFAState";
        //to avoid reading unexpected messages
        GUIMessages.clearQueue();  //remove all elements that could be in the queue
        String name;
        GUIMessages.writeToGUI(message);  //serialize the message and send it to the gui
        Platform.runLater(SceneManager::pickNameFA); //run the method unavailableNames in SceneManager
        name = (String) GUIMessages.readToClient(); //deserialize the string the method must return
        return name;
    }

    /**
     * This method visualize the final information about the game, the number of objectives achieved by each player and then
     * show the scoreboard with the personal points of the players
     *
     * @param message declareWinnerMessage
     */
    @Override
    public void endGame(declareWinnerMessage message) {
        state = "endGameState";
        GUIMessages.writeToGUI(message);
    }

}
