package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Cell;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerArea implements Serializable {

    private final HashMap<ArrayList<Integer>, Cell> CellMatrix;
    private final ArrayList<Integer> permanentResource;

    /**
     * Creates an empty PlayerArea
     */
    public PlayerArea() {
        this.CellMatrix = new HashMap<>();
        this.permanentResource = new ArrayList<>();
        this.permanentResource.add(0); // Counter for FUNGUS
        this.permanentResource.add(0); // Counter for INSECT
        this.permanentResource.add(0); // Counter for ANIMAL
        this.permanentResource.add(0); // Counter for PLANT
        this.permanentResource.add(0); // Counter for MANUSCRIPT
        this.permanentResource.add(0); // Counter for QUILL
        this.permanentResource.add(0); // Counter for INKWELL
        this.permanentResource.add(0); // Counter for EMPTY
        this.permanentResource.add(0); // Counter for BLOCKED
    }

    /**
     * Return if a cell exists at the given coordinates
     * @param x Row coordinate
     * @param y Column coordinate
     * @return True if exists a cell with the given coordinates.
     */
    public boolean contains(int x, int y) {
        ArrayList<Integer> coordinates = new ArrayList<>();
        coordinates.add(x);
        coordinates.add(y);
        return CellMatrix.containsKey(coordinates);
    }

    /**
     * Return a cell given its coordinates
     * @param x Row coordinate
     * @param y Column coordinate
     * @return A reference to the Cell, if the cell exists; null otherwise
     */
    private Cell getCell(int x, int y) {
        ArrayList<Integer> coordinates = new ArrayList<>();
        coordinates.add(x);
        coordinates.add(y);
        return CellMatrix.get(coordinates);
    }

    /**
     * Add a cell to the PlayerArea
     * @param cell Reference to the cell to add
     */
    public void addCell(Cell cell) {
        ArrayList<Integer> coordinates = new ArrayList<>();
        coordinates.add(cell.getRow());
        coordinates.add(cell.getColumn());
        CellMatrix.put(coordinates, cell);
    }

    /**
     * Returns a list of all existing cells
     * @return List of all cell
     */
    private ArrayList<Cell> cellList() {
        return new ArrayList<>(CellMatrix.values());
    }

    /**
     * Increase of 1 the stored counter of the given resource
     * @param resource Resource to increase
     */
    private void addPermanentResource(Resource resource) {
        int selector = switch (resource) {
            case Resource.Fungus -> 0;
            case Resource.Insect -> 1;
            case Resource.Animal -> 2;
            case Resource.Plant -> 3;
            case Resource.Manuscript -> 4;
            case Resource.Quill -> 5;
            case Resource.Inkwell -> 6;
            case Resource.Empty -> 7;
            case Resource.Blocked -> 8;
        };
        this.permanentResource.set(selector, this.permanentResource.get(selector) + 1);
    }

    /**
     * Counts all resources, permanent and not
     * @return A list of 8 counters:
     * 0. Counter for FUNGUS
     * 1. Counter for INSECT
     * 2. Counter for ANIMAL
     * 3. Counter for PLANT
     * 4. Counter for MANUSCRIPT
     * 5. Counter for QUILL
     * 6. Counter for INKWELL
     * 7. Counter for EMPTY
     * 8. Counter for BLOCKED
     */
    public ArrayList<Integer> getResources() {
        ArrayList<Integer> resourceList = new ArrayList<>(permanentResource);
        CellMatrix.values().forEach((x)-> addResourceToList(resourceList, x.getResource()));
        return resourceList;
    }

    /**
     * Used to count resources given the list of counters
     * @param list ArrayList of counters
     * @param resource Resource of which increase counter
     */
    private void addResourceToList(ArrayList<Integer> list, Resource resource)
    {

        int selector = switch (resource) {
            case Resource.Fungus -> 0;
            case Resource.Insect -> 1;
            case Resource.Animal -> 2;
            case Resource.Plant -> 3;
            case Resource.Manuscript -> 4;
            case Resource.Quill -> 5;
            case Resource.Inkwell -> 6;
            case Resource.Empty -> 7;
            case Resource.Blocked -> 8;
        };
        list.set(selector, list.get(selector) + 1);
    }

    /**
     * Returns a list of coordinates of available position.
     * A position is made of a square of four cells, and it is represented by the coordinates of its top-left cell
     * @return A list of positions where a card could be placed
     */
    public ArrayList<Integer[]> getAvailablePosition() {
        /*
        For each available cell of coordinates x,y, there are the following positions to check:
        0. top-left (x-1, y-1)
        1. top-right (x-1, y)
        2. bottom-left (x, y-1)
        3. bottom-right (x, y)
         */
        ArrayList<Integer[]> positions = new ArrayList<>();
        CellMatrix.values().stream()
                .filter(Cell::isAvailable)
                .forEach( cell -> {
                    Integer [] pos0 = {cell.getRow() - 1, cell.getColumn() -1};
                    Integer [] pos1 = {cell.getRow() - 1, cell.getColumn()};
                    Integer [] pos2 = {cell.getRow() , cell.getColumn() -1};
                    Integer [] pos3 = {cell.getRow(), cell.getColumn()};

                    if(this.checkPosition(pos0[0], pos0[1]))
                        positions.add(pos0);

                    if(this.checkPosition(pos1[0], pos1[1]))
                        positions.add(pos1);

                    if(this.checkPosition(pos2[0], pos2[1]))
                        positions.add(pos2);

                    if(this.checkPosition(pos3[0], pos3[1]))
                        positions.add(pos3);
                });

        return positions;


    }

    /**
     * Checks if a position is available to place a card
     * @param x Row coordinate
     * @param y Column coordinate
     * @return True if at the given position a card can be placed
     */
    public boolean checkPosition(int x, int y) {
        //Basic check: if a position is already taken
        if(this.getCard(x,y) != null)
            return false;

        /*
          The cells to check are:
          (x  , y  )
          (x  , y+1)
          (x+1, y  )
          (x+1, y+1)
         */
        //Obtains the existing cells
        ArrayList<Cell> position = new ArrayList<>();

        if(this.contains(x, y))
            position.add(getCell(x,y));

        if(this.contains(x, y+1))
            position.add(getCell(x,y+1));

        if(this.contains(x+1, y))
            position.add(getCell(x+1,y));

        if(this.contains(x+1, y+1))
            position.add(getCell(x+1,y+1));

        //check if there are existing cells
        if(position.isEmpty())
            return false;

        //checks if all cells are available
        if (position.stream().filter(Cell::isAvailable).count() < position.size())
            return false;

        //checks if some cells share the same bottom card
        if(position.stream().map(Cell::getBottomCard).distinct().count() < position.size())
            return false;


        //checks if blocked cells exists
        return position.stream().noneMatch(a -> a.getResource() == Resource.Blocked);

    }

    /**
     * Returns the card given the coordinates of its position
     * @param x Row coordinate
     * @param y Column coordinate
     * @return Reference to the placed card or null if there is no card at the given position
     */
    private PlaceableCard getCard(int x, int y) {
        /*
          There are two candidates for the return: the topCard and the bottomCard
          in the cell at coordinates (x,y)
         */
        Cell cell = getCell(x,y);
        if (cell == null)
            return null;

        PlaceableCard topCard = cell.getTopCard();
        PlaceableCard bottomCard = cell.getBottomCard();

        if(topCard != null
                && topCard.getCells().getFirst().getRow() == cell.getRow()
                && topCard.getCells().getFirst().getColumn() == cell.getColumn())
            return topCard;


        if(bottomCard != null
                && bottomCard.getCells().getFirst().getRow() == cell.getRow()
                && bottomCard.getCells().getFirst().getColumn() == cell.getColumn())
            return bottomCard;

        return null;
    }

    /**
     * Places the starter card
     * @param card StarterCard
     * @param front Side of the starter card
     */
    public void placeStarterCard(PlaceableCard card, boolean front)
    {
        card.setFront(front);
        card.getPermanentResource().forEach(this::addPermanentResource);
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0,0,card));
        cells.add(new Cell(0,1,card));
        cells.add(new Cell(1,0,card));
        cells.add(new Cell(1,1,card));
        card.setCells(cells);
        cells.forEach(this::addCell);
    }

    /**
     * Places a card at the given position
     * @param card Reference to the card to place
     * @param x Row coordinate
     * @param y Column coordinate
     * @return Points given by the placement of the card
     * @throws noPlaceCardException If in the given position a card cannot be placed
     */
    public int placeCard(PlaceableCard card, int x, int y, boolean front) throws noPlaceCardException
    {
        // checks if the position is available
        if (!checkPosition(x, y))
            throw new noPlaceCardException();


        int points = 0;

        /*
          The cells to check are:
          (x  , y  )
          (x  , y+1)
          (x+1, y  )
          (x+1, y+1)
         */
        //Obtain the existing cells
        ArrayList<Cell> position = new ArrayList<>();
        position.add(getCell(x,y));
        position.add(getCell(x,y+1));
        position.add(getCell(x+1,y));
        position.add(getCell(x+1,y+1));

        //setting front or back
        card.setFront(front);

        // obtaining points
        if(front) {
            if(!card.checkRequirement(this.getResources()))
                throw new noPlaceCardException();
            else {
                ArrayList<Integer> pointsOnResource = new ArrayList<>();
                ArrayList<Integer> pointsOnCorner = new ArrayList<>();
                //FUNGUS
                pointsOnResource.add(41);
                pointsOnResource.add(42);
                pointsOnResource.add(43);
                pointsOnCorner.add(44);
                pointsOnCorner.add(45);
                pointsOnCorner.add(46);

                //PLANT
                pointsOnResource.add(51);
                pointsOnResource.add(52);
                pointsOnResource.add(53);
                pointsOnCorner.add(54);
                pointsOnCorner.add(45);
                pointsOnCorner.add(56);

                //ANIMAL
                pointsOnResource.add(61);
                pointsOnResource.add(62);
                pointsOnResource.add(63);
                pointsOnCorner.add(64);
                pointsOnCorner.add(65);
                pointsOnCorner.add(66);

                //INSECT
                pointsOnResource.add(71);
                pointsOnResource.add(72);
                pointsOnResource.add(73);
                pointsOnCorner.add(74);
                pointsOnCorner.add(75);
                pointsOnCorner.add(76);


                if(pointsOnResource.contains(card.getID())) {
                    //ArrayList<Integer> resource = this.getResources();

                    // Quill
                    if (card.getID() == 41 || card.getID() == 51 || card.getID() == 63 || card.getID() == 71) {
                        points = (int) (1 + CellMatrix.values().stream()
                                .filter(cell -> !position.contains(cell))
                                .filter(cell -> cell.getResource() == Resource.Quill)
                                .count());
                    }

                    // Inkwell
                    if(card.getID() == 42 || card.getID() == 53 || card.getID() == 61 || card.getID() == 73) {
                        points = (int) (1 + CellMatrix.values().stream()
                                .filter(cell -> !position.contains(cell))
                                .filter(cell -> cell.getResource() == Resource.Inkwell)
                                .count());
                    }

                    // Manuscript
                    if(card.getID() == 41 || card.getID() == 52 || card.getID() == 62 || card.getID() == 72) {
                        points = (int) (1 + CellMatrix.values().stream()
                                .filter(cell -> !position.contains(cell))
                                .filter(cell -> cell.getResource() == Resource.Manuscript)
                                .count());
                    }


                } else if (pointsOnCorner.contains(card.getID())) {
                        points = (int) (2 * position.stream()
                                .filter(Objects::nonNull)
                                .count());
                } else {
                    points = card.getPoints();
                }
            }
        }



        //Setting up the new positions
        for (int i=0; i < position.size(); i++) {
            if(position.get(i) != null)
            {
                position.get(i).linkCard(card);

            } else {
                int ro = 0;
                int co = switch (i) {
                    case 0 -> {
                        ro = x;
                        yield y;
                    }
                    case 1 -> {
                        ro = x;
                        yield y + 1;
                    }
                    case 2 -> {
                        ro = x + 1;
                        yield y;
                    }
                    case 3 -> {
                        ro = x + 1;
                        yield y + 1;
                    }
                    default -> y;
                };
                Cell tmpCell = new Cell(ro, co, card);
                this.addCell(tmpCell);
                position.set(i, tmpCell);
            }
        }
        card.setCells(position);

        // Eventually adds the permanent resources
        card.getPermanentResource().forEach(this::addPermanentResource);

        return points;
    }

    /**
     * Returns a sorted list of all cards: first the card on top and on the left
     * @return Ordered list of all cards
     */
    public ArrayList<PlaceableCard> getAllCards() {
        return CellMatrix.values().stream()
                .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing((PlaceableCard card) ->  card.getCells().getFirst().getRow())
                        .thenComparing(card -> card.getCells().getFirst().getColumn()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Check if there are pattern of the given ID
     * @param card Reference to the objective to identify
     * @return Number of points generated by the patterns
     */
    public int checkPattern(ObjectiveCard card) {
        HashSet<PlaceableCard> checkedCards = new HashSet<>();
        ArrayList<PlaceableCard> allCards;
        int points = 0;

        if(card == null)
            return 0;

        if(card.getPattern() != null && !card.getPattern().isEmpty()) {
            allCards = getAllCards();
            ArrayList<Integer[]> pattern = card.getPattern();
            ArrayList<Reign> reigns = card.getReignCards();
            for(PlaceableCard card0 : allCards) {
                if(!checkedCards.contains(card0) && card0.getReign() != null && card0.getReign().equals(reigns.getFirst())) {
                    int row = card0.getCells().getFirst().getRow();
                    int column = card0.getCells().getFirst().getColumn();
                    PlaceableCard card1 = this.getCard(row + pattern.get(0)[0], column + pattern.get(0)[1]);
                    PlaceableCard card2 = this.getCard(row + pattern.get(1)[0], column + pattern.get(1)[1]);

                    if( card1 != null && card2 != null
                            && card1.getReign() != null && card2.getReign() != null
                            && card1.getReign() == reigns.get(1) && card2.getReign() == reigns.get(2)
                            && !checkedCards.contains(card1) && !checkedCards.contains(card2)) {
                        points = points + card.getPoints();
                        checkedCards.add(card0);
                        checkedCards.add(card1);
                        checkedCards.add(card2);
                    }

                }
            }
            return points;
        }

        if(card.getRequirements() != null && !card.getRequirements().isEmpty()) {
            ArrayList<Resource> requirements = card.getRequirements();
            ArrayList<Integer> resources = this.getResources();

            if(requirements.stream().distinct().count() == 1)
            {
                int index = switch (requirements.getFirst()) {
                    case Fungus -> 0;
                    case Insect -> 1;
                    case Animal -> 2;
                    case Plant -> 3;
                    case Manuscript -> 4;
                    case Quill -> 5;
                    case Inkwell -> 6;
                    case Empty -> 7;
                    case Blocked -> 8;
                };

                if(index < 4)
                    points = card.getPoints() * (resources.get(index) / 3);
                else
                    points = card.getPoints() * (resources.get(index) / 2);
                return points;
            }

            if(card.getID() == 99) {
                if(resources.get(4) < resources.get(5)) {
                    if(resources.get(4) < resources.get(6))
                        return 3 * resources.get(4);
                    else
                        return 3 * resources.get(6);
                }
                else {
                    if(resources.get(5) < resources.get(6))
                        return 3 * resources.get(5);
                    else
                        return 3 * resources.get(6);
                }
            }

            return points;
        }
        return 0;
    }

    /**
     * Counts the number of times a pattern is achieved
     * @param card Identifies the pattern to search for
     * @return number of times a pattern is encountered
     */
    public int countPattern(ObjectiveCard card) {
        return this.checkPattern(card) / card.getPoints();
    }


}




