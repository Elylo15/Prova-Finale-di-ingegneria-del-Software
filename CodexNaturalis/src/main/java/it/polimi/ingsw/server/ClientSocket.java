package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.connectionState.*;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.messages.objectiveState.objectiveCardMessage;
import it.polimi.ingsw.messages.playerTurnState.pickCardMessage;
import it.polimi.ingsw.messages.playerTurnState.placeCardMessage;
import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.messages.responseMessage;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.messages.staterCardState.starterCardMessage;
import it.polimi.ingsw.messages.waitingForPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.messages.waitingForPlayerState.newHostMessage;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientSocket extends ClientConnection implements Serializable {
    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    /**
     * method {@code ClientSocket}: constructs a new ClientSocket
     *
     * @param IP:   String, Ip of the client
     * @param port: String, port of the client
     */
    public ClientSocket(String IP, String port, Socket socket) {
        super(IP, port);
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Empty method, needed for executors to work.
     */
    @Override
    public void run() {

    }

    /**
     * method {@code getServerOption}: sends an empty server option message and expects an answer.
     *
     * @return serverOptionMessage
     */
    @Override
    public synchronized serverOptionMessage getServerOption(ArrayList<Integer> waitingMatches, ArrayList<Integer> runningMatches, ArrayList<Integer> savedMatches) {
        try {
            outputStream.reset();
            outputStream.writeObject(new serverOptionMessage(false, null, null, false, null, waitingMatches, runningMatches, savedMatches));
            outputStream.flush();
            return (serverOptionMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method {@code sendNewHostMessage}: creates and sends a newHostMessage
     *
     * @param hostNickname: String
     */
    @Override
    public synchronized void sendNewHostMessage(String hostNickname) {
        try {
            outputStream.reset();
            outputStream.writeObject(new newHostMessage(hostNickname));
            outputStream.flush();
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code getExpectedPlayer}: receives a expectedPlayerMessage
     *
     * @return expectedPlayersMessage
     */
    @Override
    public synchronized expectedPlayersMessage getExpectedPlayer() {
        try {
            return (expectedPlayersMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method {@code sendAnswer}: sends a connectionResponseMessage
     *
     * @param message: connectionResponseMessage
     */
    @Override
    public synchronized void sendAnswerToConnection(connectionResponseMessage message) {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code sendAnswer}:
     * create a new responseMessage object passing in its constructor the boolean parameter of the method
     * and sends it
     *
     * @param correct: boolean
     */
    @Override
    public synchronized void sendAnswer(boolean correct) {
        try {
            outputStream.reset();
            outputStream.writeObject(new responseMessage(correct));
            outputStream.flush();
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code sendUnavailableName}: creates and sends a unavailableNamesMessage
     *
     * @param unavailableNames: ArrayList<String>
     */
    @Override
    protected synchronized void sendUnavailableName(ArrayList<String> unavailableNames) {
        try {
            outputStream.reset();
            outputStream.writeObject(new unavailableNamesMessage(unavailableNames));
            outputStream.flush();
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code getName}: receives a chosenNameMessage and returns it
     *
     * @return chosenNameMessage
     */
    @Override
    public synchronized chosenNameMessage getName(ArrayList<String> unavailableNames) {
        try {
            this.sendUnavailableName(unavailableNames);
            return (chosenNameMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method {@code sendAvailableColor}: creates and sends a availableColorsMessage
     *
     * @param availableColors: ArrayList<String>
     */
    @Override
    protected synchronized void sendAvailableColor(ArrayList<String> availableColors) {
        try {
            outputStream.reset();
            outputStream.writeObject(new availableColorsMessage(availableColors));
            outputStream.flush();
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code getColor}: receives a chosenColorMessage and returns it
     *
     * @return chosenColorMessage
     */
    @Override
    public synchronized chosenColorMessage getColor(ArrayList<String> color) {
        try {
            this.sendAvailableColor(color);
            return (chosenColorMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method {@code sendCurrentState}: sends a currentStateMessage
     *
     * @param currentState: currentStateMessage
     */
    @Override
    public void sendCurrentState(currentStateMessage currentState) {
        try {
            outputStream.reset();
            outputStream.reset();
            outputStream.writeObject(currentState);
            outputStream.flush();
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code getStaterCard}: receives a starterCardMessage and returns it
     *
     * @return starterCardMessage
     */
    @Override
    public synchronized starterCardMessage getStaterCard() {
        try {
            return (starterCardMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method {@code getChosenObjective}: sends the array of ObjectiveCards it receives as parameter and
     * receives an objectiveCardMessage
     *
     * @return objectiveCardMessage
     */
    @Override
    public synchronized objectiveCardMessage getChosenObjective(ArrayList<ObjectiveCard> objectiveCards) {
        try {
            outputStream.reset();
            outputStream.writeObject(new objectiveCardMessage(objectiveCards));
            outputStream.flush();
        } catch (IOException ignore) {
        }
        try {
            return (objectiveCardMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * method {@code getPlaceCard}: receives a placeCardMessage and returns it
     *
     * @return placeCardMessage
     */
    @Override
    public synchronized placeCardMessage getPlaceCard() {
        try {
            outputStream.reset();
            return (placeCardMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method {@code getChosenPick}: receives a pickCardMessage and returns it
     *
     * @return pickCardMessage
     */
    @Override
    public synchronized pickCardMessage getChosenPick() {
        try {
            return (pickCardMessage) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method {@code sendEndGame}: sends a declareWinnerMessage to communicate the scores and the number of objectives achieved by each player
     *
     * @param scores:             HashMap<String, Integer> score
     * @param numberOfObjectives: HashMap<String, Integer> score
     */
    @Override
    public synchronized void sendEndGame(HashMap<String, Integer> scores, HashMap<String, Integer> numberOfObjectives) {
        try {
            outputStream.reset();
            outputStream.writeObject(new declareWinnerMessage(scores, numberOfObjectives));
            outputStream.flush();
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code sendUpdatePlayer}: sends an updatePlayerMessage
     *
     * @param updateMessage: updatePlayerMessage
     */
    @Override
    public synchronized void sendUpdatePlayer(updatePlayerMessage updateMessage) {
        try {
            outputStream.reset();
            outputStream.writeObject(updateMessage);
            outputStream.flush();
        } catch (Exception ignore) {
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
        } catch (Exception ignore) {
        }
    }

    /**
     * method {@code isConnected}: sends a currentStateMessage to check if the connection is still active
     * and expects an answer
     *
     * @return boolean true if the connection is active, false or no answer otherwise
     */
    @Override
    public boolean isConnected() {
        try {
            String answer = "ACK";
            currentStateMessage message = new currentStateMessage(null, null, "AnswerCheckConnection", false, null, null, null);
            this.sendCurrentState(message);
            return answer.equals(inputStream.readObject());
        } catch (Exception e) {
            return false;
        }
    }
}
