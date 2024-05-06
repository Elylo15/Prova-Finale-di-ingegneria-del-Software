package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;

import java.rmi.RemoteException;

public abstract class Controller {
    //Controller is an abstract class so cannot be instantiated, the View will instantiate a ControllerSocket
    //or ControllerRMI object depending on which connection the user chooses
    //the controller methods will allow the client to handle the user inputs and send them to the server
    //the methods of the Controller will then be different as their implementation will change in ControllerSocket and ControllerRMI classes

    String ServerIP;
    String ServerPort;


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
    public abstract void chooseObjective(int pick, boolean noResponse);

    public abstract void placeCard(int card, int side, int x, int y, boolean noResponse);
    public abstract void pickCard(int card, boolean noResponse);
    public abstract updatePlayerMessage updatePlayer();

    public abstract declareWinnerMessage endGame() throws RemoteException;
}
