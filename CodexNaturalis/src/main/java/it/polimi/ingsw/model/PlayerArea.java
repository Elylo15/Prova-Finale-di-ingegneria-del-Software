package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Cell;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerArea {

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
    public Cell getCell(int x, int y) {
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
    public ArrayList<Cell> cellList() {
        return new ArrayList<>(CellMatrix.values());
    }

    /**
     * Increase of 1 the stored counter of the given resource
     * @param resource Resource to increase
     */
    public void addPermanentResource(Resource resource) {
        int selector = -1;
        switch (resource) {
            case Resource.Fungus:
                selector = 0;
                break;
            case Resource.Insect:
                selector = 1;
                break;
            case Resource.Animal:
                selector = 2;
                break;
            case Resource.Plant:
                selector = 3;
                break;
            case Resource.Manuscript:
                selector = 4;
                break;
            case Resource.Quill:
                selector = 5;
                break;
            case Resource.Inkwell:
                selector = 6;
                break;
            case Resource.Empty:
                selector = 7;
                break;
            case Resource.Blocked:
                selector = 8;
                break;
        }
        if (selector != -1)
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
        ArrayList<Integer> resourceList = (ArrayList<Integer>) this.permanentResource.clone();
        resourceList.add(0); // Counter for FUNGUS
        resourceList.add(0); // Counter for INSECT
        resourceList.add(0); // Counter for ANIMAL
        resourceList.add(0); // Counter for PLANT
        resourceList.add(0); // Counter for MANUSCRIPT
        resourceList.add(0); // Counter for QUILL
        resourceList.add(0); // Counter for INKWELL
        resourceList.add(0); // Counter for EMPTY
        resourceList.add(0); // Counter for BLOCKED

        CellMatrix.values().forEach((x)->{addResourceToList(resourceList, x.getResource());});

        return resourceList;
    }

    /**
     * Used to count resources given the list of counters
     * @param list ArrayList of counters
     * @param resource Resource of which increase counter
     */
    private void addResourceToList(ArrayList<Integer> list, Resource resource)
    {

        int selector = -1;
        switch (resource) {
            case Resource.Fungus:
                selector = 0;
                break;
            case Resource.Insect:
                selector = 1;
                break;
            case Resource.Animal:
                selector = 2;
                break;
            case Resource.Plant:
                selector = 3;
                break;
            case Resource.Manuscript:
                selector = 4;
                break;
            case Resource.Quill:
                selector = 5;
                break;
            case Resource.Inkwell:
                selector = 6;
                break;
            case Resource.Empty:
                selector = 7;
                break;
            case Resource.Blocked:
                selector = 8;
                break;
        }
        if (selector != -1)
            list.set(selector, list.get(selector) + 1);
    }

    /**
     * Returns a list of coordinates of available position.
     * A position is made of a square of four cells and it is represented by the coordinates of its top-left cell
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
        /*
          The cells to check are:
          (x  , y  )
          (x  , y+1)
          (x+1, y  )
          (x+1, y+1)
         */
        //Obtain the existing cells
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

        //checks if cells share the same bottom card
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
    public PlaceableCard getCard(int x, int y) {
        /*
          The cells to check are:
          (x  , y  )
          (x  , y+1)
          (x+1, y  )
          (x+1, y+1)
         */
        //Obtain the existing cells
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
            return null;

        Set<PlaceableCard> commonCards = new HashSet<>();

        return position.stream()
                .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                .filter(card -> card != null || !commonCards.add(card)) // to add a duplicate card in the set returns false
                .findFirst()
                .orElse(null);
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
                    ArrayList<Integer> resource = this.getResources();

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

        for (int i=0; i < position.size(); i++) {
            if(position.get(i) != null)
            {
                position.get(i).linkCard(card,card.getResource().get(i));

            } else {
                int ro = 0;
                int co = 0;
                switch (i) {
                    case 0:
                        ro = x;
                        co = y;
                        break;
                    case 1:
                        ro = x;
                        co = y+1;
                        break;
                    case 2:
                        ro = x+1;
                        co = y;
                        break;
                    case 3:
                        ro = x+1;
                        co = y+1;
                }
                this.addCell(new Cell(ro, co, card, card.getResource().get(i)));
            }
        }

        ArrayList<Resource> permanent = card.getPermanentResource();
        for(Resource r : permanent)
            this.addPermanentResource(r);

        return points;
    }

    /**
     * Check if there are pattern of the given ID
     * @param ID Identifier of the pattern to identify
     * @return Number of points generated by the patterns
     */
    public int checkPattern(int ID) {
        HashSet<PlaceableCard> checkedCards = new HashSet<>();
        HashSet<PlaceableCard> allCards;
        int points = 0;
        switch (ID) {
            case 87:
                allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());

                // fungus diagonal
                for(PlaceableCard card : allCards) {
                    if(card.getReign() == Reign.Fungus && !checkedCards.contains(card)) {
                        int row = card.getCells().getFirst().getRow();
                        int column = card.getCells().getFirst().getColumn();
                        PlaceableCard card1 = this.getCard(row - 1, column + 1);
                        PlaceableCard card2 = this.getCard(row - 2, column + 2);
                        Reign reignToCheck = Reign.Fungus;

                        if (card1 != null && card2 != null && card1.getReign() == reignToCheck && card2.getReign() == reignToCheck) {
                            points = points + 2;
                            checkedCards.add(card);
                            checkedCards.add(card1);
                            checkedCards.add(card2);
                        }
                    }
                }


               break;

           case 88:

               allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());

               // plant diagonal
               for(PlaceableCard card : allCards) {
                   if(card.getReign() == Reign.Plant && !checkedCards.contains(card)) {
                       int row = card.getCells().getFirst().getRow();
                       int column = card.getCells().getFirst().getColumn();
                       PlaceableCard card1 = this.getCard(row + 1, column + 1);
                       PlaceableCard card2 = this.getCard(row + 2, column + 2);
                       Reign reignToCheck = Reign.Plant;

                       if (card1 != null && card2 != null && card1.getReign() == reignToCheck && card2.getReign() == reignToCheck) {
                           points = points + 2;
                           checkedCards.add(card);
                           checkedCards.add(card1);
                           checkedCards.add(card2);
                       }
                   }
               }


               break;

           case 89:

               allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());


               // animal diagonal
               for(PlaceableCard card : allCards) {
                   if(card.getReign() == Reign.Animal && !checkedCards.contains(card)) {
                       int row = card.getCells().getFirst().getRow();
                       int column = card.getCells().getFirst().getColumn();
                       PlaceableCard card1 = this.getCard(row - 1, column + 1);
                       PlaceableCard card2 = this.getCard(row - 2, column + 2);
                       Reign reignToCheck = Reign.Animal;

                       if (card1 != null && card2 != null && card1.getReign() == reignToCheck && card2.getReign() == reignToCheck) {
                           points = points + 2;
                           checkedCards.add(card);
                           checkedCards.add(card1);
                           checkedCards.add(card2);
                       }
                   }
               }

               break;

           case 90:

               allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());


               // insect diagonal
               for(PlaceableCard card : allCards) {
                   if(card.getReign() == Reign.Insect && !checkedCards.contains(card)) {
                       int row = card.getCells().getFirst().getRow();
                       int column = card.getCells().getFirst().getColumn();
                       PlaceableCard card1 = this.getCard(row + 1, column + 1);
                       PlaceableCard card2 = this.getCard(row + 2, column + 2);
                       Reign reignToCheck = Reign.Insect;

                       if (card1 != null && card2 != null && card1.getReign() == reignToCheck && card2.getReign() == reignToCheck) {
                           points = points + 2;
                           checkedCards.add(card);
                           checkedCards.add(card1);
                           checkedCards.add(card2);
                       }
                   }
               }

               break;

           case 91:

               allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());

               // fungus-plant vertical
               for(PlaceableCard card : allCards) {
                   if(card.getReign() == Reign.Fungus && !checkedCards.contains(card)) {
                       int row = card.getCells().getFirst().getRow();
                       int column = card.getCells().getFirst().getColumn();
                       PlaceableCard card1 = this.getCard(row + 2, column);
                       PlaceableCard card2 = this.getCard(row + 3, column + 1);

                       if (card1 != null && card2 != null && card1.getReign() == Reign.Fungus && card2.getReign() == Reign.Plant) {
                           points = points + 3;
                           checkedCards.add(card);
                           checkedCards.add(card1);
                           checkedCards.add(card2);
                       }
                   }
               }
               break;

           case 92:

               allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());

               // plant-insect vertical
               for(PlaceableCard card : allCards) {
                   if(card.getReign() == Reign.Insect && !checkedCards.contains(card)) {
                       int row = card.getCells().getFirst().getRow();
                       int column = card.getCells().getFirst().getColumn();
                       PlaceableCard card1 = this.getCard(row - 1, column + 1);
                       PlaceableCard card2 = this.getCard(row - 3, column + 1);

                       if (card1 != null && card2 != null && card1.getReign() == Reign.Plant && card2.getReign() == Reign.Plant) {
                           points = points + 3;
                           checkedCards.add(card);
                           checkedCards.add(card1);
                           checkedCards.add(card2);
                       }
                   }
               }
               break;


           case 93:

               allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());

               // animal-fungus vertical
               for(PlaceableCard card : allCards) {
                   if(card.getReign() == Reign.Fungus && !checkedCards.contains(card)) {
                       int row = card.getCells().getFirst().getRow();
                       int column = card.getCells().getFirst().getColumn();
                       PlaceableCard card1 = this.getCard(row + 1, column - 1);
                       PlaceableCard card2 = this.getCard(row + 3, column - 1);

                       if (card1 != null && card2 != null && card1.getReign() == Reign.Animal && card2.getReign() == Reign.Animal) {
                           points = points + 3;
                           checkedCards.add(card);
                           checkedCards.add(card1);
                           checkedCards.add(card2);
                       }
                   }
               }
               break;

           case 94:

               allCards = (HashSet<PlaceableCard>) CellMatrix.values().stream()
                       .flatMap(cell -> Stream.of(cell.getBottomCard(), cell.getTopCard()))
                       .collect(Collectors.toSet());

               // insect-animal vertical
               for(PlaceableCard card : allCards) {
                   if(card.getReign() == Reign.Animal && !checkedCards.contains(card)) {
                       int row = card.getCells().getFirst().getRow();
                       int column = card.getCells().getFirst().getColumn();
                       PlaceableCard card1 = this.getCard(row + 1, column + 1);
                       PlaceableCard card2 = this.getCard(row + 3, column + 1);

                       if (card1 != null && card2 != null && card1.getReign() == Reign.Insect && card2.getReign() == Reign.Insect) {
                           points = points + 3;
                           checkedCards.add(card);
                           checkedCards.add(card1);
                           checkedCards.add(card2);
                       }
                   }
               }
               break;

           case 95:
               points = (int) (this.getResources().get(0) / 3 * 2);
               break;

           case 96:
               points = (int) (this.getResources().get(3) / 3 * 2);
               break;

           case 97:
               points = (int) (this.getResources().get(2) / 3 * 2);
               break;

           case 98:
               points = (int) (this.getResources().get(1) / 3 * 2);
               break;

           case 99:
               points = (int) (3 * this.getResources().stream()
                       .skip(4)
                       .limit(3)
                       .min(Integer::compare)
                       .orElse(0)
               );
               break;

           case 100:
               points = (int) (((int) (this.getResources().get(4) / 2)) * 2);
               break;

           case 101:
               points = (int) (((int) (this.getResources().get(6) / 2)) * 2);
               break;

           case 102:
               points = (int) (((int) (this.getResources().get(5) / 2)) * 2);
               break;

           default:
               return 0;
       }

       return points;
    }
}
