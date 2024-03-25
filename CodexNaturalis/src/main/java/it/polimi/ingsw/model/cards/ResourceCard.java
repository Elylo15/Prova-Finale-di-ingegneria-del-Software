package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;

public class ResourceCard extends PlaceableCard{
    /**
     * Constructor
     *
     * @param ID, it must be >=1  and =<41
     * @throws InvalidIdException if the condition on ID is not met
     */
    public ResourceCard(int ID) throws InvalidIdException {
        if(ID >0 && ID <41) {
            this.ID = ID;
        } else if (ID <=0) {
            throw new InvalidIdException("ID is too small");
        }
        else if(ID >=41){
            throw new InvalidIdException("ID is too big");
        }
    }
    /**
     * returns an ArrayList containing the resources you must have to place the card,
     * it will always be empty as resource cards have no requirement
     * @return an empty ArrayList<Resource>
     */
    @Override
    public ArrayList<Resource> getRequirement() {
        ArrayList<Resource> temporary = new ArrayList<Resource>();
        return temporary;
    }

    /**
     * check if the requirement to place the card is met, it always returns true as resource cards have no requirement
     * @param req
     * @return true
     */
    @Override
    public boolean checkRequirement(ArrayList<Integer> req) {
        return true;
    }

    /**
     *
     * @return the points associated with the card
     */
    @Override
    public int getPoints() {
        if(this.ID >= 8 && this.ID <=10 || this.ID >=18 && this.ID <= 20 || this.ID >= 28 && this.ID <=30 || this.ID >=38 &&this.ID <=40){
            return 1;
        }
        else {
            return 0;
        }

    }

    /**
     * Resource cards contains only one permanent resource
     * @return an Arraylist containing the permanent resource in the back of the card
     */
    @Override
    public ArrayList<Resource> getPermanentResource() {
        ArrayList<Resource> temporary = new ArrayList<Resource>();
        if(this.ID >=1 && this.ID <= 10){
            temporary.add(Resource.Fungus);
        }
        if(this.ID >=11 && this.ID <= 20){
            temporary.add(Resource.Plant);
        }
        if(this.ID >=21 && this.ID <= 30){
            temporary.add(Resource.Animal);
        }
        if(this.ID >=31 && this.ID <=40){
            temporary.add(Resource.Insect);
        }
        return temporary;

    }
}