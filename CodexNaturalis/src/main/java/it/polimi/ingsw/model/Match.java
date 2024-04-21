package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.Collections;

public class Match {
    protected final ArrayList<Player> players;
    private final ObjectiveCard[] commonObjective;
    protected final CommonArea commonArea;

    /**
     * Constructs a new {@code Match} object.
     */
    public Match() {
        this.players = new ArrayList<>();
        this.commonObjective = new ObjectiveCard[2];
        this.commonArea = new CommonArea();
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
    public void start(boolean initialSide, int initialPick) throws Exception {

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
     * method {@code playerTurn}: the player plays its turn.
     * @param player int that indicates the player that has to play.
     * @param cardPick  integer that indicates the card chosen.
     * @param x position.
     * @param y  position.
     * @param side integer that indicates the side chosen.
     * @param drawPick integer that indicates the card chosen.
     * @throws Exception if there are no more cards on the commonArea, or if there is an error in playTurn.
     */
    public void playerTurn(int player, int cardPick, int x, int y, int side, int drawPick) throws Exception {

        if (players.get(player).getScore() < 20 && (commonArea.drawFromToPlayer(1) != null ||
                commonArea.drawFromToPlayer(2) != null || commonArea.getTableCards() != null)) // || to check all the cond are false, no cards available
               players.get(player).playTurn(cardPick, x, y, side, drawPick);
        else
            throw new Exception("No more cards to play, match ended."); //Starts last turn
        }

    /**
     *  method {@code lastTurn}: the player plays its last turn.
     * @param player int that indicates the player that has to play.
     * @param cardPick  integer that indicates the card chosen.
     * @param x position.
     * @param y  position.
     * @param side integer that indicates the side chosen.
     * @param drawPick integer that indicates the card chosen.
     * @throws Exception if there is an error in playTurn.
     */

    public void lastTurn(int player, int cardPick, int x, int y, int side, int drawPick) throws Exception {
        players.get(player).playTurn(cardPick, x, y, side, drawPick);
        addObjectivePoints(players.get(player));
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
    private void addObjectivePoints(Player player) {
        int score = player.getScore();

        score += player.getPlayerArea().checkPattern(player.getObjective());
        score += player.getPlayerArea().checkPattern(commonObjective[0]);
        score += player.getPlayerArea().checkPattern(commonObjective[1]);

        player.setScore(score);

    }

    private int totalObjective(Player player) {
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
}