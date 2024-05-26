package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.view.View;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract  class Client {

    public Client(){
    }

    public abstract void connection(boolean isSocket);
    public abstract void run();
    public abstract void serverOptions();
    public abstract void name();
    public abstract void color();
    public abstract void waitingPlayer(currentStateMessage current);
    public abstract void starter();
    public abstract void pickObjective();
    public abstract void pickCard();
    public abstract void placeCard();
    public abstract void setIP(String serverIP);
    public abstract void setController(String server, boolean isSocket);
    public abstract void pickNameFA();

}

