package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;

public class GoldCard extends PlaceableCard{
    /**
     * Constructor
     *
     * @param ID, it must be >=41  and =<80
     * @throws InvalidIdException if the condition on ID is not met
     */
    public GoldCard(int ID) throws InvalidIdException
    {
        super(ID);
        if(ID>=41 && ID <=80) {
            this.ID = ID;
        } else if (ID <40) {
            throw new InvalidIdException("ID is too small");
        }
        else if(ID >80){
            throw new InvalidIdException("ID is too big");
        }
    }


}
