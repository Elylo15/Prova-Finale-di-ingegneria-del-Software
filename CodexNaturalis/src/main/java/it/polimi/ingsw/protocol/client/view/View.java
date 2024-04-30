package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;


import java.util.ArrayList;

public abstract class View {
    //every client will have its GUI or CLI view and will call the methods of the view to visualize what the user needs to see to play the game
    public View(){

    }
    public abstract boolean askSocket();
    public abstract void answerToConnection(connectionResponseMessage message);
    public abstract serverOptionMessage serverOptions(serverOptionMessage message);
    public abstract void answerToOption(serverOptionResponseMessage message);
    public abstract String unavaibleNames(unavailableNamesMessage message);
    public abstract void answerToNameChosen(nameResponseMessage message);
    public abstract String availableColors(availableColorsMessage message);
    public abstract void answerToColorChosen(colorResponseMessage message);

    public abstract int placeStarter();
    public abstract void answerToPlaceStarter(starterCardResponseMessage message);

    public abstract int expectedPlayers();
    public abstract void answerToExpectedPlayers(expectedPlayersResponseMessage message);
    public abstract int chooseObjective();
    public abstract void answerToChooseObjective(objectiveCardResponseMessage message);


    public abstract int[] placeCard();
    public abstract void answerToPlaceCard(placeCardResponseMessage message);


    public abstract int pickCard();

    public abstract void answerToPickCard(pickCardResponseMessage message);
    public abstract void endGame(declareWinnerMessage message);
}
