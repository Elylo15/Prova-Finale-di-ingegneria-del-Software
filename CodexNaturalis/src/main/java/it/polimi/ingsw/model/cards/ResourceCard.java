package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;

public class ResourceCard extends PlaceableCard{
    /**
     * Constructor
     *
     * @param ID, it must be >=1  and =<40
     * @throws InvalidIdException if the condition on ID is not met
     */
    public ResourceCard(int ID) throws InvalidIdException {
        super(ID);
        if(ID>=1 && ID <=40) {
            this.ID = ID;
        } else if (ID <1) {
            throw new InvalidIdException("ID is too small");
        }
        else if(ID >40){
            throw new InvalidIdException("ID is too big");
        }
    }


    public ResourceCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources) throws InvalidIdException {
        super(ID,points,reign,front,resources);
    }


    /**
     * check if the requirement to place the card is met, it always returns true as resource cards have no requirement
     * @param req it receives as parameter an ArrayList containing the number of resources of each type the player possesses in his PlayerArea
     * @return true
     */
    @Override
    public boolean checkRequirement(ArrayList<Integer> req) {
        return true;
    }




}