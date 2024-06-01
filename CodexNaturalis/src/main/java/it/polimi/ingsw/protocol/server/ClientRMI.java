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
import java.util.concurrent.*;

public class ClientRMI extends ClientConnection implements Runnable, Serializable {
    private MessageExchangerInterface toServer;
    private MessageExchangerInterface toClient;
    private Registry registry;
    private String lookupString;

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

        this.lookupString = lookupString;
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
    public synchronized serverOptionMessage getServerOption(ArrayList<Integer> waitingMatches, ArrayList<Integer> runningMatches, ArrayList<Integer> savedMatches) {
        try {
            this.toClient.write(new serverOptionMessage(false,null,null,false,null, waitingMatches, runningMatches, savedMatches));
            return (serverOptionMessage) this.toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * method {@code sendNewHostMessage}: sends a newHostMessage
     * @param hostNickname: String
     */
    @Override
    public synchronized void sendNewHostMessage(String hostNickname){
        try {
            toClient.write(new newHostMessage(hostNickname));
        } catch (RemoteException ignore) {}
    }

    /**
     * method {@code getExpectedPlayer}: receives a expectedPlayerMessage
     * @return expectedPlayersMessage
     */
    @Override
    public synchronized expectedPlayersMessage getExpectedPlayer(){
        try {
            return (expectedPlayersMessage) toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * method {@code sendAnswer}: sends a responseMessage
     * @param correct: boolean
     */
    @Override
    public synchronized void sendAnswer(boolean correct){
        try {
            toClient.write(new responseMessage(correct));
        } catch (RemoteException ignore) {}
    }

    /**
     * method {@code sendAnswerToConnection}: sends a connectionResponseMessage
     * @param message: connectionResponseMessage
     */
    @Override
    public synchronized void sendAnswerToConnection(connectionResponseMessage message){
        try {
            toClient.write(message);
        } catch (RemoteException ignore) {}
    }

    /**
     * method {@code sendUnavailableName}: sends a unavailableNamesMessage
     * @param unavailableNames: ArrayList<String>
     */
    @Override
    protected synchronized void sendUnavailableName(ArrayList<String> unavailableNames) {
        try {
            toClient.write(new unavailableNamesMessage(unavailableNames));
        } catch (RemoteException ignore) {}
    }


    /**
     * method {@code getName}: receives a chosenNameMessage
     * @return chosenNameMessage
     */
    @Override
    public synchronized chosenNameMessage getName(ArrayList<String> unavailableNames){
        try {
            this.sendUnavailableName(unavailableNames);
            return (chosenNameMessage) toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * method {@code sendAvailableColor}: sends a availableColorsMessage
     * @param availableColors: ArrayList<String>
     */
    @Override
    protected synchronized void sendAvailableColor(ArrayList<String> availableColors){
        try {
            toClient.write(new availableColorsMessage(availableColors));
        } catch (RemoteException ignore) {}
    }

    /**
     * method {@code getColor}: receives a chosenColorMessage
     * @return chosenColorMessage
     */
    @Override
    public synchronized chosenColorMessage getColor(ArrayList<String> availableColors){
        try {
            this.sendAvailableColor(availableColors);
            return (chosenColorMessage) toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * method {@code sendCurrentState}: sends a currentStateMessage
     * @param currentState: currentStateMessage
     */
    @Override
    public synchronized void sendCurrentState(currentStateMessage currentState){
        try {
            toClient.write(currentState);
        } catch (RemoteException ignore) {}
    }

    /**
     * method {@code getStaterCard}: receives a starterCardMessage
     * @return starterCardMessage
     */
    @Override
    public synchronized starterCardMessage getStaterCard(){
        try {
            return (starterCardMessage) toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }


    /**
     * method {@code getChosenObjective}: receives a objectiveCardMessage
     * @return objectiveCardMessage
     */
    @Override
    public synchronized objectiveCardMessage getChosenObjective(ArrayList<ObjectiveCard> objectiveCards) {
        try {
            toClient.write(new objectiveCardMessage(objectiveCards));
            return (objectiveCardMessage) toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * method {@code getPlaceCard}: receives a placeCardMessage
     * @return placeCardMessage
     */
    @Override
    public synchronized placeCardMessage getPlaceCard(){
        try {
            return (placeCardMessage) toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * method {@code getChosenPick}: receives a pickCardMessage
     * @return pickCardMessage
     */
    @Override
    public synchronized pickCardMessage getChosenPick(){
        try {
            return (pickCardMessage) toServer.read();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * method {@code sendEndGame}: sends a declareWinnerMessage
     * @param score: HashMap<String, Integer> score
     * @param numberOfObjectives: HashMap<String, Integer> score
     */
    @Override
    public synchronized void sendEndGame(HashMap<String, Integer> score, HashMap<String, Integer> numberOfObjectives){
        try {
            toClient.write(new declareWinnerMessage(score, numberOfObjectives));
        } catch (RemoteException ignore) {}
    }

    /**
     * method {@code sendUpdatePlayer}: sends a updatePlayerMessage
     * @param updateMessage: updatePlayerMessage
     */
    @Override
    public synchronized void sendUpdatePlayer(updatePlayerMessage updateMessage) {
        try {
            toClient.write(updateMessage);
        } catch (RemoteException ignore) {}
    }

    /**
     * method {@code closeConnection}: closes the connection
     */
    @Override
    public void closeConnection() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                registry.unbind(lookupString + "_toServer");
                registry.unbind(lookupString + "_toClient");
                return null;
            }
        });
        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (ExecutionException | InterruptedException ignore) {
        } finally {
            executor.shutdownNow();
        }
    }


    /**
     * method {@code isConnected}: sends a currentStateMessage to check if the connection is still active
     * and expects an answer
     * @return boolean true if the connection is active, false or no answer otherwise
     */
    @Override
    public synchronized boolean isConnected() {
        try {
            String answer = "ACK";
            currentStateMessage message = new currentStateMessage(null, null, "AnswerCheckConnection", false, null, null, null );
            this.sendCurrentState(message);
            return answer.equals((String) toServer.read());
        } catch (Exception e) {
            return false;
        }
    }
}


