package it.polimi.ingsw.protocol.server.FSM;

public enum Event {
    Created,
    AllPlayerReady,
    AllStarterCardPlaced,
    AllObjectivePicked,
    Player1TurnEnded,
    Player2TurnEnded,
    Player3TurnEnded,
    Player4TurnEnded,
    SkipPlayer1,
    SkipPlayer2,
    SkipPlayer3,
    SkipPlayer4,
    gameEnded,
    destroyGame
}
