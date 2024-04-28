package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.PlayerDisconnectedState.disconnectedMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ViewCLI extends View{
    //the only purpose of ViewCLI is to visualize what the user needs to see in order to play the game
    // by printing it in the command line
    public ViewCLI(){
        super();

    }
    public void answerToConnection(answerConnectionMessage message){

    }

    /**
     * this method shows what nickname are not available
     * @param message
     * @return
     */
    public void unavaibleNames(unavailableNamesMessage message){
        //the client can call the method view.unavaibleNames passing as a parameter the arraylist of unavailable names received from server
        System.out.println("This nicknames are not avaible: " + message.toString());

    }

    /**
     * this method allow the client to get the nickname of the user
     * @return
     */
    public  String askNickname(){
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
     * @param choice
     */
    public void answerToNameChose(boolean choice){
        //the client will call view.answerToNameChose(true) if he received a positive response, else view.answerToNameChose(false)
        if(choice){
            System.out.println("You have entered a correct choice");
        }
        else{
            System.out.println("You didn't entered a correct choice, please try again");
        }
    }


    /**
     * this method tell the client he needs to choose a color and shows what colors are available
     * @param message
     */
    public void avaibleColors(availableColorsMessage message){
        //the client can call the method view.avaibleColors passing as a parameter the arraylist of available colors received from server
        System.out.println("This are the colors that are available: " + message.toString());

    }
    /**
     * this method allow the client to get the color of the user
     */
    public String askColor(){
        String color=null;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buf = new BufferedReader(input);
        try {
            do {
                System.out.println("You need to choose a color, please enter a valid color to play");

                color = buf.readLine();

            } while (!color.equalsIgnoreCase("rosso")&&!color.equalsIgnoreCase("giallo")
                    &&!color.equalsIgnoreCase("verde")&&!color.equalsIgnoreCase("blu"));

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
     * @param choice
     */
    public void answerToColorChosen(boolean choice){
        //the client will call view.answerToNameChose(true) if he received a positive response, else view.answerToNameChose(false)
        if(choice){
            System.out.println("You have entered a correct color");
        }
        else {
            System.out.println("You didn't entered a correct color, please try again");
        }
    }

    /**
     * this method tells the user it's his turn to play
     * @param turn
     */
    public void yourTurnStarts(yourTurnStarts turn){
        System.out.println(turn.toString());
    }

    /**
     * this method tells the user it is not his turn to play
     * @param turn
     */
    public void notYourTurnStarts(notYourTurnStarts turn){
        System.out.println(turn.toString());
    }
    public int placeStarter(){

    }
    public boolean answerToPlaceStarter(starterCardResponseMessage message){

    }
    public int chooseObjective(){

    }
    public boolean answerToChooseObjective(objectiveCardResponseMessage message){

    }


    public  int[] placeCard(){

    }
    public  boolean answerToPlaceCard(placeCardResponseMessage message){

    }


    public  int pickCard(){

    }

    public  boolean answerToPickCard(pickCardResponseMessage message){

    }
    public void endGame(declareWinnerMessage message){

    }

    /**
     * with this method show to the client the message he has been kicked from the game
     * @param message
     */
    public void youAreKickedPlayer(kickedMessage message){
        System.out.println(message);
    }



}