package it.polimi.ingsw.protocol.server;



import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardResponseMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardResponseMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayerMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayersResponseMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.newHostMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientSocket extends ClientConnection {
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;


    /**
     * Class constructor
     *
     * @param IP   the IP address of the server.
     * @param port the port of the server.
     */
    public ClientSocket(String IP, String port, Socket socket) {
        super(IP, port, socket);
    }

    @Override
    public void sendNewHostMessage(String hostNickname){
        try {
            outputStream.writeObject(new newHostMessage(hostNickname, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public expectedPlayerMessage getExpectedPlayer(){
        try {
            return (expectedPlayerMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAnswerToExpected (boolean correct){
        try {
            outputStream.writeObject(new expectedPlayersResponseMessage(correct));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startCheckConnection(){
        Thread thread = new Thread(() -> getConnection().startConnectionCheck());
        thread.start();
    }

    @Override
    public void sendAnswerToConnection(connectionResponseMessage message){
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void  sendUnvailableName(ArrayList<String> unavailableNames) {
        try {
            outputStream.writeObject(new unavailableNamesMessage(unavailableNames));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public chosenNameMessage getName(){
        try {
            return (chosenNameMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAvailableColor(ArrayList<String> availableColors){
        try {
            outputStream.writeObject(new availableColorsMessage(availableColors));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public chosenColorMessage getColor(){
        try {
            return (chosenColorMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendCurrentState(currentStateMessage currentState){
        try {
            outputStream.writeObject(currentState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public starterCardMessage getStaterCard(){
        try {
            return (starterCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public objectiveCardMessage getChosenObjective(){
        try {
            return (objectiveCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public placeCardMessage getPlaceCard(){
        try {
            return (placeCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public pickCardMessage getChosenPick(){
        try {
            return (pickCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAnswerToChosenName(boolean correctChoice){
        try {
            outputStream.writeObject(new nameResponseMessage(correctChoice));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void sendAnswerToChosenColor(boolean correctChoice){
        try {
            outputStream.writeObject(new colorResponseMessage(correctChoice));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAnswerToStaterCard(boolean correctChoice){
        try {
            outputStream.writeObject(new starterCardResponseMessage(correctChoice));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendAnswerToObjectiveCard(boolean correctChoice){
        try {
            outputStream.writeObject(new objectiveCardResponseMessage(correctChoice));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAnswerToPlaceCard(boolean correctChoice){
        try {
            outputStream.writeObject(new placeCardResponseMessage(correctChoice));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAnswerToPickCard(boolean correctChoice){
        try {
            outputStream.writeObject(new pickCardResponseMessage(correctChoice));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(getSocket().getOutputStream());
            inputStream = new ObjectInputStream(getSocket().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEndGame(HashMap<String, Integer> scores, HashMap<String, Integer> numberOfObjectives){
        try {
            outputStream.writeObject(new declareWinnerMessage(scores, numberOfObjectives));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
