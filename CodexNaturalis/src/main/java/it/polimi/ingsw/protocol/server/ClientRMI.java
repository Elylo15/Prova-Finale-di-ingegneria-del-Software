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
import it.polimi.ingsw.protocol.server.RMI.RemoteServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientRMI extends ClientConnection {
    private RemoteServerInterface remoteServer;

    /**
     * method {@code ClientRMI}: constructs a new ClientRMI
     * @param IP: String
     * @param port: String
     */
    public ClientRMI(String IP, String port) {
        super(IP, port);
    }

    @Override
    public serverOptionMessage getServerOption() {
        return null;
    }

    /**
     * method {@code run}: establishes the RMI connection by obtaining a reference to the remote server object.
     */
    @Override
    public void run() {

//        try {
//            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(getPort()));
//            MessageRegistryInterface messageRegistry = new MessageRegistry();
//            registry.bind("MessageRegistry", messageRegistry);
//            RemoteServerInterface remoteObject = new RemoteServer(messageRegistry);
//            registry.bind("RemoteServer", remoteObject);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        try {
            Registry registry = LocateRegistry.getRegistry(getIP(), Integer.parseInt(getPort()));
            remoteServer = (RemoteServerInterface) Naming.lookup("RemoteServer");
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
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
            remoteServer.sendNewHostMessageRMI(new newHostMessage(hostNickname));
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
            return remoteServer.getExpectedPlayerRMI();
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
            remoteServer.sendAnswerRMI(new responseMessage(correct));
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
            remoteServer.sendAnswerToConnectionRMI(message);
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
            remoteServer.sendUnavailableNameRMI(new unavailableNamesMessage(unavailableNames));
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
            return remoteServer.getNameRMI();
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
            remoteServer.sendAvailableColorRMI(new availableColorsMessage(availableColors));
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
            return remoteServer.getColorRMI();
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
            remoteServer.sendCurrentStateRMI(currentState);
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
            return remoteServer.getStaterCardRMI();
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
            return remoteServer.getChosenObjectiveRMI();
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
            return remoteServer.getPlaceCardRMI();
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
            return remoteServer.getChosenPickRMI();
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
            remoteServer.sendEndGameRMI(new declareWinnerMessage(score, numberOfObjectives));
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
            remoteServer.sendUpdatePlayerRMI(updateMessage);
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
            Registry registry = LocateRegistry.getRegistry();
            registry.unbind("RemoteServer");
            UnicastRemoteObject.unexportObject(registry, true);
            UnicastRemoteObject.unexportObject(remoteServer, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


