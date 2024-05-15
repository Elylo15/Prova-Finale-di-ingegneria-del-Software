package it.polimi.ingsw.model.cards;

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
     * @param ID, it must be >=81 and <=86
     * @throws InvalidIdException if the condition on ID is not met
     */
    public StarterCard(int ID) throws InvalidIdException {
        super(ID);
        if(ID>=81 && ID <=86){
            this.ID = ID;
        }
        else if(ID<81){
            throw new InvalidIdException("ID is too small");
        } else {
            throw new InvalidIdException("ID is too big");
        }
    }

    public StarterCard(int ID, int points, Reign reign, boolean front, ArrayList<Resource> resources, ArrayList<Resource> permanentResource, ArrayList<Resource> bottomResource) throws InvalidIdException {
        super(ID,points,reign,front,resources,null);
        this.permanentResource = permanentResource;
        this.bottomResource = bottomResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StarterCard that = (StarterCard) o;
        return Objects.equals(permanentResource, that.permanentResource) && Objects.equals(bottomResource, that.bottomResource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), permanentResource, bottomResource);
    }

    /**
     *
     * @return the resources in the front of the card if front is true, the resources in the back of the card
     * if front is false
     */
    @Override
    public ArrayList<Resource> getResource() {
        if(this.isFront()) {
            return super.getResource();
        } else {
            return new ArrayList<>(this.bottomResource);
        }
    }

    @Override
    public ArrayList<Resource> getPermanentResource() {
        if(this.isFront()) {
            return permanentResource;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean checkRequirement(ArrayList<Integer> req) {
        return true;
    }


}
