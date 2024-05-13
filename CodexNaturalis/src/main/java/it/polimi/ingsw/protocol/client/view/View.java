package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import it.polimi.ingsw.protocol.server.PlayerInfo;

import java.util.ArrayList;

public abstract class View {
    //every client will have its GUI or CLI view and will call the methods of the view to visualize what the user needs to see to play the game
    public View(){ }

    public abstract String[] askPortIP();
    public abstract void showPlayerHand(Player player, String viewer);
    public abstract void showCommonArea(CommonArea commonArea);
    public abstract void playerDisconnected();
    public abstract boolean askSocket();
    public abstract void updatePlayer(currentStateMessage message);
    public abstract void answerToConnection(connectionResponseMessage message);
    public abstract serverOptionMessage serverOptions(serverOptionMessage message);
    public abstract void answerToOption(serverOptionResponseMessage message);
    public abstract String unavailableNames(unavailableNamesMessage message);
    public abstract void answer(responseMessage message);
    public abstract String availableColors(availableColorsMessage message);
    public abstract int placeStarter();
    public abstract int expectedPlayers();
    public abstract int chooseObjective(ArrayList<ObjectiveCard> objectives);
    public abstract int[] placeCard();
    public abstract int pickCard();
    public abstract void endGame(declareWinnerMessage message);
    public abstract void update(updatePlayerMessage update);
}
