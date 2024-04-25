package it.polimi.ingsw.protocol.server.FSM;

public enum State {
    Creation,
    WaitingForNewPlayers,
    StarterCard,
    Objective,
    Player1Turn,
    Player2Turn,
    Player3Turn,
    Player4Turn,
    EndGame,
    Cleaning
}
