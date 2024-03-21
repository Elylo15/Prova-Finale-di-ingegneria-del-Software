package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;

public class Cell {

    private int row;
    private int column;
    private boolean available;
    private Resource resource;
    private Card bottomCard;
    private Card topCard;

    public Cell(int row, int column, Card bottomCard, Resource resource)
    {
        this.row = row;
        this.column = column;
        this.resource = resource;
        this.bottomCard = bottomCard;
        this.topCard = null;
        if(resource.equals(Resource.Blocked))
            this.available = false;
        else
            this.available = true;

    }


    public void linkCard(Card topCard, Resource resource)
    {
        this.topCard = topCard;
        this.resource = resource;
        this.available = false;
    }

    public Resource getResource() {
        return resource;
    }

    public boolean isAvailable() {
        return available;
    }
}
