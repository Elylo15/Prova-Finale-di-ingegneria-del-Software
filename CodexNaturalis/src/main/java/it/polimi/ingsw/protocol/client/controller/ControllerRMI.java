package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.server.RemoteServerInterface;

import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ControllerRMI extends Controller {
    private RemoteServerInterface remoteServer;


    /**
     * method {@code ControllerRMI}: constructs a new ControllerRMI
     * @param serverIP: String
     * @param serverPort: String
     */
    public ControllerRMI(String serverIP, String serverPort) throws RemoteException {
        super(serverIP, serverPort);
    }

    /**
     * method {@code connectToServer}: connects to an existing server
     * @param serverIP: String
     * @param serverPort: String
     */
    public void connectToServer(String serverIP, String serverPort) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, Integer.parseInt(serverPort));
            remoteServer = (RemoteServerInterface) Naming.lookup("RemoteServer");
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
            return remoteServer.answerConnectionRMI();
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
            return remoteServer.getCurrentRMI();
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
            return remoteServer.serverOptionsRMI();
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
            return remoteServer.correctAnswerRMI();
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
            remoteServer.sendOptionsRMI(options);
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
            return remoteServer.getUnavailableNameRMI();
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
            remoteServer.chooseNameRMI(new chosenNameMessage(name));
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
            return remoteServer.getAvailableColorRMI();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseColor}: sends a chosenColorMessage
     * @param color: String
     */
    @Override
    public void chooseColor(String color) {
        try {
            remoteServer.chooseColorRMI(new chosenColorMessage(color));
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
            return remoteServer.newHostRMI();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code expectedPlayers}: sends a expectedPlayersMessage
     * @param expected: int
     * @param noResponse: boolean
     */
    @Override
    public void expectedPlayers(int expected, boolean noResponse) {
        try {
            remoteServer.expectedPlayersRMI(new expectedPlayersMessage(expected, noResponse));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code placeStarter}: sends a starterCardMessage
     * @param side: int
     * @param noResponse: boolean
     */
    @Override
    public void placeStarter(int side, boolean noResponse) {
        try {
            remoteServer.placeStarterRMI(new starterCardMessage(side, noResponse));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseObjective}: sends a objectiveCardMessage
     * @param pick: int
     * @param noResponse: boolean
     */
    @Override
    public void chooseObjective(int pick, boolean noResponse) {
        try {
            remoteServer.chooseObjectiveRMI(new objectiveCardMessage(pick, noResponse));
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
     * @param noResponse: boolean
     */
    @Override
    public void placeCard(int card, int side, int x, int y, boolean noResponse) {
        try {
            remoteServer.placeCardRMI(new placeCardMessage(card, side, x, y, noResponse));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code pickCard}: sends a pickCardMessage
     * @param card: int
     * @param noResponse: boolean
     */
    @Override
    public void pickCard(int card, boolean noResponse) {
        try {
            remoteServer.pickCardRMI(new pickCardMessage(card, noResponse));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code updatePlayer}: receives a updatePlayerMessage
     * @return updatePlayerMessage
     */
    public updatePlayerMessage updatePlayer(){
        try {
            return remoteServer.updatePlayerRMI();
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
            return remoteServer.endGameRMI();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
