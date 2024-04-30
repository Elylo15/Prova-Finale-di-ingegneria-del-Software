package it.polimi.ingsw.protocol.client.controller;


import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;

import java.io.*;
import java.net.*;

public class ControllerSocket  extends Controller {
    private final String serverIP;
    private final String serverPort;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ControllerSocket (String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public void connectToServer(String serverIP, String serverPort) {
        try {
            socket = new Socket(serverIP, Integer.parseInt(serverPort));
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public connectionResponseMessage answerConnection() {
        try {
            return (connectionResponseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public currentStateMessage getCurrent() {
        try {
            return (currentStateMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public serverOptionMessage serverOptions() {
        try {
            return (serverOptionMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendOptions(serverOptionMessage options) {
        try {
            outputStream.writeObject(options);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public responseMessage correctAnswer() {
        try {
            return (responseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public unavailableNamesMessage getUnavailableName() {
        try {
            return (unavailableNamesMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseName(String name) {
        try {
            outputStream.writeObject(new chosenNameMessage(name));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public availableColorsMessage getAvailableColor() {
        try {
            return (availableColorsMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseColor(String color) {
        try {
            outputStream.writeObject(new chosenNameMessage(color));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public newHostMessage newHost() {
        try {
            return (newHostMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void expectedPlayers(int expected) {
        try {
            outputStream.writeObject(new expectedPlayersMessage (expected));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStarter(int side) {
        try {
            outputStream.writeObject(new starterCardMessage(side));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseObjective(int pick) {
        try {
            outputStream.writeObject(new objectiveCardMessage(pick));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(int card, int side, int x, int y) {
        try {
            outputStream.writeObject(new PlaceCardMessage(card, side, x, y));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pickCard(int card) {
        try {
            outputStream.writeObject(new pickCardMessage(card));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public declareWinnerMessage endGame() {
        try {
            return (declareWinnerMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

}