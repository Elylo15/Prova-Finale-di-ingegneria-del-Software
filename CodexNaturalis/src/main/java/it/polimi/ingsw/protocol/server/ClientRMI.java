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

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientRMI extends ClientConnection implements Runnable, Serializable {
    private MessageExchangerInterface toServer;
    private MessageExchangerInterface toClient;
    private Registry registry;

    /**
     * method {@code ClientRMI}: constructs a new ClientRMI
     * @param IP: String
     * @param port: String
     */
    public ClientRMI(String IP, String port) {
        super(IP, port);
    }

    public ClientRMI(int rmiCounter, String lookupString, Registry registry) throws RemoteException, AlreadyBoundException {
        super("RMI_Client_" + rmiCounter, "no_port");

        this.toServer = new MessageExchanger();
        this.toClient = new MessageExchanger();

        this.registry = registry;

        this.registry.bind(lookupString + "_toServer", this.toServer);
        this.registry.bind(lookupString + "_toClient", this.toClient);
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
    public serverOptionMessage getServerOption(ArrayList<Integer> waitingMatches,ArrayList<Integer> runningMatches, ArrayList<Integer> savedMatches) {
        try {
            this.toClient.write(new serverOptionMessage(false,null,null,false,null, waitingMatches, runningMatches, savedMatches));
            return (serverOptionMessage) this.toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getServerOption");
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
            toClient.write(new newHostMessage(hostNickname));
        } catch (RemoteException e) {
            System.out.println("Error in sendNewHostMessage");
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
            return (expectedPlayersMessage) toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getExpectedPlayer");
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
            toClient.write(new responseMessage(correct));
        } catch (RemoteException e) {
            System.out.println("Error in sendAnswer");
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
            toClient.write(message);
        } catch (RemoteException e) {
            System.out.println("Error in sendAnswerToConnection");
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
            toClient.write(new unavailableNamesMessage(unavailableNames));
        } catch (RemoteException e) {
            System.out.println("Error in sendUnavailableName");
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
            this.sendUnavailableName(unavailableNames);
            return (chosenNameMessage) toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getName");
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
            toClient.write(new availableColorsMessage(availableColors));
        } catch (RemoteException e) {
            System.out.println("Error in sendAvailableColor");
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
            this.sendAvailableColor(availableColors);
            return (chosenColorMessage) toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getColor");
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
            toClient.write(currentState);
        } catch (RemoteException e) {
            System.out.println("Error in sendCurrentState");
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
            return (starterCardMessage) toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getStaterCard");
            throw new RuntimeException(e);
        }
    }


    /**
     * method {@code getChosenObjective}: receives a objectiveCardMessage
     * @return objectiveCardMessage
     */
    @Override
    public objectiveCardMessage getChosenObjective(ArrayList<ObjectiveCard> objectiveCards) {
        try {
            toClient.write(new objectiveCardMessage(objectiveCards));
            return (objectiveCardMessage) toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getChosenObjective");
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
            return (placeCardMessage) toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getPlaceCard");
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
            return (pickCardMessage) toServer.read();
        } catch (RemoteException e) {
            System.out.println("Error in getChosenPick");
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
            toClient.write(new declareWinnerMessage(score, numberOfObjectives));
        } catch (RemoteException e) {
            System.out.println("Error in sendEndGame");
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
            toClient.write(updateMessage);
        } catch (RemoteException e) {
            System.out.println("Error in sendUpdatePlayer");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code closeConnection}: closes the connection
     */
    @Override
    public void closeConnection() {
        try {
            UnicastRemoteObject.unexportObject(toServer, true);
            UnicastRemoteObject.unexportObject(toClient, true);
        } catch (Exception e) {
            System.out.println("Error in closeConnection");
            throw new RuntimeException(e);
        }
    }
}


