package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;

import java.net.*;
import java.rmi.*;

public class ControllerRMI extends Controller implements RemoteController {
    private final String serverIP;
    private final String serverPort;
    private RemoteController remoteController;


    /**
     * method {@code ControllerRMI}: creates a new ControllerRMI
     * @param serverIP: String
     * @param serverPort: String
     */
    public ControllerRMI(String serverIP, String serverPort) throws RemoteException {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    /**
     * method {@code connectToServer}: connects to an existing server
     * @param serverIP: String
     * @param serverPort: String
     */
    public void connectToServer(String serverIP, String serverPort) {
        try {
            String registryURL = "rmi://" + serverIP + ":" + serverPort + "/ControllerRMI";
            remoteController = (RemoteController) Naming.lookup(registryURL);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code answerConnection}: receives a connectionResponseMessage
     * @return connectionResponseMessage
     */
    @Override
    public connectionResponseMessage answerConnection() {
        try {
            return remoteController.answerConnection();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getCurrent}: receives a currentStateMessage
     * @return currentStateMessage
     */
    @Override
    public currentStateMessage getCurrent() {
        try {
            return remoteController.getCurrent();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code serverOptions}: receives an empty serverOptionMessage
     * @return serverOptionMessage
     */
    @Override
    public serverOptionMessage serverOptions() {
        try {
            return remoteController.serverOptions();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code correctAnswer}: receives a responseMessage
     * @return responseMessage
     */
    @Override
    public responseMessage correctAnswer() {
        try {
            return remoteController.correctAnswer();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code sendOptions}: sends a serverOptionMessage
     * @param options: serverOptionMessage
     */
    @Override
    public void sendOptions(serverOptionMessage options) {
        try {
            remoteController.sendOptions(options);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getUnavailableName}: receives a unavailableNamesMessage
     * @return unavailableNamesMessage
     */
    @Override
    public unavailableNamesMessage getUnavailableName() {
        try {
            return remoteController.getUnavailableName();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseName}: sends a chosenNameMessage
     * @param name: String
     */
    @Override
    public void chooseName(String name) {
        try {
            remoteController.chooseName(name);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getAvailableColor}: receives a availableColorsMessage
     * @return availableColorsMessage
     */
    @Override
    public availableColorsMessage getAvailableColor() {
        try {
            return remoteController.getAvailableColor();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseColor}: sends a chosenNameMessage
     * @param color: String
     */
    @Override
    public void chooseColor(String color) {
        try {
            remoteController.chooseColor(color);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code newHost}: receives a newHostMessage
     * @return newHostMessage
     */
    @Override
    public newHostMessage newHost() {
        try {
            return remoteController.newHost();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code expectedPlayers}: sends a expectedPlayersMessage
     * @param expected: int
     */
    @Override
    public void expectedPlayers(int expected, boolean noResponse) {
        try {
            remoteController.expectedPlayers(expected, noResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code placeStarter}: sends a starterCardMessage
     * @param side: int
     */
    @Override
    public void placeStarter(int side, boolean noResponse) {
        try {
            remoteController.placeStarter(side, noResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseObjective}: sends a objectiveCardMessage
     * @param pick: int
     */
    @Override
    public void chooseObjective(int pick, boolean noResponse) {
        try {
            remoteController.chooseObjective(pick, noResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code placeCard}: sends a PlaceCardMessage
     * @param card: int
     * @param side: int
     * @param x: int
     * @param y: int
     */
    @Override
    public void placeCard(int card, int side, int x, int y, boolean noResponse) {
        try {
            remoteController.placeCard(card, side, x, y, noResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code pickCard}: sends a pickCardMessage
     * @param card: int
     */
    @Override
    public void pickCard(int card, boolean noResponse) {
        try {
            remoteController.pickCard(card, noResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code updatePlayer}: sends a updatePlayerMessage
     * @return updatePlayerMessage
     */
    public updatePlayerMessage updatePlayer(){
        try {
            return remoteController.updatePlayer();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code endGame}: receives a declareWinnerMessage
     * @return declareWinnerMessage
     */
    @Override
    public declareWinnerMessage endGame() {
        try {
            return remoteController.endGame();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
