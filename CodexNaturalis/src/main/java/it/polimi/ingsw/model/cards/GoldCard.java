package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;

import java.util.ArrayList;

public class GoldCard extends PlaceableCard{
    /**
     * Constructor
     *
     * @param ID, it must be >=41  and =<80
     * @throws InvalidIdException if the condition on ID is not met
     */
    public GoldCard(int ID) throws InvalidIdException {
        if(ID>=41 && ID <=80) {
            this.ID = ID;
        } else if (ID <40) {
            throw new InvalidIdException("ID is too small");
        }
        else if(ID >80){
            throw new InvalidIdException("ID is too big");
        }
    }

    /**
     * returns an ArrayList containing the resources you must have to place the card,
     * @return an ArrayList<Resource>
     */
    @Override
    public ArrayList<Resource> getRequirement() {
        ArrayList<Resource> requirement = new ArrayList<Resource>();
        if(ID>=41 && ID<=50){
            requirement.add(Resource.Fungus);
            requirement.add(Resource.Fungus);
            if(ID==41){
                requirement.add(Resource.Animal);
            }
            if(ID==42){
                requirement.add(Resource.Plant);
            }
            if(ID==43){
                requirement.add(Resource.Insect);
            }
            else{
                requirement.add(Resource.Fungus);
                if(ID==44){
                    requirement.add(Resource.Animal);
                }
                if(ID==45){
                    requirement.add(Resource.Plant);
                }
                if(ID==46){
                    requirement.add(Resource.Insect);
                }
                if(ID==50){
                    requirement.add(Resource.Fungus);
                    requirement.add(Resource.Fungus);

                }
            }

        }
        if(ID>=51 && ID<=60){
            requirement.add(Resource.Plant);
            requirement.add(Resource.Plant);
            if(ID==51){
                requirement.add(Resource.Insect);
            }
            if(ID==52){
                requirement.add(Resource.Fungus);
            }
            if(ID==53){
                requirement.add(Resource.Animal);
            }
            else{
                requirement.add(Resource.Plant);
                if(ID==54){
                    requirement.add(Resource.Insect);
                }
                if(ID==55){
                    requirement.add(Resource.Animal);
                }
                if(ID==56){
                    requirement.add(Resource.Fungus);
                }
                if(ID==60){
                    requirement.add(Resource.Plant);
                    requirement.add(Resource.Plant);
                }
            }


        }
        if(ID>=61 && ID<=70){
            requirement.add(Resource.Animal);
            requirement.add(Resource.Animal);
            if(ID==61){
                requirement.add(Resource.Insect);
            }
            if(ID==62){
                requirement.add(Resource.Plant);
            }
            if(ID==63){
                requirement.add(Resource.Fungus);
            }
            else{
                requirement.add(Resource.Animal);
                if(ID==64){
                    requirement.add(Resource.Insect);
                }
                if(ID==65){
                    requirement.add(Resource.Fungus);
                }
                if(ID==66){
                    requirement.add(Resource.Plant);
                }
                if(ID==70){
                    requirement.add(Resource.Animal);
                    requirement.add(Resource.Animal);
                }
            }

        }
        if(ID>=71 && ID<=80){
            requirement.add(Resource.Insect);
            requirement.add(Resource.Insect);
            if(ID==71){
                requirement.add(Resource.Plant);
            }
            if(ID==72){
                requirement.add(Resource.Animal);
            }
            if(ID==73){
                requirement.add(Resource.Fungus);
            }
            else {
                requirement.add(Resource.Insect);
                if(ID==74){
                    requirement.add(Resource.Animal);
                }
                if(ID==75){
                    requirement.add(Resource.Plant);
                }
                if(ID==76){
                    requirement.add(Resource.Fungus);
                }
                if(ID==80){
                    requirement.add(Resource.Insect);
                    requirement.add(Resource.Insect);
                }
            }
        }

        return requirement;

    }

    /**
     * check if the requirement to place the card is met, return true if the player possesses the
     * necessary resources in his PlayerArea
     * @param req
     * @return a boolean
     */
    @Override
    public boolean checkRequirement(ArrayList<Integer> req) {
        int playgroundFungus = req.get(0);
        int playgroundPlant = req.get(1);
        int playgroundAnimal = req.get(2);
        int playgroundInsect = req.get(3);
        int cardFungus = 0;
        int cardPlant = 0;
        int cardAnimal = 0;
        int cardInsect = 0;
        ArrayList<Resource> requirement = this.getRequirement();
        for(int i = 0; i<requirement.size(); i++){
            if(requirement.get(i)==Resource.Fungus){
                cardFungus++;
            }
            if(requirement.get(i)==Resource.Plant){
                cardPlant++;
            }
            if(requirement.get(i)==Resource.Animal){
                cardAnimal++;
            }
            if(requirement.get(i)==Resource.Insect){
                cardInsect++;
            }
        }
        if(cardFungus<=playgroundFungus && cardPlant<=playgroundPlant && cardAnimal<=playgroundAnimal && cardInsect<=playgroundInsect){
            return true;
        }

        else{
            return false;
        }
    }



    /**
     *
     * @return the point associated with the card
     */
    @Override
    public int getPoints(){
        if(ID>=41 && ID<=43 || ID>=51 && ID<=53 || ID>=61 && ID<=63 || ID>=71 && ID<=73){
            return 1;
        }
        else if(ID>=44 && ID<=46 || ID>=54 && ID<=56 || ID>=64 && ID<=66 || ID>=74 && ID<=76){
            return 2;
        }
        else if(ID>=47 && ID <=49 || ID>=57 && ID<=59 || ID>=67 && ID<=69 || ID>=77 && ID<=79){
            return 3;
        }
        else if(ID==50 || ID==60 || ID==70 || ID==80){
            return 5;
        }
        else{
            return 0;
        }
    }
    /**
     * Gold cards contain only one permanent resource
     * @return an Arraylist containing the permanent resource in the back of the card
     */
    @Override
    public ArrayList<Resource> getPermanentResource() {
        ArrayList<Resource> resources = new ArrayList<Resource>();
        if(this.ID >=41 && this.ID <= 50){
            resources.add(Resource.Fungus);
        }
        if(this.ID >=51 && this.ID <= 60){
            resources.add(Resource.Plant);
        }
        if(this.ID >=61 && this.ID <= 70){
            resources.add(Resource.Animal);
        }
        if(this.ID >=71 && this.ID <=80){
            resources.add(Resource.Insect);
        }
        return resources;

    }

}
