package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.protocol.messages.*;

/**
 * ClientConnection class
 * @author elylo
 */
public abstract class ClientConnection implements Runnable {
    private String status;
    private String IP;
    private String port;
    private CheckConnection connection;
    private int timeOut;

    /**
     * Class constructor
     *
     * @param IP the IP address of the server.
     * @param port the port of the server.
     * @param timeOut time after which, if not responding, the client will be disconnected.
     */
    public ClientConnection(String IP, String port, int timeOut){
        this.IP = IP;
        this.port = port;
        this.timeOut = timeOut;
        this.connection = new CheckConnection(timeOut);
    }

    /**
     * Method return the status of the client.
     */
    public String getStatus() {
        return status;
    }

    /**
    * Method return the IP address of the server.
    */
    public String getIP() {
        return IP;
    }
    /**
     * Method return the port address of the server.
     */
    public String getPort() {
        return port;
    }
    /**
     * Method return how long the client has been connected.
     */
    public int getTimeOut() {
        return timeOut;
    }
    /**
     * Method return the custom player size of this match
     */
    abstract int getExpectedPlayers();

    /**
     * Method asks the host if the match should start
     */
    abstract boolean getStart();

    /**
     * Method set the status of the client
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * Method start the connection
     */
    abstract void startCheckConnection();

    /**
     * Method retun the name of the player
     */
    abstract String getName();

    /**
     *Method return the color of the player
     */
    abstract String getColor();

    /**
     * Method return the chosen ObjectiveCard
     */
    abstract  ObjectiveCardMessage getChosenObjective();

    /**
     * Method return the chosen StarterCard
     */
    abstract StarterCardMessage getStarterCard();

    /**
     * Method return the chosen Placeablecard
     */
    abstract PlaceableCardMessage getPlaceCard();

    /**
     * Method return the chosen pickCard
     */
    abstract pickCardMessage getPickCard();

    /**
     * Method sends StarterCard information
     */
    abstract void sendStateStarterCard(StarterCardMessage starterCardMessage);

    /**
     * Method sends two ObjectiveCard information of witch the player can choose one
     */
    abstract void sendStateChooseObjective(ObjectiveCardMessage message);

    /**
     * Method sends all game information
     */
    abstract void sendStateTurn(TurnMessage message);

    /**
     * Method sends all game information when the match is finished
     */
    abstract void sendStateFinishMatch(endGameMessage message);



    abstract void sendAnswerToConnection(answerConnectionMessage message);

    /**
     * Method retun all unavailable names
     */
    abstract void sendUnavailableNames(unavailableNamesMessage message);

    /**
     * Method asks the player to choose a name
     */
    abstract void sendAnswerToChosenName(choseNameMessage message);

    /**
     * Method asks the player to choose a color
     */
    abstract void sendAnswerToChosenColor(choseColorMessage message);

    /**
     * Method tells the client the available colors
     */
    abstract void sendAvailableColors(availableColorsMessage message);

    /**
     * Method send information about the player's hand
     */
    abstract void sendShowYourHand(playerHandMessage message);

    /**
     * Method send all the players' points
     */
    abstract void sendShowAllPoints(allPointsMessage message);

    /**
     * Method send all available positions to place cards
     */
    abstract void sendAvailablePositions(availablePositionMessage message);

    /**
     * Method asks the client to choose one of the cards in his hand to place
     */
    abstract void sendShowAnswerToPlaceCard(placeCardResponseMessage message);

    /**
     * Method asks the client to choose one of the cards to pick
     */
    abstract void sendShowAnswerToPickCard(pickCardResponseMessage message);

    /**
     * Method sends end of game information
     */
    abstract void sendEndGame(endGameMessage message);

    /**
     * Method sends the number of completed ObjectiveCards
     */
    abstract void senCountObjectives(countObjectivesMessage message);

    /**
     * Method sends the winner of the match
     */
    abstract void sendDeclareWinner(declareWinnerMessage message);

    /**
     * Method sends a message to throw the client out
     */
    abstract void sendKicked(kickedMessage message);






}
