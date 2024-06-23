package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.connectionState.availableColorsMessage;
import it.polimi.ingsw.messages.connectionState.unavailableNamesMessage;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.messages.responseMessage;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewCLITest {

    private ViewCLI viewCLI;
    private PlayerArea playerArea;
    private CommonArea commonArea;
    private InputStream sysInBackup;

    @BeforeEach
    void setUp() {
        Match match = new Match();
        match.start();
        commonArea = match.getCommonArea();
        sysInBackup = System.in;
        playerArea = new PlayerArea();
        viewCLI = new ViewCLI();

    }

    @Test
    @DisplayName("Ask IP test")
    void askIpTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("192.168.1.1\n".getBytes());
        System.setIn(in);

        Assertions.assertEquals("192.168.1.1", viewCLI.askIP());

        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("PlayerDisconnected test")
    void playerDisconnectedTest() {
        viewCLI.playerDisconnected(new Exception());
    }

    @Test
    @DisplayName("ShowPlayerArea test")
    void showPlayerAreaTest() throws noPlaceCardException {

        PlaceableCard starterCard = commonArea.drawFromToPlayer(3);
        PlaceableCard testCard = commonArea.drawFromToPlayer(1);
        PlaceableCard testCard2 = commonArea.drawFromToPlayer(2);
        PlaceableCard testCard3 = commonArea.drawFromToPlayer(1);


        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1, 1, false);
        playerArea.placeCard(testCard2, 2, 2, false);
        playerArea.placeCard(testCard3, -1, -1, true);

        viewCLI.showPlayerArea(playerArea);


    }
    @Test
    @DisplayName("ShowPlayerArea test")
    void showPlayerAreaTest2() throws noPlaceCardException {

        PlaceableCard starterCard = commonArea.drawFromToPlayer(3);
        PlaceableCard testCard = commonArea.drawFromToPlayer(1);
        PlaceableCard testCard3 = commonArea.drawFromToPlayer(1);


        playerArea.placeStarterCard(starterCard, false);
        System.out.println(starterCard.getResource());
       playerArea.placeCard(testCard, -1, 1, false);
       playerArea.placeCard(testCard3, -1, -1, true);

        viewCLI.showPlayerArea(playerArea);


    }


    @Test
    @DisplayName("Scoreboard test: purple winner")
    void testEndOneWinnerPurple() {
        Player alfa = new Player("alfa", "red", null);
        Player beta = new Player("beta", "purple", null);
        Player gamma = new Player("gamma", "blue", null);

        viewCLI.savePlayerColor(alfa);
        viewCLI.savePlayerColor(beta);
        viewCLI.savePlayerColor(gamma);

        HashMap<String, Integer> scores = new HashMap<>();
        scores.put(alfa.getNickname(), 10);
        scores.put(beta.getNickname(), 20);
        scores.put(gamma.getNickname(), 16);
        HashMap<String, Integer> objective = new HashMap<>();
        objective.put("alfa", 3);
        objective.put("beta", 4);
        objective.put("gamma", 2);
        declareWinnerMessage message = new declareWinnerMessage(scores, objective);
        viewCLI.endGame(message);
    }

    @Test
    @DisplayName("Scoreboard test: blue winner")
    void testEndOneWinnerBlue() {
        Player alfa = new Player("alfa", "red", null);
        Player beta = new Player("beta", "blue", null);
        Player gamma = new Player("gamma", "green", null);

        viewCLI.savePlayerColor(alfa);
        viewCLI.savePlayerColor(beta);
        viewCLI.savePlayerColor(gamma);

        HashMap<String, Integer> scores = new HashMap<>();
        scores.put(alfa.getNickname(), 10);
        scores.put(beta.getNickname(), 20);
        scores.put(gamma.getNickname(), 16);
        HashMap<String, Integer> objective = new HashMap<>();
        objective.put("alfa", 3);
        objective.put("beta", 4);
        objective.put("gamma", 2);
        declareWinnerMessage message = new declareWinnerMessage(scores, objective);
        viewCLI.endGame(message);

    }

    @Test
    @DisplayName("Scoreboard test: green winner")
    void testEndOneWinnerGreen() {
        Player alfa = new Player("alfa", "red", null);
        Player beta = new Player("beta", "blue", null);
        Player gamma = new Player("gamma", "green", null);

        viewCLI.savePlayerColor(alfa);
        viewCLI.savePlayerColor(beta);
        viewCLI.savePlayerColor(gamma);

        HashMap<String, Integer> scores = new HashMap<>();
        scores.put(alfa.getNickname(), 10);
        scores.put(beta.getNickname(), 12);
        scores.put(gamma.getNickname(), 21);
        HashMap<String, Integer> objective = new HashMap<>();
        objective.put("alfa", 3);
        objective.put("beta", 4);
        objective.put("gamma", 2);
        declareWinnerMessage message = new declareWinnerMessage(scores, objective);
        viewCLI.endGame(message);
    }

    @Test
    @DisplayName("Scoreboard test: red winner")
    void testEndOneWinnerRed() {
        Player alfa = new Player("alfa", "red", null);
        Player beta = new Player("beta", "blue", null);
        Player gamma = new Player("gamma", "green", null);

        viewCLI.savePlayerColor(alfa);
        viewCLI.savePlayerColor(beta);
        viewCLI.savePlayerColor(gamma);

        HashMap<String, Integer> scores = new HashMap<>();
        scores.put(alfa.getNickname(), 22);
        scores.put(beta.getNickname(), 12);
        scores.put(gamma.getNickname(), 14);
        HashMap<String, Integer> objective = new HashMap<>();
        objective.put("alfa", 3);
        objective.put("beta", 4);
        objective.put("gamma", 2);
        declareWinnerMessage message = new declareWinnerMessage(scores, objective);
        viewCLI.endGame(message);
    }

    @Test
    @DisplayName("Scoreboard test: two winners")
    void testEndTwoWinners() {
        Player alfa = new Player("alfa", "red", null);
        Player beta = new Player("beta", "purple", null);
        Player gamma = new Player("gamma", "blue", null);
        Player delta = new Player("delta", "green", null);

        viewCLI.savePlayerColor(alfa);
        viewCLI.savePlayerColor(beta);
        viewCLI.savePlayerColor(gamma);
        viewCLI.savePlayerColor(delta);

        HashMap<String, Integer> scores = new HashMap<>();
        scores.put(alfa.getNickname(), 10);
        scores.put(beta.getNickname(), 21);
        scores.put(gamma.getNickname(), 16);
        scores.put(delta.getNickname(), 21);
        HashMap<String, Integer> objective = new HashMap<>();
        objective.put(alfa.getNickname(), 3);
        objective.put(beta.getNickname(), 4);
        objective.put(gamma.getNickname(), 2);
        objective.put(delta.getNickname(), 7);
        declareWinnerMessage message = new declareWinnerMessage(scores, objective);
        viewCLI.endGame(message);
    }

    @Test
    @DisplayName("CheckAnswer test")
    void checkAnswer() {
        responseMessage message = new responseMessage(false);
        viewCLI.answer(message);
        message = new responseMessage(true);
        viewCLI.answer(message);
    }

    @Test
    void printObjectives() {
        ArrayList<String> output = new ArrayList<>();
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        for (int i = 102; i != 86; i--) {
            viewCLI.printObjective(i, output);
            output.forEach(System.out::println);
        }
    }


    @Test
    @DisplayName("ShowCommonArea and PlayerHand test")
    void commonAreaAndPlayerHandPrintingTest() throws Exception {
        Match match = new Match();
        match.start();
        Player player1 = new Player("player1", "RED", match.getCommonArea());
        match.addPlayer(player1);
        player1.initialHand();
        viewCLI.showCommonArea(match.getCommonArea());
        viewCLI.showPlayerHand(player1, "player1");

        viewCLI.showPlayerHand(player1, "player2");
    }

    @Test
    @DisplayName("Partially empty and fully empty commonArea test")
    void emptyCommonAreaTest() {
        while (commonArea.getD2().getSize() > 0 && commonArea.getD1().getSize() > 0) {
            commonArea.drawFromToPlayer(2);
            commonArea.drawFromToPlayer(1);
        }
        int id;
        viewCLI.showCommonArea(commonArea);
        id = commonArea.getTableCards().getFirst().getID();
        commonArea.pickTableCard(id);
        viewCLI.showCommonArea(commonArea);
        id = commonArea.getTableCards().getFirst().getID();
        commonArea.pickTableCard(id);
        viewCLI.showCommonArea(commonArea);
        id = commonArea.getTableCards().getFirst().getID();
        commonArea.pickTableCard(id);
        viewCLI.showCommonArea(commonArea);
        id = commonArea.getTableCards().getFirst().getID();
        commonArea.pickTableCard(id);
        viewCLI.showCommonArea(commonArea);
    }

    @Test
    @DisplayName("UpdatePlayer from currentStateMessage test")
    void updatePlayerTest() throws noPlaceCardException {
        Player player = new Player("player1", "RED", commonArea);
        Player player2 = new Player("player2", "BLUE", commonArea);
        PlayerArea playerArea = player.getPlayerArea();

        ObjectiveCard[] objectives = new ObjectiveCard[2];
        objectives[0] = commonArea.drawObjectiveCard();
        objectives[1] = commonArea.drawObjectiveCard();

        player.pickObjectiveCard(1, objectives);
        player2.pickObjectiveCard(2, objectives);

        PlaceableCard starterCard = commonArea.drawFromToPlayer(3);
        PlaceableCard testCard = commonArea.drawFromToPlayer(1);
        PlaceableCard testCard2 = commonArea.drawFromToPlayer(2);
        PlaceableCard testCard3 = commonArea.drawFromToPlayer(1);


        playerArea.placeStarterCard(starterCard, true);
        playerArea.placeCard(testCard, 1, 1, false);
        playerArea.placeCard(testCard2, 2, 2, false);
        playerArea.placeCard(testCard3, -1, -1, true);

        currentStateMessage message = new currentStateMessage(player, player, "PickCard", false, null, objectives, 12345);
        viewCLI.updatePlayer(message);
        message = new currentStateMessage(player, player2, "PickCard", true, null, objectives, 12345);
        viewCLI.updatePlayer(message);
    }

    @Test
    @DisplayName("UpdatePlayer from update test")
    void updatePlayerAfterTurnTest() {
        Player player = new Player("player1", "RED", commonArea);
        Player player2 = new Player("player2", "BLUE", commonArea);

        ObjectiveCard[] objectives = new ObjectiveCard[2];
        objectives[0] = commonArea.drawObjectiveCard();
        objectives[1] = commonArea.drawObjectiveCard();

        player.pickObjectiveCard(1, objectives);
        player2.pickObjectiveCard(2, objectives);

        updatePlayerMessage message = new updatePlayerMessage(player, "player1");
        viewCLI.update(message);
        message = new updatePlayerMessage(player, "player2");
        viewCLI.update(message);
    }

    @Test
    @DisplayName("Choice of the name test")
    void nameChoiceTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("GAMMA\n".getBytes());
        System.setIn(in);

        ArrayList<String> names = new ArrayList<>();
        names.add("ALFA");
        names.add("BETA");
        unavailableNamesMessage message = new unavailableNamesMessage(names);

        Assertions.assertEquals("GAMMA", viewCLI.unavailableNames(message));


        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("Color choice test")
    void colorChoiceTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("RED\n".getBytes());
        System.setIn(in);

        ArrayList<String> colors = new ArrayList<>();
        colors.add("BLUE");
        colors.add("YELLOW");
        colors.add("GREEN");

        availableColorsMessage message = new availableColorsMessage(colors);
        Assertions.assertEquals("RED", viewCLI.availableColors(message));

        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("StarterCard placement test")
    void starterCardPlacementTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("front\n".getBytes());
        System.setIn(in);
        assertEquals(1, viewCLI.placeStarter());

        in = new ByteArrayInputStream("back\n".getBytes());
        System.setIn(in);
        assertEquals(0, viewCLI.placeStarter());

        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("Expected players test")
    void expectedPlayersTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("a\n1\n".getBytes());
        System.setIn(in);

        assertEquals(1, viewCLI.expectedPlayers());

        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("Objective choice test")
    void chooseObjectiveTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(in);

        ArrayList<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(commonArea.drawObjectiveCard());
        objectives.add(commonArea.drawObjectiveCard());

        assertEquals(1, viewCLI.chooseObjective(objectives));

        in = new ByteArrayInputStream("2\n".getBytes());
        System.setIn(in);
        assertEquals(2, viewCLI.chooseObjective(objectives));

        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("Card placement test")
    void cardPlacementTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\nfront\n2\n3\n".getBytes());
        System.setIn(in);

        int[] expected = {1, 1, 2, 3};
        int[] result = viewCLI.placeCard();

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }

        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("CardPick test")
    void cardPickTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(in);

        assertEquals(1, viewCLI.pickCard());

        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("ServerOptions test")
    void serverOptionsTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("yes\n1\n".getBytes());
        System.setIn(in);
        ArrayList<Integer> waitingMatches = new ArrayList<>();
        waitingMatches.add(1000);
        waitingMatches.add(1001);
        ArrayList<Integer> runningMatches = new ArrayList<>();
        runningMatches.add(1002);
        runningMatches.add(1003);
        ArrayList<Integer> savedMatches = new ArrayList<>();
        savedMatches.add(1004);
        savedMatches.add(1005);
        serverOptionMessage message = new serverOptionMessage(false, null, null, false, null, waitingMatches, runningMatches, savedMatches);

        // Creates a new match
        serverOptionMessage answer = viewCLI.serverOptions(message);
        Assertions.assertTrue(answer.isNewMatch());
        Assertions.assertNull(answer.getMatchID());
        Assertions.assertNull(answer.getStartedMatchID());
        Assertions.assertFalse(answer.isLoadMatch());
        Assertions.assertNull(answer.getSavedMatchID());
        Assertions.assertNull(answer.getWaitingMatches());
        Assertions.assertNull(answer.getRunningMatches());
        Assertions.assertNull(answer.getSavedMatches());

        // Joins a waiting match
        in = new ByteArrayInputStream("yes\n2\n1\n".getBytes());
        System.setIn(in);
        answer = viewCLI.serverOptions(message);
        Assertions.assertTrue(answer.isNewMatch());
        Assertions.assertEquals(answer.getMatchID(), 1000);
        Assertions.assertNull(answer.getStartedMatchID());
        Assertions.assertFalse(answer.isLoadMatch());
        Assertions.assertNull(answer.getSavedMatchID());
        Assertions.assertNull(answer.getWaitingMatches());
        Assertions.assertNull(answer.getRunningMatches());
        Assertions.assertNull(answer.getSavedMatches());

        // Joins a running match
        in = new ByteArrayInputStream("no\nyes\n1\n".getBytes());
        System.setIn(in);
        answer = viewCLI.serverOptions(message);
        Assertions.assertFalse(answer.isNewMatch());
        Assertions.assertNull(answer.getMatchID());
        Assertions.assertEquals(answer.getStartedMatchID(), 1002);
        Assertions.assertNull(answer.getSavedMatchID());
        Assertions.assertFalse(answer.isLoadMatch());
        Assertions.assertNull(answer.getWaitingMatches());
        Assertions.assertNull(answer.getRunningMatches());
        Assertions.assertNull(answer.getSavedMatches());

        // Loads a saved match
        in = new ByteArrayInputStream("no\nno\nyes\n1\n".getBytes());
        System.setIn(in);
        answer = viewCLI.serverOptions(message);
        Assertions.assertFalse(answer.isNewMatch());
        Assertions.assertNull(answer.getMatchID());
        Assertions.assertNull(answer.getStartedMatchID());
        Assertions.assertTrue(answer.isLoadMatch());
        Assertions.assertEquals(answer.getSavedMatchID(), 1004);
        Assertions.assertNull(answer.getWaitingMatches());
        Assertions.assertNull(answer.getRunningMatches());
        Assertions.assertNull(answer.getSavedMatches());


        System.setIn(sysInBackup);
    }

    @Test
    @DisplayName("Pick name FA test")
    void pickNameFATest() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(in);

        ArrayList<String> names = new ArrayList<>();
        names.add("ALFA");
        names.add("BETA");
        unavailableNamesMessage message = new unavailableNamesMessage(names);

        Assertions.assertEquals("ALFA", viewCLI.pickNameFA(message));

        System.setIn(sysInBackup);
    }

}