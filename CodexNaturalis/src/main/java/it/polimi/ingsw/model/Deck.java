package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Deck {
    ArrayList<GoldCard> goldCardsDeck;
    ArrayList<ResourceCard> resourceCardsDeck;
    ArrayList<ObjectiveCard> objectiveCardsDeck;
    ArrayList<StarterCard> starterCardsDeck;

    public Deck(ArrayList<GoldCard> goldCardsDeck,ArrayList<ResourceCard> resourceCardsDeck,ArrayList<ObjectiveCard> objectiveCardsDeck,ArrayList<StarterCard> starterCardsDeck){
        this.goldCardsDeck  = goldCardsDeck;
        this.resourceCardsDeck = resourceCardsDeck;
        this.objectiveCardsDeck = objectiveCardsDeck;
        this.starterCardsDeck = starterCardsDeck;
    }

    public void addGoldCard(GoldCard gc){
        goldCardsDeck.add(gc);
    }
    public void addResourceCard(ResourceCard rc){
        resourceCardsDeck.add(rc);
    }
    public void addObjectivedCard(ObjectiveCard oc){
        objectiveCardsDeck.add(oc);
    }
    public void addStarterCard(StarterCard sc){ starterCardsDeck.add(sc);}
}
