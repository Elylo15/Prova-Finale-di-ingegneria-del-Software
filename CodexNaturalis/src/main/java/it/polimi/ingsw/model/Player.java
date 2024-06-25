package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;

import java.io.Serializable;

/**
 * This class represents a player in the game.
 * It contains the player's nickname, color, score, deck of cards, player area, common area, and objective card.
 * It provides methods for drawing and placing cards, picking objectives, and playing a turn.
 * It implements the Serializable interface.
 */
public class
Player implements Serializable {
    private final String nickname; // The player's nickname
    private final String color; // The player's color
    private final PlayerHand deck; // The player's deck of cards
    private final PlayerArea playerArea; // The player's area
    private final CommonArea commonArea; // The common area
    private int score; // The player's score
    private ObjectiveCard objective; // The player's objective card

    /**
     * This is the constructor for the Player class.
     * It initializes the player's nickname, color, score, deck of cards, player area, and common area.
     *
     * @param nickname   The player's nickname.
     * @param color      The player's color.
     * @param commonArea The common area.
     */
    public Player(String nickname, String color, CommonArea commonArea) {
        this.nickname = nickname;
        this.color = color;
        this.score = 0;
        this.deck = new PlayerHand();
        this.playerArea = new PlayerArea();
        if (commonArea == null)
            this.commonArea = (new LoadDecks()).load();
        else
            this.commonArea = commonArea;
    }

    /**
     * This method draws a starter card for the player.
     */
    public void drawStarter() {
        deck.addNewPlaceableCard(commonArea.drawFromToPlayer(3));
    }

    /**
     * This method places the player's starter card.
     *
     * @param side The side of the card to place (1 for front, 2 for back).
     */
    public void placeStarter(int side) {
        //call the method removePlaceableCard() of PlayerHand and pass as a parameter the id of the starter card
        playerArea.placeStarterCard(deck.removeplaceableCard(deck.getPlaceableCards().getFirst().getID()), pickSide(side));
    }

    /**
     * This method draws the initial hand for the player.
     * It draws two resource cards and one gold card.
     */
    public void initialHand() {
        deck.addNewPlaceableCard(commonArea.drawFromToPlayer(1)); //draw resource
        deck.addNewPlaceableCard(commonArea.drawFromToPlayer(1)); //draw resource
        deck.addNewPlaceableCard(commonArea.drawFromToPlayer(2)); //draw gold
    }

    /**
     * This method draws two objective cards for the player.
     *
     * @return ObjectiveCard[] The array of drawn objective cards.
     */
    public ObjectiveCard[] drawObjectives() {
        ObjectiveCard[] objective = new ObjectiveCard[2];
        objective[0] = commonArea.drawObjectiveCard();
        objective[1] = commonArea.drawObjectiveCard();

        return objective;
    }

    /**
     * This method allows the player to pick one of the two drawn objective cards.
     *
     * @param pick      The index of the objective card to pick (1 for the first card, 2 for the second card).
     * @param objective The array of drawn objective cards.
     */
    public void pickObjectiveCard(int pick, ObjectiveCard[] objective) {

        if (pick == 1)
            setObjective(objective[0]);
        else
            setObjective(objective[1]);
    }

    /**
     * This method allows the player to pick a side for a card.
     *
     * @param side The side to pick (1 for front, 2 for back).
     * @return boolean True if the player picks the front side, false if the player picks the back side.
     */
    private boolean pickSide(int side) {
        return side == 1;
    }

    /**
     * This method allows the player to play a turn.
     * The player chooses a card and a position, removes the card from their deck, places it in their player area, and draws a new card.
     *
     * @param cardPick The index of the card to play.
     * @param x        The x-coordinate of the position to place the card.
     * @param y        The y-coordinate of the position to place the card.
     * @param side     The side of the card to place (1 for front, 2 for back).
     * @throws noPlaceCardException If there is an error placing the card.
     */
    public void playTurn(int cardPick, int x, int y, int side) throws noPlaceCardException {
        int[] position;
        int cardID;
        Card card;

        card = pickPlaceableCard(cardPick); //card chosen from the cards in playerHand
        //the card is null if cardPick is not 0 or 1 or 2
        if (card == null)
            throw new noPlaceCardException();

        cardID = card.getID();
        position = pickPosition(x, y);

        PlaceableCard selectedCard = deck.getPlaceableCards().stream().filter(placeableCard -> placeableCard.getID() == cardID).findFirst().orElse(null);

        // Impossible, just to be sure
        if (selectedCard == null)
            throw new noPlaceCardException();

        try {
            score = getScore() + playerArea.placeCard(selectedCard, position[0], position[1], pickSide(side));
            deck.removeplaceableCard(cardID);
            if (score > 29)
                score = 29;
        } catch (noPlaceCardException e) {
            throw new noPlaceCardException();
        }
    }

    /**
     * This method allows the player to pick a position for a card.
     *
     * @param x The x-coordinate of the position to pick.
     * @param y The y-coordinate of the position to pick.
     * @return int[] The array of the picked position's coordinates.
     */
    private int[] pickPosition(int x, int y) {
        int[] position = new int[2];
        position[0] = x;
        position[1] = y;

        return position;
    }

    /**
     * This method allows the player to pick a card from their hand.
     *
     * @param cardPick The index of the card to pick.
     * @return PlaceableCard The picked card. If the player can only choose between the three cards in their hand, it returns null.
     */
    private PlaceableCard pickPlaceableCard(int cardPick) {

        if(cardPick == 0){ //return the first card in playerHand
            return deck.getPlaceableCards().getFirst();
        }
        if (cardPick == 1) { //return the second card in playerHand
            return deck.getPlaceableCards().get(1);
        }
        if (cardPick == 2) { //return the third card in playerHand
            return deck.getPlaceableCards().get(2);
        }
        //the player can only choose between the three cards in his hand
        return null;

    }

    /**
     * This method allows the player to draw a new card and add it to their deck.
     *
     * @param drawPick The index of the card to draw.
     * @throws InvalidIdException If there is an error drawing the card.
     */
    public void pickNewCard(int drawPick) throws InvalidIdException {

        if (drawPick < 1 || drawPick > 6)
            throw new InvalidIdException();
        //we throw an exception if the player wants to pick a card from a deck that is empty
        //or if he wants to pick from a position in tableCards that does not contain a card anymore

        // Check if the resource deck is empty
        if (commonArea.getD1().getSize() == 0 && drawPick == 1)
            throw new InvalidIdException();

        // Check if the gold deck is empty
        if (commonArea.getD2().getSize() == 0 && drawPick == 2)
            throw new InvalidIdException();

        // Check if the picked card is not null
        if (drawPick > 2 && commonArea.getTableCards().get(drawPick - 3) == null)
            throw new InvalidIdException();


        if (drawPick == 1)
            deck.addNewPlaceableCard(commonArea.drawFromToPlayer(1));
        else if (drawPick == 2)
            deck.addNewPlaceableCard(commonArea.drawFromToPlayer(2));
        else if (drawPick == 3)
            deck.addNewPlaceableCard(commonArea.pickTableCard(commonArea.getTableCards().getFirst().getID()));
        else if (drawPick == 4)
            deck.addNewPlaceableCard(commonArea.pickTableCard(commonArea.getTableCards().get(1).getID()));
        else if (drawPick == 5)
            deck.addNewPlaceableCard(commonArea.pickTableCard(commonArea.getTableCards().get(2).getID()));
        else
            deck.addNewPlaceableCard(commonArea.pickTableCard(commonArea.getTableCards().get(3).getID()));

    }

    /**
     * This method returns the player's score.
     *
     * @return int The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * This method returns the player's area.
     *
     * @return PlayerArea The player's area.
     */
    public PlayerArea getPlayerArea() {
        return playerArea;
    }

    /**
     * This method returns the player's objective card.
     *
     * @return ObjectiveCard The player's objective card.
     */
    public ObjectiveCard getObjective() {
        return objective;
    }

    /**
     * This method sets the player's objective card.
     *
     * @param newObjective The new objective card.
     */
    private void setObjective(ObjectiveCard newObjective) {
        this.objective = newObjective;
    }

    /**
     * This method returns the player's deck of cards.
     *
     * @return playerHand related to the player.
     */
    public PlayerHand getPlayerHand() {
        return deck;
    }

    /**
     * This method returns the player's nickname.
     *
     * @return nickname related to the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method returns the player's color.
     *
     * @return color related to the player.
     */
    public String getColor() {
        return color;
    }

    /**
     * This method returns the common area.
     *
     * @return commonArea related to the player.
     */
    public CommonArea getCommonArea() {
        return this.commonArea;
    }

}
