package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;


import java.util.ArrayList;

public abstract class View {
    //every client will have its GUI or CLI view and will call the methods of the view to visualize what the user needs to see to play the game
    public View(){

    }
    public abstract boolean askSocket();
    public abstract void updatePlayer(currentStateMessage message);
    public abstract void answerToConnection(connectionResponseMessage message);
    public abstract serverOptionMessage serverOptions(serverOptionMessage message);
    public abstract void answerToOption(serverOptionResponseMessage message);
    public abstract String unavaibleNames(unavailableNamesMessage message);
    public abstract void answer(responseMessage message);
    public abstract String availableColors(availableColorsMessage message);

    public abstract int placeStarter();

    public abstract int expectedPlayers();
    public abstract int chooseObjective();


    public abstract int[] placeCard();


    public abstract int pickCard();

    public abstract void endGame(declareWinnerMessage message);
}
