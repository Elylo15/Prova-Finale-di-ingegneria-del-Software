package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import it.polimi.ingsw.protocol.server.FSM.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class ClientManager implements Runnable{
    private Match match;
    private HashMap<Player, ClientConnection> players; // Only online players, total players are stored in Match
    private final String matchFolderPath;
    private String portSocket;
    private String portRMI;

    private ThreadPoolExecutor executor;

    private GameStatusFSM FSM;

    private boolean lastTurn;


    /**
     * Standard constructor for ClientManager
     * @param match object representing the model
     * @param matchFolderPath path where the game is saved
     */
    public ClientManager(Match match,  String matchFolderPath) {
        this.match = match;
        this.matchFolderPath = matchFolderPath;
        this.lastTurn = false;

        int corePoolSize = 15;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

        FSM = new GameStatusFSM();
    }

    public void setStatus(Player player) {

    }

    /**
     * Sets the new ports for the protocols.
     * @param portSocket port for Socket
     * @param portRMI port for RMI
     */
    public void setPorts(String portSocket, String portRMI) {
        if(portSocket != portRMI) {
            this.portSocket = portSocket;
            this.portRMI = portRMI;
        }
    }

    public void acceptConnectionSocket() {

    }

    public void acceptConnectionRMI() {

    }

    public void kickPlayer(String name) {

    }

    // Maybe should be eliminated
    public void closeGame() {

    }

    // Maybe should be eliminated
    public void startLoadedGame() {

    }

    public void saveMatch() {

    }


    /**
     * Runs the host session for the game
     */
    @Override
    public void run() {
        this.FSM.transition(Event.Created);

        while(this.FSM.getState() != State.Cleaning) {
            switch(FSM.getState()) {

                case WaitingForNewPlayers -> {
                    executor.submit(this::acceptConnectionSocket);
                    executor.submit(this::acceptConnectionRMI);
                    while(this.players.isEmpty()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {

                        }
                    }

                    Player startingHost = this.players.keySet().stream().findFirst().get();
                    int num = players.get(startingHost).getExpectedPlayers();
                    if(num >= 2)
                        this.expectedPlayers = num;

                    // wait for players to join (there should be minimum 2 players) then asks the host if
                    // the game should start anyway
                    int waitCounter = 0;
                    int waitTime = 240;
                    boolean startSignal = false;
                    while(this.players.size() < expectedPlayers && !startSignal) {
                        try {
                            Thread.sleep(1000);
                            waitCounter++;

                            if (waitCounter >= waitTime) {
                                waitCounter = 0;
                                waitTime = 120;
                                if(this.players.size() >= 2) {
                                    startingHost = this.players.keySet().stream().findFirst().get();
                                    startSignal = this.players.get(startingHost).getStart();
                                }
                            }
                        } catch (InterruptedException ignored) {}

                    }
                    this.FSM.transition(Event.AllPlayerReady);
                    this.saveMatch();
                }

                case StarterCard -> {
                    ArrayList<StarterCardMessage> messages = new ArrayList<>();
                    ArrayList<PlaceableCard> cards = new ArrayList<>();
                    ArrayList<Future<StarterCardMessage>> futures = new ArrayList<>();
                    CommonArea commonArea = match.getCommonArea();

                    // Set up of common area
                    try {
                        match.start();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    players.keySet().forEach(player -> {
                        player.drawStarter();
                        players.get(player).sendStateStarterCard(new StarterCardMessage(player, true));
                    });

                    players.values().forEach(connection -> {
                        Future<StarterCardMessage> fut = executor.submit(connection::getStarterCard);
                       futures.add(fut);
                    });

                    for(Future<StarterCardMessage> future : futures) {
                        try {
                            messages.add(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                        futures.remove(future);
                    }

                    players.keySet().forEach(player -> {
                        messages.stream()
                                .filter(msg -> msg.getPlayer().getNickname().equals(player.getNickname()))
                                .findFirst().ifPresent(message -> player.placeStarter(message.isFront()));

                    });

                    players.keySet().forEach(Player::initialHand);

                    this.FSM.transition(Event.AllStarterCardPlaced);
                }

                case Objective -> {
                    // HashMap<Player, ObjectiveCardMessage> objectives = new HashMap<>();
                    ArrayList<Future<ObjectiveCardMessage>> futures = new ArrayList<>();
                    CommonArea commonArea = match.getCommonArea();

                    // Set up common objectives
                    match.drawCommonObjective();

//                    players.keySet().forEach(player -> {
//                       objectives.put(player, new ObjectiveCardMessage(commonArea.drawObjectiveCard(), commonArea.drawObjectiveCard(), player));
//                       players.get(player).sendStateChooseObjective(objectives.get(player));
//                    });
//
//                    players.values().forEach(connection -> futures.add(executor.submit(connection::getChosenObjective)));
//
//                    for(Future<ObjectiveCardMessage> future : futures) {
//                        try {
//                            future.get().getPlayer().setObjective(future.get().getChosenObjectiveCard());
//                        } catch (InterruptedException | ExecutionException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }

                    for(Player player : players.keySet()) {
                        ObjectiveCard[] objectives = player.drawObjectives();
                        ObjectiveCardMessage message = new ObjectiveCardMessage(objectives[0], objectives[1], player);
                        //TODO message
                    }



                    this.FSM.transition(Event.AllObjectivePicked);
                    // TODO implement save match
                    this.saveMatch();
                }
                // TODO implement main game cycle, saving and loading
                case Player1Turn -> {
                    if(players.keySet().stream().anyMatch(player -> player.getScore() >= 20)) {
                        this.lastTurn = true;
                    }

                    if(players.containsKey(match.getPlayers().getFirst()))
                    {
                        Player currentPlayer = match.getPlayers().getFirst();
                        TurnMessage message = new TurnMessage(currentPlayer, match.getCommonArea(), this.lastTurn);

                        // Start turn data
                        players.values().forEach(connection -> connection.sendStateTurn(message));

                        // Current player has to choose a card
                        Future<PlaceableCardMessage> future;
                        boolean correctChoise = false;
                        while(!correctChoise) {
                            ClientConnection connection = players.get(currentPlayer);
                            future = executor.submit(connection::getPlaceCard);
                            PlaceableCardMessage response;
                            try {
                                 response = future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }

                            if(response.getPlayer().equals(currentPlayer)) {
                                try {
                                    PlaceableCard pick = currentPlayer.pickPlaceableCard(response.getPositionInHand());

                                    currentPlayer.getPlayerArea().placeCard(pick, response.getX(), response.getY(), response.isFront());
                                    correctChoise = true;+
                                } catch (noPlaceCardException e) {
                                    correctChoise = false;
                                }

                                placeCardResponseMessage responseMessage = new placeCardResponseMessage(correctChoise);
                                players.get(currentPlayer).sendAnswerToPlaceCard(responseMessage);

                            }
                        }

                        // Message to update the view
                        message = new TurnMessage(currentPlayer, match.getCommonArea(), this.lastTurn);
                        players.values().forEach(connection -> connection.sendStateTurn(message));

                        // Current player hat to pick a card






                        this.FSM.transition(Event.Player1TurnEnded);
                    } else {
                        this.FSM.transition(Event.SkipPlayer1);
                    }

                    this.saveMatch();
                }

                case Player2Turn -> {

                    this.saveMatch();
                }

                case Player3Turn -> {

                    this.saveMatch();
                }

                case Player4Turn -> {

                    this.saveMatch();
                }

                case EndGame -> {

                    this.saveMatch();
                }

            }
        }

        // TODO get back the executors

    }



}
