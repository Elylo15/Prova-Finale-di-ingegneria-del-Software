package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.server.RMI.MainRemoteServerInterface;
import it.polimi.ingsw.protocol.server.RMI.MessageExchangerInterface;

import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ControllerRMI extends Controller {
    private MessageExchangerInterface toServer;
    private MessageExchangerInterface toClient;

    /**
     * method {@code ControllerRMI}: constructs a new ControllerRMI
     * @param serverIP: String
     * @param serverPort: String
     */
    public ControllerRMI(String serverIP, String serverPort) throws RemoteException {
        super(serverIP, serverPort);
    }

    /**
     * method {@code connectToServer}: connects to an existing server
     * @param serverIP: String
     * @param serverPort: String
     */
    public void connectToServer(String serverIP, String serverPort) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, Integer.parseInt(serverPort));
            MainRemoteServerInterface server = (MainRemoteServerInterface) registry.lookup("MainServer");
            // todo check Naming.lookup()

            String lookup = server.helloFromClient();
            this.toServer = (MessageExchangerInterface) registry.lookup(lookup + "_toServer");
            this.toClient = (MessageExchangerInterface) registry.lookup(lookup + "_toClient");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code answerConnection}: receives a connectionResponseMessage
     * @return connectionResponseMessage
     */
    @Override
    public connectionResponseMessage answerConnection() {
        try {
            return (connectionResponseMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in answerConnection");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getCurrent}: receives a currentStateMessage
     * @return currentStateMessage
     */
    @Override
    public currentStateMessage getCurrent() {
        try {
            return (currentStateMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in getCurrent");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code serverOptions}: receives an empty serverOptionMessage
     * @return serverOptionMessage
     */
    @Override
    public serverOptionMessage serverOptions() {
        try {
            return (serverOptionMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in serverOptions");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code correctAnswer}: receives a responseMessage
     * @return responseMessage
     */
    @Override
    public responseMessage correctAnswer() {
        try {
            return (responseMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in correctAnswer");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code sendOptions}: sends a serverOptionMessage
     * @param options: serverOptionMessage
     */
    @Override
    public void sendOptions(serverOptionMessage options) {
        try {
            toServer.write(options);
        } catch (RemoteException e) {
            System.out.println("Error in sendOptions");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getUnavailableName}: receives a unavailableNamesMessage
     * @return unavailableNamesMessage
     */
    @Override
    public unavailableNamesMessage getUnavailableName() {
        try {
            return (unavailableNamesMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in getUnavailableName");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseName}: sends a chosenNameMessage
     * @param name: String
     */
    @Override
    public void chooseName(String name) {
        try {
            toServer.write(new chosenNameMessage(name));
        } catch (RemoteException e) {
            System.out.println("Error in chooseName");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code getAvailableColor}: receives a availableColorsMessage
     * @return availableColorsMessage
     */
    @Override
    public availableColorsMessage getAvailableColor() {
        try {
            return (availableColorsMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in getAvailableColor");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseColor}: sends a chosenColorMessage
     * @param color: String
     */
    @Override
    public void chooseColor(String color) {
        try {
            toServer.write(new chosenColorMessage(color));
        } catch (RemoteException e) {
            System.out.println("Error in chooseColor");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code newHost}: receives a newHostMessage
     * @return newHostMessage
     */
    @Override
    public newHostMessage newHost() {
        try {
            return (newHostMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in newHost");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code expectedPlayers}: sends a expectedPlayersMessage
     * @param expected: int
     * @param noResponse: boolean
     */
    @Override
    public void expectedPlayers(int expected, boolean noResponse) {
        try {
            toServer.write(new expectedPlayersMessage(expected, noResponse));
        } catch (RemoteException e) {
            System.out.println("Error in expectedPlayers");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code placeStarter}: sends a starterCardMessage
     * @param side: int
     * @param noResponse: boolean
     */
    @Override
    public void placeStarter(int side, boolean noResponse) {
        try {
            toServer.write(new starterCardMessage(side, noResponse));
        } catch (RemoteException e) {
            System.out.println("Error in placeStarter");
            throw new RuntimeException(e);
        }
    }

    /**
     * @return
     */
    @Override
    public objectiveCardMessage getObjectiveCards() {
        try {
            return (objectiveCardMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in getObjectiveCards");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code chooseObjective}: sends a objectiveCardMessage
     * @param pick: int
     * @param noResponse: boolean
     */
    @Override
    public void chooseObjective(int pick, boolean noResponse) {
        try {
            toServer.write(new objectiveCardMessage(pick, noResponse));
        } catch (RemoteException e) {
            System.out.println("Error in chooseObjective");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code placeCard}: sends a PlaceCardMessage
     * @param card: int
     * @param side: int
     * @param x: int
     * @param y: int
     * @param noResponse: boolean
     */
    @Override
    public void placeCard(int card, int side, int x, int y, boolean noResponse) {
        try {
            toServer.write(new placeCardMessage(card, side, x, y, noResponse));
        } catch (RemoteException e) {
            System.out.println("Error in placeCard");
            throw new RuntimeException(e);
        }
    }

    /**
     *  method {@code pickCard}: sends a pickCardMessage
     * @param card: int
     * @param noResponse: boolean
     */
    @Override
    public void pickCard(int card, boolean noResponse) {
        try {
            toServer.write(new pickCardMessage(card, noResponse));
        } catch (RemoteException e) {
            System.out.println("Error in pickCard");
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code updatePlayer}: receives a updatePlayerMessage
     * @return updatePlayerMessage
     */
    public updatePlayerMessage updatePlayer(){
        try {
            return (updatePlayerMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in updatePlayer" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * method {@code endGame}: receives a declareWinnerMessage
     * @return declareWinnerMessage
     */
    @Override
    public declareWinnerMessage endGame() {
        try {
            return (declareWinnerMessage) toClient.read();
        } catch (RemoteException e) {
            System.out.println("Error in endGame");
            throw new RuntimeException(e);
        }
    }
}
