package it.polimi.ingsw.server.fsm;

/**
 * Enum representing the state of a match
 */
public enum MatchState {
    Waiting,
    Player1,
    Player2,
    Player3,
    Player4,
    Endgame,
    KickingPlayers
}
