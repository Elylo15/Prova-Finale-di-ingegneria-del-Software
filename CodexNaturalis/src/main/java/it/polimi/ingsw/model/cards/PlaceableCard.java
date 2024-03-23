package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;

import java.util.ArrayList;

public class PlaceableCard extends Card{

    private ArrayList<Resource> requirement;
    private int points;
    private Reign reign;
    private boolean front;
    private ArrayList<Cell> cells;

    public PlaceableCard(int ID) {}
    public ArrayList<Resource> getRequirement() {}

    public boolean checkRequirement(ArrayList<Integer> req) {}

    public void setCells(ArrayList<Cell> cells) {}

    public Reign getReign() {}

    public boolean isFront() {}

    public ArrayList<Cell> getCells() {}

    public int getPoints() {}

    public ArrayList<Resource> getResource() {}

    public ArrayList<Resource> getPermanentResource() {}

    public boolean isResource() {}

    public boolean isGold() {}

    public boolean isStarter() {}

}
