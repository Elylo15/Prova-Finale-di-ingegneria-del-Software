package it.polimi.ingsw.protocol.server;




import it.polimi.ingsw.protocol.messages.ConnectionState.choseColorMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.choseNameMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ClientConnection class
 * @author elylo
 */
public abstract class ClientConnection implements Runnable {
    private String status;
    private String IP;
    private String port;
    private CheckConnection connection;
    private final Socket socket;

    /**
     * Class constructor
     *
     * @param IP the IP address of the server.
     * @param port the port of the server.
     */
    public ClientConnection(String IP, String port, Socket socket) {
        this.IP = IP;
        this.port = port;
        this.socket = socket;
    }

    /**
     * Method return the status of the client.
     */
    public String getStatus() {
        return status;
    }

    /**
    * Method return the IP address of the server.
    */
    public String getIP() {
        return IP;
    }
    /**
     * Method return the port address of the server.
     */
    public String getPort() {
        return port;
    }


    public CheckConnection getConnection(){
        return connection;
    }

    public Socket getSocket() {
        return socket;
    }

    //manda il nome dell'host
    public abstract void sendNewHostMessage(String hostNickname);

    //ricevi il numero dei player per la partita
    public abstract expectedPlayerMessage getExpectedPlayer();
    //manda feedback al client
    public  abstract void sendAnswerToExpected (boolean correct);

    //crea un thread e fai partire il checkConnection
    public abstract void startCheckConnection();

    public abstract void sendAnswerToConnection(connectionResponseMessage message);

    //manda la lista dei nomi già usati
    public abstract void sendUnvailableName(ArrayList<String> unavailableNames);

    //ricevi il nome del client
    public abstract choseNameMessage getName();

    //manda la lista dei colori disponibili
    public abstract void sendAvailableColor(ArrayList<String> availableColors);

    //ricevi il colore scelto
    public abstract choseColorMessage getColor();

    //manda lo stato al client
    public abstract void sendCurrentState(currentStateMessage currentState);

    //riceve StarterCardMessage dal client
    public abstract starterCardMessage getStaterCard();

    //riceve ObjectiveCardMessage dal client
    public abstract objectiveCardMessage getChosenObjective();

    //riceve PlaceCardMessage dal client
    public abstract PlaceCardMessage getPlaceCard();

    //riceve pickCardMessage dal client
    public abstract pickCardMessage getChosenPick();

    //mandi al client il feedback del server (crea il messaggio che si chiama uguale ma con Response)
    public abstract void sendAnswerToChosenName(boolean correctChoice);
    public abstract void sendAnswerToChosenColor(boolean correctChoice);
    public abstract void sendAnswerToStaterCard(boolean correctChoice);
    public abstract void sendAnswerToObjectiveCard(boolean correctChoice);
    public abstract void sendAnswerToPlaceCard(boolean correctChoice);
    public abstract void sendAnswerToPickCard(boolean correctChoice);


    //crea il decleareWinnerMessage con i valori nei paramentri
    public abstract void sendEndGame(HashMap<String, Integer> scores,HashMap<String, Integer> numberOfObjectives);

    //Nuovo metodo
    public abstract void closeConnection();


}
