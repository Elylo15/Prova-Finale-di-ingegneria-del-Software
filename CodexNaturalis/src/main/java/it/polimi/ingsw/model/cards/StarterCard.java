package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;

import java.util.ArrayList;

public class StarterCard extends Card{
    /**
     * Constructor
     * @param ID
     */
    public StarterCard(int ID){
        this.ID = ID;
    }

    /**
     *
     * @return an ArrayList with the permanent resources based on the ID of the card
     */
    public ArrayList<Resource> getPermanentResources(){

    }
}
