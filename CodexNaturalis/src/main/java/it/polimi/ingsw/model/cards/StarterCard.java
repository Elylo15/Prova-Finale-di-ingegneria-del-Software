package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class StarterCard extends PlaceableCard implements Serializable {
    private ArrayList<Resource> permanentResource;
    private ArrayList<Resource> bottomResource;

    /**
     * Constructor
     *
     * @param ID, it must be >=81 and <=86
     * @throws InvalidIdException if the condition on ID is not met
     */
    public StarterCard(int ID) throws InvalidIdException {
        super(ID);
        if (ID >= 81 && ID <= 86) {
            this.ID = ID;
        } else if (ID < 81) {
            throw new InvalidIdException("ID is too small");
        } else {
            throw new InvalidIdException("ID is too big");
        }
    }

    /**
     * Constructor used to import from JSON
     *
     * @param ID,                it must be >=1  and =<40
     * @param front,             true if the card is front, false if the card is back
     * @param requirement,       resources required to place the card
     * @param points,            points given by the card
     * @param reign,             reign of the card
     * @param cells,             cells of the card
     * @param resources,         resources given by the card
     * @param permanentResource, resources given by the card
     * @param bottomResource,    resources given by the card
     * @throws InvalidIdException if the condition on ID is not met
     */
    @JsonCreator
    public StarterCard(@JsonProperty("ID") int ID,
                       @JsonProperty("front") boolean front,
                       @JsonProperty("requirement") ArrayList<Resource> requirement,
                       @JsonProperty("points") int points,
                       @JsonProperty("reign") Reign reign,
                       @JsonProperty("cells") ArrayList<Cell> cells,
                       @JsonProperty("resources") ArrayList<Resource> resources,
                       @JsonProperty("permanentResource") ArrayList<Resource> permanentResource,
                       @JsonProperty("bottomResource") ArrayList<Resource> bottomResource) throws InvalidIdException {
        super(ID, front, requirement, points, reign, cells, resources);
        this.permanentResource = permanentResource;
        this.bottomResource = bottomResource;
    }

    /**
     * Constructor
     *
     * @param ID,                it must be >=1  and =<40
     * @param front,             true if the card is front, false if the card is back
     * @param points,            points given by the card
     * @param reign,             reign of the card
     * @param resources,         resources given by the card
     * @param permanentResource, resources given by the card
     * @param bottomResource,    resources given by the card
     * @throws InvalidIdException if the condition on ID is not met
     */
    public StarterCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources, ArrayList<Resource> permanentResource, ArrayList<Resource> bottomResource) throws InvalidIdException {
        super(ID, points, reign, front, resources, null);
        this.permanentResource = permanentResource;
        this.bottomResource = bottomResource;
    }

    /**
     * Overrides the equals method from the Object class.
     * Compares two PlaceableCard objects to check for equality.
     *
     * @param o the object to be compared
     * @return true if the specified objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StarterCard that = (StarterCard) o;
        return Objects.equals(permanentResource, that.permanentResource) && Objects.equals(bottomResource, that.bottomResource);
    }

    /**
     * Overrides the hashCode method from the Object class.
     *
     * @return the hash of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), permanentResource, bottomResource);
    }

    /**
     * @return the resources in the front of the card if front is true, the resources in the back of the card
     * if front is false
     */
    @Override
    public ArrayList<Resource> getResource() {
        if (this.isFront()) {
            return super.getResource();
        } else {
            return new ArrayList<>(this.bottomResource);
        }
    }

    /**
     * @return the permanent resources
     */
    @Override
    public ArrayList<Resource> getPermanentResource() {
        if (this.isFront()) {
            return permanentResource;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * @param req ArrayList containing the number of resources
     * @return true
     */
    @Override
    public boolean checkRequirement(ArrayList<Integer> req) {
        return true;
    }

}
