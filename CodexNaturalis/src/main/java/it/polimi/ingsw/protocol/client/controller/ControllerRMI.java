package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;

import java.net.*;
import java.rmi.*;

public class ControllerRMI extends Controller implements RemoteController {
    private final String serverIP;
    private final String serverPort;
    private RemoteController remoteController;


    public ControllerRMI(String serverIP, String serverPort) throws RemoteException {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void connectToServer(String serverIP, String serverPort) {
        try {
            String registryURL = "rmi://" + serverIP + ":" + serverPort + "/ControllerRMI";
            remoteController = (RemoteController) Naming.lookup(registryURL);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public connectionResponseMessage answerConnection() {
        try {
            return remoteController.answerConnection();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public currentStateMessage getCurrent() {
        try {
            return remoteController.getCurrent();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public serverOptionMessage serverOptions() {
        try {
            return remoteController.serverOptions();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendOptions(serverOptionMessage options) {
        try {
            remoteController.sendOptions(options);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public serverOptionResponseMessage correctOption() {
        try {
            return remoteController.correctOption();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public unavailableNamesMessage getUnavailableName() {
        try {
            return remoteController.getUnavailableName();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseName(String name) {
        try {
            remoteController.chooseName(name);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public nameResponseMessage correctName() {
        try {
            return remoteController.correctName();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public availableColorsMessage getAvailableColor() {
        try {
            return remoteController.getAvailableColor();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseColor(String color) {
        try {
            remoteController.chooseColor(color);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public colorResponseMessage correctColor() {
        try {
            return remoteController.correctColor();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public newHostMessage newHost() {
        try {
            return remoteController.newHost();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void expectedPlayers(int expected) {
        try {
            remoteController.expectedPlayers(expected);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public expectedPlayersResponseMessage correctExpectedPlayers() {
        try {
            return remoteController.correctExpectedPlayers();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStarter(int side) {
        try {
            remoteController.placeStarter(side);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public starterCardResponseMessage correctStarter() {
        try {
            return remoteController.correctStarter();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseObjective(int pick) {
        try {
            remoteController.chooseObjective(pick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public objectiveCardResponseMessage correctObjective() {
        try {
            return remoteController.correctObjective();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(int card, int side, int x, int y) {
        try {
            remoteController.placeCard(card, side, x, y);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public placeCardResponseMessage correctPlaced() {
        try {
            return remoteController.correctPlaced();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pickCard(int card) {
        try {
            remoteController.pickCard(card);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public pickCardResponseMessage correctPicked() {
        try {
            return remoteController.correctPicked();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public declareWinnerMessage endGame() throws RemoteException {
        try {
            return remoteController.endGame();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
