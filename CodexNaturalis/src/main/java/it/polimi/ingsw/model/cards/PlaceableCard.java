package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;
import java.util.Objects;

public abstract class PlaceableCard extends Card{

    private ArrayList<Resource> requirement;
    private int points;
    private Reign reign;
    private boolean front;
    private ArrayList<Cell> cells;

    private ArrayList<Resource> resources;

    public PlaceableCard(int ID) throws InvalidIdException
    {
        super(ID);

        this.front = true;
        this.requirement = new ArrayList<>();
        this.cells = new ArrayList<>();

    }

    public PlaceableCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources, ArrayList<Resource> requirement) throws InvalidIdException
    {
        super(ID);
        this.points = points;
        this.reign = reign;
        this.front = front;
        this.resources = resources;
        this.requirement = requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceableCard that = (PlaceableCard) o;
        return points == that.points && front == that.front && Objects.equals(requirement, that.requirement) && reign == that.reign && Objects.equals(cells, that.cells) && Objects.equals(resources, that.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirement, points, reign, front, cells, resources);
    }

    /**
     * returns an ArrayList containing the resources you must have to place the card,
     * @return an ArrayList<Resource>
     */
    public ArrayList<Resource> getRequirement() {
        return requirement;
    }

    /**
     * check if the requirement to place the card is met, return true if the player possesses the
     * necessary resources in his PlayerArea
     * the requirement applies only if the player wants to play the front of the card
     * @param req it receives as parameter an ArrayList containing the number of resources of each type
     * the player possesses in his PlayerArea
     * @return a boolean
     */
    public boolean checkRequirement(ArrayList<Integer> req) {
        if(front) {
            int playgroundFungus = req.get(0);
            int playgroundPlant = req.get(1);
            int playgroundAnimal = req.get(2);
            int playgroundInsect = req.get(3);
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
            if (cardFungus <= playgroundFungus && cardPlant <= playgroundPlant && cardAnimal <= playgroundAnimal && cardInsect <= playgroundInsect) {
                return true;
            } else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    /**
     * Given a list of cells, it updates the references to the new cells.
     * @param cells ArryList of Cells
     */
    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    public Reign getReign() {
        return reign;
    }

    public boolean isFront() {
        return front;
    }

    public ArrayList<Cell> getCells() {
        return new ArrayList<>(cells);
    }

    /**
     *
     * @return the point associated with the card
     */
    public int getPoints() {
        return points;
    }

    public ArrayList<Resource> getResource() {
        if(this.front) {
            return new ArrayList<>(this.resources);
        } else {
            ArrayList<Resource> backResources = new ArrayList<>();
            backResources.add(Resource.Empty);
            backResources.add(Resource.Empty);
            backResources.add(Resource.Empty);
            backResources.add(Resource.Empty);
            return backResources;
        }
    }
    /**
     * Gold and Resource cards contain only one permanent resources in their back
     * Starter cards contain from one to three permanent resources in their front
     * @return an Arraylist containing the permanent resources based on the ID of the card
     */
    public ArrayList<Resource> getPermanentResource(){
        ArrayList<Resource> resources = new ArrayList<>();
        if(!front) {

            if(this.reign == Reign.Fungus)
                resources.add(Resource.Fungus);

            if(this.reign == Reign.Plant)
                resources.add(Resource.Plant);

            if(this.reign == Reign.Animal)
                resources.add(Resource.Animal);

            if(this.reign == Reign.Insect)
                resources.add(Resource.Insect);
        }

        return resources;
    }

    public boolean isResource() {
        if(this.ID >= 1 && this.ID <=40){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isGold() {
        if(this.ID>=41 && this.ID <=80){
            return true;
        }
        else {
            return false;
        }

    }

    public boolean isStarter() {
        if(this.ID>=81 && this.ID <=86){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "PlaceableCard{" +
                "ID=" + ID +
                ", requirement=" + requirement +
                ", points=" + points +
                ", reign=" + reign +
                ", front=" + front +
                ", resources=" + resources +
                '}';
    }
}
