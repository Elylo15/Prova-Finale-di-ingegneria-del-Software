package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
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

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ClientManager implements Runnable{
    private MatchInfo matchInfo;
    private ArrayList<PlayerInfo> playersInfo; // Only online players
    private int timeout;
    private int turnNumber;

    private ThreadPoolExecutor executor;

    private LogCreator logCreator;
    /**
     * Standard constructor for ClientManager
     * @param match object representing the model and data related to the server
     */
    public ClientManager(MatchInfo match) {
        this.matchInfo = match;
        this.matchInfo.setLastTurn(false);
        this.turnNumber = 0;

        playersInfo = new ArrayList<>();

        this.timeout = 2 * 60 * 1000;

        logCreator = new LogCreator(this.matchInfo.getID().toString());


        int corePoolSize = 15;
        int maximumPoolSize = 50;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

        logCreator.log("Client manager started");

    }


    /**
     * Checks if the player is in the list of online players or offline players
     * @param playerInfo player to be checked
     * @throws Exception if the player is not found
     */
    public synchronized void addPlayerInfo(PlayerInfo playerInfo) throws FailedToJoinMatch {
        if(playerInfo != null
                && matchInfo.getStatus() == MatchState.Waiting
                && (matchInfo.getExpectedPlayers() == null || matchInfo.getExpectedPlayers() > this.playersInfo.size())
                && this.playersInfo.stream().noneMatch(playerInfo1 -> playerInfo1.getPlayer().getNickname().equalsIgnoreCase(playerInfo.getPlayer().getNickname()))
                && this.playersInfo.stream().noneMatch(playerInfo1 -> playerInfo1.getPlayer().getColor().equalsIgnoreCase(playerInfo.getPlayer().getColor()))
                && this.matchInfo.getOfflinePlayers().stream().noneMatch(playerInfo1 -> playerInfo1.getPlayer().getNickname().equalsIgnoreCase(playerInfo.getPlayer().getNickname())
                && playerInfo1.getPlayer().getColor().equalsIgnoreCase(playerInfo.getPlayer().getColor())))
        {
            this.playersInfo.add(playerInfo);
            logCreator.log("Player added: " + playerInfo.getPlayer().getNickname() + " " + playerInfo.getPlayer().getColor());
            this.notifyAll();
        } else {
            throw new FailedToJoinMatch("Player cannot join the match");
        }
    }

    /**
     * Kicks a player from the match, removing it from the list of online players and adding it to the list of offline players
     *
     * @param playerInfo player to be kicked
     */
    private void kickPlayer(PlayerInfo playerInfo) {
        this.playersInfo.remove(playerInfo);
        playerInfo.getConnection().closeConnection();
        playerInfo.setConnection(null);
        matchInfo.addOfflinePlayer(playerInfo);
        logCreator.log("Player kicked: " + playerInfo.getPlayer().getNickname() + " and added to offline players");
    }

    /**
     * Adds a player to the list of online players and removes it from the list of offline players
     *
     * @param playerInfo player to be moved
     */
    public synchronized void movePlayer(PlayerInfo playerInfo) {
        if (matchInfo.getOfflinePlayers().contains(playerInfo)) {
            this.playersInfo.add(playerInfo);
            matchInfo.getOfflinePlayers().remove(playerInfo);
            this.notifyAll();
            logCreator.log("Player moved: " + playerInfo.getPlayer().getNickname() + " from offline to online players");
        } else {
            if (playerInfo != null)
                logCreator.log("Player " + playerInfo.getPlayer().getNickname() + " not found in offline players");
            else
                logCreator.log("Player is null and cannot be brought back online");
        }
    }



    /**
     * Checks if new players can join. If the number of player meets with expectedPlayers, the eventually new player is kicked.
     * @param connection connection manager of the new player.
     */
    public synchronized void checkAvailability(ClientConnection connection) {
        if(this.matchInfo.getExpectedPlayers() <= this.playersInfo.size())
            connection.closeConnection();
    }

    /**
     * Getter of MathInfo
     * @return MatchInfo
     */
    public MatchInfo getMatchInfo() {return this.matchInfo;}

    /**
     * Getter of PlayersInfo
     * @return list of PlayersInfo
     */
    public ArrayList<PlayerInfo> getPlayersInfo() {return this.playersInfo;}

    /**
     * Getter of Match (model)
     * @return Match
     */
    public Match getMatch() {return this.matchInfo.getMatch();}


    public synchronized void saveMatch() {
        logCreator.log("Match saved");

    }


    /**
     * Hosts a session for the current game
     */
    @Override
    public void run() {
        boolean gameOver = false;
        while(!gameOver) {

            // After the first turns prioritizes the players who reconnect and are in the starter state
            // or objective state
            synchronized(this) {
                // Draws common objective cards
                if (this.turnNumber == 2) {
                    this.matchInfo.getMatch().drawCommonObjective();
                    executor.submit(this::saveMatch);
                }

                // Checks if there are online player in the starter state after the initial turns
                if (this.turnNumber > 1) {
                    this.playersInfo.stream()
                            .filter(playerInfo -> playerInfo.getState() == State.StarterCard)
                            .forEach(playerInfo -> {
                                this.player(playerInfo.getPlayer());
                            });
                }


                // Checks if there are online player in the objective state after the initial turns
                if (this.turnNumber > 2) {
                    this.playersInfo.stream()
                            .filter(playerInfo -> playerInfo.getState() == State.Objective)
                            .forEach(playerInfo -> {
                                this.player(playerInfo.getPlayer());
                            });
                }


                // Eventually update all players to last turn
                if (this.matchInfo.isLastTurn()) {
                    this.playersInfo.stream()
                            .filter(playerInfo -> playerInfo.getState() == State.PlaceCard)
                            .forEach(playerInfo -> playerInfo.setState(State.LastTurn));
                }

            }

            // Starts the normal flow of the game
            switch (this.matchInfo.getStatus()) {
                case Waiting -> {
                    this.waiting();
                }
                case Player1 -> {
                    Player currentPlayer;
                    if(matchInfo.getMatch().getPlayers().size() < 1)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(0);

                    this.player(currentPlayer);
                }
                case Player2 -> {
                    Player currentPlayer;
                    if(matchInfo.getMatch().getPlayers().size() < 2)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(1);

                    this.player(currentPlayer);
                }
                case Player3 -> {
                    Player currentPlayer;
                    if(matchInfo.getMatch().getPlayers().size() < 3)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(2);
                    this.player(currentPlayer);
                }
                case Player4 -> {
                    Player currentPlayer;
                    if(matchInfo.getMatch().getPlayers().size() < 4)
                        currentPlayer = null;
                    else
                        currentPlayer = matchInfo.getMatch().getPlayers().get(3);

                    this.player(currentPlayer);



                    if(this.matchInfo.isLastTurn()) {
                        this.matchInfo.setStatus(MatchState.Endgame);
                    } else {
                        if (this.findPlayer(currentPlayer) == null) {
                            this.matchInfo.setStatus(MatchState.Player1);
                        } else {
                            if (findPlayer(currentPlayer).getState() != State.PlaceCard)
                                this.matchInfo.setStatus(MatchState.Player1);
                        }
                    }
                    this.turnNumber += 1;
                    if(this.matchInfo.getMatch().getPlayers().stream()
                            .anyMatch(player -> player.getScore() >= 20) ||
                            (this.matchInfo.getMatch().getCommonArea().getTableCards().isEmpty()
                            && this.getMatch().getCommonArea().getD1().getList().isEmpty()
                            && this.getMatch().getCommonArea().getD2().getList().isEmpty())) {
                        logCreator.log("This is the last turn");
                        matchInfo.setLastTurn(true);
                    }
                }
                case Endgame -> {
                    this.endgame();
                }
                case KickingPlayers -> {
                    this.kickingPlayers();
                    gameOver = true;
                }
            }
            this.checkOnlinePlayersNumber();
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
        while(this.matchInfo.getExpectedPlayers() == null ) {

            synchronized(this) {
                // Waiting for the first player that will be indicated as the "host"
                while (this.playersInfo.isEmpty()) {
                    try{
                        this.wait();
                    } catch (InterruptedException ignore) {
                        logCreator.log("Waiting for first player, InterruptedException received and ignored.");
                    }
                }
            }

            // preventing new player from joining at this moment and not getting all messages correctly
            synchronized(this) {


                PlayerInfo host = this.playersInfo.getFirst();

                if(host != null) {
                    // Sends current state data
                    this.playersInfo.forEach(playerInfo -> {
                                currentStateMessage curr = new currentStateMessage(null, playerInfo.getPlayer(),"WaitingForPlayerState",false, this.onlinePlayers(), null);
                                playerInfo.getConnection().sendCurrentState(curr);
                                playerInfo.getConnection().sendNewHostMessage(host.getPlayer().getNickname());
                            });

                    boolean correctAnswer = false;
                    while(!correctAnswer){
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
                            if(expected.isNoResponse()) {
                                correctAnswer = true;
                                this.kickPlayer(host);
                            } else {
                                // Checks if the response is valid and answer back
                                if(expected.getExpectedPlayers() >= 2 && expected.getExpectedPlayers() <= 4) {
                                    this.matchInfo.setExpectedPlayers(expected.getExpectedPlayers());
                                    correctAnswer = true;
                                    host.getConnection().sendAnswer(true);
                                    logCreator.log("Player " + host.getPlayer().getNickname() + " has correctly answered");
                                } else {
                                    host.getConnection().sendAnswer(false);
                                    logCreator.log("Player " + host.getPlayer().getNickname() + " has not correctly answered");
                                }
                            }

                        }
                    }


                }

            }

        }

        logCreator.log("Expected players obtained : " + this.matchInfo.getExpectedPlayers() + " from " + this.playersInfo.getFirst().getPlayer().getNickname());

        synchronized (this) {

            // Wait for the specified number of expected players
            while(this.playersInfo.size() < this.matchInfo.getExpectedPlayers()) {
                try {
                    this.wait();
                } catch (InterruptedException ignore) {}

//            // Gives a little of room to new players that want to join
//            try {
//                Thread.sleep(1000);
//            } catch (Exception ignore) {}
            }
        }

        while(this.playersInfo.size() > this.matchInfo.getExpectedPlayers()) {
            logCreator.log("Too many players have joined the match");
            this.kickPlayer(this.playersInfo.getLast());
        }

        // Updates state of the match
        this.matchInfo.setStatus(MatchState.Player1);
        logCreator.log("Match state updated from \"Waiting\" to \"Player 1\"");


        // Adds all players to the match (match of the model)
        for(PlayerInfo playerInfo : this.playersInfo) {
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
        executor.submit(this::saveMatch);
    }

    private synchronized void player(Player player) {
        if(player == null) {
            logCreator.log("Player is null");
            switch (this.matchInfo.getStatus()) {
                case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
                case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
                case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
                case Player4 -> this.matchInfo.setStatus(MatchState.Player1);
            }
            return;
        }
        PlayerInfo playerInfo = this.findPlayer(player);
        if(playerInfo == null) {
            logCreator.log("Player " + player.getNickname() + " not found and skipped");
            return;
        }


        switch (playerInfo.getState()) {
            case StarterCard -> {
                logCreator.log("Player " + player.getNickname() + " has to place the starter card");

                // Compute starter state itself
                this.starterState(playerInfo);

                // Updates the view of every player about the current one
                for(PlayerInfo playerInfo1 : this.playersInfo) {
                    updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }


                logCreator.log("Player " + player.getNickname() + " placed the starter card");

                // End turn and update states
                if(this.playersInfo.contains(playerInfo)) { // Checks if the player is online
                    playerInfo.setState(State.Objective); // TODO keep only objective

                    playerInfo.getPlayer().initialHand();

                    // REMOVE THIS
                    System.out.println("Player " + player.getNickname() + " is online and got his initial hand");
                }
                switch (this.matchInfo.getStatus()) {
                    case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
                    case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
                    case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
                    case Player4 -> this.matchInfo.setStatus(MatchState.Player1);
                }

                // Saves the progress of the game
                this.saveMatch();
            }
            case Objective -> {
                logCreator.log("Player " + player.getNickname() + " has to choose an objective");

                // Compute objective state itself
                this.objectiveState(playerInfo);


                // Updates the view of every player about the current one
                for(PlayerInfo playerInfo1 : this.playersInfo) {
                    updatePlayerMessage update = new updatePlayerMessage(player, playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                logCreator.log("Player " + player.getNickname() + " has correctly chosen an objective");
                // End turn and update states
                if(this.playersInfo.contains(playerInfo)) {
                    if(this.matchInfo.isLastTurn())
                        playerInfo.setState(State.EndGame);
                    else
                        playerInfo.setState(State.PlaceCard);
                }
                switch (this.matchInfo.getStatus()) {
                    case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
                    case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
                    case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
                    case Player4 -> this.matchInfo.setStatus(MatchState.Player1);
                }

                // Saves the progress of the game
                this.saveMatch();
            }
            case PlaceCard -> {
                logCreator.log("Player " + player.getNickname() + " starts normal turn and has to place a card");

                // Compute place card state itself
                this.placeCardState(playerInfo);



                // Updates all clients on the current situation
                for(PlayerInfo playerInfo1 : this.playersInfo) {
                    updatePlayerMessage update = new updatePlayerMessage(playerInfo.getPlayer(), playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                // Update the states
                if(this.playersInfo.contains(playerInfo)) {
                    playerInfo.setState(State.PickCard);
                }

                /*
                Match status is not updated, because this player has yet to pick a card to finish his turn.
                 */
            }
            case PickCard -> {

                if(this.matchInfo.getMatch().getCommonArea().getTableCards().isEmpty()) {
                    logCreator.log("Common area is empty, player " + player.getNickname() + " cannot pick a card. Ending his turn.");

                } else {
                    logCreator.log("Player " + player.getNickname() + " has to pick a card from common area");


                    // Compute pick card state itself
                    this.pickCardState(playerInfo);



                    // Updates all clients on the current situation
                    for(PlayerInfo playerInfo1 : this.playersInfo) {
                        updatePlayerMessage update = new updatePlayerMessage(playerInfo.getPlayer(), playerInfo1.getPlayer().getNickname());
                        playerInfo1.getConnection().sendUpdatePlayer(update);
                    }

                    logCreator.log("Player " + player.getNickname() + " has ended his normal turn");
                }

                // End turn
                // Update all states
                if(this.playersInfo.contains(playerInfo)) {
                    if(this.matchInfo.isLastTurn())
                        playerInfo.setState(State.EndGame);
                    else
                        playerInfo.setState(State.PlaceCard);
                }
                switch (this.matchInfo.getStatus()) {
                    case Player1 -> this.matchInfo.setStatus(MatchState.Player2);
                    case Player2 -> this.matchInfo.setStatus(MatchState.Player3);
                    case Player3 -> this.matchInfo.setStatus(MatchState.Player4);
                    case Player4 -> this.matchInfo.setStatus(MatchState.Player1);
                }


                // Saves the progress of the game
                this.saveMatch();


            }
            case LastTurn -> {
                logCreator.log("Player " + player.getNickname() + " plays his last turn");

                // Compute place card state itself
                this.placeCardState(playerInfo);

                // Updates all clients on the current situation
                for(PlayerInfo playerInfo1 : this.playersInfo) {
                    updatePlayerMessage update = new updatePlayerMessage(playerInfo.getPlayer(), playerInfo1.getPlayer().getNickname());
                    playerInfo1.getConnection().sendUpdatePlayer(update);
                }

                logCreator.log("Player " + player.getNickname() + " has ended his last turn");

                // Update the state
                if(this.playersInfo.contains(playerInfo))
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
     * Manages the starter state of a player.
     * @param playerInfo data about the player to manage.
     */
    private void starterState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Draw a StarterCard if player has none in hand
        if(playerInfo.getPlayer().getPlayerHand().getPlaceableCards().stream().
                noneMatch(PlaceableCard::isStarter))
            playerInfo.getPlayer().drawStarter();

        // Sends current state messages to all clients
        for(PlayerInfo playerInfo1 : this.playersInfo) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "StarterCardState", this.matchInfo.isLastTurn(), this.onlinePlayers(), null);
            playerInfo1.getConnection().sendCurrentState(currState);
        }

        // Obtains side of the starter card
        boolean correctAnswer = false;
        while(!correctAnswer) {


            Future<starterCardMessage> future = executor.submit(() -> playerInfo.getConnection().getStaterCard());
            starterCardMessage starter = null;


            try {
                starter = future.get(this.timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logCreator.log("Player " + player.getNickname() + " has not answered");
                this.kickPlayer(playerInfo);
            }

            if(starter != null) {

                // Checks if the client has properly given a response
                if(starter.isNoResponse()) {
                    correctAnswer = true;
                    logCreator.log("Player " + player.getNickname() + " has not answered");
                    this.kickPlayer(playerInfo);
                } else {
                    // Checks if the answer is valid
                    if(starter.getSide() == 0 || starter.getSide() == 1) {

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
                if(this.playersInfo.contains(playerInfo)) {
                    logCreator.log("Player " + player.getNickname() + " wanted to place a null card");
                    this.kickPlayer(playerInfo);
                }
            }
        }
    }

    /**
     * Manages the objective state of a player.
     * @param playerInfo data about the player to manage.
     */
    private void objectiveState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Draw two objective cards if there are none
        if(playerInfo.getSavedObjectives() == null)
            playerInfo.setSavedObjectives(playerInfo.getPlayer().drawObjectives());

        // Sends current state messages to all clients
        for(PlayerInfo playerInfo1 : this.playersInfo) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "ObjectiveState", this.matchInfo.isLastTurn(), this.onlinePlayers(), this.matchInfo.getMatch().getCommonObjective());
            playerInfo1.getConnection().sendCurrentState(currState);
        }

        boolean correctAnswer = false;
        while(!correctAnswer) {
            ArrayList<ObjectiveCard> objectives = new ArrayList<>(Arrays.asList(playerInfo.getSavedObjectives()));
            Future<objectiveCardMessage> future = executor.submit(() -> playerInfo.getConnection().getChosenObjective(objectives));
            objectiveCardMessage objective = null;

            try {

                // REMOVE THIS
                System.out.println("Waiting for objective to be placed");

                objective = future.get(this.timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logCreator.log("Player " + player.getNickname() + " has not answered");
                this.kickPlayer(playerInfo);
            }

            if(objective != null) {
                // Checks if the client has properly given a response
                if(objective.isNoResponse()) {
                    correctAnswer = true;
                    this.kickPlayer(playerInfo);
                } else {
                    // Checks if the answer is valid
                    if(objective.getChoice() == 1 || objective.getChoice() == 2) {
                        playerInfo.getPlayer().pickObjectiveCard(objective.getChoice(), playerInfo.getSavedObjectives());
                        correctAnswer = true;
                        playerInfo.getConnection().sendAnswer(true);
                        logCreator.log("Player " + player.getNickname() + " has correctly answered");
                    } else {
                        playerInfo.getConnection().sendAnswer(false);
                        logCreator.log("Player " + player.getNickname() + " has not correctly answered");
                    }
                }
            }

        }
    }

    /**
     * Manages the place card state of a player.
     * @param playerInfo data about the player to manage.
     */
    private void placeCardState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        // Sends current state messages to all clients
        for(PlayerInfo playerInfo1 : this.playersInfo) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "PlaceTurnState", this.matchInfo.isLastTurn(), this.onlinePlayers(), this.matchInfo.getMatch().getCommonObjective());
            playerInfo1.getConnection().sendCurrentState(currState);
        }



        // Asks client to place a card
        boolean correctAnswer = false;
        while(!correctAnswer) {
            Future<placeCardMessage> future = executor.submit(() -> playerInfo.getConnection().getPlaceCard());
            placeCardMessage placeCard = null;

            try {
                placeCard = future.get(this.timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logCreator.log("Player " + player.getNickname() + " has not answered");
                this.kickPlayer(playerInfo);
            }

            if(placeCard != null) {
                // Checks if the client has properly given a response
                if(placeCard.isNoResponse()) {
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
            }
        }
    }

    /**
     * Manages the pick card state of a player.
     * @param playerInfo data about the player to manage.
     */
    private void pickCardState(PlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();

        for(PlayerInfo playerInfo1 : this.playersInfo) {
            currentStateMessage currState = new currentStateMessage(player, playerInfo1.getPlayer(), "PickTurnState", this.matchInfo.isLastTurn(), this.onlinePlayers(), this.matchInfo.getMatch().getCommonObjective());
            playerInfo1.getConnection().sendCurrentState(currState);
        }


        // If the client is still online, it proceeds to ask to pick a card
        boolean correctAnswer;
        if(this.playersInfo.contains(playerInfo)) {
            correctAnswer = false;
            while(!correctAnswer) {
                Future<pickCardMessage> future = executor.submit(() -> playerInfo.getConnection().getChosenPick());
                pickCardMessage pickCard = null;

                try {
                    pickCard = future.get(this.timeout, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    logCreator.log("Player " + player.getNickname() + "has not answered");
                    this.kickPlayer(playerInfo);
                }

                if(pickCard != null) {
                    // Checks if the client has properly given a response
                    if(pickCard.isNoResponse()) {
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
        for(PlayerInfo playerInfo1 : this.playersInfo) {
            currentStateMessage currState = new currentStateMessage(null, playerInfo1.getPlayer(), "EndGameState", this.matchInfo.isLastTurn(), this.onlinePlayers(), this.matchInfo.getMatch().getCommonObjective());
            playerInfo1.getConnection().sendCurrentState(currState);
        }

        HashMap<String, Integer> scores = new HashMap<>();
        HashMap<String, Integer> numberOfObjects = new HashMap<>();

        ArrayList<Player> players =this.playersInfo.stream()
                .map(PlayerInfo::getPlayer)
                .collect(Collectors.toCollection(ArrayList::new));

        // Compute all points and number of objective accomplished
        for(Player player : players) {
            int score = player.getScore();
            int count = 0;
            score += player.getPlayerArea().checkPattern(player.getObjective());
            count += player.getPlayerArea().countPattern(player.getObjective());

            score += player.getPlayerArea().checkPattern(this.matchInfo.getMatch().getCommonObjective()[0]);
            count += player.getPlayerArea().countPattern(this.matchInfo.getMatch().getCommonObjective()[0]);

            score += player.getPlayerArea().checkPattern(this.matchInfo.getMatch().getCommonObjective()[1]);
            count += player.getPlayerArea().countPattern(this.matchInfo.getMatch().getCommonObjective()[1]);

            scores.put(player.getNickname(), score);
            numberOfObjects.put(player.getNickname(), count);
        }

        for(PlayerInfo playerInfo1 : this.playersInfo) {

            // REMOVE THIS
            System.out.println("Sending endgame message to " + playerInfo1.getPlayer().getNickname());

            playerInfo1.getConnection().sendEndGame(scores, numberOfObjects);
        }

        this.matchInfo.setStatus(MatchState.KickingPlayers);

        this.saveMatch();
    }

    private void kickingPlayers() {
        logCreator.log("Kicking out all players");
        this.playersInfo.forEach(this::kickPlayer);
    }

    /**
     * Finds a player by their nickname.
     * @param player The player object to search for.
     * @return The player information if found, or {@code null} if not found.
     */
    private PlayerInfo findPlayer(Player player) {
        return this.playersInfo.stream()
                .filter(playerInfo -> Objects.equals(player.getNickname(), playerInfo.getPlayer().getNickname()))
                .findFirst()
                .orElse(null);
    }




    // TODO check this method later
    // This method is to check if all players are online and to eventually declare a winner
    private void checkOnlinePlayersNumber() {
        // This function must be used at the end of every turn
        if(this.playersInfo.size() == 1) {
            // Waits for a timeout.
            // Then, if a player remains, he is declared the new winner
            Timer timer = new Timer();
            ClientManager manager = this;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (manager) {
                        if(manager.playersInfo.size() == 1) {
                            manager.matchInfo.setStatus(MatchState.Endgame);
                            this.notifyAll();
                        }

                        if(manager.playersInfo.isEmpty()) {
                            manager.matchInfo.setStatus(MatchState.KickingPlayers);
                            manager.saveMatch();
                            this.notifyAll();
                        }
                    }
                }
            };
            timer.schedule(task, this.timeout);
        }

        synchronized (this) {
            while (this.playersInfo.size() == 1 && (
                    this.matchInfo.getStatus() != MatchState.Endgame
                            || this.matchInfo.getStatus() != MatchState.KickingPlayers
            )) {
                try {
                    this.wait();
                } catch (InterruptedException ignore) {}
            }



            if (this.playersInfo.isEmpty()) {
                // Closes and saves the match
                this.saveMatch();
                this.matchInfo.setStatus(MatchState.KickingPlayers);
            }
        }
    }


    /**
     * Returns a list of all online players
     * @return list of online players
     */
    private ArrayList<String> onlinePlayers() {
        return this.playersInfo.stream()
                .map(playerInfo -> playerInfo.getPlayer().getNickname())
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
