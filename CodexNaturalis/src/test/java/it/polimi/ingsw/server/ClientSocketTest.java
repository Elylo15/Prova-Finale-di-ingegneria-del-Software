package it.polimi.ingsw.server;

import it.polimi.ingsw.client.controller.ControllerSocket;
import it.polimi.ingsw.messages.connectionState.connectionResponseMessage;
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
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientSocketTest {
    ControllerSocket controller;
    ClientConnection connection;
    private ServerSocket serverSocket;
    private Socket socket;
    private ThreadPoolExecutor executor;

    //in order to see if ClientSocket can correctly send and receive messages we create a ControllerSocket object to exchange them



    @BeforeEach
    void setUp() throws IOException {
        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());

        serverSocket = new ServerSocket(1024);
        Future<ClientSocket> connectionFuture = executor.submit(() -> {
            try {
                socket = serverSocket.accept();
                return new ClientSocket(socket.getInetAddress().toString(), Integer.toString(socket.getPort()), socket);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });

        controller = new ControllerSocket("localhost", "1024");
        controller.connectToServer("localhost", "1024");
        try {
            connection = connectionFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        connection.closeConnection();
        if (socket != null)
            socket.close();
        if (serverSocket != null)
            serverSocket.close();
        executor.shutdown();
    }


    @Test
    @DisplayName("Sending answer after connection")
    public void sendAnswerToConnectionTest() {
        connectionResponseMessage expectedMessage = new connectionResponseMessage(true);
        executor.submit(() -> connection.sendAnswerToConnection(expectedMessage));
        connectionResponseMessage response = controller.answerConnection();
        assertEquals(expectedMessage.getCorrect(), response.getCorrect());
    }


    @Test
    @DisplayName("Sending the current state")
    void getCurrentTest() {
        //ClientSocket should correctly send a currentStateMessage
        Player player = new Player("player", "red", null);
        currentStateMessage current = new currentStateMessage(player, player, "State", false, null, null, 0);
        connection.sendCurrentState(current);
        currentStateMessage controllerCurrent = controller.getCurrent();  //controller receives message
        Assertions.assertEquals("State", controllerCurrent.getStateName());
        Assertions.assertFalse(controllerCurrent.isLastTurn());
        Assertions.assertEquals("red", controllerCurrent.getCurrentPlayer().getColor());
        Assertions.assertEquals("player", controllerCurrent.getCurrentPlayer().getNickname());
        Assertions.assertEquals(0, (int) controllerCurrent.getMatchID());
    }

    @Test
    @DisplayName("Sending and receiving server options")
    public void getServerOptionsTest() {
        //ClientSocket should correctly create and send a serverOptionMessage and then receive a serverOptionMessage from controller
        serverOptionMessage options = new serverOptionMessage(true, 0, 1, false, 0);
        controller.sendOptions(options); //controller sends message
        serverOptionMessage receivedOptions = connection.getServerOption(null, null, null); //message received from controller

        Assertions.assertTrue(receivedOptions.isNewMatch());
        Assertions.assertEquals(0, receivedOptions.getMatchID());
        Assertions.assertEquals(1, receivedOptions.getStartedMatchID());
        Assertions.assertFalse(receivedOptions.isLoadMatch());
        Assertions.assertEquals(0, receivedOptions.getSavedMatchID());
    }


    @Test
    @DisplayName("Sending a correct answer")
    void correctAnswerTest() {
        connection.sendAnswer(true);
        responseMessage response = controller.correctAnswer();
        Assertions.assertTrue(response.getCorrect());
    }


    @Test
    @DisplayName("Sending unavailable names and receiving a name")
    void NamesTest() throws InterruptedException, ExecutionException {
        //ClientSocket should correctly an unavailableNamesMessage and then receive a chosenNameMessage
        ArrayList<String> unavailableNames = new ArrayList<>();
        unavailableNames.add("Alfa");
        unavailableNames.add("Beta");
        Future<String> nameReceived = executor.submit(() -> connection.getName(unavailableNames).getName());
        ArrayList<String> names = controller.getUnavailableName().getNames();
        Assertions.assertEquals("Alfa", names.get(0));
        Assertions.assertEquals("Beta", names.get(1));
        controller.chooseName("Alfa");
        Assertions.assertEquals("Alfa", nameReceived.get());
    }

    @Test
    @DisplayName("Sending available colors and receiving a color")
    void ColorsTest() throws ExecutionException, InterruptedException {
        //ClientSocket should correctly create and send an availableColorsMessage and then receive a chosenColorMessage
        ArrayList<String> availableColors = new ArrayList<>();
        availableColors.add("Red");
        availableColors.add("Blue");
        availableColors.add("purple");
        Future<String> color = executor.submit(() -> connection.getColor(availableColors).getColor());
        ArrayList<String> colorsReceived = controller.getAvailableColor().getColors();
        Assertions.assertEquals("Red", colorsReceived.get(0));
        Assertions.assertEquals("Blue", colorsReceived.get(1));
        Assertions.assertEquals("purple", colorsReceived.get(2));

        controller.chooseColor("Red");
        Assertions.assertEquals("Red", color.get());
    }

    @Test
    @DisplayName("Sending the new host")
    void newHostTest() {
        //ClientSocket should correctly create and send a newHostMessage
        connection.sendNewHostMessage("Alfa");
        Assertions.assertEquals("Alfa", controller.newHost().getNewHostNickname());
    }

    @Test
    @DisplayName("Receiving the expected players")
    void expectedPlayersTest() {
        //ClientSocket should correctly receive an expectedPlayersMessage
        controller.expectedPlayers(3, false);
        expectedPlayersMessage expected = connection.getExpectedPlayer();
        Assertions.assertEquals(3, expected.getExpectedPlayers());
        Assertions.assertFalse(expected.isNoResponse());
    }

    @Test
    @DisplayName("Placing the starter card")
    void placeStarterTest() {
        //ClientSocket should correctly receive a starterCardMessage
        controller.placeStarter(0, false);
        starterCardMessage starter = connection.getStaterCard();
        Assertions.assertEquals(0, starter.getSide());
        Assertions.assertFalse(starter.isNoResponse());
    }

    @Test
    @DisplayName("Sending the objective cards and receiving an answer")
    void ObjectiveCardsTest() throws ExecutionException, InterruptedException {
        //ClientSocket should correctly create and send an objectiveCardMessage and then receive an objectiveCardMessage from controller
        ArrayList<ObjectiveCard> cards = new ArrayList<>();
        CommonArea area = (new LoadDecks()).load();
        cards.add(area.drawObjectiveCard());
        cards.add(area.drawObjectiveCard());

        Future<objectiveCardMessage> messageFuture = executor.submit(() -> connection.getChosenObjective(cards));
        ArrayList<ObjectiveCard> receivedCards = controller.getObjectiveCards().getObjectiveCard();
        Assertions.assertEquals(cards.get(0).getID(), receivedCards.get(0).getID());
        Assertions.assertEquals(cards.get(1).getID(), receivedCards.get(1).getID());
        controller.chooseObjective(0, false);
        Assertions.assertEquals(0, messageFuture.get().getChoice());
        Assertions.assertFalse(messageFuture.get().isNoResponse());
    }

    @Test
    @DisplayName("Placing a card")
    void placeCardTest() {
        //ClientSocket should correctly receive a placeCardMessage
        controller.placeCard(0, 0, 0, 0, false);
        placeCardMessage message = connection.getPlaceCard();
        Assertions.assertEquals(0, message.getCard());
        Assertions.assertEquals(0, message.getFront());
        Assertions.assertEquals(0, message.getRow());
        Assertions.assertEquals(0, message.getColumn());
        Assertions.assertFalse(message.isNoResponse());
    }

    @Test
    @DisplayName("Picking a card")
    void pickCardTest() {
        //ClientSocket should correctly receive a pickCardMessage
        controller.pickCard(0, false);
        pickCardMessage messageReceived = connection.getChosenPick();
        Assertions.assertEquals(0,messageReceived.getCard());
        Assertions.assertFalse(messageReceived.isNoResponse());
    }

    @Test
    @DisplayName("Updating the player")
    void updatePlayerTest() throws InvalidIdException {
        //ClientSocket should correctly send an updatePlayerMessage
        CommonArea area = new CommonArea();
        ResourceCard testCard = new ResourceCard(1);
        area.getD1().addCard(testCard);
        Player player = new Player("Alfa", "Red", area);
        updatePlayerMessage message = new updatePlayerMessage(player, "Alfa");
        connection.sendUpdatePlayer(message);
        updatePlayerMessage received = controller.updatePlayer();
        Assertions.assertEquals("Alfa", received.getPlayer().getNickname());
        Assertions.assertEquals("Red", received.getPlayer().getColor());
        //commonArea of the player send in the message is equal to the commonArea of the player received
        Assertions.assertEquals(area.getD1().getSize(),received.getPlayer().getCommonArea().getD1().getSize());
        Assertions.assertEquals(area.getD1().getCard(1),received.getPlayer().getCommonArea().getD1().getCard(1));

    }

    @Test
    @DisplayName("Ending the game")
    void endGameTest() {
        //ClientSocket should correctly create and send a declareWinnerMessage
        HashMap<String, Integer> score = new HashMap<>();
        score.put("Alfa", 10);
        score.put("Beta", 20);
        HashMap<String, Integer> objectives = new HashMap<>();
        objectives.put("Alfa", 2);
        objectives.put("Beta", 3);
        connection.sendEndGame(score, objectives);
        declareWinnerMessage message = controller.endGame();
        Assertions.assertEquals(10, message.getPlayersPoints().get("Alfa"));
        Assertions.assertEquals(20, message.getPlayersPoints().get("Beta"));
        Assertions.assertEquals(2, message.getNumberOfObjects().get("Alfa"));
        Assertions.assertEquals(3, message.getNumberOfObjects().get("Beta"));
    }

    @Test
    @DisplayName("Sending an answer to a ping test")
    void sendAnswerToPingTest() {
        controller.sendAnswerToPing();
        Assertions.assertTrue(connection.isConnected());
    }


    @Test
    @DisplayName("Closing the connection")
    void closeConnectionTest() {
        connection.closeConnection();
        Assertions.assertFalse(connection.isConnected());

        Assertions.assertNull(connection.getServerOption(null, null, null));
        Assertions.assertNull(connection.getExpectedPlayer());
        Assertions.assertNull(connection.getStaterCard());
        Assertions.assertNull(connection.getChosenObjective(null));
        Assertions.assertNull(connection.getPlaceCard());
        Assertions.assertNull(connection.getChosenPick());
        Assertions.assertNull(connection.getName(null));
        Assertions.assertNull(connection.getColor(null));
    }


}