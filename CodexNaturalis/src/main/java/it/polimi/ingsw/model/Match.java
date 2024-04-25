package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.Json.LoadDecks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Match implements Serializable {
    private final ArrayList<Player> players;
    private final ObjectiveCard[] commonObjective;
    private final CommonArea commonArea;

    /**
     * Constructs a new {@code Match} object.
     */
    public Match() {
        this.players = new ArrayList<>();
        this.commonObjective = new ObjectiveCard[2];
        this.commonArea = (new LoadDecks()).load();
    }

    /**
     * method {@code addPlayer}: adds a new player. Can't add more than 4 players.
     * @param player: player to be added.
     * @throws Exception if players are more than 4.
     */
    public void addPlayer(Player player) throws Exception {
        if (players.size() < 4)
            players.add(player);
        else
            throw new Exception("The maximum number of players is 4. Can't add more players.");
    }

    /**
     * method {@code start}: start the match. Each player plays its turn.
     * @param initialSide integer that indicates the side chosen.
     * @param initialPick integer that indicates the objective card chosen.
     * @throws Exception if player are less than 2.
     */
    public void start(int initialSide, int initialPick) throws Exception {

        if (players.size() < 2) {
            throw new Exception("Not enough players to start the match");
        }

        commonArea.getD1().shuffle();
        commonArea.getD2().shuffle();
        commonArea.getD3().shuffle();
        commonArea.getD4().shuffle();

        drawCommonObjective();

        Collections.shuffle(players); //randomizes the order of the players

        for (Player player : players)
            player.initialHand(initialSide, initialPick);
    }

    /**
     * Method {@code drawCommonObjective} draws the two commonObjective cards;
     */
    public void drawCommonObjective() {
        commonObjective[0] = commonArea.drawObjectiveCard();
        commonObjective[1] = commonArea.drawObjectiveCard();
    }

    /**
     * method {@code nextPlayer}: calculates the next player.
     * @param current: the current player
     * @return the next player
     */
    public Player nextPlayer(Player current) {
        int i = players.indexOf(current);
        int size = players.size();
        int nextI;

        if (i == size - 1)
            nextI = 0;
        else
            nextI = i + 1;

        return players.get(nextI);
    }

    /**
     * Method {@code winner} calculates which player has higher score.
     * @return player that won.
     */
    public Player winner() {
        int winnerIndex = 0;
        int winnerIndex2;

        for (int i = 1; i < (players.size()- 1); i++) {
            if (players.get(winnerIndex).getScore() == players.get(i).getScore()){
                winnerIndex2 = i;
                if(totalObjective(players.get(winnerIndex)) < totalObjective(players.get(winnerIndex2)))
                    winnerIndex = winnerIndex2;
            } else if (players.get(winnerIndex).getScore() < players.get(i).getScore())
                winnerIndex = i;
        }

        return players.get(winnerIndex);
    }

    /**
     * Method {@code addObjectivePoints}: adds the objective points to the players score.
     */
    public void addObjectivePoints(Player player) {
        int score = player.getScore();

        score += player.getPlayerArea().checkPattern(player.getObjective());
        score += player.getPlayerArea().checkPattern(commonObjective[0]);
        score += player.getPlayerArea().checkPattern(commonObjective[1]);

        player.setScore(score);

    }

    /**
     * Method {@code totalObjective}: counts how many times the objectives were completed by the player.
     * @param player of which the times are calculated.
     * @return integer;
     */
    protected int totalObjective(Player player) {
        int times = player.getPlayerArea().countPattern(player.getObjective());
        times += player.getPlayerArea().countPattern(commonObjective[0]);
        times += player.getPlayerArea().countPattern(commonObjective[1]);

        return times;
    }

    /**
     * @return commonArea.
     */
    public CommonArea getCommonArea(){
        return commonArea;
    }

    /**
     * @return commonObjective.
     */
    public ObjectiveCard[] getCommonObjective(){
        return commonObjective;
    }

    /**
     * @return player.
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

}