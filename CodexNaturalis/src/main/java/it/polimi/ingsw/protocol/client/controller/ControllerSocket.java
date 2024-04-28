package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.Message;
import it.polimi.ingsw.protocol.server.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ControllerSocket  extends Controller implements Runnable {
    private String serverIP;
    private String serverPort;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ClientConnection clientConnection;

    public ControllerSocket(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public void connectToServer(String serverIP, String serverPort) {
        try {
            socket = new Socket(serverIP, Integer.parseInt(serverPort));
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendAnswerToConnection(){
        return clientConnection.sendAnswerToConnection();
    }

    @Override
    public ArrayList<String> unaviableNicknames(){
        return clientConnection.sendUnaviableNicknames();
    }

    @Override
    public void chooseNickname(String name) {
        try {
            outputStream.writeObject(new Message(MessageType.CHOOSE_NAME, name));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean nicknameConfirmation(){
        return clientConnection.sendAnswerToChosenName();
    }

    @Override
    public void chooseColor(String color) {
        try {
            outputStream.writeObject(new Message(MessageType.CHOOSE_COLOR, color));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<String> aviableColors(){
        return clientConnection.sendAviableColorss();
    }

    @Override
    public boolean colorConfirmation(){
        return clientConnection.sendAnswerToChosenColor();
    }

    @Override
    public boolean enoughPlayers(){
        return (clientConnection.getPlayers().size() > 1);
    }

    @Override
    public boolean everyoneCorrectColor(){
        return clientConnection.everyoneReadY();
    }

    @Override
    public boolean startingMatch(boolean readyStartMatch){
        if(readyStartMatch){
            //wait 10/20/30 sec, if no one connects, return true and start match
        } //else return false
    }

    @Override
    public String getAvailablePosition(){
        return clientConnection.getAvailablePosition();
    }

    @Override
    public boolean placeStarter(int side) {
        //true if placed correctly
    }

    @Override
    public boolean pickedObjective(int pick) {
        // return true if choose correctly
    }


    @Override
    public boolean placeCard(int card, int side, int x, int y) {
        // return true if placed Correctly
    }

    @Override
    public boolean pickCard( int card) {
        // return true if picked correctly
    }

    @Override
    public boolean isHost(){
        //return true if is host
    }

    @Override
    public boolean yourTurnSignal(){
        //return true if is yourTurn
    }

    @Override
    public boolean yourFirstTurnSignal(){
        //return true if is yourFirstTurn
    }

    @Override
    public boolean yourLastTurnSignal(){
        //return true if is yourLastTurn
    }

    @Override
    public boolean lastTurnSignal(){
        //return true if is lastTurn
    }

    @Override
    public boolean firstTurnSignal(){
        //return true if is firstTurn
    }

    @Override
    public void closeConnection() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

    }
}