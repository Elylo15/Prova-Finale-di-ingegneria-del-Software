package it.polimi.ingsw.protocol.server.FSM;

import java.io.Serializable;

public enum State implements Serializable {
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
