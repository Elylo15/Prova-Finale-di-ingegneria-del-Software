package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;
import java.util.ArrayList;


public class ObjectiveCard extends Card implements Serializable {

    private int points;

    private ArrayList<Resource> requirements;

    private ArrayList<Integer[]> pattern;

    private ArrayList<Reign> reignCards;

    /**
     * Creates a new objective card based on ID
     * @param ID identifier of the card, must be 87 <= ID <= 102
     * @throws InvalidIdException if the condition on ID is not met
     */
    public ObjectiveCard(int ID) throws InvalidIdException
    {
        if(ID > 86 && ID < 102) {
            this.ID = ID;
            if(ID <= 90) {this.points = 2;}
            if(ID >= 91 && ID <= 94) {this.points = 3;}
            if(ID >= 95 && ID <= 98) {this.points = 2;}
            if(ID == 99) {this.points = 3;}
            if(ID >= 100) {this.points = 2;}

        } else if (ID < 87) {
            throw new InvalidIdException("ID too small");
        } else {
            throw new InvalidIdException("ID too big");
        }
    }

    public ObjectiveCard(int ID, int points, ArrayList<Resource> requirements, ArrayList<Integer[]> pattern, ArrayList<Reign> reignCards) {
        super();
        this.ID = ID;
        this.points = points;
        this.requirements = requirements;
        this.pattern = pattern;
        this.reignCards = reignCards;
    }

    /**
     * Returns the points given by a single identified pattern
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the resources required to get points, or null if the objective doesn't require resources.
     * @return List of resource or null il the objective requires no resources
     */
    public ArrayList<Resource> getRequirements() {
        return requirements;
    }

    /**
     * If the objective requires a pattern, and taking as reference the position of the most top-left card,
     * returns the relative positions of the other cards.
     * @return List of coordinates of the two cards or null if the objective requires no pattern
     */
    public ArrayList<Integer[]> getPattern() {
        return pattern;
    }

    /**
     * If the objective requires a pattern, returns the reign of all cards involved in the pattern
     * @return List of Reign or null if the objective requires no pattern
     */
    public ArrayList<Reign> getReignCards() {
        return reignCards;
    }
}
