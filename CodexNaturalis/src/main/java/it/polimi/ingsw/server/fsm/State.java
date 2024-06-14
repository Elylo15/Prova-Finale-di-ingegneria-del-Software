package it.polimi.ingsw.server.fsm;

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
