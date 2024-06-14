package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.messages.connectionState.availableColorsMessage;
import it.polimi.ingsw.messages.connectionState.connectionResponseMessage;
import it.polimi.ingsw.messages.connectionState.unavailableNamesMessage;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.messages.objectiveState.objectiveCardMessage;
import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.messages.responseMessage;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.messages.waitingForPlayerState.newHostMessage;

import java.rmi.RemoteException;

/**
 * Controller is an abstract class. The methods of the Controller will be implemented
 * differently in ControllerSocket and ControllerRMI classes.
 */
public abstract class Controller {
    String ServerIP;
    String ServerPort;

    /**
     * method {@code Controller}: constructs a new Controller
     *
     * @param serverIP:   IP of the server
     * @param serverPort: port of the server
     */
    public Controller(String serverIP, String serverPort) {
        this.ServerIP = serverIP;
        this.ServerPort = serverPort;
    }

    /**
     * methods implemented by the rmi and Socket controllers
     */
    public abstract void connectToServer(String IP, String port);

    public abstract connectionResponseMessage answerConnection();

    public abstract currentStateMessage getCurrent();

    public abstract serverOptionMessage serverOptions();

    public abstract void sendOptions(serverOptionMessage options);

    public abstract responseMessage correctAnswer();

    public abstract unavailableNamesMessage getUnavailableName();

    public abstract void chooseName(String name);

    public abstract availableColorsMessage getAvailableColor();

    public abstract void chooseColor(String color);

    public abstract newHostMessage newHost();

    public abstract void expectedPlayers(int expected, boolean noResponse);

    public abstract void placeStarter(int side, boolean noResponse);

    public abstract objectiveCardMessage getObjectiveCards();

    public abstract void chooseObjective(int pick, boolean noResponse);

    public abstract void placeCard(int card, int side, int x, int y, boolean noResponse);

    public abstract void pickCard(int card, boolean noResponse);

    public abstract updatePlayerMessage updatePlayer();

    public abstract declareWinnerMessage endGame() throws RemoteException;

    public abstract void sendAnswerToPing();
}
