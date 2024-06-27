package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This abstract class represents a card that can be placed on the board.
 * It extends the Card class and implements the Serializable interface.
 * It provides constructors for creating a PlaceableCard with different parameters.
 */
public abstract class PlaceableCard extends Card implements Serializable {
    private final ArrayList<Resource> requirement;
    private int points;
    private Reign reign;
    private ArrayList<Cell> cells;
    private ArrayList<Resource> resources;

    /**
     * This constructor creates a new PlaceableCard with the given ID.
     * It throws an InvalidIdException if the ID is not between 1 and 86.
     *
     * @param ID must be 1 <= ID <= 86
     * @throws InvalidIdException if the condition on ID is not met
     */
    public PlaceableCard(int ID) throws InvalidIdException {
        super(ID, true);

        this.requirement = new ArrayList<>();
        this.cells = new ArrayList<>();
    }

    /**
     * This constructor is used to load the cards from JSON.
     * It creates a new PlaceableCard with the given parameters.
     *
     * @param ID          must be 1 <= ID <= 86
     * @param front       true if the card is front, false if the card is back
     * @param requirement resources required to place the card
     * @param points      points given by the card
     * @param reign       reign of the card
     * @param cells       cells of the card
     * @param resources   resources of the card
     */
    @JsonCreator
    public PlaceableCard(@JsonProperty("ID") int ID,
                         @JsonProperty("front") boolean front,
                         @JsonProperty("requirement") ArrayList<Resource> requirement,
                         @JsonProperty("points") int points,
                         @JsonProperty("reign") Reign reign,
                         @JsonProperty("cells") ArrayList<Cell> cells,
                         @JsonProperty("resources") ArrayList<Resource> resources) throws InvalidIdException {
        super(ID, front);
        this.requirement = requirement;
        this.points = points;
        this.reign = reign;
        this.cells = cells;
        this.resources = resources;
    }

    /**
     * This constructor creates a new PlaceableCard with the given parameters.
     * It throws an InvalidIdException if the ID is not between 1 and 86.
     *
     * @param ID          must be 1 <= ID <= 86
     * @param points      points given by the card
     * @param reign       reign of the card
     * @param front       true if the card is front, false if the card is back
     * @param resources   resources of the card
     * @param requirement resources required to place the card
     * @throws InvalidIdException if the condition on ID is not met
     */
    public PlaceableCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources, ArrayList<Resource> requirement) throws InvalidIdException {
        super(ID, front);
        this.points = points;
        this.reign = reign;
        this.resources = resources;
        this.requirement = requirement;
    }

    /**
     * Overrides the equals method from the Object class.
     * Compares two PlaceableCard objects to check for equality.
     *
     * @param o the object to be compared
     * @return true if the specified object is equal to this PlaceableCard, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceableCard that = (PlaceableCard) o;
        return points == that.points && this.isFront() == that.isFront() && Objects.equals(requirement, that.requirement) && reign == that.reign && Objects.equals(resources, that.resources);
    }

    /**
     * Overrides the hashCode method from the Object class.
     *
     * @return the hash code of the PlaceableCard object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(requirement, points, reign, cells, resources);
    }

    /**
     * Returns an ArrayList containing the resources the player must have to place the card
     *
     * @return an ArrayList<Resource>
     */
    public ArrayList<Resource> getRequirement() {
        return requirement;
    }

    /**
     * Check if the requirement to place the card is met, return true if the player possesses the
     * necessary resources in his PlayerArea
     * The requirement applies only if the player wants to play the front of the card
     *
     * @param req it receives as parameter an ArrayList containing the number of resources of each type
     *            the player possesses in his PlayerArea
     * @return a boolean
     */
    public boolean checkRequirement(ArrayList<Integer> req) {
        if (this.isFront()) {
            int playgroundFungus = req.get(0);
            int playgroundInsect = req.get(1);
            int playgroundAnimal = req.get(2);
            int playgroundPlant = req.get(3);
            int cardFungus = 0;
            int cardPlant = 0;
            int cardAnimal = 0;
            int cardInsect = 0;
            ArrayList<Resource> requirement = this.getRequirement();
            for (Resource resource : requirement) {
                if (resource == Resource.Fungus) {
                    cardFungus++;
                }
                if (resource == Resource.Plant) {
                    cardPlant++;
                }
                if (resource == Resource.Animal) {
                    cardAnimal++;
                }
                if (resource == Resource.Insect) {
                    cardInsect++;
                }
            }
            return cardFungus <= playgroundFungus && cardPlant <= playgroundPlant && cardAnimal <= playgroundAnimal && cardInsect <= playgroundInsect;
        } else {
            return true;
        }
    }

    /**
     * Returns the reign of the card.
     *
     * @return the reign of the card
     */
    public Reign getReign() {
        return reign;
    }

    /**
     * Returns the cells of the card.
     *
     * @return the cells of the card
     */
    public ArrayList<Cell> getCells() {
        return new ArrayList<>(cells);
    }

    /**
     * Given a list of cells, it updates the references to the new cells.
     *
     * @param cells ArrayList of Cells
     */
    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    /**
     * Returns the point associated with the card.
     *
     * @return the point associated with the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the resources of the card.
     *
     * @return the resources of the card
     */
    public ArrayList<Resource> getResource() {
        if (this.isFront()) {
            return new ArrayList<>(this.resources);
        } else {
            ArrayList<Resource> backResources = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                backResources.add(Resource.Empty);
            }
            return backResources;
        }
    }

    /**
     * Gold and Resource cards contain only one permanent resources in their back
     *
     * @return an Arraylist containing the permanent resource of the card
     */
    public ArrayList<Resource> getPermanentResource() {
        ArrayList<Resource> resources = new ArrayList<>();
        if (!this.isFront()) {

            if (this.reign == Reign.Fungus)
                resources.add(Resource.Fungus);

            if (this.reign == Reign.Plant)
                resources.add(Resource.Plant);

            if (this.reign == Reign.Animal)
                resources.add(Resource.Animal);

            if (this.reign == Reign.Insect)
                resources.add(Resource.Insect);
        }

        return resources;
    }

    /**
     * Returns true if the card is a resource card.
     *
     * @return true if the card is a resource card
     */
    public boolean isResource() {
        return this.ID >= 1 && this.ID <= 40;
    }

    /**
     * Returns true if the card is a gold card.
     *
     * @return true if the card is a gold card
     */
    public boolean isGold() {
        return this.ID >= 41 && this.ID <= 80;

    }

    /**
     * Returns true if the card is a starter card.
     *
     * @return true if the card is a starter card
     */
    public boolean isStarter() {
        return this.ID >= 81 && this.ID <= 86;
    }

    /**
     * Overrides the toString method from the Object class.
     *
     * @return a string representation of the PlaceableCard object.
     */
    @Override
    public String toString() {
        return "PlaceableCard{" +
                "ID=" + ID +
                ", requirement=" + requirement +
                ", points=" + points +
                ", reign=" + reign +
                ", front=" + this.isFront() +
                ", resources=" + resources +
                '}';
    }

}
