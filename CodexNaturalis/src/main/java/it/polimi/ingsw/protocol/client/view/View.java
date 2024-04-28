package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.PlayerDisconnectedState.disconnectedMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;


import java.util.ArrayList;

public abstract class View {
    //every client will have its GUI or CLI view and will call the methods of the view to visualize what the user needs to see to play the game
    public View(){

    }
    public abstract void answerToConnection(answerConnectionMessage message);
    public abstract void unavaibleNames(unavailableNamesMessage message);
    public abstract String askNickname();
    public abstract void answerToNameChose(boolean correctchoice);
    public abstract String askColor();
    public abstract void avaibleColors(availableColorsMessage message);
    public abstract void answerToColorChosen(boolean correctchoice);

    public abstract int placeStarter();
    public abstract boolean answerToPlaceStarter(starterCardResponseMessage message);

    public abstract void yourTurnStarts(yourTurnStarts turn);
    public abstract void notYourTurnStarts(notYourTurnStarts turn);

    public abstract int chooseObjective();
    public abstract boolean answerToChooseObjective(objectiveCardResponseMessage message);


    public abstract int[] placeCard();
    public abstract boolean answerToPlaceCard(placeCardResponseMessage message);


    public abstract int pickCard();

    public abstract boolean answerToPickCard(pickCardResponseMessage message);
    public abstract void endGame(declareWinnerMessage message);
    public abstract void lastTurn();
    public abstract void disconnected(disconnectedMessage message);

    public abstract void youAreKickedPlayer(kickedMessage message);
}
