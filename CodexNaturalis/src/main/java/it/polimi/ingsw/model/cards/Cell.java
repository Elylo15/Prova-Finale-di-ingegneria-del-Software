package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;

public class Cell {

    private int row;
    private int column;
    private boolean available;
    private Resource resource;
    private PlaceableCard bottomCard;
    private PlaceableCard topCard;

    /**
     * Creates a new cell
     * @param row row coordinate of the matrix of cells
     * @param column column coordinate of the matrix of cells
     * @param bottomCard pointer to the card on the bottom
     * @param resource pointer to the card on the top
     */
    public Cell(int row, int column, PlaceableCard bottomCard, Resource resource)
    {
        this.row = row;
        this.column = column;
        this.resource = resource;
        this.bottomCard = bottomCard;
        this.topCard = null;
        this.available = !resource.equals(Resource.Blocked);

    }

    /**
     * Initialize the pointer to the card on top and update the stored resource
     * @param topCard pointer to the card on top
     * @param resource new stored resource
     */
    public void linkCard(PlaceableCard topCard, Resource resource)
    {
        this.topCard = topCard;
        this.resource = resource;
        this.available = false;
    }

    /**
     * Returns the stored resource
     * @return stored resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Determines if a cell is available to get a card on top
     * @return true if the pointer to the top card is null and if the stored resource is not "Blocked"
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Returns the row coordinate of the cell
     * @return row
     */
    public int getRow() { return row; }

    /**
     * Returns the column coordinate of the cell
     * @return column
     */
    public int getColumn() { return column; }

    /**
     * Return the reference to the card on bottom
     * @return pointer to the card on bottom
     */
    public PlaceableCard getBottomCard() {
        return bottomCard;
    }


    /**
     * Return the reference to the card on top
     * @return pointer to the card on top
     */
    public PlaceableCard getTopCard() {
        return topCard;
    }
}
