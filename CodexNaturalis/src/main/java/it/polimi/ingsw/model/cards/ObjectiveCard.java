package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;


public class ObjectiveCard extends Card{

    private int points;

    /**
     * Creates a new objective card based on ID
     * @param ID identifier of the card, must be 87 <= ID <= 102
     * @throws InvalidIdException if the condition on ID is not met
     */
    public ObjectiveCard(int ID) throws InvalidIdException
    {
        if(ID > 86 && ID < 102) {
            this.ID = ID;
            if(ID >= 87 && ID <= 90) {this.points = 2;}
            if(ID >= 91 && ID <= 94) {this.points = 3;}
            if(ID >= 95 && ID <= 98) {this.points = 2;}
            if(ID == 99) {this.points = 3;}
            if(ID >= 100 && ID <= 102) {this.points = 2;}

        } else if (ID < 87) {
            throw new InvalidIdException("ID too small");
        } else if (ID > 101) {
            throw new InvalidIdException("ID too big");
        }
    }

    /**
     * Returns the points given by a single identified pattern
     * @return points
     */
    public int getPoints() {
        return points;
    }
}
