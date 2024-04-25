package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.messages.ObjectiveCardMessage;
import it.polimi.ingsw.protocol.messages.StarterCardMessage;

public abstract class ClientConnection implements Runnable {
    public int getExpectedPlayers() {
    }

    public boolean getStart() {
    }

    public void sendStateStarterCard(StarterCardMessage starterCardMessage) {

    }

    public void sendStateChooseObjective(ObjectiveCardMessage message) {

    }


    public StarterCardMessage getStarterCard() {
    }

    public ObjectiveCardMessage getChosenObjective() {

    }


}
