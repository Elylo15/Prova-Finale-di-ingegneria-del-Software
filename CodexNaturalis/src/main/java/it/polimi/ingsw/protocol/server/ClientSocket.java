package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
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
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Socket socket;

    /**
     * method {@code ClientSocket}: constructs a new ClientSocket
     * @param IP: String
     * @param port: String
     */
    public ClientSocket(String IP, String port, Socket socket) {
        super(IP, port);
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code run}: initializes the outputStream and inputStream
     */
    @Override
    public void run() {

    }

    @Override
    public serverOptionMessage getServerOption() {
        try {
            return (serverOptionMessage) inputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendNewHostMessage}: creates and sends a newHostMessage
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
     * method {@code sendAnswer}:
     * create a new responseMessage object passing in its constructor the boolean parameter of the method
     * and sends it
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
     * method {@code sendUnavailableName}: creates and sends a unavailableNamesMessage
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
     * method {@code getName}: receives a chosenNameMessage and returns it
     * @return chosenNameMessage
     */
    @Override
    public chosenNameMessage getName(ArrayList<String> unavailableNames) {
        try {
            this.sendUnavailableName(unavailableNames);
            return (chosenNameMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAvailableColor}: creates and sends a availableColorsMessage
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
     * method {@code getColor}: receives a chosenColorMessage and returns it
     * @return chosenColorMessage
     */
    @Override
    public chosenColorMessage getColor(ArrayList<String> color){
        try {
            this.sendAvailableColor(color);
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
     * method {@code getStaterCard}: receives a starterCardMessage and returns it
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
     * method {@code getChosenObjective}: sends the array of ObjectiveCards it receives as parameter and
     * receives an objectiveCardMessage
     * @return objectiveCardMessage
     */
    @Override
    public objectiveCardMessage getChosenObjective(ObjectiveCard[] objectiveCards){
        try {
            outputStream.writeObject(objectiveCards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return (objectiveCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * method {@code getPlaceCard}: receives a placeCardMessage and returns it
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
     * method {@code getChosenPick}: receives a pickCardMessage and returns it
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
     * method {@code sendEndGame}: sends a declareWinnerMessage to communicate the scores and the number of objectives achieved by each player
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
     * method {@code sendUpdatePlayer}: sends an updatePlayerMessage
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
     * method {@code closeConnection}: closes the connection closing socket, inputStream, outputStream
     */
    @Override
    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
