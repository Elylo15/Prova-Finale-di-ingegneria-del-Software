package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;

public class StarterCard extends PlaceableCard{
    private ArrayList<Resource> permanentResource;
    private ArrayList<Resource> bottomResource;
    /**
     * Constructor
     * @param ID, it must be >=81 and <=86
     * @throws InvalidIdException if the condition on ID is not met
     */
    public StarterCard(int ID) throws InvalidIdException {
        if(ID>=81 && ID <=86){
            this.ID = ID;
        }
        else if(ID<81){
            throw new InvalidIdException("ID is too small");
        } else if (ID>86) {
            throw new InvalidIdException("ID is too big");
                    }
    }


}
