package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.protocol.messages.ObjectiveCardMessage;
import it.polimi.ingsw.protocol.messages.StarterCardMessage;
import it.polimi.ingsw.protocol.server.FSM.Event;
import it.polimi.ingsw.protocol.server.FSM.GameStatusFSM;
import it.polimi.ingsw.protocol.server.FSM.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class ClientManager implements Runnable{
    private Match match;
    private HashMap<Player, ClientConnection> players; // Only online players, total players are stored in Match
    private int expectedPlayers;
    private final String matchFolderPath;
    private String portSocket;
    private String portRMI;

    private ThreadPoolExecutor executor;

    private GameStatusFSM FSM;



    public ClientManager(Match match, int expectedPlayers, String matchFolderPath) {
        this.match = match;
        this.expectedPlayers = expectedPlayers;
        this.matchFolderPath = matchFolderPath;

        int corePoolSize = 15;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

        FSM = new GameStatusFSM();
    }

    public void setStatus(Player player) {

    }

    public void setPorts(String portSocket, String portRMI) {

    }

    public void acceptConnectionSocket() {

    }

    public void acceptConnectionRMI() {

    }

    public void kickPlayer(String name) {

    }

    public void startGame() {

    }

    public void closeGame() {

    }

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
                }

                case StarterCard -> {
                    ArrayList<StarterCardMessage> messages = new ArrayList<>();
                    ArrayList<PlaceableCard> cards = new ArrayList<>();
                    ArrayList<Future<StarterCardMessage>> futures = new ArrayList<>();
                    CommonArea commonArea = match.getCommonArea();

                    // Set up of common area
                    commonArea.getD1().shuffle();
                    commonArea.getD2().shuffle();
                    commonArea.getD3().shuffle();
                    commonArea.getD4().shuffle();
                    match.drawCommonObjective();

                    players.values().forEach(connection -> {
                        cards.add(commonArea.drawFromToPlayer(3));
                        connection.sendStateStarterCard(new StarterCardMessage(commonArea, cards.getLast()));
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
                        StarterCardMessage message = messages.stream()
                                .filter(msg -> msg.getPlayerName().equals(player.getNickname()))
                                .findFirst()
                                .orElse(null);

                        if(message != null) {
                            player.getPlayerArea().placeStarterCard(message.getStarterCard(), message.isFront());
                        }
                    });

                    players.keySet().forEach(Player::initialPlayerHand);

                    this.FSM.transition(Event.AllStarterCardPlaced);
                }

                case Objective -> {
                    HashMap<Player, ObjectiveCardMessage> objectives = new HashMap<>();
                    ArrayList<Future<ObjectiveCardMessage>> futures = new ArrayList<>();
                    CommonArea commonArea = match.getCommonArea();

                    players.keySet().forEach(player -> {
                       objectives.put(player, new ObjectiveCardMessage(commonArea.drawObjectiveCard(), commonArea.drawObjectiveCard(), player));
                       players.get(player).sendStateChooseObjective(objectives.get(player));
                    });

                    players.values().forEach(connection -> futures.add(executor.submit(connection::getChosenObjective)));

                    for(Future<ObjectiveCardMessage> future : futures) {
                        try {
                            future.get().getPlayer().setObjective(future.get().getChosenObjectiveCard());
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    this.FSM.transition(Event.AllObjectivePicked);
                    this.saveMatch();
                }
                // TODO implement main game cycle, saving and loading
                case Player1Turn -> {

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
