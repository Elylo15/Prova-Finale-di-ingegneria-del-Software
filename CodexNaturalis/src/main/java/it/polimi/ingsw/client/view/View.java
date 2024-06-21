package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.connectionState.availableColorsMessage;
import it.polimi.ingsw.messages.connectionState.unavailableNamesMessage;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.messages.responseMessage;
import it.polimi.ingsw.messages.serverOptionState.serverOptionMessage;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.util.ArrayList;
/**
 * This class is the abstract class for the View.
 * It is extended by the GUI and CLI views.
 */
public abstract class View {

    /**
     * method {@code View}: abstract constructor for the View class
     */
    public View() {
    }

    /**
     * methods implemented by the gui and CLI views
     */
    public abstract String askIP();

    public abstract void playerDisconnected(Exception e);

    public abstract boolean askSocket();

    public abstract void updatePlayer(currentStateMessage message);

    public abstract serverOptionMessage serverOptions(serverOptionMessage message);

    public abstract String unavailableNames(unavailableNamesMessage message);

    public abstract boolean answer(responseMessage message);

    public abstract String availableColors(availableColorsMessage message);

    public abstract int placeStarter();

    public abstract int expectedPlayers();

    public abstract int chooseObjective(ArrayList<ObjectiveCard> objectives);

    public abstract int[] placeCard();

    public abstract int pickCard();

    public abstract void endGame(declareWinnerMessage message);

    public abstract void update(updatePlayerMessage update);

    public abstract void waiting();

    public abstract String pickNameFA(unavailableNamesMessage message);
}
