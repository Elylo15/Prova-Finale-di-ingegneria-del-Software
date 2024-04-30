package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.currentStateMessage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ViewCLI extends View{
    //the only purpose of ViewCLI is to visualize what the user needs to see in order to play the game
    // by printing it in the command line
    public ViewCLI(){
        super();

    }

    /**
     *
     * @param message
     */
    public void updatePlayer(currentStateMessage message){
        System.out.println("player " + message.getPlayer() + "is in the state " + message.getStateName());
        if(message.isLastTurn()){
            System.out.println("this is the last turn");
        }
    }


    /**
     * this method allow the user to say if he wants to connect with socket or rmi
     * @return boolean
     */
    public boolean askSocket(){
        //if the user doesn't enter a boolean, an exception is thrown and the method return default value true
        Scanner scanner = new Scanner(System.in);
        boolean socket= true;
        try {
            System.out.println("enter true if yo want to connect with socket, enter false if you want to connect with rmi");
            socket = scanner.nextBoolean();
        }
        catch (Exception e){
            System.out.println("you didn't enter a boolean");
        }

        return socket;
    }


    /**
     * this method allow the client to visualize the server response about the connection
     * @param message
     */
    public void answerToConnection(connectionResponseMessage message){
      if(message.getCorrect()){
          System.out.println("the connection has been established");
      }
      else{
          System.out.println("the connection has not been established");
      }
    }

    /**
     *
     * @param message
     * @return message with the values chosen by the user
     */
    public serverOptionMessage serverOptions(serverOptionMessage message){
        Scanner scanner = new Scanner(System.in);
        boolean newMatch = false;
        try {
            System.out.println("enter true if this is a new match or false if it is not");
            newMatch = scanner.hasNextBoolean();
            scanner.nextLine();
        }
        catch(Exception e){
            System.out.println("you didn't enter a boolean");
        }
        String startedMatchID;

            System.out.println("enter the started match ID");
            startedMatchID = scanner.nextLine();

        String nickname;
            System.out.println("enter your nickname");
            nickname = scanner.nextLine();

        boolean loadMatch = false;
        try {
            System.out.println("enter true if you want to load the match");
            loadMatch = scanner.hasNextBoolean();
            scanner.nextLine();
        }
        catch(Exception e){
            System.out.println("you didn't enter a boolean");
        }
        String pathToLoad;
            System.out.println("enter the path to load");
            pathToLoad = scanner.nextLine();
         message = new serverOptionMessage(newMatch,startedMatchID,nickname,loadMatch,pathToLoad);
        return message;
    }

    /**
     * allow the user to see if he managed to join the match
     * @param message
     */
    public void answerToOption(serverOptionResponseMessage message){
        if(message.getCorrect()){
            System.out.println("You joined correctly the match " + message.getMatchID());
        }
        else{
            System.out.println("An error occurred, you couldn't join the game");
        }
    }

    /**
     * this method shows what nickname are not available and allows the user to choose his nickname
     * @param message
     * @return
     */
    public String unavaibleNames(unavailableNamesMessage message){
        //the client can call the method view.unavaibleNames passing as a parameter the arraylist of unavailable names received from server
        System.out.println("This nicknames are not avaible: " + message.toString());

        String name = null;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buf = new BufferedReader(input);
        try {

            System.out.println("You need to choose a nickname to play the game");

            name = buf.readLine();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                buf.close();
            }
            catch (IOException e){
                e.printStackTrace();

            }
        }
        return name;
    }

    /**
     * the server will send a response to the client about its nickname choice
     * @param message
     */
    public void answerToNameChosen(nameResponseMessage message){
        if(message.getCorrect()){
            System.out.println("You have entered a valid name");
        }
        else{
            System.out.println("You didn't entered a valid name, please try again");
        }
    }


    /**
     * this method shows what colors are available and allows the user to choose his color
     * @param message
     */
    public String availableColors(availableColorsMessage message){
        //the client can call the method view.avaibleColors passing as a parameter the arraylist of available colors received from server
        System.out.println("This are the colors that are available: " + message.toString());

        String color=null;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buf = new BufferedReader(input);
        try {
                System.out.println("You need to choose a color, please enter a valid color to play");

                color = buf.readLine();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        finally {
            try {
                buf.close();
            }
            catch (IOException e){
                e.printStackTrace();

            }
        }
        return color;
    }

    /**
     *the server will send a response to the client about its color choice
     * @param message
     */
    public void answerToColorChosen(colorResponseMessage message){
        if(message.getCorrect()){
            System.out.println("You have entered a correct color");
        }
        else {
            System.out.println("You didn't entered a correct color, please try again");
        }
    }


    /**
     *this method allow the user to place his starter card
     * @return int
     */
    public int placeStarter(){
        //if the user does not enter an int an exception is thrown and the method return the initialized value
        int side = 10;
       System.out.println("You have to place your starter");
        System.out.println("Enter 1 if you want to play the front side, 0 if you want to play the back side");
        try {
            Scanner scanner = new Scanner(System.in);
            side = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("You didn't enter an integer value");
        }
        return side;
    }

    /**
     *
     * @param message
     * @return
     */
    public void answerToPlaceStarter(starterCardResponseMessage message){
       if(message.getCorrect()){
           System.out.println("You placed the starter card correctly");
       }
       else{
           System.out.println("You didn't place the starter card correctly, please try again");
       }
    }

    /**
     * allow the user to choose how many players will play
     * @return number of expected players
     */
    public int expectedPlayers(){
        int numExpected=0;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("how many player do you want to be in the game");
            numExpected = scanner.nextInt();
        }catch (Exception e){
            System.out.println("you didn't enter an integer value");
        }
        return numExpected;

    }

    /**
     *
     * @param message
     */
    public void answerToExpectedPlayers(expectedPlayersResponseMessage message){
        if(message.getCorrect()){
            System.out.println("the number of players has been set correctly");
        }
    }

    /**
     * allow the user to choose his secret objective
     * @return objective
     */
    public int chooseObjective(){
        int objective = 10;
        System.out.println("You have to choose your personal objective");
        System.out.println("Enter 1 if you want to choose the first one, 2 if you want to choose the second one");
        try {
            Scanner scanner = new Scanner(System.in);
            objective = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("You didn't enter an integer value");
        }
        return objective;
    }

    /**
     * the user visualize the response about his objective choice
     * @param message
     */
    public void answerToChooseObjective(objectiveCardResponseMessage message){
        if(message.getCorrect()){
            System.out.println("you have chosen your secret objective correctly");
        }
        else {
            System.out.println("You didn't chosen your secret objective correctly");
        }

    }

    /**
     * allow the user to say what card he wants to play, front or back, and in which position
     * @return
     */
    public  int[] placeCard(){
       int[] chosenCard = new int[4];
       //if the user does not enter integer values, the method will return an array of invalid values for placing the card
        chosenCard[0] = 1000;
        chosenCard[1] = 1000;
        chosenCard[2] = 1000;
        chosenCard[3] = 1000;
        Scanner scanner = new Scanner(System.in);
        try{
            System.out.println("enter the ID of the card you want to place");
            chosenCard[0]= scanner.nextInt();
        }catch (Exception e){
            System.out.println("you didn't enter an integer value");
        }
        try{
            System.out.println("enter 1 if you want to place the card front, 0 if you want to place the card back");
            chosenCard[1]= scanner.nextInt();
        }catch (Exception e){
            System.out.println("you didn't enter an integer value");
        }
        try{
            System.out.println("enter the x coordinate of the cell where you want to place the card");
            chosenCard[2]= scanner.nextInt();
        }catch (Exception e){
            System.out.println("you didn't enter an integer value");
        }
        try{
            System.out.println("enter the y coordinate of the cell where you want to place the card");
            chosenCard[3]= scanner.nextInt();
        }catch (Exception e){
            System.out.println("you didn't enter an integer value");
        }
        return chosenCard;


    }

    /**
     * the user visualize the response about the card he placed
     * @param message
     */
    public void answerToPlaceCard(placeCardResponseMessage message){
       if(message.getCorrect()){
           System.out.println("you placed the card correctly");
       }
       else{
           System.out.println("you didn't placed the card correctly");
       }
    }

    /**
     * allow the user to say what card he wants to pick
     * @return the id of the card the user wants to pick
     */
    public  int pickCard(){
       int choice=1000;
       Scanner scanner = new Scanner(System.in);
       try{
           System.out.println("enter the ID of the card you want to pick");
           choice = scanner.nextInt();
       }
       catch (Exception e){
           System.out.println("you didn't enter an integer value");
       }
       return choice;
    }

    /**
     * the user visualize the response about the card he picked
     * @param message
     */
    public void answerToPickCard(pickCardResponseMessage message){
        if(message.getCorrect()){
            System.out.println("you picked the card correctly");
        }
        else {
            System.out.println("you could not pick the card correctly");
        }
    }

    /**
     * visualize the final information about the game, points and number of objectives achieved by the players
     * @param message
     */
    public void endGame(declareWinnerMessage message){
        HashMap<String, Integer> points = new HashMap<>();
        HashMap<String, Integer> numObjectives = new HashMap<>();

        points = message.getPlayersPoints();
        numObjectives = message.getNumberOfObjects();
        System.out.println("the points of the players are: " + points);
        System.out.println("the players have achieved this number of objectives:  " + numObjectives);

    }





}