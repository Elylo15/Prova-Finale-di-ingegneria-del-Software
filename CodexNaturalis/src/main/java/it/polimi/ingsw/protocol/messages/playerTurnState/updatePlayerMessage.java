package it.polimi.ingsw.protocol.messages.playerTurnState;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

/**
 * Message used to update the player's information
 */
public class updatePlayerMessage implements Message, Serializable {
    private final Player player;
    private final String nicknameViewer;


    /**
     * Constructor for the updatePlayerMessage class, which is used to update the player's information.
     *
     * @param player         The player whose information is to be updated.
     * @param nicknameViewer The nickname of the player who is viewing the updated information.
     */
    public updatePlayerMessage(Player player, String nicknameViewer) {
        this.player = player;
        this.nicknameViewer = nicknameViewer;

    }

    /**
     * Getter for the player attribute.
     *
     * @return The player whose information is to be updated.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for the nicknameViewer attribute.
     *
     * @return The nickname of the player who is viewing the updated information.
     */
    public String getNicknameViewer() {
        return nicknameViewer;
    }

}
