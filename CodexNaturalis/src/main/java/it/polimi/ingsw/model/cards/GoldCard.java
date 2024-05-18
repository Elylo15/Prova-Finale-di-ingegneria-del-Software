package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.io.Serializable;
import java.util.ArrayList;

public class GoldCard extends PlaceableCard implements Serializable {
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

    public GoldCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources, ArrayList<Resource> requirements) throws InvalidIdException {
        super(ID,points,reign,front,resources, requirements);
    }


}
