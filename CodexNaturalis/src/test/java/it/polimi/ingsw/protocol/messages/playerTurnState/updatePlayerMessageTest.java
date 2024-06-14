package it.polimi.ingsw.protocol.messages.playerTurnState;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.LoadDecks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UpdatePlayerMessageTest {

    private updatePlayerMessage message;
    private Player player;
    private String nicknameViewer;
    private CommonArea area;

    @BeforeEach
    void setup() {
        area = (new LoadDecks()).load();
        player = new Player("Test Player", "Player_COLOR", area);
        nicknameViewer = "Viewer";
        message = new updatePlayerMessage(player, nicknameViewer);
    }

    @Test
    @DisplayName("Player is correctly retrieved")
    void playerIsCorrectlyRetrieved() {
        Assertions.assertEquals(player, message.getPlayer());
    }

    @Test
    @DisplayName("Nickname viewer is correctly retrieved")
    void nicknameViewerIsCorrectlyRetrieved() {
        Assertions.assertEquals(nicknameViewer, message.getNicknameViewer());
    }
}