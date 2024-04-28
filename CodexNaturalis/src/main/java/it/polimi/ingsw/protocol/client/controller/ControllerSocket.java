package it.polimi.ingsw.protocol.client.controller;


import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardResponseMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;

import java.io.*;
import java.net.*;

public class ControllerSocket  extends Controller implements Runnable {
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
    public void answerConnection() throws IOException {
        try {
            outputStream.writeObject(new answerConnectionMessage());
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public currentStateMessage getCurrent() {
        try {
            return (currentStateMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
                // Handle exceptions
            return null;
        }
    }

    @Override
    public unavailableNamesMessage getUnavailableName() throws IOException {
        try {
            return (unavailableNamesMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public void chooseName(String name) throws IOException {
        try {
            outputStream.writeObject(new choseNameMessage(name));
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public answerNameMessage CorrectName() throws IOException {
        try {
            return (answerNameMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public availableColorsMessage getAvailableColor() throws IOException {
        try {
            return (availableColorsMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public void chooseColor(String color) throws IOException {
        try {
            outputStream.writeObject(new choseNameMessage(color));
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public answerColorMessage correctColor() throws IOException {
        try {
            return (answerColorMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public newHostMessage newHost() throws IOException {
        try {
            return (newHostMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public void startSignal(String name, boolean start) throws IOException {
        try {
            outputStream.writeObject(new forceStartMessage(name, start));
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStarter(int side) throws IOException {
        try {
            outputStream.writeObject(new starterCardMessage(side));
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public starterCardResponseMessage correctStarter() throws IOException {
        try {
            return (starterCardResponseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public void chooseObjective(int pick) throws IOException {
        try {
            outputStream.writeObject(new objectiveCardMessage(pick));
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public objectiveCardResponseMessage correctObjective() throws IOException {
        try {
            return (objectiveCardResponseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public void placeCard(int card, int side, int x, int y) throws IOException {
        try {
            outputStream.writeObject(new PlaceCardMessage(card, side, x, y));
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public placeCardResponseMessage correctPlaced() throws IOException {
        try {
            return (placeCardResponseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public void pickCard(int card) throws IOException {
        try {
            outputStream.writeObject(new pickCardMessage(card));
            outputStream.flush();
        } catch (IOException e) {
            // Handle exceptions
            throw new RuntimeException(e);
        }
    }

    @Override
    public pickCardResponseMessage correctPicked() throws IOException {
        try {
            return (pickCardResponseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public declareWinnerMessage endGame() throws IOException {
        try {
            return (declareWinnerMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }

    @Override
    public disconnectedMessage disconnected() throws IOException {
        try {
            return (disconnectedMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // Handle exceptions
            return null;
        }
    }
}