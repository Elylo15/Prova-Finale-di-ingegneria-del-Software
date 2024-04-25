package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Json.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;

import java.io.Serializable;

public class  Player implements Serializable {
    private final String nickname;
    private final String color;
    private int score;
    private final PlayerHand deck;
    private final PlayerArea playerArea;
    private final CommonArea commonArea;
    private ObjectiveCard objective;

    /**
     * Constructs a new {@code Player} object with the specified {@param nickname} and {@param color}.
     */
    public Player(String nickname, String color, CommonArea commonArea){
        this.nickname = nickname;
        this.color = color;
        this.score = 0;
        this.deck = new PlayerHand();
        this.playerArea = new PlayerArea();
        if(commonArea == null)
            this.commonArea = (new LoadDecks()).load();
        else
            this.commonArea = commonArea;
    }

    /**
     * method {@code initialHand}: the player draws one starterCard and chooses if he wants to place it front or back.
     * The player draws two resourceCard, one goldCard, two objectiveCard.
     * The player chooses one between the two objectiveCard.
     * @param pick integer that indicates the objective card chosen.
     * @param side integer that indicates the side chosen.
     */
    public void initialHand(int side, int pick){

        playerArea.placeStarterCard(commonArea.drawFromToPlayer(3), pickSide(side));
        deck.addNewplaceableCard(commonArea.drawFromToPlayer(3)); //starterCard
        deck.addNewplaceableCard(commonArea.drawFromToPlayer(1)); //draw resource
        deck.addNewplaceableCard(commonArea.drawFromToPlayer(1)); //draw resource
        deck.addNewplaceableCard(commonArea.drawFromToPlayer(2)); //draw gold
        this.objective = pickObjectiveCard(pick);
    }

    /**
     * method {@code pickObjectiveCard}: the player picks one of the two ObjectiveCard drawn.
     * @param pick integer that indicates the objective card chosen.
     * @return ObjectiveCard.
     */
    public ObjectiveCard pickObjectiveCard(int pick){
        ObjectiveCard objective1 = commonArea.drawObjectiveCard();
        ObjectiveCard objective2 = commonArea.drawObjectiveCard();

        if(pick==1)
            return objective1;
        else
            return objective2;
    }

    /**
     * Method {@code pickSide}: the player picks if he wants to place the card front (true) or back (false).
     * @param side integer that indicates the side chosen.
     * @return boolean.
     */
    public boolean pickSide(int side){
        return side == 1;
    }

    /**
     * method {@code playTurn}: the player chooses a card and a position
     * The card is removed from the player's deck and placed in his playerArea.
     * The player chooses to draw a card from one of the decks on the table, or to draw one of the face-up cards.
     * @param cardPick integer that indicates the card chosen.
     * @param x position.
     * @param y position.
     * @param side integer that indicates the side chosen.
     * @param drawPick integer that indicates the card chosen.
     * @throws noPlaceCardException if there is an error placing the card.
     * @throws InvalidIdException if there is an error in pickNewCard.
     */
    public void playTurn(int cardPick, int x, int y, int side, int drawPick) throws noPlaceCardException, InvalidIdException {
        int[] position;
        int cardID;
        Card card;

        card = pickPlaceableCard(cardPick);
        cardID = card.getID();
        position = pickPosition(x, y);
        try {
            score = getScore() + playerArea.placeCard(deck.removeplaceableCard(cardID), position[0], position[1], pickSide(side));
            if(score > 29)
                score = 29;
        } catch (noPlaceCardException e) {
            throw new noPlaceCardException();
        }

        pickNewCard(drawPick);
    }

    /**
     * method {@code pickPosition}: the player picks one of the available positions to place the card.
     * @param x position.
     * @param y position.
     * @return array of two integers.
     */
    public int[] pickPosition(int x, int y){
        int[] position = new int[2];
        position[0] = x;
        position[1] = y;

        return position;
    }

    /**
     *  method {@code pickPlaceableCard}: the player has chooses one of his cards.
     * @param cardPick integer that indicates the card chosen.
     * @return PlaceableCard.
     */
    public PlaceableCard pickPlaceableCard(int cardPick){

        if(cardPick==1)
            return deck.getPlaceableCards().get(1);
        if(cardPick==2)
            return deck.getPlaceableCards().get(2);
        else
            return deck.getPlaceableCards().get(3);
    }

    /**
     * method {@code pickNewCard}: the player draws a card which is added to his personal deck.
     * @param drawPick integer that indicates the card chosen.
     */
    public void pickNewCard(int drawPick) throws InvalidIdException {

        if(drawPick < 1 || drawPick > 6)
            throw new InvalidIdException();

        if (drawPick == 1)
            deck.addNewplaceableCard(commonArea.drawFromToPlayer(1));
        else if (drawPick == 2)
            deck.addNewplaceableCard(commonArea.drawFromToPlayer(2));
        else if (drawPick == 3)
            deck.addNewplaceableCard(commonArea.pickTableCard(commonArea.getTableCards().getFirst().getID()));
        else if(drawPick==4)
            deck.addNewplaceableCard(commonArea.pickTableCard(commonArea.getTableCards().get(1).getID()));
        else if(drawPick==5)
            deck.addNewplaceableCard(commonArea.pickTableCard(commonArea.getTableCards().get(2).getID()));
        else
            deck.addNewplaceableCard(commonArea.pickTableCard(commonArea.getTableCards().get(3).getID()));

    }

    /**
     * @return score related to the player.
     */
    public int getScore(){
        return score;
    }

    /**
     * @param newScore updated score.
     */
    public void setScore(int newScore){
        score = newScore;
    }

    /**
     * @param newObjective score.
     */
    public void setObjective(ObjectiveCard newObjective){
        objective = newObjective;
    }

    /**
     * @return playerArea related to the player.
     */
    public PlayerArea getPlayerArea(){
        return playerArea;
    }

    /**
     * @return objective related to the player.
     */
    public ObjectiveCard getObjective() {
        return objective;
    }

    /**
     * @return playerHand related to the player.
     */
    public PlayerHand getPlayerHand(){
        return deck;
    }

}
