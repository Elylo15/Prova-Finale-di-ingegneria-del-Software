package it.polimi.ingsw.protocol.messages;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class CurrentStateMessageTest {

    private CommonArea area;

    private currentStateMessage message;
    private Player currentPlayer;
    private Player player;
    private String stateName;
    private boolean lastTurn;
    private ArrayList<String> onlinePlayers;
    private ArrayList<ObjectiveCard> commonObjectiveCards;
    private Integer matchID;

    @BeforeEach
    void setup() {
        area = (new LoadDecks()).load();
        currentPlayer = new Player("Current Player", "RED", area);
        player = new Player("Player", "BLUE", area);
        stateName = "Test State";
        lastTurn = true;
        onlinePlayers = new ArrayList<>(Arrays.asList("Player1", "Player2", "Player3"));
        commonObjectiveCards = new ArrayList<>(Arrays.asList(area.drawObjectiveCard()));
        matchID = 1;
        message = new currentStateMessage(currentPlayer, player, stateName, lastTurn, onlinePlayers, commonObjectiveCards.toArray(new ObjectiveCard[0]), matchID);
    }

    @Test
    @DisplayName("Current player is correctly retrieved")
    void currentPlayerIsCorrectlyRetrieved() {
        Assertions.assertEquals(currentPlayer, message.getCurrentPlayer());
    }

    @Test
    @DisplayName("Player is correctly retrieved")
    void playerIsCorrectlyRetrieved() {
        Assertions.assertEquals(player, message.getPlayer());
    }

    @Test
    @DisplayName("State name is correctly retrieved")
    void stateNameIsCorrectlyRetrieved() {
        Assertions.assertEquals(stateName, message.getStateName());
    }

    @Test
    @DisplayName("Last turn flag is correctly retrieved")
    void lastTurnFlagIsCorrectlyRetrieved() {
        Assertions.assertTrue(message.isLastTurn());
    }

    @Test
    @DisplayName("Online players are correctly retrieved")
    void onlinePlayersAreCorrectlyRetrieved() {
        Assertions.assertEquals(onlinePlayers, message.getOnlinePlayers());
    }

    @Test
    @DisplayName("Common objective cards are correctly retrieved")
    void commonObjectiveCardsAreCorrectlyRetrieved() {
        Assertions.assertEquals(commonObjectiveCards, message.getCommonObjectiveCards());
    }

    @Test
    @DisplayName("Match ID is correctly retrieved")
    void matchIDIsCorrectlyRetrieved() {
        Assertions.assertEquals(matchID, message.getMatchID());
    }
}