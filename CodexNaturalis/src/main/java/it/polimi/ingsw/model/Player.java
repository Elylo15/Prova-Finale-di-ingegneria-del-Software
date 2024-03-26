package it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private Integer score;
    private PlayerHand deck;
    private PlayerArea playerArea;
    private CommonArea commonArea;
    private ObjectiveCard objective;

    public Player(String nickname){
        this.nickname = nickname;
        this.score = 0;
        this.deck = new PlayerHand();
        this.playerArea = new PlayerArea();
    }

    public void initialHand(){

    }

    public void playTurn(){

    }

    public Integer getScore(){
        return score;
    }

}
