package it.polimi.ingsw.client.controller;

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * This class is the controller for the socket connection.
 * It handles the connection to the server and the messages exchange.
 */
public class ControllerSocket extends Controller {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /**
     * method {@code ControllerSocket}: creates a new ControllerSocket
     *
     * @param serverIP:   String
     * @param serverPort: String
     */
    public ControllerSocket(String serverIP, String serverPort) {
        super(serverIP, serverPort);
    }

    /**
     * method {@code connectToServer}: connects to an existing server
     *
     * @param serverIP:   String
     * @param serverPort: String
     */
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

    /**
     * method {@code answerConnection}: receives a connectionResponseMessage
     *
     * @return connectionResponseMessage
     */
    @Override
    public connectionResponseMessage answerConnection() {
        try {
            return (connectionResponseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getCurrent}: receives a currentStateMessage
     *
     * @return currentStateMessage
     */
    @Override
    public currentStateMessage getCurrent() {
        try {
            return (currentStateMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code serverOptions}: receives an empty serverOptionMessage
     *
     * @return serverOptionMessage
     */
    @Override
    public serverOptionMessage serverOptions() {
        try {
            return (serverOptionMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendOptions}: sends a serverOptionMessage
     *
     * @param options: serverOptionMessage
     */
    @Override
    public void sendOptions(serverOptionMessage options) {
        try {
            outputStream.reset();
            outputStream.writeObject(options);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code correctAnswer}: receives a responseMessage
     *
     * @return responseMessage
     */
    @Override
    public responseMessage correctAnswer() {
        try {
            return (responseMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getUnavailableName}: receives a unavailableNamesMessage
     *
     * @return unavailableNamesMessage
     */
    @Override
    public unavailableNamesMessage getUnavailableName() {
        try {
            return (unavailableNamesMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code chooseName}: sends a chosenNameMessage
     *
     * @param name: String
     */
    @Override
    public void chooseName(String name) {
        try {
            outputStream.reset();
            outputStream.writeObject(new chosenNameMessage(name));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getAvailableColor}: receives a availableColorsMessage
     *
     * @return availableColorsMessage
     */
    @Override
    public availableColorsMessage getAvailableColor() {
        try {
            return (availableColorsMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code chooseColor}: sends a chosenNameMessage
     *
     * @param color: String
     */
    @Override
    public void chooseColor(String color) {
        try {
            outputStream.reset();
            outputStream.writeObject(new chosenColorMessage(color));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code newHost}: receives a newHostMessage
     *
     * @return newHostMessage
     */
    @Override
    public newHostMessage newHost() {
        try {
            return (newHostMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code expectedPlayers}: sends a expectedPlayersMessage
     *
     * @param expected:   int
     * @param noResponse: boolean
     */
    @Override
    public void expectedPlayers(int expected, boolean noResponse) {
        try {
            outputStream.reset();
            outputStream.writeObject(new expectedPlayersMessage(expected, noResponse));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code placeStarter}: sends a starterCardMessage
     *
     * @param side:       int
     * @param noResponse: boolean
     */
    @Override
    public void placeStarter(int side, boolean noResponse) {
        try {
            outputStream.reset();
            outputStream.writeObject(new starterCardMessage(side, noResponse));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getObjectiveCards}: receives a objectiveCardMessage
     *
     * @return objectiveCardMessage
     */
    @Override
    public objectiveCardMessage getObjectiveCards() {
        try {
            return (objectiveCardMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code chooseObjective}: sends a objectiveCardMessage
     *
     * @param pick:       int
     * @param noResponse: boolean
     */
    @Override
    public void chooseObjective(int pick, boolean noResponse) {
        try {
            outputStream.reset();
            outputStream.writeObject(new objectiveCardMessage(pick, noResponse));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code placeCard}: sends a PlaceCardMessage
     *
     * @param card:       int
     * @param side:       int
     * @param x:          int
     * @param y:          int
     * @param noResponse: boolean
     */
    @Override
    public void placeCard(int card, int side, int x, int y, boolean noResponse) {
        try {
            outputStream.reset();
            outputStream.writeObject(new placeCardMessage(card, side, x, y, noResponse));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code pickCard}: sends a pickCardMessage
     *
     * @param card:       int
     * @param noResponse: boolean
     */
    @Override
    public void pickCard(int card, boolean noResponse) {
        try {
            outputStream.reset();
            outputStream.writeObject(new pickCardMessage(card, noResponse));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code updatePlayer}: receives a updatePlayerMessage
     *
     * @return updatePlayerMessage
     */
    @Override
    public updatePlayerMessage updatePlayer() {
        try {
            return (updatePlayerMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code endGame}: receives a declareWinnerMessage
     *
     * @return declareWinnerMessage
     */
    @Override
    public declareWinnerMessage endGame() {
        try {
            return (declareWinnerMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAnswerToPing}: sends an ACK message
     */
    @Override
    public void sendAnswerToPing() {
        try {
            outputStream.reset();
            outputStream.writeObject("ACK");
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
