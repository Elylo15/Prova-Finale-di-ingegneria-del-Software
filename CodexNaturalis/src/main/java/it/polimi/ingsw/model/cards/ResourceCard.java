package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the Resource cards in the game.
 * It extends the PlaceableCard class and implements the Serializable interface.
 * It provides constructors for creating a ResourceCard with different parameters.
 */
public class ResourceCard extends PlaceableCard implements Serializable {
    /**
     * This constructor creates a new ResourceCard with the given ID.
     * It throws an InvalidIdException if the ID is not between 1 and 40.
     *
     * @param ID The ID of the card. It must be between 1 and 40.
     * @throws InvalidIdException If the ID is not between 1 and 40.
     */
    public ResourceCard(int ID) throws InvalidIdException {
        super(ID);
        if (ID >= 1 && ID <= 40) {
            this.ID = ID;
        } else if (ID < 1) {
            throw new InvalidIdException("ID is too small");
        } else {
            throw new InvalidIdException("ID is too big");
        }
    }

    /**
     * This constructor is used to load the cards from JSON.
     * It creates a new ResourceCard with the given parameters.
     *
     * @param ID,          it must be >=1  and =<40
     * @param front,       true if the card is front, false if the card is back
     * @param requirement, resources required to place the card
     * @param points,      points given by the card
     * @param reign,       reign of the card
     * @param cells,       cells of the card
     * @param resources,   resources given by the card
     * @throws InvalidIdException if the condition on ID is not met
     */
    @JsonCreator
    public ResourceCard(@JsonProperty("ID") int ID,
                        @JsonProperty("front") boolean front,
                        @JsonProperty("requirement") ArrayList<Resource> requirement,
                        @JsonProperty("points") int points,
                        @JsonProperty("reign") Reign reign,
                        @JsonProperty("cells") ArrayList<Cell> cells,
                        @JsonProperty("resources") ArrayList<Resource> resources) throws InvalidIdException {
        super(ID, front, requirement, points, reign, cells, resources);
    }

    /**
     * This constructor creates a new ResourceCard with the given parameters.
     * It throws an InvalidIdException if the ID is not between 1 and 40.
     *
     * @param ID,       it must be >=1  and =<40
     * @param points    points given by the card
     * @param reign     reign of the card
     * @param front     true if the card is front, false if the card is back
     * @param resources resources given by the card
     * @throws InvalidIdException if the condition on ID is not met
     */
    public ResourceCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources) throws InvalidIdException {
        super(ID, points, reign, front, resources, new ArrayList<>());
    }

    /**
     * This method checks if the requirement to place the card is met.
     * It always returns true as resource cards have no requirement.
     *
     * @param req it receives as parameter an ArrayList containing the number of resources of each type the player possesses in his PlayerArea
     * @return true
     */
    @Override
    public boolean checkRequirement(ArrayList<Integer> req) {
        return true;
    }

}
