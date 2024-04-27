package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.messages.*;

public abstract class ClientConnection implements Runnable {
    private String status;
    private String IP;
    private String port;
    private CheckConnection connection;
    private int timeOut;


    public ClientConnection(String IP, String port, int timeOut){
        this.IP = IP;
        this.port = port;
        this.timeOut = timeOut;
    }

    public String getStatus() {
        return status;
    }

    public String getIP() {
        return IP;
    }

    public String getPort() {
        return port;
    }

    public int getTimeOut() {
        return timeOut;
    }

    abstract int getExpectedPlayers();

    abstract boolean getStart();

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    abstract void startCheckConnection();

    abstract String getName();
    abstract String getColor();

    abstract  ObjectiveCardMessage getChosenObjective();
    abstract StarterCardMessage getStarterCard();

    abstract PlaceableCardMessage getPlaceableCard();
    abstract pickCardMessage getPickCard();

    abstract void sendStateStarterCard(StarterCardMessage starterCardMessage);
    abstract void sendStateChooseObjective(ObjectiveCardMessage message);
    abstract void sendStateYourTurn(TurnMessage message);
    abstract void sendStateNotYourTurn(TurnMessage message);
    abstract void sendStateFinishMatch(endGameMessage message);

    abstract void youTurnSignal();

    abstract void sendAnswerToConnection(answerConnectionMessage message);






}
