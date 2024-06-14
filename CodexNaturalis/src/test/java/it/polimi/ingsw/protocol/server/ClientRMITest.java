package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.messages.connectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.objectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.playerTurnState.placeCardMessage;
import it.polimi.ingsw.protocol.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.staterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.waitingForPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import it.polimi.ingsw.protocol.server.rmi.MainRemoteServer;
import org.junit.jupiter.api.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientRMITest {

    private MainRemoteServer server;
    private Registry registry;
    private ThreadPoolExecutor executor;
    private ClientConnection connection;
    private ControllerRMI controller;

    private ClientConnection waitForConnectionServer(Registry registry) {
        try {
            return server.clientConnected(registry);
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Server exception: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @BeforeEach
    public void setUp() throws ExecutionException, InterruptedException {
        int corePoolSize = 8;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());

        try {
            registry = LocateRegistry.getRegistry(1099);
            // Checks if the registry is running by listing bindings
            registry.list();
        } catch (RemoteException e) {
            // The server is not running, creating a new one
            try {
                registry = LocateRegistry.createRegistry(1099);
            } catch (RemoteException re) {
                throw new RuntimeException("Failed to create registry", re);
            }
        }

        try {
            server = new MainRemoteServer();
            registry.rebind("MainServer", server);
        } catch (Exception e) {
            System.err.println("Error binding the server: " + e.getMessage());
        }


        Future<ClientConnection> connectionFuture = executor.submit(() -> this.waitForConnectionServer(registry));
        controller = new ControllerRMI("localhost", "1099");
        controller.connectToServer("localhost", "1099");
        connection = connectionFuture.get();
    }

    @AfterEach
    public void tearDown() throws NotBoundException, RemoteException {
        registry.unbind("MainServer");
        UnicastRemoteObject.unexportObject(server, true);
        UnicastRemoteObject.unexportObject(registry, true);
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
        currentStateMessage current = new currentStateMessage(null, null, "State", false, null, null, 0);
        connection.sendCurrentState(current);
        currentStateMessage controllerCurrent = controller.getCurrent();
        Assertions.assertEquals("State", controllerCurrent.getStateName());
        Assertions.assertFalse(controllerCurrent.isLastTurn());
        Assertions.assertEquals(0, (int) controllerCurrent.getMatchID());
    }

    @Test
    @DisplayName("Sending and receiving server options")
    public void getServerOptionsTest() {
        serverOptionMessage options = new serverOptionMessage(true, 0, 1, false, 0);
        controller.sendOptions(options);
        serverOptionMessage receivedOptions = connection.getServerOption(null, null, null);
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
        ArrayList<String> unavailableNames = new ArrayList<>();
        unavailableNames.add("Alfa");
        unavailableNames.add("Beta");
        Future<String> name = executor.submit(() -> connection.getName(unavailableNames).getName());
        ArrayList<String> names = controller.getUnavailableName().getNames();
        Assertions.assertEquals("Alfa", names.get(0));
        Assertions.assertEquals("Beta", names.get(1));
        controller.chooseName("Alfa");
        Assertions.assertEquals("Alfa", name.get());
    }

    @Test
    @DisplayName("Sending available colors and receiving a color")
    void ColorsTest() throws ExecutionException, InterruptedException {
        ArrayList<String> availableColors = new ArrayList<>();
        availableColors.add("Red");
        availableColors.add("Blue");
        Future<String> color = executor.submit(() -> connection.getColor(availableColors).getColor());
        ArrayList<String> colors = controller.getAvailableColor().getColors();
        Assertions.assertEquals("Red", colors.get(0));
        Assertions.assertEquals("Blue", colors.get(1));
        controller.chooseColor("Red");
        Assertions.assertEquals("Red", color.get());
    }

    @Test
    @DisplayName("Sending the new host")
    void newHostTest() {
        connection.sendNewHostMessage("Alfa");
        Assertions.assertEquals("Alfa", controller.newHost().getNewHostNickname());
    }

    @Test
    @DisplayName("Receiving the expected players")
    void expectedPlayersTest() {
        controller.expectedPlayers(3, false);
        expectedPlayersMessage expected = connection.getExpectedPlayer();
        Assertions.assertEquals(3, expected.getExpectedPlayers());
    }

    @Test
    @DisplayName("Placing the starter card")
    void placeStarterTest() {
        controller.placeStarter(0, false);
        starterCardMessage starter = connection.getStaterCard();
        Assertions.assertEquals(0, starter.getSide());
    }

    @Test
    @DisplayName("Sending the objective cards and receiving an answer")
    void ObjectiveCardsTest() throws ExecutionException, InterruptedException {
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
    }

    @Test
    @DisplayName("Placing a card")
    void placeCardTest() {
        controller.placeCard(0, 0, 0, 0, false);
        placeCardMessage message = connection.getPlaceCard();
        Assertions.assertEquals(0, message.getCard());
        Assertions.assertEquals(0, message.getFront());
        Assertions.assertEquals(0, message.getRow());
        Assertions.assertEquals(0, message.getColumn());
    }

    @Test
    @DisplayName("Picking a card")
    void pickCardTest() {
        controller.pickCard(0, false);
        Assertions.assertEquals(0, connection.getChosenPick().getCard());
    }

    @Test
    @DisplayName("Updating the player")
    void updatePlayerTest() {
        Player player = new Player("Alfa", "Red", null);
        updatePlayerMessage message = new updatePlayerMessage(player, "Alfa");
        connection.sendUpdatePlayer(message);
        updatePlayerMessage received = controller.updatePlayer();
        Assertions.assertEquals("Alfa", received.getPlayer().getNickname());
        Assertions.assertEquals("Red", received.getPlayer().getColor());
        Assertions.assertNotNull(received.getPlayer().getCommonArea());
    }

    @Test
    @DisplayName("Ending the game")
    void endGameTest() {
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