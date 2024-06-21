package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the Gold cards in the game.
 * It extends the PlaceableCard class and implements the Serializable interface.
 * It provides constructors for creating a GoldCard with different parameters.
 */
public class GoldCard extends PlaceableCard implements Serializable {
    /**
     * This constructor creates a new GoldCard with the given ID.
     * It throws an InvalidIdException if the ID is not between 41 and 80.
     *
     * @param ID The ID of the card. It must be between 41 and 80.
     * @throws InvalidIdException If the ID is not between 41 and 80.
     */
    public GoldCard(int ID) throws InvalidIdException {
        super(ID);
        if (ID >= 41 && ID <= 80) {
            this.ID = ID;
        } else if (ID < 40) {
            throw new InvalidIdException("ID is too small");
        } else if (ID > 80) {
            throw new InvalidIdException("ID is too big");
        }
    }

    /**
     * This constructor is used to load the cards from JSON.
     * It creates a new GoldCard with the given parameters.
     *
     * @param ID          The ID of the card. It must be between 41 and 80.
     * @param front       True if the card is front, false if the card is back.
     * @param requirement The requirements to place the card.
     * @param points      The points of the card.
     * @param reign       The reign of the card.
     * @param cells       The cells of the card.
     * @param resources   The resources of the card.
     * @throws InvalidIdException If the ID is not between 41 and 80.
     */
    @JsonCreator
    public GoldCard(@JsonProperty("ID") int ID,
                    @JsonProperty("front") boolean front,
                    @JsonProperty("requirement") ArrayList<Resource> requirement,
                    @JsonProperty("points") int points,
                    @JsonProperty("reign") Reign reign,
                    @JsonProperty("cells") ArrayList<Cell> cells,
                    @JsonProperty("resources") ArrayList<Resource> resources) throws InvalidIdException {
        super(ID, front, requirement, points, reign, cells, resources);
    }

    /**
     * This constructor creates a new GoldCard with the given parameters.
     * It throws an InvalidIdException if the ID is not between 41 and 80.
     *
     * @param ID           The ID of the card. It must be between 41 and 80.
     * @param points       The points of the card.
     * @param reign        The reign of the card.
     * @param front        True if the card is front, false if the card is back.
     * @param resources    The resources of the card.
     * @param requirements The requirements to place the card.
     * @throws InvalidIdException If the ID is not between 41 and 80.
     */
    public GoldCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources, ArrayList<Resource> requirements) throws InvalidIdException {
        super(ID, points, reign, front, resources, requirements);
    }

}
