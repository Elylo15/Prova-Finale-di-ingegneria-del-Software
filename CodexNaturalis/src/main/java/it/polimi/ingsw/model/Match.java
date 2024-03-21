package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class Match {
    private ArrayList<Player> players;
    private ObjectiveCard[] commonObjective = new ObjectiveCard[2];
    private CommonArea commonArea;
    private ArrayList<Integer> playerScore;

    public Match(){
        players = new ArrayList<Player>();
        playerScore = new ArrayList<Integer>();
    }

    public void addPlayer(Player player){
        if(players.size() < 5)
            players.add(player);
        else
            System.out.println("The maximum number of players is 4. Can't add more players.");
    }

    public boolean start() {
        if (players.size() > 1)
            return true;
        else {
            System.out.println("Not enough players to start the match");
            return false;
        }
    }

    public void addObjectivePoints(){
        // objectivePoints = Objective points calculated
        // playerScore += objectivePoints;
    }

    public Player winner() {
        int winnerIndex = 0;
        int size = players.size();

        for (int i = 1; i < (size-1) ; i++) {
            if (playerScore.get(winnerIndex) < playerScore.get(i))
                winnerIndex = i;
        }

        return players.get(winnerIndex);

        //to be implemented: if playerscore == playerscore and we have two winners

    }

    public Player nextPlayer(Player current){
        int index = players.indexOf(current);
        int size = players.size();
        int nextIndex;

        if (index == size - 1)
            nextIndex = 0;
        else
            nextIndex = index + 1;

        return players.get(nextIndex);
    }

}


