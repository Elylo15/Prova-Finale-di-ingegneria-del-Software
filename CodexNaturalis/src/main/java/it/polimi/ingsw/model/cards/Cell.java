package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class Cell: represents a cell of the matrix of cells of the player area
 */
public class Cell implements Serializable {
    private final int row;
    private final int column;

    private final PlaceableCard bottomCard;
    private PlaceableCard topCard;

    /**
     * Creates a new cell
     * @param row row coordinate of the matrix of cells
     * @param column column coordinate of the matrix of cells
     * @param bottomCard pointer to the card on the bottom
     */
    public Cell(int row, int column, PlaceableCard bottomCard) {
        this.row = row;
        this.column = column;
        this.bottomCard = bottomCard;
        this.topCard = null;
    }

    /**
     * Overrides the equals method from the Object class.
     * Compares two Cell objects to check for equality.
     * @param o the object to be compared
     * @return true if the specified object is equal to this Cell, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && column == cell.column && Objects.equals(bottomCard, cell.bottomCard) && Objects.equals(topCard, cell.topCard);
    }

    /**
     * Initialize the pointer to the card on top and update the stored resource
     * @param topCard pointer to the card on top
     */
    public void linkCard(PlaceableCard topCard)
    {
        this.topCard = topCard;
    }

    /**
     * Returns the resource of the existing card on top
     * @return resource
     */
    public Resource getResource() {

        PlaceableCard referenceCard;
        if(this.topCard == null)
            referenceCard = this.bottomCard;
        else
            referenceCard = this.topCard;

        for(int i = 0; i<referenceCard.getCells().size(); i++) {
            if(referenceCard.getCells().get(i) != null && referenceCard.getCells().get(i).equals(this))
                return referenceCard.getResource().get(i);
        }
        return null;
    }

    /**
     * Determines if a cell is available to get a card on top
     * @return true if the pointer to the top card is null and if the stored resource is not "Blocked"
     */
    public boolean isAvailable() {
        return this.topCard == null && this.getResource() != Resource.Blocked;
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
