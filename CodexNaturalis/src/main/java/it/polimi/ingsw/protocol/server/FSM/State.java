package it.polimi.ingsw.protocol.server.FSM;

import java.io.Serializable;

/**
 * Enum representing the state of a turn
 */
public enum State implements Serializable {
    WaitingForPlayers,
    StarterCard,
    Objective,
    PlaceCard,
    PickCard,
    LastTurn,
    EndGame
}
