package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.server.RMI.MessageExchanger;
import it.polimi.ingsw.protocol.server.RMI.MessageExchangerInterface;
import it.polimi.ingsw.protocol.server.RMI.RemoteServerInterface;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientRMI extends ClientConnection implements Runnable{
    private MessageExchangerInterface exchanger;

    /**
     * method {@code ClientRMI}: constructs a new ClientRMI
     * @param IP: String
     * @param port: String
     */
    public ClientRMI(String IP, String port) {
        super(IP, port);
    }

    public ClientRMI(int rmiCounter, String lookupString, int portRMI) throws RemoteException, AlreadyBoundException {
        super("RMI_Client_" + rmiCounter, "no_port");
        Registry registry = LocateRegistry.createRegistry(portRMI);
        this.exchanger = new MessageExchanger();
        registry.bind(lookupString, this.exchanger);
    }



    /**
     *  Empty method, needed for executors to work
     */
    @Override
    public void run() {

    }

    /**
     * method {@code getServerOption}: sends an empty server option message and expects an answer.
     * @return serverOptionMessage
     */
    @Override
    public serverOptionMessage getServerOption() {
        try {
            this.exchanger.sendMessage(new serverOptionMessage(false,null,null,false,null));
            return (serverOptionMessage) this.exchanger.receiveMessage();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendNewHostMessage}: sends a newHostMessage
     * @param hostNickname: String
     */
    @Override
    public void sendNewHostMessage(String hostNickname){
        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getExpectedPlayer}: receives a expectedPlayerMessage
     * @return expectedPlayersMessage
     */
    @Override
    public expectedPlayersMessage getExpectedPlayer(){
        try {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAnswer}: sends a responseMessage
     * @param correct: boolean
     */
    @Override
    public void sendAnswer(boolean correct){
        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAnswerToConnection}: sends a connectionResponseMessage
     * @param message: connectionResponseMessage
     */
    @Override
    public void sendAnswerToConnection(connectionResponseMessage message){
        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendUnavailableName}: sends a unavailableNamesMessage
     * @param unavailableNames: ArrayList<String>
     */
    @Override
    public void sendUnavailableName(ArrayList<String> unavailableNames) {
        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * method {@code getName}: receives a chosenNameMessage
     * @return chosenNameMessage
     */
    @Override
    public chosenNameMessage getName(ArrayList<String> unavailableNames){
        try {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendAvailableColor}: sends a availableColorsMessage
     * @param availableColors: ArrayList<String>
     */
    @Override
    public void sendAvailableColor(ArrayList<String> availableColors){
        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getColor}: receives a chosenColorMessage
     * @return chosenColorMessage
     */
    @Override
    public chosenColorMessage getColor(ArrayList<String> availableColors){
        try {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendCurrentState}: sends a currentStateMessage
     * @param currentState: currentStateMessage
     */
    @Override
    public void sendCurrentState(currentStateMessage currentState){
        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getStaterCard}: receives a starterCardMessage
     * @return starterCardMessage
     */
    @Override
    public starterCardMessage getStaterCard(){
        try {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getChosenObjective}: receives a objectiveCardMessage
     * @return objectiveCardMessage
     */
    @Override
    public objectiveCardMessage getChosenObjective(ObjectiveCard[] objectiveCards){
        try {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getPlaceCard}: receives a placeCardMessage
     * @return placeCardMessage
     */
    @Override
    public placeCardMessage getPlaceCard(){
        try {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getChosenPick}: receives a pickCardMessage
     * @return pickCardMessage
     */
    @Override
    public pickCardMessage getChosenPick(){
        try {
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendEndGame}: sends a declareWinnerMessage
     * @param score: HashMap<String, Integer> score
     * @param numberOfObjectives: HashMap<String, Integer> score
     */
    @Override
    public void sendEndGame(HashMap<String, Integer> score, HashMap<String, Integer> numberOfObjectives){
        try {
            return;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code sendUpdatePlayer}: sends a updatePlayerMessage
     * @param updateMessage: updatePlayerMessage
     */
    @Override
    public void sendUpdatePlayer(updatePlayerMessage updateMessage) {
        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code closeConnection}: closes the connection
     */
    @Override
    public void closeConnection() {
        try {


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


