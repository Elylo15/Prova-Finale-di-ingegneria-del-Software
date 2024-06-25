package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.objectiveState.objectiveCardMessage;
import it.polimi.ingsw.messages.playerTurnState.pickCardMessage;
import it.polimi.ingsw.messages.playerTurnState.placeCardMessage;
import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.messages.staterCardState.starterCardMessage;
import it.polimi.ingsw.messages.waitingForPlayerState.expectedPlayersMessage;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import it.polimi.ingsw.server.exceptions.FailedToJoinMatch;
import it.polimi.ingsw.server.fsm.MatchState;
import it.polimi.ingsw.server.fsm.State;

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
     * Standard constructor for MatchManager
     *
     * @param match: is a MatchInfo object representing the model and data related to the server
     */
    public MatchManager(MatchInfo match) {
        this.matchInfo = match;
        this.matchInfo.setLastTurn(false);
        this.turnNumber = 0; //initialize to 0 the number of turn played

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
        //this method is synchronized, so it is not possible to add simultaneously more than one playerInfo on the same MatchManager object
        if (playerInfo != null
                && matchInfo.getStatus() == MatchState.Waiting
                && (matchInfo.getExpectedPlayers() == null || matchInfo.getExpectedPlayers() > matchInfo.getAllPlayersInfo().size())
                && matchInfo.getAllPlayersInfo().stream().noneMatch(playerInfo1 -> playerInfo1.getPlayer().getNickname().equalsIgnoreCase(playerInfo.getPlayer().getNickname()))
                && matchInfo.getAllPlayersInfo().stream().noneMatch(playerInfo1 -> playerInfo1.getPlayer().getColor().equalsIgnoreCase(playerInfo.getPlayer().getColor()))) {
            //in order to add playerInfo to the MatchInfo attribute of MatchManager the match must be in state waiting, number of expected players must be null or lower than the number of players
            //none of the players already involved in the match has the same name and color of the player to add
            this.matchInfo.addPlayer(playerInfo);
            logCreator.log("Player added: " + playerInfo.getPlayer().getNickname() + " " + playerInfo.getPlayer().getColor());
            this.notifyAll();
        } else {
            throw new FailedToJoinMatch("Player cannot join the match");
        }
    }

    /**
     * Kicks a player out of the match, closing its connection.
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

        // Check if the state of the match allows the player to join, throws exception if it is not possible to join
        if (this.matchInfo.getStatus() == MatchState.Waiting || this.matchInfo.getStatus() == MatchState.KickingPlayers || this.matchInfo.getStatus() == MatchState.Endgame) {
            throw new FailedToJoinMatch("Player cannot join the match, match is not in the right state");
        }

        // Check if the player is in the list of players involved in the match and he is offline
        PlayerInfo playerInfo = matchInfo.getAllPlayersInfo().stream()
                .filter(playerInfo1 -> playerInfo1.getPlayer().getNickname().equalsIgnoreCase(nickname))
                .filter(playerInfo1 -> playerInfo1.getConnection() == null) //if the player is offline his connection has been previously set to null
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
     * @return An ArrayList of PlayerInfo objects containing the online players.
     */
    protected ArrayList<PlayerInfo> getOnlinePlayerInfo() {
        return this.matchInfo.getAllPlayersInfo().stream()
                .filter(playerInfo -> playerInfo.getConnection() != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Retrieves a list of all online players in the match.
     * @return An ArrayList of PlayerInfo objects containing the offline players.
     */

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
     * Getter of the Match object (belonging to model)
     *
     * @return the Match attribute of matchInfo
     */
    protected Match getMatch() {
        return this.matchInfo.getMatch();
    }

    /**
     * create a clone of matchInfo and write it to a file that is created in the directory 'savedMatches'
     */
    private synchronized void saveMatch() {
        MatchInfo copy = matchInfo.cloneForSerialization(); //obtain an object MatchInfo equal to matchInfo attribute
        File dir = new File("savedMatches");
        if (!dir.exists()) {
            boolean dirCreated = dir.mkdir(); //create the file if it does not already exist

            if (!dirCreated) {
                System.out.println("Failed to create directory to save the match: " + dir.getAbsolutePath()); //standard output stream
                System.err.println("Failed to create directory to save the match: " + dir.getAbsolutePath()); //error output stream
            }
        }

        String filename = "savedMatches/match_" + copy.getID() + ".match";
        try (FileOutputStream fileOut = new FileOutputStream(filename); //creates a file with path filename and a FileOutputStream to write bytes to it
             ObjectOutputStream out = new ObjectOutputStream(fileOut))  //create a ObjectOutputStream to write serialized object to fileOut
        {    //fileOut and out are automatically closed after try block
            out.writeObject(copy); //write the matchInfo clone to the file
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

            // After the first turns prioritizes the players who reconnect and are in state StarterCard or Objective
            synchronized (this) {
                // Draws common objective cards, if the array of common objectives in the match is null call Match method drawCommonObjective()
                if (this.turnNumber == 2 && this.matchInfo.getMatch().getCommonObjective()[0] == null) {
                    this.matchInfo.getMatch().drawCommonObjective();
                    this.saveMatch();
                }

                // Checks if there are online player in the StarterCard state after turnNumber 1 in which players place starter card
                if (this.turnNumber > 1) {
                    this.getOnlinePlayerInfo().stream()
                            .filter(playerInfo -> playerInfo.getState() == State.StarterCard)
                            //now stream contains only playerInfo objects whose player is online and in state StarterCard
                            .forEach(playerInfo -> this.player(playerInfo.getPlayer()));
                }


                // Checks if there are online player in the Objective state after turnNumber 2 in which players choose private objective
                if (this.turnNumber > 2) {
                    this.getOnlinePlayerInfo().stream()
                            .filter(playerInfo -> playerInfo.getState() == State.Objective)
                            .forEach(playerInfo -> this.player(playerInfo.getPlayer()));
                }


                // Eventually, if the match is in last turn, we update all players in turn state PlaceCard to LastTurn
                if (this.matchInfo.isLastTurn()) {
                    this.getOnlinePlayerInfo().stream()
                            .filter(playerInfo -> playerInfo.getState() == State.PlaceCard)
                            .forEach(playerInfo -> playerInfo.setState(State.LastTurn));
                }

            }

            this.checkPlayersConnections();

            // Starts the normal flow of the game, we call the methods of this class according to the state of the match
            switch (this.matchInfo.getStatus()) {
                // enters the case corresponding to match status and then proceeds entering following cases (there is no break)
                case Waiting -> this.waiting();
                //turn if first player
                case Player1 -> {
                    Player currentPlayer;
                    if (matchInfo.getMatch().getPlayers().isEmpty())
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().getFirst();

                    this.player(currentPlayer);
                } //turn of second player
                case Player2 -> {
                    Player currentPlayer;
                    if (matchInfo.getMatch().getPlayers().size() < 2)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(1);

                    this.player(currentPlayer);
                } //turn of third player
                case Player3 -> {
                    Player currentPlayer;
                    if (matchInfo.getMatch().getPlayers().size() < 3)
                        currentPlayer = null; //third player is null if the size of the ArrayList of players is 0 or 1 or 2
                    else  //else he is the player at index 2
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

                    this.turnNumber += 1; //increment the number of turns played after every cycle of players playing their turn

                    //state of the match is actually Player4 only if fourth player is not null and in turn state PickCard
                    if (currentPlayer != null && findPlayer(currentPlayer) != null && findPlayer(currentPlayer).getState() == State.PickCard) {
                        this.matchInfo.setStatus(MatchState.Player4);
                    }

                    if (this.matchInfo.isLastTurn())
                        this.matchInfo.setStatus(MatchState.Endgame);

                    //if there is no fourth player and one of the players has achieved at least 20 points or
                    //there is no fourth player and both decks of resource card and gold card have run out of cards
                    // we set lastTurn to true
                    if (currentPlayer == null || findPlayer(currentPlayer) == null) {
                        if (this.matchInfo.getMatch().getPlayers().stream()
                                .anyMatch(player -> player.getScore() >= 20) ||
                                ( this.getMatch().getCommonArea().getD1().getList().isEmpty()
                                        && this.getMatch().getCommonArea().getD2().getList().isEmpty())) {
                            logCreator.log("This is the last turn");
                            matchInfo.setLastTurn(true);
                        }
                    } else
                    {  //if there are four players and the fourth player is not in state PickCard
                        //and one of the players has achieved at least 20 points or both resource ang gold cards decks have run out of cards
                        //we set lastTurn to true
                        if (findPlayer(currentPlayer).getState() != State.PickCard) {
                            if (this.matchInfo.getMatch().getPlayers().stream()
                                    .anyMatch(player -> player.getScore() >= 20) ||
                                    ( this.getMatch().getCommonArea().getD1().getList().isEmpty()
                                            && this.getMatch().getCommonArea().getD2().getList().isEmpty())) {
                                logCreator.log("This is the last turn");
                                matchInfo.setLastTurn(true);
                            }
                        }
                    }


                }
                case Endgame -> this.endgame();

                case KickingPlayers -> {
                    this.kickingPlayers();
                    gameOver = true;
                }
            }
            //after each turn we check the connection of each player and the number of online players
            this.checkPlayersConnections();
            this.checkOnlinePlayersNumber();

            // Leaves a pause between each turn in order to allow new clients to join,
            // each iteration of while cycle is performed after half a second from the previous one
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logCreator.log("Thread interrupted, while sleeping: " + e.getMessage());
            }
        }


        logCreator.close();
    }


    /**
     * Manages the WaitingForPlayers state for players to join the match.
     * It sends the current state data to all connected players, asks for the number of expected players for this match
     * and waits for the expected number of players to join.
     * When the number of expected players is met, it adds them to the match (model).
     * If a player takes too long to respond or provides an invalid response, he is kicked from the match.
     */
    private void waiting() {
        logCreator.log("Waiting for players");

        // Obtains the number of expected players for this match
        while (this.matchInfo.getExpectedPlayers() == null) {

            synchronized (this) {
                // Waiting for the first player to join the match, he will choose the number of players and will be referred as the "host"
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
                    // Sends current state data to each online player
                    this.getOnlinePlayerInfo().forEach(playerInfo -> {
                        currentStateMessage curr = new currentStateMessage(null, playerInfo.getPlayer(), "WaitingForPlayerState", false, this.onlinePlayersNicknames(), null, this.matchInfo.getID());
                        playerInfo.getConnection().sendCurrentState(curr); //sends to the client the message
                        playerInfo.getConnection().sendNewHostMessage(host.getPlayer().getNickname());
                    });

                    boolean correctAnswer = false;
                    while (!correctAnswer) {
                        // If the timer ends the player is kicked
                        Future<expectedPlayersMessage> future = executor.submit(() -> host.getConnection().getExpectedPlayer());
                        expectedPlayersMessage expected = null;
                        try {
                            expected = future.get(this.timeout, TimeUnit.MILLISECONDS); //waits 2 minutes to receive the expectedPlayersMessage response of the host
                        } catch (Exception e) {
                            logCreator.log("Player " + host.getPlayer().getNickname() + " has not answered");
                            this.kickPlayer(host); //the host is kicked if his answer is not received
                        }

                        if (expected != null) {
                            // Checks if the client has properly given a response
                            if (expected.isNoResponse()) {
                                correctAnswer = true;
                                //the host user is kicked if he does not make his choice on time, client will set the noResponse attribute to true on the expectedPlayersMessage forwarded by the controller
                                this.kickPlayer(host);
                            } else {
                                // Checks if the response is valid and answer back
                                if (expected.getExpectedPlayers() >= 2 && expected.getExpectedPlayers() <= 4) {
                                    this.matchInfo.setExpectedPlayers(expected.getExpectedPlayers());
                                    correctAnswer = true;
                                    host.getConnection().sendAnswer(true);
                                    logCreator.log("Player " + host.getPlayer().getNickname() + " has correctly answered");
                                } else {
                                    host.getConnection().sendAnswer(false); //send the host a negative responseMessage
                                    //there will be another iteration of the while cycle until he chose a valid value
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

            // Wait for the specified number of expected players to join
            while (this.getOnlinePlayerInfo().size() < this.matchInfo.getExpectedPlayers()) {
                try {
                    this.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
        //the number of players must be exactly the one specified by the host
        while (this.getOnlinePlayerInfo().size() > this.matchInfo.getExpectedPlayers()) {
            logCreator.log("Too many players have joined the match");
            this.kickPlayer(this.getOnlinePlayerInfo().getLast());
        }

        // Updates state of the match to turn of the first player
        this.matchInfo.setStatus(MatchState.Player1);
        logCreator.log("Match state updated from \"Waiting\" to \"Player 1\"");


        // Adds all players to the match (match of the model)
        for (PlayerInfo playerInfo : this.getOnlinePlayerInfo()) {
            try { //each online player is added to the match and the state of their turn is StarterCard
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
            updateMatchStatus(); //set match state to the next player turn
            return;
        }
        PlayerInfo playerInfo = this.findPlayer(player);
        if (playerInfo == null) {
            logCreator.log("Player " + player.getNickname() + " not found and skipped");
            updateMatchStatus();
        }

        if(playerInfo!=null && playerInfo.getState()!=null){
        switch (playerInfo.getState()) {
            //switch according to the state of the turn of the player
            case StarterCard -> {
                logCreator.log("Player " + player.getNickname() + " has to place the starter card");

                // Compute starter state itself
                this.starterState(playerInfo);

                // Updates the view of each online player with information about current one (player)
                for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                    updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update); //sends message to each player
                }

                // End turn and update states
                //If the player is online
                if (this.getOnlinePlayerInfo().contains(playerInfo)) {
                    logCreator.log("Player " + player.getNickname() + " placed the starter card");
                    playerInfo.setState(State.Objective); //set the state of his turn to Objective
                    playerInfo.getPlayer().initialHand(); //draws cards
                }

                this.updateMatchStatus();

                // Saves the progress of the game
                this.saveMatch();
            }
            case Objective -> {
                logCreator.log("Player " + player.getNickname() + " has to choose an objective");

                // Compute objective state itself
                this.objectiveState(playerInfo);

                // Updates the view of each online player with information about current one (player)
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
                        playerInfo.setState(State.PlaceCard); //set the state of the player's turn to PlaceCard
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
                    //if the player does not possess any available position in his area the state of the match is set to his next player's turn
                    this.updateMatchStatus();
                    canPlace = false;
                } else {
                    // Compute place card state itself
                    this.placeCardState(playerInfo);
                }

                // Updates the view of each online player with information about current one (player)
                for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                    updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                // Update the state of player's turn to PickCard
                if (this.getOnlinePlayerInfo().contains(playerInfo) && canPlace) {
                    playerInfo.setState(State.PickCard);
                }

                /*
                Match status is not updated, because this player has yet to pick a card to finish his turn.
                 */
                // Saves the progress of the game
                this.saveMatch();
            }
            case PickCard -> {

                if (this.matchInfo.getMatch().getCommonArea().getTableCards().isEmpty()
                        || this.getMatch().getCommonArea().getTableCards().stream().noneMatch(Objects::nonNull)){
                    logCreator.log("Common area is empty, player " + player.getNickname() + " cannot pick a card. Ending his turn.");

                } else {
                    logCreator.log("Player " + player.getNickname() + " has to pick a card from common area");

                    // Compute pick card state itself
                    this.pickCardState(playerInfo);

                    // Updates the view of each online player with information about current one (player)
                    for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                        updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                        playerInfo1.getConnection().sendUpdatePlayer(update);
                    }

                    logCreator.log("Player " + player.getNickname() + " has ended his normal turn");
                }

                // End turn
                // Update all states, if this is last turn, next state of the player's turn will be EndGame, else it will be PlaceCard
                if (this.getOnlinePlayerInfo().contains(playerInfo)) {
                    if (this.matchInfo.isLastTurn())
                        playerInfo.setState(State.EndGame);
                    else
                        playerInfo.setState(State.PlaceCard);
                }
                //update the state of the match
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

                // Updates the view of each online player with information about current one (player)
                for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
                    updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                logCreator.log("Player " + player.getNickname() + " has ended his last turn");

                // Update the state of the player's turn from LastTurn to EndGame
                if (this.getOnlinePlayerInfo().contains(playerInfo))
                    playerInfo.setState(State.EndGame);

                switch (this.matchInfo.getStatus()) {
                    //update the state of the match, if the cycle of the players' turn is completed match state is set to EndGame
                    case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
                    case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
                    case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
                    case Player4 -> this.matchInfo.setStatus(MatchState.Endgame);
                }

                this.saveMatch();
            }
        }}


    }

    /**
     * Updates the status of the match.
     */
    private void updateMatchStatus() {
        switch (this.matchInfo.getStatus()) {
            //the match state is updated to the player's turn
            case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
            case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
            case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
            case Player4 -> this.matchInfo.setStatus(MatchState.Player1);
        }
    }

    /**
     * Manages the StarterCard state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void starterState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Draw a StarterCard if player has none in hand
        if (playerInfo.getPlayer().getPlayerHand().getPlaceableCards().stream().
                noneMatch(PlaceableCard::isStarter))
            playerInfo.getPlayer().drawStarter();

        // Sends currentStateMessage to all online players with information about current player (player)
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
                //waits 2 minutes to obtain starterCardMessage from client
                starter = future.get(this.timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logCreator.log("Player " + player.getNickname() + " has not answered");
                this.kickPlayer(playerInfo);
                return;
            }
            //If we don't get the result of the task submitted to the executor within two minutes, an exception is thrown and player kicked
            //If we receive the starterCardMessage result but inNoResponse is set to true because client did not receive on time the input of the user from cli or gui
            //the player is kicked
            //If we receive the starterCardMessage result but the value is not valid, there will be another iteration of while cycle
            if (starter != null) {

                // Checks if the client has properly given a response
                if (starter.isNoResponse()) {
                    correctAnswer = true;
                    logCreator.log("Player " + player.getNickname() + " has not answered");
                    this.kickPlayer(playerInfo);
                } else {
                    // Checks if the answer is valid
                    if (starter.getSide() == 0 || starter.getSide() == 1) {
                         //if the answer is valid the card is placed
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
     * Manages the Objective state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void objectiveState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Draw two objective cards if there are none
        if (playerInfo.getSavedObjectives() == null)
            playerInfo.setSavedObjectives(playerInfo.getPlayer().drawObjectives());

        // Sends currentStateMessage to all online players with information about current player (player)
        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "objectiveState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), this.matchInfo.getMatch().getCommonObjective(), this.matchInfo.getID());
            playerInfo1.getConnection().sendCurrentState(currState);
        }

        boolean correctAnswer = false;
        while (!correctAnswer) {
            //creates an ArrayList<ObjectiveCard> with the objective cards in the savedObjectives array of PlayerInfo
            ArrayList<ObjectiveCard> objectives = new ArrayList<>(Arrays.asList(playerInfo.getSavedObjectives()));
            Future<objectiveCardMessage> future = executor.submit(() -> playerInfo.getConnection().getChosenObjective(objectives));
            objectiveCardMessage objective = null;

            try {
                //waits for receive the objectiveCardMessage from client
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
     * Manages the PlaceCard state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void placeCardState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Sends currentStateMessage to all online players with information about current player (player)
        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "PlaceTurnState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), this.matchInfo.getMatch().getCommonObjective(), this.matchInfo.getID());
            playerInfo1.getConnection().sendCurrentState(currState);
        }


        // Asks client to place a card
        boolean correctAnswer = false;
        while (!correctAnswer) {
            Future<placeCardMessage> future = executor.submit(() -> playerInfo.getConnection().getPlaceCard());
            placeCardMessage placeCard = null;

            try { //waits to receive the placeCardMessage from client
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
                        //if the user chose a card or positions that are not available noPlaceCardException is thrown and a negative responseMessage is sent
                        playerInfo.getConnection().sendAnswer(false);
                        logCreator.log("Player " + player.getNickname() + " has not correctly answered");
                    }
                }
            } else {
                logCreator.log("Player " + player.getNickname() + " failed to answer");
                this.kickPlayer(playerInfo);
               // correctAnswer = true;
                return;
            }
        }
    }

    /**
     * Manages the PickCard state of a player.
     *
     * @param playerInfo data about the player to manage.
     */
    private void pickCardState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Sends currentStateMessage to all online players with information about current player (player)
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
                        // Checks if the pickCardMessage has a valid value,
                        // otherwise InvalidIdException is thrown, a negative responseMessage is sent, there will be another iteration of while cycle
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
                    // correctAnswer = true;
                    return;
                }
            }
        }
    }


    /**
     * Manages the EndGame state of the match.
     * Calculates score and number of objectives achieved by each player and sends a declareWinnerMessage to the client
     */
    private void endgame() {
        logCreator.log("ENDGAME");
        // Sends current state messages to all online players
        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
            //currentPlayer is null as we don't need to update other players about him anymore
            currentStateMessage currState = new currentStateMessage(null, playerInfo1.getPlayer(), "endGameState", this.matchInfo.isLastTurn(), this.onlinePlayersNicknames(), this.matchInfo.getMatch().getCommonObjective(), this.matchInfo.getID());
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
            //update score and count given by private objective
            if (player.getObjective() != null) {
                score += player.getPlayerArea().checkPattern(player.getObjective());
                count += player.getPlayerArea().countPattern(player.getObjective());
            }
            //update score and count given by first common objective
            if (matchInfo.getMatch().getCommonObjective()[0] != null) {
                score += player.getPlayerArea().checkPattern(this.matchInfo.getMatch().getCommonObjective()[0]);
                count += player.getPlayerArea().countPattern(this.matchInfo.getMatch().getCommonObjective()[0]);
            }
            //update score and count given by second common objective
            if (matchInfo.getMatch().getCommonObjective()[1] != null) {
                score += player.getPlayerArea().checkPattern(this.matchInfo.getMatch().getCommonObjective()[1]);
                count += player.getPlayerArea().countPattern(this.matchInfo.getMatch().getCommonObjective()[1]);
            }

            scores.put(player.getNickname(), score);
            numberOfObjects.put(player.getNickname(), count);
        }

        for (PlayerInfo playerInfo1 : this.getOnlinePlayerInfo()) {
             //send declareWinnerMessage to each online player
            playerInfo1.getConnection().sendEndGame(scores, numberOfObjects);
        }
        //update the state of the match from EndGame to KickingPlayers
        this.matchInfo.setStatus(MatchState.KickingPlayers);

        this.saveMatch();
    }

    /**
     * Kick out of the match each online player
     */
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
     * If only one player remains, he is declared the winner.
     * If no players remain, the match is over.
     */
    private void checkOnlinePlayersNumber() {
        // This method must be used at the end of every turn

        this.checkPlayersConnections();

        Timer timer = new Timer();
        Timer timerCheckConnections = new Timer();

        synchronized (this) {
            if (this.getOnlinePlayerInfo().size() == 1) {
                // Waits for a timeout.
                // Then, if a player remains, he is declared the new winner
                MatchManager manager = this;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (manager) {
                            manager.checkPlayersConnections(); //update the ArrayList of online players
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
                //task is performed after 2 minutes
                timer.schedule(task, this.timeout);


                // Timer taskCheckConnections that checks every 15 seconds if all players are still connected
                TimerTask taskCheckConnections = new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (manager) {
                            manager.checkPlayersConnections(); //the ArrayList of online players is updated
                            manager.notifyAll(); //wakes up threads synchronized on manager
                        }
                    }
                };
                //taskCheckConnections is performed every 15 seconds
                timerCheckConnections.schedule(taskCheckConnections, 0, 15000);
            }

            //while there is only one online player and the match is not in state EndGame or KickingPlayers the current thread waits,
            //it can be awakened by the manager.notifyAll() and methods wakeUpPlayer, addPlayerInfo
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
     * Loads a saved match and waits for the right number of players to join. Then starts the game.
     */
    protected synchronized void loadAndWaitSavedMatch() {
        logCreator.log("Saved match " + this.matchInfo.getID() + " loaded: in state " + this.matchInfo.getStatus());
        logCreator.log("Waiting for players; expected players: " + this.matchInfo.getExpectedPlayers() + " players to be loaded: " + this.matchInfo.getAllPlayersInfo());

        // Sets all players of the saved match to offline
        this.matchInfo.getAllPlayersInfo().forEach(this.matchInfo::setOffline);

        // Waits for all players to join, we wait as long as the number of online players is lower than the number of expected players
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
        //if we are not in wait anymore the number of online players is reached the number of expected players, call the method run
        this.run();
    }

    /**
     * Checks if all players are still connected.
     * If a player is not connected, he is kicked out of the match.
     */
    private synchronized void checkPlayersConnections() {
        // This method must be used at the end of every turn

        HashMap<Future<Boolean>, PlayerInfo> futures = new HashMap<>();
        ArrayList<Future<Boolean>> results = new ArrayList<>();

        // Sends a ping to all players
        for (PlayerInfo playerInfo : this.getOnlinePlayerInfo()) {
            //for each playerInfo object whose player is online, we submit to the executor a task to check if he is online
            Future<Boolean> future = executor.submit(() -> playerInfo.getConnection().isConnected(this.getOnlinePlayerInfo().stream()
                    .map(playerInfo1 -> playerInfo1.getPlayer().getNickname())
                    .collect(Collectors.toCollection(ArrayList::new))));
            futures.put(future, playerInfo);
        }
        int timeout = 5;
        TimeUnit unit = TimeUnit.SECONDS;

        // Expects a response from all players
        for (Future<Boolean> currFuture : futures.keySet()) {
            //for each boolean key in futures, representing whether players are online or offline we submit to the executor a task to kick offline players
            Future<Boolean> responseFuture = executor.submit(() -> {
                try {
                    boolean response = currFuture.get(timeout, unit); //waits 5 seconds to obtain
                    if (!response) //if response is false, the player is offline, throws exception that will kick the player
                        throw new Exception();
                    //logCreator.log("Player " + futures.get(currFuture).getPlayer().getNickname() + " is online");
                    return true;
                } catch (Exception e) {
                    logCreator.log("Player " + futures.get(currFuture).getPlayer().getNickname() + " has not answered to ping");
                    this.kickPlayer(futures.get(currFuture));
                    return false;
                }
            });
            //results will contain objects of type Future<Boolean>, they will be true if we obtain in less than 5 seconds the result of the task that check players' connection
            results.add(responseFuture);
        }

        // Waits for all tasks to complete, blocks the thread until we obtain the result for each element of the ArrayList results
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
