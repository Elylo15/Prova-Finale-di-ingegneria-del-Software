package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class Match {
    private final ArrayList<Player> players;
    private final ObjectiveCard[] commonObjective;
    private final CommonArea commonArea;
//    private final ArrayList<Integer> playerScore; I think we don't need this

    /**
     * Constructs a new {@code Match} object.
     */
    public Match() {
        this.players = new ArrayList<>();
//        this.playerScore = new ArrayList<Integer>();
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

        commonArea.d1.shuffle();
        commonArea.d2.shuffle();
        commonArea.d3.shuffle();
        commonArea.d4.shuffle();

        playerOrder();

        for (Player player : players)
            player.initialHand();

        while (players.get(i).getScore() < 20){ //and deck not empty
            for (i =0 ; i < players.size(); i++) {
                nextPlayer(players.get(i)).playTurn();
            }
        }

        for (i=0; i < players.size(); i++)      //lastTurn
            nextPlayer(players.get(i)).playTurn();

        addObjectivePoints();

        return true;
    }

    /**
     * Method {@code playerOrder}: randomizes the order of the players.
     */
    private void playerOrder() {
        int max = players.size();
        int min = 2;
        int times = 0;

        ArrayList<Player> playersOrdered = new ArrayList<>(players);

        while (times < max) {
            int order = (int) (Math.random() * (max - min) + min); //what if I get twice the same number
            players.set(times, playersOrdered.get(order));
            times++;
        }

    }

    /**
     * Method {@code addObjectivePoints}: adds the objective points to the players score.
     */
    private void addObjectivePoints() {

    //    for(int i=0; i<players.size(); i++)
    //        players.get(i).getScore() += objectivePoints;

        // objectivePoints = Objective points calculated

    }

    /**
     * Method {@code winner} calculates which player has higher score.
     * @return player that won.
     */
    public Player winner() {
        int winnerIndex = 0;

        for (int i = 1; i < (players.size()- 1); i++) {
            if (players.get(winnerIndex).getScore() < players.get(i).getScore())
                winnerIndex = i;
        }

        return players.get(winnerIndex);

        //to do if playerScore == playerScore and we have two winners

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

}