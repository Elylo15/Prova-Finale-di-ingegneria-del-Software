package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.protocol.server.FSM.MatchState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represents the information of a match used by the server, in addition to the match object itself.
 * This class will be serialized and saved as backup.
 *
 * <p>It contains information about the match, such as the match object, the match ID, the expected number of players,
 * the current status of the match, whether it's the last turn, and a list of all player information.
 */
public class MatchInfo implements Serializable {
    private final Match match;
    private final Integer ID;
    private final CopyOnWriteArrayList<PlayerInfo> allPlayersInfo;
    private Integer expectedPlayers;
    private MatchState status;
    private boolean lastTurn;

    public MatchInfo(Match match, int ID, Integer expectedPlayers, MatchState status) {
        this.match = match;
        this.ID = ID;
        this.expectedPlayers = expectedPlayers;
        this.status = status;
        this.lastTurn = false;
        this.allPlayersInfo = new CopyOnWriteArrayList<>();
    }


    /**
     * Creates a deep copy of this MatchInfo object for serialization.
     * For the PlayerInfo objects, new instances are created with null for the ClientConnection to avoid serialization issues.
     *
     * @return A cloned MatchInfo object suitable for serialization.
     */
    public MatchInfo cloneForSerialization() {
        MatchInfo clonedMatchInfo = new MatchInfo(this.match, this.ID, this.expectedPlayers, this.status);
        clonedMatchInfo.setLastTurn(this.lastTurn);

        for (PlayerInfo playerInfo : this.allPlayersInfo) {
            PlayerInfo clonedPlayerInfo = new PlayerInfo(playerInfo);
            clonedMatchInfo.addPlayer(clonedPlayerInfo);
        }

        return clonedMatchInfo;
    }

//    // Uncomment to show the status of all players' connections when checked
//    public void printPlayersStatus() {
//        for (PlayerInfo player : allPlayersInfo) {
//            String connection = player.getConnection() == null ? "null" : "not null";
//            System.out.println(player.getPlayer().getNickname() + " " + connection);
//        }
//        System.out.println(" ");
//    }

    /**
     * Adds a player to the match.
     *
     * @param player The PlayerInfo object representing the player to be added.
     */
    protected void addPlayer(PlayerInfo player) {
        this.allPlayersInfo.add(player);
    }

    /**
     * Sets a player as offline by closing their connection and setting it to null.
     *
     * @param player The PlayerInfo object representing the player to be set offline.
     */
    protected void setOffline(PlayerInfo player) {
        if (player == null)
            return;

        if (player.getConnection() != null)
            player.getConnection().closeConnection();
        player.setConnection(null);
    }

    /**
     * Brings a player online by setting their connection.
     *
     * @param Nickname   The nickname of the player to be brought online.
     * @param connection The ClientConnection object representing the player's connection.
     */
    protected void bringOnline(String Nickname, ClientConnection connection) {
        for (PlayerInfo player : allPlayersInfo) {
            if (player.getPlayer().getNickname().equalsIgnoreCase(Nickname)) {
                player.setConnection(connection);
                return;
            }
        }
    }

    /**
     * Gets the match object.
     *
     * @return The Match object representing the match.
     */
    protected Match getMatch() {
        return match;
    }

    /**
     * Gets the ID of the match.
     *
     * @return The ID of the match.
     */
    protected Integer getID() {
        return ID;
    }

    /**
     * Gets the expected number of players for the match.
     *
     * @return The expected number of players.
     */
    protected Integer getExpectedPlayers() {
        return expectedPlayers;
    }

    /**
     * Sets the expected number of players for the match.
     *
     * @param expectedPlayers The expected number of players.
     */
    protected void setExpectedPlayers(Integer expectedPlayers) {
        this.expectedPlayers = expectedPlayers;
    }

    /**
     * Gets the status of the match.
     *
     * @return The MatchState object representing the status of the match.
     */
    protected MatchState getStatus() {
        return status;
    }

    /**
     * Sets the status of the match.
     *
     * @param status The MatchState object representing the status of the match.
     */
    protected void setStatus(MatchState status) {
        this.status = status;
    }

    /**
     * Gets the status of the match.
     *
     * @return The MatchState object representing the status of the match.
     */
    protected boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * Sets whether it's the last turn of the match.
     *
     * @param lastTurn A boolean representing whether it's the last turn.
     */
    protected void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }

    /**
     * Gets the information of all players in the match.
     *
     * @return An ArrayList of PlayerInfo objects representing all players in the match.
     */
    protected ArrayList<PlayerInfo> getAllPlayersInfo() {
        return new ArrayList<>(this.allPlayersInfo);
    }

}
