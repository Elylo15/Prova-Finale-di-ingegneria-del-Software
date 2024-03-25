package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;

public class StarterCard extends PlaceableCard{
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

    /**
     *
     * @return an ArrayList with the permanent resources based on the ID of the card
     */
    @Override
    public ArrayList<Resource> getPermanentResource() {
        ArrayList<Resource> resources = new ArrayList<Resource>();
        if(ID==81){
            resources.add(Resource.Insect);
        }
        else if(ID==82) {
             resources.add(Resource.Fungus);
        }
        else if(ID==83) {
             resources.add(Resource.Plant);
             resources.add(Resource.Fungus);
        }
        else if(ID==84) {
             resources.add(Resource.Animal);
             resources.add(Resource.Insect);
        }
        else if(ID==85) {
              resources.add(Resource.Animal);
              resources.add(Resource.Insect);
              resources.add(Resource.Plant);
        }
        else if(ID==86) {
              resources.add(Resource.Plant);
              resources.add(Resource.Animal);
              resources.add(Resource.Fungus);
        }
        return resources;
    }
}
