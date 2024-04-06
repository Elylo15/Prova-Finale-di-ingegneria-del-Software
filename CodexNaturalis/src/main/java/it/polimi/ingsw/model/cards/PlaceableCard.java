package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;

public abstract class PlaceableCard extends Card{

    private ArrayList<Resource> requirement;
    private int points;
    private Reign reign;
    private boolean front;
    private ArrayList<Cell> cells;

    public PlaceableCard(int ID) throws InvalidIdException
    {
        super(ID);

        front = true;
        requirement = new ArrayList<>();
        switch(ID) {
            case 1 -> {

            }
            case 2 -> {

            }
            case 3 -> {

            }
            case 4 ->{

            }

            case 5 ->{

            }
            case 6 ->{

            }
            case 7 ->{

            }
            case 8 ->{

            }
            case 9 ->{

            }
            case 10 ->{

            }case 11 ->{

            }case 12 ->{

            }case 13 ->{

            }case 14 ->{

            }case 15 ->{

            }case 16 ->{

            }







            case 77,78,79 -> {
                //Requirements
                requirement.add(Resource.Insect);
                requirement.add(Resource.Insect);
                requirement.add(Resource.Insect);
                //Points
                points = 3;
                //Reign
                reign = Reign.Insect;
            }

            case 80 -> {
                //Requirements
                requirement.add(Resource.Insect);
                requirement.add(Resource.Insect);
                requirement.add(Resource.Insect);
                requirement.add(Resource.Insect);
                requirement.add(Resource.Insect);
                //Points
                points = 5;
                //Reign
                reign = Reign.Insect;
            }

            case 81,82,83,84,85,86 -> {
                //No Requirements
                //Points
                points = 0;
                //Reign
                reign = null;

            }

        }
    }
    /**
     * returns an ArrayList containing the resources you must have to place the card,
     * @return an ArrayList<Resource>
     */
    public ArrayList<Resource> getRequirement() {
        return requirement;
    }

    /**
     * check if the requirement to place the card is met, return true if the player possesses the
     * necessary resources in his PlayerArea
     * the requirement applies only if the player wants to play the front of the card
     * @param req it receives as parameter an ArrayList containing the number of resources of each type
     * the player possesses in his PlayerArea
     * @return a boolean
     */
    @Override
    public boolean checkRequirement(ArrayList<Integer> req) {
        if(front==true) {
            int playgroundFungus = req.get(0);
            int playgroundPlant = req.get(1);
            int playgroundAnimal = req.get(2);
            int playgroundInsect = req.get(3);
            int cardFungus = 0;
            int cardPlant = 0;
            int cardAnimal = 0;
            int cardInsect = 0;
            ArrayList<Resource> requirement = this.getRequirement();
            for (int i = 0; i < requirement.size(); i++) {
                if (requirement.get(i) == Resource.Fungus) {
                    cardFungus++;
                }
                if (requirement.get(i) == Resource.Plant) {
                    cardPlant++;
                }
                if (requirement.get(i) == Resource.Animal) {
                    cardAnimal++;
                }
                if (requirement.get(i) == Resource.Insect) {
                    cardInsect++;
                }
            }
            if (cardFungus <= playgroundFungus && cardPlant <= playgroundPlant && cardAnimal <= playgroundAnimal && cardInsect <= playgroundInsect) {
                return true;
            } else {
                return false;
            }
        }
        if(front==false){
            return true;
        }
    }


    public void setCells(ArrayList<Cell> cells) {}

    public Reign getReign() {
        return reign;
    }

    public boolean isFront() {
        return front;
    }

    public ArrayList<Cell> getCells() {}

    /**
     *
     * @return the point associated with the card
     */
    public int getPoints() {
        return points;
    }

    public ArrayList<Resource> getResource() {
        ArrayList<Resource> resource = new ArrayList<Resource>();
        if(front==true){
            switch (ID){
                case 1 ->{
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Blocked);
                }
                case 2 ->{
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                }
                case 3 ->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Fungus);
                }
                case 4 ->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Fungus);
                }
                case 5 ->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Quill);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Fungus);
                }
                case 6 ->{
                    resource.add(Resource.Inkwell);
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Animal);
                }
                case 7 ->{
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Manuscript);
                    resource.add(Resource.Empty);
                }
                case 8 ->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 9 ->{
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                }
                case 10 ->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Empty);
                }
                case 11 ->{
                    resource.add(Resource.Plant);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Blocked);
                }
                case 12 ->{
                    resource.add(Resource.Plant);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                }
                case 13 ->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Plant);
                }
                case 14 ->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Plant);
                }
                case 15 ->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Quill);
                    resource.add(Resource.Plant);
                }
                case 16 ->{
                    resource.add(Resource.Fungus);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Inkwell);
                }
                case 17 ->{
                    resource.add(Resource.Manuscript);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Animal);
                }
                case 18 ->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Blocked);
                }
                case 19 ->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Plant);
                }
                case 20  ->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Plant);
                }
                case 21->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 22->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 23->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 24->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 25->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 26 ->{
                    resource.add(Resource.Plant);
                    resource.add(Resource.Animal);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Manuscript);
                }
                case 27 ->{
                    resource.add(Resource.Quill);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Animal);
                    resource.add(Resource.Fungus);
                }
                case 28->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Animal);
                    resource.add(Resource.Empty);
                }
                case 29->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Animal);
                }
                case 30->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Animal);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 31->{
                    resource.add(Resource.Insect);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 32->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Insect);
                }
                case 33->{
                    resource.add(Resource.Insect);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Empty);
                }
                case 34->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Insect);
                }
                case 35->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Quill);
                    resource.add(Resource.Animal);
                    resource.add(Resource.Insect);
                }
                case 36->{
                    resource.add(Resource.Manuscript);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Fungus);
                }
                case 37->{
                    resource.add(Resource.Insect);
                    resource.add(Resource.Plant);
                    resource.add(Resource.Inkwell);
                    resource.add(Resource.Blocked);
                }
                case 38->{
                    resource.add(Resource.Insect);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                }
                case 39->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Insect);
                }
                case 40->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Insect);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                }
                case 41->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Quill);
                }
                case 42->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Inkwell);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                }
                case 43->{
                    resource.add(Resource.Manuscript);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 44->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                }
                case 45->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 46->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                }
                case 47->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Inkwell);
                    resource.add(Resource.Blocked);
                }
                case 48->{
                    resource.add(Resource.Quill);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Blocked);
                }
                case 49->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Manuscript);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                }
                case 50->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 51->{
                    resource.add(Resource.Quill);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 52->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Manuscript);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                }
                case 53->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Inkwell);
                    resource.add(Resource.Empty);
                }
                case 54->{
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                }
                case 55->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                }
                case 56->{
                    resource.add(Resource.Empty);
                    resource.add(Resource.Blocked);
                    resource.add(Resource.Empty);
                    resource.add(Resource.Empty);
                }
                case 57->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 58->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 59->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 60->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 61->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 62->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 63->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 64->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 65->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 66->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 67->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 68->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 69->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 70->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 71->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 72->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 73->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 74->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 75->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 76->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 77->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 78->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 79->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 80->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 81->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 82->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 83->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 84->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 85->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }
                case 86->{
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                    resource.add(Resource);
                }



            }

        }
        if(front==false){
            if(ID>=1 && ID<=80) {
                resource.add(Resource.Empty);
                resource.add(Resource.Empty);
                resource.add(Resource.Empty);
                resource.add(Resource.Empty);
            }
            if(ID==81){
                resource.add(Resource.Fungus);
                resource.add(Resource.Plant);
                resource.add(Resource.Insect);
                resource.add(Resource.Animal);
            }
            if(ID==82){
                resource.add(Resource.Plant);
                resource.add(Resource.Animal);
                resource.add(Resource.Fungus);
                resource.add(Resource.Insect);
            }
            if(ID==83){
                resource.add(Resource.Insect);
                resource.add(Resource.Animal);
                resource.add(Resource.Fungus);
                resource.add(Resource.Plant);
            }
            if(ID==84){
                resource.add(Resource.Plant);
                resource.add(Resource.Insect);
                resource.add(Resource.Animal);
                resource.add(Resource.Fungus);
            }
            if(ID==85){
                resource.add(Resource.Insect);
                resource.add(Resource.Fungus);
                resource.add(Resource.Plant);
                resource.add(Resource.Animal);
            }
            if(ID==86){
                resource.add(Resource.Fungus);
                resource.add(Resource.Animal);
                resource.add(Resource.Plant);
                resource.add(Resource.Insect);
            }
        }
        return resource;
    }
    /**
     * Gold and Resource cards contain only one permanent resources in their back
     * Starter cards contain from one to three permanent resources in their front
     * @return an Arraylist containing the permanent resources based on the ID of the card
     */
    public ArrayList<Resource> getPermanentResource(){
        ArrayList<Resource> resources = new ArrayList<Resource>();
        if(front==false) {

            if (this.ID >=1 && this.ID <= 10 || this.ID >= 41 && this.ID <= 50) {
                resources.add(Resource.Fungus);
            }
            if (this.ID >=11 && this.ID <= 20 || this.ID >= 51 && this.ID <= 60) {
                resources.add(Resource.Plant);
            }
            if (this.ID >=21 && this.ID <= 30 || this.ID >= 61 && this.ID <= 70) {
                resources.add(Resource.Animal);
            }
            if (this.ID >=31 && this.ID <=40 || this.ID >= 71 && this.ID <= 80) {
                resources.add(Resource.Insect);
            }
        }
        if(front==true){
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
        }
        return resources;

    }

    public boolean isResource() {
        if(this.ID >= 1 && this.ID <=40){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isGold() {
        if(this.ID>=41 && this.ID <=80){
            return true;
        }
        else {
            return false;
        }

    }

    public boolean isStarter() {
        if(this.ID>=81 && this.ID <=86){
            return true;
        }
        else {
            return false;
        }
    }

}
