package it.polimi.ingsw.protocol.server.FSM;

import java.io.Serializable;

public enum State implements Serializable {
    WaitingForPlayers,
    StarterCard,
    Objective,
    PlayerTurn,
    //PlaceCard,
    //PickCard,
    LastTurn,
    EndGame
}
