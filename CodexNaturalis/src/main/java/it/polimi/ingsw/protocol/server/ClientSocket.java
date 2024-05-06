package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientSocket extends ClientConnection {
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    private final Socket socket;

    /**
     * method {@code ClientSocket}: constructs a new ClientSocket
     * @param IP: String
     * @param port: String
     */
    public ClientSocket(String IP, String port, Socket socket) {
        super(IP, port);
        this.socket = socket;
    }

    /**
     * method {@code run}: initializes the outputStream and inputStream
     */
    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendNewHostMessage}: sends a newHostMessage
     * @param hostNickname: String
     */
    @Override
    public void sendNewHostMessage(String hostNickname){
        try {
            outputStream.writeObject(new newHostMessage(hostNickname));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getExpectedPlayer}: receives a expectedPlayerMessage
     * @return expectedPlayersMessage
     */
    @Override
    public expectedPlayersMessage getExpectedPlayer(){
        try {
            return (expectedPlayersMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAnswer}: sends a connectionResponseMessage
     * @param message: connectionResponseMessage
     */
    @Override
    public void sendAnswerToConnection(connectionResponseMessage message){
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAnswer}: sends a responseMessage
     * @param correct: boolean
     */
    @Override
    public void sendAnswer(boolean correct){
        try {
            outputStream.writeObject(new responseMessage(correct));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendUnavailableName}: sends a unavailableNamesMessage
     * @param unavailableNames: ArrayList<String>
     */
    @Override
    public void sendUnavailableName(ArrayList<String> unavailableNames) {
        try {
            outputStream.writeObject(new unavailableNamesMessage(unavailableNames));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getName}: receives a chosenNameMessage
     * @return chosenNameMessage
     */
    @Override
    public chosenNameMessage getName(){
        try {
            return (chosenNameMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAvailableColor}: sends a availableColorsMessage
     * @param availableColors: ArrayList<String>
     */
    @Override
    public void sendAvailableColor(ArrayList<String> availableColors){
        try {
            outputStream.writeObject(new availableColorsMessage(availableColors));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getColor}: receives a chosenColorMessage
     * @return chosenColorMessage
     */
    @Override
    public chosenColorMessage getColor(){
        try {
            return (chosenColorMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendCurrentState}: sends a currentStateMessage
     * @param currentState: currentStateMessage
     */
    @Override
    public void sendCurrentState(currentStateMessage currentState){
        try {
            outputStream.writeObject(currentState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getStaterCard}: receives a starterCardMessage
     * @return starterCardMessage
     */
    @Override
    public starterCardMessage getStaterCard(){
        try {
            return (starterCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getChosenObjective}: receives a objectiveCardMessage
     * @return objectiveCardMessage
     */
    @Override
    public objectiveCardMessage getChosenObjective(){
        try {
            return (objectiveCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getPlaceCard}: receives a placeCardMessage
     * @return placeCardMessage
     */
    @Override
    public placeCardMessage getPlaceCard(){
        try {
            return (placeCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getChosenPick}: receives a pickCardMessage
     * @return pickCardMessage
     */
    @Override
    public pickCardMessage getChosenPick(){
        try {
            return (pickCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendEndGame}: sends a declareWinnerMessage
     * @param scores: HashMap<String, Integer> score
     * @param numberOfObjectives: HashMap<String, Integer> score
     */
    @Override
    public void sendEndGame(HashMap<String, Integer> scores, HashMap<String, Integer> numberOfObjectives){
        try {
            outputStream.writeObject(new declareWinnerMessage(scores, numberOfObjectives));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendUpdatePlayer}: sends a updatePlayerMessage
     * @param updateMessage: updatePlayerMessage
     */
    @Override
    public void sendUpdatePlayer(updatePlayerMessage updateMessage) {
        try {
            outputStream.writeObject(updateMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code closeConnection}: closes the connection
     */
    @Override
    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
