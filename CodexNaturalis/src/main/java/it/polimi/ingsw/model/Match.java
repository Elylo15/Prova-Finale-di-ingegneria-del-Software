package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.Collections;

public class Match {
    private final ArrayList<Player> players;
    private final ObjectiveCard[] commonObjective;
    private final CommonArea commonArea;

    /**
     * Constructs a new {@code Match} object.
     */
    public Match() {
        this.players = new ArrayList<>();
        this.commonObjective = new ObjectiveCard[2];
        this.commonArea = new CommonArea();
    }

    /**
     * method {@code addPlayer}: adds a new player. Can't be added more than 4 players.
     *
     * @param player: player to be added.
     */
    public void addPlayer(Player player) {
        if (players.size() < 4)
            players.add(player);
        else
            System.out.println("The maximum number of players is 4. Can't add more players.");
    }

    /**
     * method {@code start}: start the match. Each player plays its turn.
     * @return true if the match ended correctly.
     */
    public boolean start() {
        int i =0;

        if (players.size() < 2) {
            System.out.println("Not enough players to start the match");
            return false;
        }

        commonArea.getD1().shuffle();
        commonArea.getD2().shuffle();
        commonArea.getD3().shuffle();
        commonArea.getD4().shuffle();

        drawCommonObjective();

        Collections.shuffle(players); //randomizes the order of the players

        for (Player player : players)
            player.initialHand();

        while (players.get(i).getScore() < 20 && (commonArea.drawFromToPlayer(1) != null ||
                commonArea.drawFromToPlayer(2) != null || commonArea.getTableCards() != null) ){ //I need the || to check all the cond are false, no cards available
            for (i =0 ; i < players.size(); i++) {
                nextPlayer(players.get(i)).playTurn();
            }
        }

        for (i=0; i < players.size(); i++)  //lastTurn
            nextPlayer(players.get(i)).playTurn();

        for (i=0; i < players.size(); i++)
            addObjectivePoints(players.get(i));

        return true;
    }

    /**
     * Method {@code drawCommonObjective} draws the two commonObjective cards;
     */
    private void drawCommonObjective() {
        commonObjective[0] = commonArea.drawObjectiveCard();
        commonObjective[1] = commonArea.drawObjectiveCard();
    }

    /**
     * method {@code nextPlayer}: calculates the next player.
     * @param current: the current player
     * @return the next player
     */
    private Player nextPlayer(Player current) {
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
        int winnerIndex2 = 0;

        for (int i = 1; i < (players.size()- 1); i++) {
            if (players.get(winnerIndex).getScore() == players.get(i).getScore()){
                winnerIndex2 = i;
            } else if (players.get(winnerIndex).getScore() < players.get(i).getScore())
                winnerIndex = i;
        }

        if(winnerIndex != winnerIndex2) {
            if(totalObjective(players.get(winnerIndex)) < totalObjective(players.get(winnerIndex2)))
                return players.get(winnerIndex2);
            else
                return players.get(winnerIndex);
        }

        return players.get(winnerIndex);
    }

    /**
     * Method {@code addObjectivePoints}: adds the objective points to the players score.
     */
    private void addObjectivePoints(Player player) {
        int times;
        int score = player.getScore();

        times = player.getPlayerArea().countPattern(player.getObjective());
        while(times>0) {
            score +=  player.getPlayerArea().checkPattern(player.getObjective());
            times--;
        }

        times = player.getPlayerArea().countPattern(commonObjective[0]);
        while(times>0) {
            score += player.getPlayerArea().checkPattern(commonObjective[0]);
            times--;
        }

        times = player.getPlayerArea().countPattern(commonObjective[1]);
        while(times>0) {
            score += player.getPlayerArea().checkPattern(commonObjective[1]);
            times--;
        }

        player.setScore(score);

    }

    private int totalObjective(Player player) {
        int times = player.getPlayerArea().countPattern(player.getObjective());
        times += player.getPlayerArea().countPattern(commonObjective[0]);
        times += player.getPlayerArea().countPattern(commonObjective[1]);

        return times;
    }
}