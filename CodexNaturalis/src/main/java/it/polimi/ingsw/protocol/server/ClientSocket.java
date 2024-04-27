package it.polimi.ingsw.protocol.server;


import it.polimi.ingsw.protocol.messages.*;

public class ClientSocket extends ClientConnection {

    public ClientSocket(String IP, String port, int timeOut) {
        super(IP, port, timeOut);
    }

    /**
     * Method return the custom player size of this match
     */
    @Override
    int getExpectedPlayers() {
        return 0;
    }

    /**
     * Method asks the host if the match should start
     */
    @Override
    boolean getStart() {
        return false;
    }

    /**
     * Method start the connection
     */
    @Override
    void startCheckConnection() {

    }

    /**
     * Method retun the name of the player
     */
    @Override
    String getName() {
        return "";
    }

    /**
     * Method return the color of the player
     */
    @Override
    String getColor() {
        return "";
    }

    /**
     * Method return the chosen ObjectiveCard
     */
    @Override
    ObjectiveCardMessage getChosenObjective() {
        return null;
    }

    /**
     * Method return the chosen StarterCard
     */
    @Override
    StarterCardMessage getStarterCard() {
        return null;
    }

    /**
     * Method return the chosen Placeablecard
     */
    @Override
    PlaceableCardMessage getPlaceCard() {
        return null;
    }

    /**
     * Method return the chosen pickCard
     */
    @Override
    pickCardMessage getPickCard() {
        return null;
    }

    /**
     * Method sends StarterCard information
     *
     * @param starterCardMessage
     */
    @Override
    void sendStateStarterCard(StarterCardMessage starterCardMessage) {

    }

    /**
     * Method sends two ObjectiveCard information of witch the player can choose one
     *
     * @param message
     */
    @Override
    void sendStateChooseObjective(ObjectiveCardMessage message) {

    }

    /**
     * Method sends all game information
     *
     * @param message
     */
    @Override
    void sendStateTurn(TurnMessage message) {

    }

    /**
     * Method sends all game information when the match is finished
     *
     * @param message
     */
    @Override
    void sendStateFinishMatch(endGameMessage message) {

    }

    @Override
    void sendAnswerToConnection(answerConnectionMessage message) {

    }

    /**
     * Method retun all unavailable names
     *
     * @param message
     */
    @Override
    void sendUnavailableNames(unavailableNamesMessage message) {

    }

    /**
     * Method asks the player to choose a name
     *
     * @param message
     */
    @Override
    void sendAnswerToChosenName(choseNameMessage message) {

    }

    /**
     * Method asks the player to choose a color
     *
     * @param message
     */
    @Override
    void sendAnswerToChosenColor(choseColorMessage message) {

    }

    /**
     * Method tells the client the available colors
     *
     * @param message
     */
    @Override
    void sendAvailableColors(availableColorsMessage message) {

    }

    /**
     * Method send information about the player's hand
     *
     * @param message
     */
    @Override
    void sendShowYourHand(playerHandMessage message) {

    }

    /**
     * Method send all the players' points
     *
     * @param message
     */
    @Override
    void sendShowAllPoints(allPointsMessage message) {

    }

    /**
     * Method send all available positions to place cards
     *
     * @param message
     */
    @Override
    void sendAvailablePositions(availablePositionMessage message) {

    }

    /**
     * Method asks the client to choose one of the cards in his hand to place
     *
     * @param message
     */
    @Override
    void sendShowAnswerToPlaceCard(placeCardResponseMessage message) {

    }

    /**
     * Method asks the client to choose one of the cards to pick
     *
     * @param message
     */
    @Override
    void sendShowAnswerToPickCard(pickCardResponseMessage message) {

    }

    /**
     * Method sends end of game information
     *
     * @param message
     */
    @Override
    void sendEndGame(endGameMessage message) {

    }

    /**
     * Method sends the number of completed ObjectiveCards
     *
     * @param message
     */
    @Override
    void senCountObjectives(countObjectivesMessage message) {

    }

    /**
     * Method sends the winner of the match
     *
     * @param message
     */
    @Override
    void sendDeclareWinner(declareWinnerMessage message) {

    }

    /**
     * Method sends a message to throw the client out
     *
     * @param message
     */
    @Override
    void sendKicked(kickedMessage message) {

    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {

    }
}
