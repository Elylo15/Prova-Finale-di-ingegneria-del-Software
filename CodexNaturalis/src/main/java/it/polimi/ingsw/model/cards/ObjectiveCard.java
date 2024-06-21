package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the Objective cards in the game.
 * It extends the Card class and implements the Serializable interface.
 * It provides constructors for creating an ObjectiveCard with different parameters.
 */
public class ObjectiveCard extends Card implements Serializable {
    private int points;
    private ArrayList<Resource> requirements;
    private ArrayList<int[]> pattern;
    private ArrayList<Reign> reignCards;

    /**
     * This constructor creates a new ObjectiveCard with the given ID.
     * It throws an InvalidIdException if the ID is not between 87 and 102.
     *
     * @param ID The ID of the card. It must be between 87 and 102.
     * @throws InvalidIdException If the ID is not between 87 and 102.
     */
    public ObjectiveCard(int ID) throws InvalidIdException {
        if (ID >= 87 && ID <= 102) {
            this.ID = ID;
            if (ID <= 90) {
                this.points = 2;
            }
            if (ID >= 91 && ID <= 94) {
                this.points = 3;
            }
            if (ID >= 95 && ID <= 98) {
                this.points = 2;
            }
            if (ID == 99) {
                this.points = 3;
            }
            if (ID >= 100) {
                this.points = 2;
            }

        } else if (ID < 87) {
            throw new InvalidIdException("ID too small");
        } else {
            throw new InvalidIdException("ID too big");
        }
    }

    /**
     * This constructor is used to load the cards from JSON.
     * It creates a new ObjectiveCard with the given parameters.
     *
     * @param ID           The ID of the card. It must be between 87 and 102.
     * @param front        True if the card is front, false if the card is back.
     * @param points       The points of the card.
     * @param requirements The requirements to place the card.
     * @param pattern      The pattern of the card.
     * @param reignCards   The reign of the cards involved in the pattern.
     * @throws InvalidIdException If the ID is not between 87 and 102.
     */
    @JsonCreator
    public ObjectiveCard(@JsonProperty("ID") int ID,
                         @JsonProperty("front") boolean front,
                         @JsonProperty("points") int points,
                         @JsonProperty("requirement") ArrayList<Resource> requirements,
                         @JsonProperty("pattern") ArrayList<int[]> pattern,
                         @JsonProperty("reign") ArrayList<Reign> reignCards) throws InvalidIdException {
        super(ID, front);
        properties(points, requirements, pattern, reignCards);
    }

    /**
     * This constructor creates a new ObjectiveCard with the given parameters.
     * It throws an InvalidIdException if the ID is not between 87 and 102.
     *
     * @param ID           The ID of the card. It must be between 87 and 102.
     * @param points       The points of the card.
     * @param requirements The requirements to place the card.
     * @param pattern      The pattern of the card.
     * @param reignCards   The reign of the cards involved in the pattern.
     */
    public ObjectiveCard(int ID, int points, ArrayList<Resource> requirements, ArrayList<int[]> pattern, ArrayList<Reign> reignCards) {
        super();
        this.ID = ID;
        properties(points, requirements, pattern, reignCards);
    }

    /**
     * This method sets the properties of the ObjectiveCard.
     *
     * @param points       The points of the card.
     * @param requirements The requirements to place the card.
     * @param pattern      The pattern of the card.
     * @param reignCards   The reign of the cards involved in the pattern.
     */
    private void properties(int points, ArrayList<Resource> requirements, ArrayList<int[]> pattern, ArrayList<Reign> reignCards) {
        this.points = points;
        this.requirements = requirements;
        this.pattern = pattern;
        this.reignCards = reignCards;
    }

    /**
     * This method returns the points given by a single identified pattern.
     *
     * @return The points of the card.
     */
    public int getPoints() {
        return points;
    }

    /**
     * This method returns the resources required to get points, or null if the objective doesn't require resources.
     *
     * @return The list of resources required to get points, or null if the objective doesn't require resources.
     */
    public ArrayList<Resource> getRequirements() {
        return requirements;
    }

    /**
     * If the objective requires a pattern, and taking as reference the position of the most top-left card,
     * returns the relative positions of the other cards.
     *
     * @return List of coordinates of the two cards or null if the objective requires no pattern
     */
    public ArrayList<int[]> getPattern() {
        return pattern;
    }

    /**
     * If the objective requires a pattern, returns the reign of all cards involved in the pattern
     *
     * @return List of Reign or null if the objective requires no pattern
     */
    public ArrayList<Reign> getReignCards() {
        return reignCards;
    }

}
