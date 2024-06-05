package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import it.polimi.ingsw.protocol.messages.ObjectiveState.objectiveCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.pickCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.placeCardMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.StaterCardState.starterCardMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.server.FSM.MatchState;
import it.polimi.ingsw.protocol.server.FSM.State;
import it.polimi.ingsw.protocol.server.exceptions.FailedToJoinMatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This class manages the clients connected and the game logic.
 * It is responsible for handling the connection of clients, the game flow, and the disconnection of clients.
 */
public class MatchManager implements Runnable {
    private final MatchInfo matchInfo;
    private final int timeout;
    private final ThreadPoolExecutor executor;
    private final LogCreator logCreator;
    private int turnNumber;

    /**
     * Standard constructor for ClientManager
     *
     * @param match object representing the model and data related to the server
     */
    public MatchManager(MatchInfo match) {
        this.matchInfo = match;
        this.matchInfo.setLastTurn(false);
        this.turnNumber = 0;

        this.timeout = 120 * 1000;

        logCreator = new LogCreator(this.matchInfo.getID().toString());


        int corePoolSize = 15;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());

        logCreator.log("Client manager started");

    }


    /**
     * Adds a player to the list of players in the match
     *
     * @param playerInfo player to be added
     * @throws FailedToJoinMatch if the player cannot join the match
     */
    protected synchronized void addPlayerInfo(PlayerInfo playerInfo) throws FailedToJoinMatch {
        if (playerInfo != null
                && matchInfo.getStatus() == MatchState.Waiting
                && (matchInfo.getExpectedPlayers() == null || matchInfo.getExpectedPlayers() > matchInfo.getAllPlayersInfo().size())
                && matchInfo.getAllPlayersInfo().stream().noneMatch(playerInfo1 -> playerInfo1.getPlayer().getNickname().equalsIgnoreCase(playerInfo.getPlayer().getNickname()))
                && matchInfo.getAllPlayersInfo().stream().noneMatch(playerInfo1 -> playerInfo1.getPlayer().getColor().equalsIgnoreCase(playerInfo.getPlayer().getColor()))) {
            this.matchInfo.addPlayer(playerInfo);
            logCreator.log("Player added: " + playerInfo.getPlayer().getNickname() + " " + playerInfo.getPlayer().getColor());
            this.notifyAll();
        } else {
            throw new FailedToJoinMatch("Player cannot join the match");
        }
    }

    /**
     * Kicks a player from the match, closing its connection.
     *
     * @param playerInfo player to be kicked
     */
    private void kickPlayer(PlayerInfo playerInfo) {
        this.matchInfo.setOffline(playerInfo);
        logCreator.log("Player kicked: " + playerInfo.getPlayer().getNickname() + " and added to offline players");
    }

    /**
     * Adds a player to the list of online players and removes it from the list of offline players
     *
     * @param nickname nickname of the player to be moved
     */
    protected synchronized void wakeUpPlayer(String nickname, ClientConnection connection) throws FailedToJoinMatch {

        // Check if the state of the match allows the player to join
        if (this.matchInfo.getStatus() == MatchState.Waiting || this.matchInfo.getStatus() == MatchState.KickingPlayers || this.matchInfo.getStatus() == MatchState.Endgame) {
            throw new FailedToJoinMatch("Player cannot join the match, match is not in the right state");
        }

        // Check if the player is in the list of players and it is offline
        PlayerInfo playerInfo = matchInfo.getAllPlayersInfo().stream()
                .filter(playerInfo1 -> playerInfo1.getPlayer().getNickname().equalsIgnoreCase(nickname))
                .filter(playerInfo1 -> playerInfo1.getConnection() == null)
                .findFirst().orElse(null);


        // Eventually sets up a new connection for the player
        if (playerInfo != null && playerInfo.getConnection() == null) {
            matchInfo.bringOnline(nickname, connection);
            this.notifyAll();
            logCreator.log("Player moved: " + playerInfo.getPlayer().getNickname() + " from offline to online players");
        } else {
            logCreator.log("Player " + nickname + " cannot be brought back online");
            throw new FailedToJoinMatch("Player cannot join the match");
        }
    }

    /**
     * Retrieves a list of all online players in the match.
     * A player is considered online if their connection is not null.
     *
     * @return An ArrayList of PlayerInfo objects representing the online players.
     */
    protected ArrayList<PlayerInfo> getOnlinePlayerInfo() {
        return this.matchInfo.getAllPlayersInfo().stream()
                .filter(playerInfo -> playerInfo.getConnection() != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected ArrayList<PlayerInfo> getOfflinePlayerInfo() {
        return this.matchInfo.getAllPlayersInfo().stream()
                .filter(playerInfo -> playerInfo.getConnection() == null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Getter of MathInfo
     *
     * @return MatchInfo
     */
    protected MatchInfo getMatchInfo() {
        return this.matchInfo;
    }

    /**
     * Getter of Match (model)
     *
     * @return Match
     */
    protected Match getMatch() {
        return this.matchInfo.getMatch();
    }


    private synchronized void saveMatch() {
        MatchInfo copy = matchInfo.cloneForSerialization();
        File dir = new File("savedMatches");
        if (!dir.exists())
            dir.mkdir();

        String filename = "savedMatches/match_" + copy.getID() + ".match";
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(copy);
            logCreator.log("Match " + copy.getID() + " saved");
        } catch (Exception e) {
            e.printStackTrace();
            logCreator.log("Failed to save match " + e.getMessage());
        }
    }


    /**
     * Hosts a session for the current game
     */
    @Override
    public void run() {
        boolean gameOver = false;
        while (!gameOver) {


            // After the first turns prioritizes the players who reconnect and are in the starter state
            // or objective state
            synchronized (this) {
                // Draws common objective cards
                if (this.turnNumber == 2 && this.matchInfo.getMatch().getCommonObjective()[0] == null){
                    this.matchInfo.getMatch().drawCommonObjective();
                    this.saveMatch();
                }

                // Checks if there are online player in the starter state after the initial turns
                if (this.turnNumber > 1) {
                    this.getOnlinePlayerInfo().stream()
                            .filter(playerInfo -> playerInfo.getState() == State.StarterCard)
                            .forEach(playerInfo -> this.player(playerInfo.getPlayer()));
                }


                // Checks if there are online player in the objective state after the initial turns
                if (this.turnNumber > 2) {
                    this.getOnlinePlayerInfo().stream()
                            .filter(playerInfo -> playerInfo.getState() == State.Objective)
                            .forEach(playerInfo -> this.player(playerInfo.getPlayer()));
                }


                // Eventually update all players to last turn
                if (this.matchInfo.isLastTurn()) {
                    this.getOnlinePlayerInfo().stream()
                            .filter(playerInfo -> playerInfo.getState() == State.PlaceCard)
                            .forEach(playerInfo -> playerInfo.setState(State.LastTurn));
                }

            }

            this.checkPlayersConnections();

            // Starts the normal flow of the game
            switch (this.matchInfo.getStatus()) {
                case Waiting -> this.waiting();

                case Player1 -> {
                    Player currentPlayer;
                    if (matchInfo.getMatch().getPlayers().size() < 1)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(0);

                    this.player(currentPlayer);
                }
                case Player2 -> {
                    Player currentPlayer;
                    if (matchInfo.getMatch().getPlayers().size() < 2)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(1);

                    this.player(currentPlayer);
                }
                case Player3 -> {
                    Player currentPlayer;
                    if (matchInfo.getMatch().getPlayers().size() < 3)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(2);

                    this.player(currentPlayer);
                }
                case Player4 -> {
                    Player currentPlayer;
                    if (matchInfo.getMatch().getPlayers().size() < 4)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(3);

                    this.player(currentPlayer);

                    this.turnNumber += 1;


                    if (currentPlayer != null && findPlayer(currentPlayer).getState() == State.PickCard) {
                        this.matchInfo.setStatus(MatchState.Player4);
                    }

                    if (this.matchInfo.isLastTurn())
                        this.matchInfo.setStatus(MatchState.Endgame);

                    if (this.matchInfo.getMatch().getPlayers().stream()
                            .anyMatch(player -> player.getScore() >= 20) ||
                            (this.matchInfo.getMatch().getCommonArea().getTableCards().isEmpty()
                                    && this.getMatch().getCommonArea().getD1().getList().isEmpty()
                                    && this.getMatch().getCommonArea().getD2().getList().isEmpty())) {
                        logCreator.log("This is the last turn");
                        matchInfo.setLastTurn(true);
                    }

                }
                case Endgame -> this.endgame();

                case KickingPlayers -> {
                    this.kickingPlayers();
                    gameOver = true;
                }
            }
            this.checkPlayersConnections();
            this.checkOnlinePlayersNumber();

            // Leaves a pause between each turn in order to allow new clients to join
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logCreator.log("Thread interrupted, while sleeping: " + e.getMessage());
            }
        }


        logCreator.close();
    }


    /**
     * Manages the waiting state for players to join the match.
     * It sends the current state data to all connected players, asks for the number of expected players for this match
     * and waits for the expected number of players to join.
     * When the number of expected players is met, it adds them to the match (model).
     * If a player takes too long to respond or provides an invalid response, they are kicked from the match.
     */
    private void waiting() {
        logCreator.log("Waiting for players");

        // Obtains the number of expected players for this match
        while (this.matchInfo.getExpectedPlayers() == null) {

            synchronized (this) {
                // Waiting for the first player that will be indicated as the "host"
                while (this.getOnlinePlayerInfo().isEmpty()) {
                    try {
                        this.wait();
                    } catch (InterruptedException ignore) {
                        logCreator.log("Waiting for first player, InterruptedException received and ignored.");
                    }
                }
            }

            // preventing new player from joining at this moment and not getting all messages correctly
            synchronized (this) {


                PlayerInfo host = this.getOnlinePlayerInfo().getFirst();

                if (host != null) {
                    // Sends current state data
                    this.getOnlinePlayerInfo().forEach(playerInfo -> {
                        currentStateMessage curr = new currentStateMessage(null, playerInfo.getPlayer(), "WaitingForPlayerState", false, this.onlinePlayersNicknames(), null, this.matchInfo.getID());
                        playerInfo.getConnection().sendCurrentState(curr);
                        playerInfo.getConnection().sendNewHostMessage(host.getPlayer().getNickname());
                    });

                    boolean correctAnswer = false;
                    while (!correctAnswer) {
                        // If the timer ends the player is kicked
                        Future<expectedPlayersMessage> future = executor.submit(() -> host.getConnection().getExpectedPlayer());
                        expectedPlayersMessage expected = null;
                        try {
                            expected = future.get(this.timeout, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            logCreator.log("Player " + host.getPlayer().getNickname() + " has not answered");
                            this.kickPlayer(host);
                        }

                        if (expected != null) {
                            // Checks if the client has properly given a response
                            if (expected.isNoResponse()) {
                                correctAnswer = true;
                                this.kickPlayer(host);
                            } else {
                                // Checks if the response is valid and answer back
                                if (expected.getExpectedPlayers() >= 2 && expected.getExpectedPlayers() <= 4) {
                                    this.matchInfo.setExpectedPlayers(expected.getExpectedPlayers());
                                    correctAnswer = true;
                                    host.getConnection().sendAnswer(true);
                                    logCreator.log("Player " + host.getPlayer().getNickname() + " has correctly answered");
                                } else {
                                    host.getConnection().sendAnswer(false);
                                    logCreator.log("Player " + host.getPlayer().getNickname() + " has not correctly answered");
                                }
                            }

                        } else {
                            logCreator.log("Player " + host.getPlayer().getNickname() + " has not answered");
                            this.kickPlayer(host);
                            correctAnswer = true;
                        }
                    }


                }

            }

        }

        logCreator.log("Expected players obtained : " + this.matchInfo.getExpectedPlayers() + " from " + this.getOnlinePlayerInfo().getFirst().getPlayer().getNickname());

        synchronized (this) {

            // Wait for the specified number of expected players
            while (this.getOnlinePlayerInfo().size() < this.matchInfo.getExpectedPlayers()) {
                try {
                    this.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }

        while (this.getOnlinePlayerInfo().size() > this.matchInfo.getExpectedPlayers()) {
            logCreator.log("Too many players have joined the match");
            this.kickPlayer(this.getOnlinePlayerInfo().getLast());
        }

        // Updates state of the match
        this.matchInfo.setStatus(MatchState.Player1);
        logCreator.log("Match state updated from \"Waiting\" to \"Player 1\"");


        // Adds all players to the match (match of the model)
        for (PlayerInfo playerInfo : this.getOnlinePlayerInfo()) {
            try {
                this.matchInfo.getMatch().addPlayer(playerInfo.getPlayer());
                playerInfo.setState(State.StarterCard);
                logCreator.log("Player " + playerInfo.getPlayer().getNickname() + " added to the model");
            } catch (Exception e) {
                logCreator.log("Failed to add player to model: " + playerInfo.getPlayer().getNickname());
            }
        }

        // Prepares the match.
        this.matchInfo.getMatch().start();
        this.turnNumber = 1;


        // First save of the game
        this.saveMatch();
    }

    /**
     * Manages the game logic of the specified player if it is online.
     *
     * @param player player to manage.
     */
    private synchronized void player(Player player) {
        if (player == null) {
            logCreator.log("Player is null");
            updateMatchStatus();
            return;
        }
        PlayerInfo playerInfo = this.findPlayer(player);
        if (playerInfo == null) {
            logCreator.log("Player " + player.getNickname() + " not found and skipped");
            updateMatchStatus();
        }


        switch (playerInfo.getState()) {
            case StarterCard -> {
                logCreator.log("Player " + player.getNickname() + " has to place the starter card");

                // Compute starter state itself
                this.starterState(playerInfo);

                // Updates the view of every player about the current one
                for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                    updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }


                // End turn and update states
                if (this.getOnlinePlayerInfo().contains(playerInfo)) { // Checks if the player is online
                    logCreator.log("Player " + player.getNickname() + " placed the starter card");
                    playerInfo.setState(State.Objective);
                    playerInfo.getPlayer().initialHand();
                }

                this.updateMatchStatus();

                // Saves the progress of the game
                this.saveMatch();
            }
            case Objective -> {
                logCreator.log("Player " + player.getNickname() + " has to choose an objective");

                // Compute objective state itself
                this.objectiveState(playerInfo);


                // Updates the view of every player about the current one
                for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                    updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                // End turn and update states
                if (this.getOnlinePlayerInfo().contains(playerInfo)) {
                    logCreator.log("Player " + player.getNickname() + " has correctly chosen an objective");
                    if (this.matchInfo.isLastTurn())
                        playerInfo.setState(State.EndGame);
                    else
                        playerInfo.setState(State.PlaceCard);
                }

                this.updateMatchStatus();

                // Saves the progress of the game
                this.saveMatch();
            }
            case PlaceCard -> {
                logCreator.log("Player " + player.getNickname() + " starts normal turn and has to place a card");

                // Check if the current player can place a card
                boolean canPlace = true;
                if (playerInfo.getPlayer().getPlayerArea().getAvailablePosition().isEmpty()) {
                    logCreator.log("Player " + player.getNickname() + " cannot place a card. Ending his turn.");
                    this.updateMatchStatus();
                    canPlace = false;
                } else {
                    // Compute place card state itself
                    this.placeCardState(playerInfo);
                }


                // Updates all clients on the current situation
                for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                    updatePlayerMessage update = new updatePlayerMessage(playerInfo.getPlayer(), playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                // Update the states
                if (this.getOnlinePlayerInfo().contains(playerInfo) && canPlace) {
                    playerInfo.setState(State.PickCard);
                }

                /*
                Match status is not updated, because this player has yet to pick a card to finish his turn.
                 */

                this.saveMatch();
            }
            case PickCard -> {

                if (this.matchInfo.getMatch().getCommonArea().getTableCards().isEmpty()) {
                    logCreator.log("Common area is empty, player " + player.getNickname() + " cannot pick a card. Ending his turn.");

                } else {
                    logCreator.log("Player " + player.getNickname() + " has to pick a card from common area");


                    // Compute pick card state itself
                    this.pickCardState(playerInfo);


                    // Updates all clients on the current situation
                    for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                        updatePlayerMessage update = new updatePlayerMessage(playerInfo.getPlayer(), playerInfo1.getPlayer().getNickname());
                        playerInfo1.getConnection().sendUpdatePlayer(update);
                    }

                    logCreator.log("Player " + player.getNickname() + " has ended his normal turn");
                }

                // End turn
                // Update all states
                if (this.getOnlinePlayerInfo().contains(playerInfo)) {
                    if (this.matchInfo.isLastTurn())
                        playerInfo.setState(State.EndGame);
                    else
                        playerInfo.setState(State.PlaceCard);
                }

                this.updateMatchStatus();


                // Saves the progress of the game
                this.saveMatch();


            }
            case LastTurn -> {
                logCreator.log("Player " + player.getNickname() + " plays his last turn");

                if (playerInfo.getPlayer().getPlayerArea().getAvailablePosition().isEmpty()) {
                    logCreator.log("Player " + player.getNickname() + " cannot place a card. Ending his turn.");
                    this.updateMatchStatus();
                } else {
                    // Compute place card state itself
                    this.placeCardState(playerInfo);
                }


                // Updates all clients on the current situation
                for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                    updatePlayerMessage update = new updatePlayerMessage(playerInfo.getPlayer(), playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                logCreator.log("Player " + player.getNickname() + " has ended his last turn");

                // Update the state
                if (this.getOnlinePlayerInfo().contains(playerInfo))
                    playerInfo.setState(State.EndGame);

                switch (this.matchInfo.getStatus()) {
                    case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
                    case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
                    case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
                    case Player4 -> this.matchInfo.setStatus(MatchState.Endgame);
                }

                this.saveMatch();
            }
        }


    }

    /**
     * Updates the status of the match.
     */
    private void updateMatchStatus() {
        switch (this.matchInfo.getStatus()) {
            case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
            case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
            case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
            case Player4 -> this.matchInfo.setStatus(MatchState.Player1);
        }
    }

    /**
     * Manages the starter state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void starterState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Draw a StarterCard if player has none in hand
        if (playerInfo.getPlayer().getPlayerHand().getPlaceableCards().stream().
                noneMatch(PlaceableCard::isStarter))
            playerInfo.getPlayer().drawStarter();

        // Sends current state messages to all clients
        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "StarterCardState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), null, this.matchInfo.getID());
            playerInfo1.getConnection().sendCurrentState(currState);
        }

        // Obtains side of the starter card
        boolean correctAnswer = false;
        while (!correctAnswer) {


            Future<starterCardMessage> future = executor.submit(() -> playerInfo.getConnection().getStaterCard());
            starterCardMessage starter = null;


            try {
                starter = future.get(this.timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logCreator.log("Player " + player.getNickname() + " has not answered");
                this.kickPlayer(playerInfo);
                return;
            }

            if (starter != null) {

                // Checks if the client has properly given a response
                if (starter.isNoResponse()) {
                    correctAnswer = true;
                    logCreator.log("Player " + player.getNickname() + " has not answered");
                    this.kickPlayer(playerInfo);
                } else {
                    // Checks if the answer is valid
                    if (starter.getSide() == 0 || starter.getSide() == 1) {

                        playerInfo.getPlayer().placeStarter(starter.getSide());
                        correctAnswer = true;
                        playerInfo.getConnection().sendAnswer(true);
                        logCreator.log("Player " + player.getNickname() + " has correctly answered");
                    } else {
                        playerInfo.getConnection().sendAnswer(false);
                        logCreator.log("Player " + player.getNickname() + " has not answered correctly");
                    }
                }
            } else {
                if (this.getOnlinePlayerInfo().contains(playerInfo)) {
                    logCreator.log("Player " + player.getNickname() + " failed to answer");
                    this.kickPlayer(playerInfo);
                    correctAnswer = true;
                }
                return;
            }
        }
    }

    /**
     * Manages the objective state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void objectiveState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Draw two objective cards if there are none
        if (playerInfo.getSavedObjectives() == null)
            playerInfo.setSavedObjectives(playerInfo.getPlayer().drawObjectives());

        // Sends current state messages to all clients
        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "ObjectiveState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), this.matchInfo.getMatch().getCommonObjective(), this.matchInfo.getID());
            playerInfo1.getConnection().sendCurrentState(currState);
        }

        boolean correctAnswer = false;
        while (!correctAnswer) {
            ArrayList<ObjectiveCard> objectives = new ArrayList<>(Arrays.asList(playerInfo.getSavedObjectives()));
            Future<objectiveCardMessage> future = executor.submit(() -> playerInfo.getConnection().getChosenObjective(objectives));
            objectiveCardMessage objective = null;

            try {

                objective = future.get(this.timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logCreator.log("Player " + player.getNickname() + " has not answered");
                this.kickPlayer(playerInfo);
                return;
            }

            if (objective != null) {
                // Checks if the client has properly given a response
                if (objective.isNoResponse()) {
                    correctAnswer = true;
                    this.kickPlayer(playerInfo);
                } else {
                    // Checks if the answer is valid
                    if (objective.getChoice() == 1 || objective.getChoice() == 2) {
                        playerInfo.getPlayer().pickObjectiveCard(objective.getChoice(), playerInfo.getSavedObjectives());
                        correctAnswer = true;
                        playerInfo.getConnection().sendAnswer(true);
                        logCreator.log("Player " + player.getNickname() + " has correctly answered");
                    } else {
                        playerInfo.getConnection().sendAnswer(false);
                        logCreator.log("Player " + player.getNickname() + " has not correctly answered");
                    }
                }
            } else {
                logCreator.log("Player " + player.getNickname() + " failed to answer");
                this.kickPlayer(playerInfo);
                correctAnswer = true;
            }
        }
    }

    /**
     * Manages the place card state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void placeCardState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Sends current state messages to all clients
        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "PlaceTurnState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), this.matchInfo.getMatch().getCommonObjective(), this.matchInfo.getID());
            playerInfo1.getConnection().sendCurrentState(currState);
        }


        // Asks client to place a card
        boolean correctAnswer = false;
        while (!correctAnswer) {
            Future<placeCardMessage> future = executor.submit(() -> playerInfo.getConnection().getPlaceCard());
            placeCardMessage placeCard = null;

            try {
                placeCard = future.get(this.timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logCreator.log("Player " + player.getNickname() + " has not answered");
                this.kickPlayer(playerInfo);
                return;
            }

            if (placeCard != null) {
                // Checks if the client has properly given a response
                if (placeCard.isNoResponse()) {
                    correctAnswer = true;
                    this.kickPlayer(playerInfo);
                } else {
                    // Checks if the answer is valid
                    int card = placeCard.getCard();
                    int x = placeCard.getRow();
                    int y = placeCard.getColumn();
                    int side = placeCard.getFront();

                    try {
                        playerInfo.getPlayer().playTurn(card, x, y, side);
                        playerInfo.getConnection().sendAnswer(true);
                        logCreator.log("Player " + player.getNickname() + " has correctly answered");
                        correctAnswer = true;
                    } catch (noPlaceCardException e) {
                        playerInfo.getConnection().sendAnswer(false);
                        logCreator.log("Player " + player.getNickname() + " has not correctly answered");
                    }
                }
            } else {
                logCreator.log("Player " + player.getNickname() + " failed to answer");
                this.kickPlayer(playerInfo);
                correctAnswer = true;
                return;
            }
        }
    }

    /**
     * Manages the pick card state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void pickCardState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "PickTurnState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), this.matchInfo.getMatch().getCommonObjective(), this.matchInfo.getID());
            playerInfo1.getConnection().sendCurrentState(currState);
        }


        // If the client is still online, it proceeds to ask to pick a card
        boolean correctAnswer;
        if (this.getOnlinePlayerInfo().contains(playerInfo)) {
            correctAnswer = false;
            while (!correctAnswer) {
                Future<pickCardMessage> future = executor.submit(() -> playerInfo.getConnection().getChosenPick());
                pickCardMessage pickCard = null;

                try {
                    pickCard = future.get(this.timeout, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    logCreator.log("Player " + player.getNickname() + "has not answered");
                    this.kickPlayer(playerInfo);
                    return;
                }

                if (pickCard != null) {
                    // Checks if the client has properly given a response
                    if (pickCard.isNoResponse()) {
                        correctAnswer = true;
                        this.kickPlayer(playerInfo);
                    } else {
                        // Checks if the answer is valid
                        try {
                            playerInfo.getPlayer().pickNewCard(pickCard.getCard());
                            correctAnswer = true;
                            playerInfo.getConnection().sendAnswer(true);
                            logCreator.log("Player " + player.getNickname() + " has correctly answered");
                        } catch (InvalidIdException e) {
                            playerInfo.getConnection().sendAnswer(false);
                            logCreator.log("Player " + player.getNickname() + " has not correctly answered");
                        }

                    }
                } else {
                    logCreator.log("Player " + player.getNickname() + " failed to answer");
                    this.kickPlayer(playerInfo);
                    correctAnswer = true;
                    return;
                }
            }
        }
    }


    /**
     * Manages the endgame state of the match.
     */
    private void endgame() {
        logCreator.log("ENDGAME");
        // Sends current state messages to all clients
        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            currentStateMessage currState = new currentStateMessage(null, playerInfo1.getPlayer(), "EndGameState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), this.matchInfo.getMatch().getCommonObjective(), this.matchInfo.getID());
            playerInfo1.getConnection().sendCurrentState(currState);
        }

        HashMap<String, Integer> scores = new HashMap<>();
        HashMap<String, Integer> numberOfObjects = new HashMap<>();

        ArrayList<Player> players = this.getOnlinePlayerInfo().stream()
                .map(PlayerInfo::getPlayer)
                .collect(Collectors.toCollection(ArrayList::new));

        // Compute all points and number of objective accomplished
        for (Player player : players) {
            int score = player.getScore();
            int count = 0;
            if (player.getObjective() != null) {
                score += player.getPlayerArea().checkPattern(player.getObjective());
                count += player.getPlayerArea().countPattern(player.getObjective());
            }

            if (matchInfo.getMatch().getCommonObjective()[0] != null) {
                score += player.getPlayerArea().checkPattern(this.matchInfo.getMatch().getCommonObjective()[0]);
                count += player.getPlayerArea().countPattern(this.matchInfo.getMatch().getCommonObjective()[0]);
            }

            if (matchInfo.getMatch().getCommonObjective()[1] != null) {
                score += player.getPlayerArea().checkPattern(this.matchInfo.getMatch().getCommonObjective()[1]);
                count += player.getPlayerArea().countPattern(this.matchInfo.getMatch().getCommonObjective()[1]);
            }

            scores.put(player.getNickname(), score);
            numberOfObjects.put(player.getNickname(), count);
        }

        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {

            playerInfo1.getConnection().sendEndGame(scores, numberOfObjects);
        }

        this.matchInfo.setStatus(MatchState.KickingPlayers);

        this.saveMatch();
    }

    private void kickingPlayers() {
        logCreator.log("Kicking out all players");
        this.getOnlinePlayerInfo().forEach(this::kickPlayer);
    }

    /**
     * Finds a player by their nickname.
     *
     * @param player The player object to search for.
     * @return The player information if found, or {@code null} if not found.
     */
    private PlayerInfo findPlayer(Player player) {
        return this.getOnlinePlayerInfo().stream()
                .filter(playerInfo -> Objects.equals(player.getNickname(), playerInfo.getPlayer().getNickname()))
                .findFirst()
                .orElse(null);
    }


    /**
     * Checks if the number of online players is correct.
     * If only one player remains, they are declared the winner.
     * If no players remain, the match is over.
     */
    private void checkOnlinePlayersNumber() {

        this.checkPlayersConnections();

        Timer timer = new Timer();
        Timer timerCheckConnections = new Timer();

        synchronized (this) {
            // This function must be used at the end of every turn
            if (this.getOnlinePlayerInfo().size() == 1) {
                // Waits for a timeout.
                // Then, if a player remains, he is declared the new winner
                MatchManager manager = this;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (manager) {
                            manager.checkPlayersConnections();
                            if (manager.getOnlinePlayerInfo().size() == 1) {
                                manager.matchInfo.setStatus(MatchState.Endgame);
                                logCreator.log("Only one player remains, he is the winner");
                                manager.notifyAll();
                            }

                            if (manager.getOnlinePlayerInfo().isEmpty()) {
                                manager.matchInfo.setStatus(MatchState.KickingPlayers);
                                logCreator.log("No players remain, the match is over");
                                manager.notifyAll();
                            }
                        }
                    }
                };
                timer.schedule(task, this.timeout);

                // Timer taskCheckConnections that checks every 15 seconds if all players are still connected

                TimerTask taskCheckConnections = new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (manager) {
                            manager.checkPlayersConnections();
                            manager.notifyAll();
                        }
                    }
                };

                timerCheckConnections.schedule(taskCheckConnections, 0, 15000);
            }


            while (this.getOnlinePlayerInfo().size() == 1
                    && this.matchInfo.getStatus() != MatchState.Endgame
                    && this.matchInfo.getStatus() != MatchState.KickingPlayers) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    logCreator.log("Thread interrupted while waiting for players to join again");
                }
            }


            if (this.getOnlinePlayerInfo().isEmpty()) {
                // Closes and doesn't save the match
                logCreator.log("No player remains, closing the match");
                this.matchInfo.setStatus(MatchState.KickingPlayers);
            }

            // Eventually cancels the timer
            timer.cancel();
            timerCheckConnections.cancel();
        }
    }


    /**
     * Returns a list of nicknames of all online players
     *
     * @return list of online players
     */
    private ArrayList<String> onlinePlayersNicknames() {
        return this.matchInfo.getAllPlayersInfo().stream()
                .filter(playerInfo -> playerInfo.getConnection() != null)
                .map(playerInfo -> playerInfo.getPlayer().getNickname())
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * Loads a saved match and waits for all players to join. Then starts the game.
     */
    protected synchronized void loadAndWaitSavedMatch() {
        logCreator.log("Saved match " + this.matchInfo.getID() + " loaded: in state " + this.matchInfo.getStatus());
        logCreator.log("Waiting for players; expected players: " + this.matchInfo.getExpectedPlayers() + " players to be loaded: " + this.matchInfo.getAllPlayersInfo());

        // Sets all players to offline
        this.matchInfo.getAllPlayersInfo().forEach(this.matchInfo::setOffline);

        // Waits for all players to join
        while (this.getOnlinePlayerInfo().size() < this.matchInfo.getExpectedPlayers()) {
            try {
                this.wait();
            } catch (InterruptedException ignore) {
            }
        }

        // Kicks players that are not supposed to be in the match
        while (this.getOnlinePlayerInfo().size() > this.matchInfo.getExpectedPlayers()) {
            logCreator.log("Too many players have joined the match");
            this.kickPlayer(this.getOnlinePlayerInfo().getLast());
        }

        this.run();
    }

    /**
     * Checks if all players are still connected.
     * If a player is not connected, they are kicked from the match.
     */
    private synchronized void checkPlayersConnections() {
        HashMap<Future<Boolean>, PlayerInfo> futures = new HashMap<>();
        ArrayList<Future<Boolean>> results = new ArrayList<>();

        // Sends a ping to all players
        for (PlayerInfo playerInfo : this.getOnlinePlayerInfo()) {
            Future<Boolean> future = executor.submit(() -> playerInfo.getConnection().isConnected());
            futures.put(future, playerInfo);
        }

        int timeout = 5;
        TimeUnit unit = TimeUnit.SECONDS;

        // Expects a response from all players
        for (Future<Boolean> currFuture : futures.keySet()) {
            Future<Boolean> responseFuture = executor.submit(() -> {
                try {
                    boolean response = currFuture.get(timeout, unit);
                    if (!response)
                        throw new Exception();
                    logCreator.log("Player " + futures.get(currFuture).getPlayer().getNickname() + " is online");
                    return true;
                } catch (Exception e) {
                    logCreator.log("Player " + futures.get(currFuture).getPlayer().getNickname() + " has not answered to ping");
                    this.kickPlayer(futures.get(currFuture));
                    return false;
                }
            });

            results.add(responseFuture);
        }

        // Waits for all tasks to complete
        for (Future<Boolean> future : results) {
            try {
                future.get();
            } catch (Exception e) {
                logCreator.log("Failed to get response from player");
            }
        }

        // Uncomment to print the status of all players' connections
        //this.matchInfo.printPlayersStatus();

    }
}
