package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.protocol.messages.Connection.*;
import it.polimi.ingsw.protocol.messages.PlayerTurn.*;
import it.polimi.ingsw.protocol.messages.EndGame.*;


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


    public abstract void placeCard(PlaceCardMessage message);

    public abstract void drawCard(pickCardMessage message);

    public abstract boolean answerToPlaceCard(placeCardResponseMessage message);
    public abstract boolean answerToPickCard(pickCardResponseMessage message);
    public abstract void endGame(DeclareWinnerMessage message);

    public abstract void youAreKickedPlayer(kickedMessage message);
}
