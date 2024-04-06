package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class  Player {
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
        this.commonArea =  commonArea;
    }

    /**
     * method {@code initialHand}: the player draws one starterCard and chooses if he wants to place it front or back.
     * The player draws two resourceCard, one goldCard, two objectiveCard.
     * The player chooses one between the two objectiveCard.
     */
    public void initialHand(){

        playerArea.placeStarterCard(commonArea.drawFromToPlayer(3), pickSide()); //starterCard

        deck.addNewplaceableCard(commonArea.drawFromToPlayer(1)); //draw resource
        deck.addNewplaceableCard(commonArea.drawFromToPlayer(1)); //draw resource
        deck.addNewplaceableCard(commonArea.drawFromToPlayer(2)); //draw gold

        objective = pickObjectiveCard( commonArea.drawObjectiveCard(), commonArea.drawObjectiveCard());

    }

    /**
     * method {@code pickObjectiveCard}: the player picks one of the two ObjectiveCard drawn.
     * @param objective1 first drawn ObjectiveCard.
     * @param objective2 second drawn ObjectiveCard.
     * @return ObjectiveCard
     */
    private ObjectiveCard pickObjectiveCard(ObjectiveCard objective1, ObjectiveCard objective2){
        Scanner scanner = new Scanner(System.in);

        //show cards
        //This will be moved

        System.out.print("""
                Pick an Objective Card:
                    -1
                    -2
                """);
        int pick = scanner.nextInt();
        scanner.nextLine();// if letter in input?

        while(pick!=1 && pick!=2) {
            System.out.println("You entered a wrong value. Try Again:");
            pick = scanner.nextInt();
            scanner.nextLine();
        }

        if(pick==1)
            return objective1;
        else
            return objective2;

    }

    /**
     * Method {@code pickSide}: the player picks if he wants to place the card front(return true) or back(return  false).
     * @return boolean
     */
    private boolean pickSide(){
        Scanner scanner = new Scanner(System.in);

        //This will be moved

        System.out.print("""
                Place the card:
                     -1 Front
                     -2 Back
                """);
        int side = scanner.nextInt();

        while(side!=1 && side!=2) {
            System.out.println("You entered a wrong value. Try Again:");
            side = scanner.nextInt();
        }

        return side == 1;

    }

    /**
     * method {@code playTurn}: the player chooses a card and a position
     * The card is removed from the player's deck and placed in his playerArea.
     * The player chooses to draw a card from one of the decks on the table, or to draw one of the face-up cards.
     */
    public void playTurn(){
        int[] position;
        int cardID;
        Card card;

        while (true) {
            try {
                card = pickPlaceableCard(deck.getPlaceableCards());
                cardID = card.getID();
                position = pickPosition(playerArea.getAvailablePosition());
                score = getScore() + playerArea.placeCard(deck.removeplaceableCard(cardID), position[0], position[1], pickSide());
                break;
            } catch (noPlaceCardException exception) {
                System.out.println("You didn't place the card correctly. Try again:");
            }
        }

        pickNewCard(commonArea.getTableCards());
    }

    /**
     * method {@code pickPosition}: the player picks one of the available positions to place the card.
     * @param availablePosition arrayList of integers that contains the available positions.
     * @return array of two integers.
     */
    private int[] pickPosition(ArrayList<Integer[]> availablePosition){
        Scanner scanner = new Scanner(System.in);
        int[] position = new int[2];

        //This will be moved

        System.out.println("Available positions:");     //print the available positions
        for(int i=0; i<availablePosition.size(); i=i+2){
            System.out.println("x: "+Arrays.toString(availablePosition.get(i))+", y: "+Arrays.toString(availablePosition.get(i+1)));
        }

        System.out.println("Pick an available x position:");
        position[0] = scanner.nextInt();
        System.out.println("Pick an available y position:");
        position[1] = scanner.nextInt();

        return position;
    }

    /**
     *  method {@code pickPlaceableCard}: the player chooses one of his cards.
     * @param cards arrayList containing his cards.
     * @return PlaceableCard.
     */
    private PlaceableCard pickPlaceableCard(ArrayList<PlaceableCard> cards){
        Scanner scanner = new Scanner(System.in);

        //show the cards
        //This will be moved

        System.out.println("""
                Pick the card you want to place:
                     -1
                     -2
                     -3
                """);
        int cardPick = scanner.nextInt();

        while(cardPick!=1 && cardPick!=2 && cardPick!=3){
            System.out.println("You entered a wrong value. Try Again.");
            cardPick = scanner.nextInt();
        }

        if(cardPick==1)
            return cards.get(0);
        if(cardPick==2)
            return cards.get(1);
        else
            return cards.get(2);
    }

    /**
     * method{@code pickNewCard}: the player chooses if he wants to draw a card from the table or from one of the decks.
     * He draws the card which is added to his personal deck.
     * @param cards arrayList containing the cards placed face-up on the table
     */
    private void pickNewCard(ArrayList<PlaceableCard> cards) {
        Scanner scanner = new Scanner(System.in);

        //show cards
        //This will be moved

        System.out.println("""
                    Do you want to draw from:
                         -1 Resource Deck
                         -2 Gold Deck
                         -3 Left ResourceCard face-up
                         -4 Right ResourceCard face-up
                         -5 Left GoldCard face-up
                         -6 Right GoldCard face-up
                    """);
        int drawPick = scanner.nextInt();

        while (drawPick != 1 && drawPick != 2 && drawPick != 3 && drawPick != 4 && drawPick != 5 && drawPick != 6) {
            System.out.println("You entered a wrong value. Try Again.");
            drawPick = scanner.nextInt();
        }
        if (drawPick == 1)
            deck.addNewplaceableCard(commonArea.drawFromToPlayer(1));
        else if (drawPick == 2)
            deck.addNewplaceableCard(commonArea.drawFromToPlayer(2));
        else if (drawPick == 3)
            deck.addNewplaceableCard(commonArea.pickTableCard(cards.getFirst().getID()));
        else if(drawPick==4)
            deck.addNewplaceableCard(commonArea.pickTableCard(cards.get(1).getID()));
        else if(drawPick==5)
            deck.addNewplaceableCard(commonArea.pickTableCard(cards.get(2).getID()));
        else
            deck.addNewplaceableCard(commonArea.pickTableCard(cards.get(3).getID()));

    }

    /**
     * @return the player's score.
     */
    public int getScore(){
        return score;
    }

    public void setScore(int newScore){
        score = newScore;
    }

    public PlayerArea getPlayerArea(){
        return playerArea;
    }

    public ObjectiveCard getObjective() {
        return objective;
    }

}

