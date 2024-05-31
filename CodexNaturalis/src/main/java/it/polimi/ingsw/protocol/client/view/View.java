package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;

import java.util.ArrayList;

public abstract class View {

    /**
     * method {@code View}: abstract constructor for the View class
     */
    public View() {
    }

    /**
     * methods implemented by the GUI and CLI views
     */
    public abstract String askIP();

    public abstract void playerDisconnected();

    public abstract boolean askSocket();

    public abstract void updatePlayer(currentStateMessage message);

    public abstract void answerToConnection(connectionResponseMessage message);

    public abstract serverOptionMessage serverOptions(serverOptionMessage message);

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

    public abstract String pickNameFA(unavailableNamesMessage message);
}
