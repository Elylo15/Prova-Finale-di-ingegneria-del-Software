package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.connectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.objectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.playerTurnState.pickCardMessage;
import it.polimi.ingsw.protocol.messages.playerTurnState.placeCardMessage;
import it.polimi.ingsw.protocol.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.staterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.waitingForPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import it.polimi.ingsw.protocol.server.ClientConnection;
import it.polimi.ingsw.protocol.server.ClientSocket;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerSocketTest {
    ControllerSocket controller;
    ClientConnection connection;

    private ServerSocket serverSocket;
    private Socket socket;
    private ThreadPoolExecutor executor;

    //in order to see if the controller can correctly send and receive messages we create a ClientSocket object to exchange them
    @BeforeEach
    void setUp() throws IOException {
        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

        serverSocket = new ServerSocket(1024);
        executor.submit(() -> {
            try {
                socket = serverSocket.accept();
                connection = new ClientSocket(socket.getInetAddress().toString(), Integer.toString(socket.getPort()), socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        controller = new ControllerSocket("localhost", "1024");
        controller.connectToServer("localhost", "1024");
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
    @DisplayName("Connection to server and receiving an answer")
    void connectToServerTest() throws InterruptedException {
        connection.sendAnswerToConnection(new connectionResponseMessage(true));
        assertTrue(controller.answerConnection().getCorrect());
    }


    @Test
    @DisplayName("Getting the current state")
    void getCurrentTest() {
        //controller should correctly receive a currentStateMessage from server
        CommonArea area = new CommonArea();
        Player player = new Player("player", "red",area);
        currentStateMessage current = new currentStateMessage(player, player, "State", false, null, null, 0);
        connection.sendCurrentState(current);
        currentStateMessage controllerCurrent = controller.getCurrent();
        Assertions.assertEquals(player.getNickname(),controllerCurrent.getCurrentPlayer().getNickname()); //the player received must be the same as the player send
        Assertions.assertEquals(player.getColor(),controllerCurrent.getPlayer().getColor());
        Assertions.assertEquals("State", controllerCurrent.getStateName());
        Assertions.assertFalse(controllerCurrent.isLastTurn());
        Assertions.assertEquals(0, (int) controllerCurrent.getMatchID());
    }

    @Test
    @DisplayName("Getting the server options")
    void serverOptionsTest() {
        //controller should correctly a serverOption message
        ArrayList<Integer> runningMatch = new ArrayList<>();
        runningMatch.add(234);
        Future<serverOptionMessage> futureMessage = executor.submit(() -> connection.getServerOption(null, runningMatch, null));
        serverOptionMessage options = controller.serverOptions();
        Assertions.assertFalse(options.isNewMatch());
        Assertions.assertNull(options.getSavedMatchID());
        Assertions.assertFalse(options.isLoadMatch());
        Assertions.assertEquals(runningMatch.get(0), options.getRunningMatches().get(0));
    }

    @Test
    @DisplayName("Sending a correct answer")
    void correctAnswerTest() {
        connection.sendAnswer(true);
        responseMessage response = controller.correctAnswer();
        Assertions.assertTrue(response.getCorrect());
    }

    @Test
    @DisplayName("Sending options to the server")
    void sendOptionsTest() {
        //controller should correctly send a serverOptionMessage
        int matchID = 34;
        int startedID = 67;
        int savedID = 234;
        serverOptionMessage options = new serverOptionMessage(true, matchID, startedID, false, savedID);
        controller.sendOptions(options); //controller sends message
        serverOptionMessage receivedOptions = connection.getServerOption(null, null, null);
        Assertions.assertTrue(receivedOptions.isNewMatch());
        Assertions.assertEquals(matchID, receivedOptions.getMatchID());
        Assertions.assertEquals(startedID, receivedOptions.getStartedMatchID());
        Assertions.assertFalse(receivedOptions.isLoadMatch());
        Assertions.assertEquals(savedID, receivedOptions.getSavedMatchID());
    }

    @Test
    @DisplayName("Getting unavailable names and choosing a name")
    void NamesTest() throws InterruptedException, ExecutionException {
        //controller should correctly receive an unavailableNameMessage and send the name chosen
        ArrayList<String> unavailableNames = new ArrayList<>();
        unavailableNames.add("Alfa");
        unavailableNames.add("Beta");
        Future<String> name = executor.submit(() -> connection.getName(unavailableNames).getName());
        ArrayList<String> names = controller.getUnavailableName().getNames();
        Assertions.assertEquals("Alfa", names.get(0));
        Assertions.assertEquals("Beta", names.get(1));

        String playername = "name";
        controller.chooseName(playername); //controller sends name chosen
        String nameReceived = name.get(); //clientSocket receives name chosen
        Assertions.assertEquals(playername, nameReceived);
    }

    @Test
    @DisplayName("Getting available colors and choosing a color")
    void ColorsTest() throws ExecutionException, InterruptedException {
        //controller should correctly receive an availableColors message and sends the color chosen
        ArrayList<String> availableColors = new ArrayList<>();
        availableColors.add("Red");
        availableColors.add("Blue");
        Future<String> color = executor.submit(() -> connection.getColor(availableColors).getColor());
        ArrayList<String> colors = controller.getAvailableColor().getColors();
        Assertions.assertEquals("Red", colors.get(0));
        Assertions.assertEquals("Blue", colors.get(1));

        String colorChosen = "purple";
        controller.chooseColor(colorChosen);
        Assertions.assertEquals(colorChosen, color.get());
    }

    @Test
    @DisplayName("Getting the new host")
    void newHostTest() {
        connection.sendNewHostMessage("Alfa");
        Assertions.assertEquals("Alfa", controller.newHost().getNewHostNickname());
    }

    @Test
    @DisplayName("Sending the expected players")
    void expectedPlayersTest() {
        //controller should correctly create and send an expectedPlayersMessage
        controller.expectedPlayers(3, false);
        expectedPlayersMessage expected = connection.getExpectedPlayer();
        Assertions.assertEquals(3, expected.getExpectedPlayers());
        Assertions.assertFalse(expected.isNoResponse());
    }

    @Test
    @DisplayName("Placing the starter card")
    void placeStarterTest() {
        //controller should correctly create and send a starterCard message
        controller.placeStarter(0, false);
        starterCardMessage starterReceived = connection.getStaterCard();
        Assertions.assertEquals(0, starterReceived.getSide());
        Assertions.assertFalse(starterReceived.isNoResponse());
    }

    @Test
    @DisplayName("Getting the objective cards and choosing one")
    void ObjectiveCardsTest() throws ExecutionException, InterruptedException {
        //controller should correctly receive an objectiveCardMessage and send the objective chosen
        ArrayList<ObjectiveCard> cards = new ArrayList<>();
        CommonArea area = (new LoadDecks()).load();
        cards.add(area.drawObjectiveCard());
        cards.add(area.drawObjectiveCard());
        Future<objectiveCardMessage> messageFuture = executor.submit(() -> connection.getChosenObjective(cards));
        ArrayList<ObjectiveCard> receivedCards = controller.getObjectiveCards().getObjectiveCard();
        Assertions.assertEquals(cards.get(0).getID(), receivedCards.get(0).getID());
        Assertions.assertEquals(cards.get(1).getID(), receivedCards.get(1).getID());

        controller.chooseObjective(1, false);
        int choiceReceived = messageFuture.get().getChoice();
        Assertions.assertEquals(1, choiceReceived);
        Assertions.assertFalse(messageFuture.get().isNoResponse());
    }

    @Test
    @DisplayName("Placing a card")
    void placeCardTest() {
        //controller should correctly create and send a placeCardMessage
        controller.placeCard(0, 0, -1, 1, false);
        placeCardMessage messageReceived = connection.getPlaceCard();
        Assertions.assertEquals(0,messageReceived.getCard());
        Assertions.assertEquals(0,messageReceived.getFront());
        Assertions.assertEquals(-1,messageReceived.getRow());
        Assertions.assertEquals(1,messageReceived.getColumn());
        Assertions.assertFalse(messageReceived.isNoResponse());

    }

    @Test
    @DisplayName("Picking a card")
    void pickCardTest() {
        //controller should correctly create and send a pickCardMessage
        controller.pickCard(0, false);
        pickCardMessage messageReceived = connection.getChosenPick();
        Assertions.assertEquals(0, messageReceived.getCard());
        Assertions.assertFalse(messageReceived.isNoResponse());
    }

    @Test
    @DisplayName("Updating the player")
    void updatePlayerTest() {
        //controller should correctly receive an updatePlayerMessage
        CommonArea area = new CommonArea();
        Player player = new Player("Alfa", "Red", area);
        updatePlayerMessage message = new updatePlayerMessage(player, "viewer");
        connection.sendUpdatePlayer(message);
        updatePlayerMessage received = controller.updatePlayer();
        Assertions.assertEquals("Alfa", received.getPlayer().getNickname());
        Assertions.assertEquals("Red", received.getPlayer().getColor());
        Assertions.assertEquals("viewer", received.getNicknameViewer());
        Assertions.assertNotNull(received.getPlayer().getPlayerArea());
    }

    @Test
    @DisplayName("Ending the game")
    void endGameTest() {
        //controller should correctly receive a declareWinner message
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

        Assertions.assertThrows(RuntimeException.class, () -> controller.getCurrent());
        Assertions.assertThrows(RuntimeException.class, () -> controller.serverOptions());
        Assertions.assertThrows(RuntimeException.class, () -> controller.correctAnswer());
        Assertions.assertThrows(RuntimeException.class, () -> controller.getUnavailableName());
        Assertions.assertThrows(RuntimeException.class, () -> controller.getAvailableColor());
        Assertions.assertThrows(RuntimeException.class, () -> controller.newHost());
        Assertions.assertThrows(RuntimeException.class, () -> controller.getObjectiveCards());
        Assertions.assertThrows(RuntimeException.class, () -> controller.updatePlayer());
        Assertions.assertThrows(RuntimeException.class, () -> controller.endGame());
        Assertions.assertThrows(RuntimeException.class, () -> controller.sendAnswerToPing());
        Assertions.assertThrows(RuntimeException.class, () -> controller.expectedPlayers(0, false));
        Assertions.assertThrows(RuntimeException.class, () -> controller.placeStarter(0, false));
        Assertions.assertThrows(RuntimeException.class, () -> controller.chooseName("Alfa"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.chooseColor("Red"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.chooseObjective(0, false));
        Assertions.assertThrows(RuntimeException.class, () -> controller.placeCard(0, 0, 0, 0, false));
        Assertions.assertThrows(RuntimeException.class, () -> controller.pickCard(0, false));
        Assertions.assertThrows(RuntimeException.class, () -> controller.sendOptions(new serverOptionMessage(false, 0, 0, false, 0)));

    }

}