package it.polimi.ingsw.model.cards.Json;

import com.google.gson.*;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.*;
import java.io.FileReader;

public class LoadDecks {
    public static void main(String[] args) {
        try {
            CommonArea c = new CommonArea();
            // Leggi il file JSON in un oggetto Reader
            FileReader reader = new FileReader("Cards.json");

            // Parsa il file JSON in un oggetto JsonObject
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            //per le resourceCard
            JsonArray ArrayResourceCard= jsonObject.getAsJsonArray("resourceCard");
            for (int i = 0; i < ArrayResourceCard.size() ; i++) {
                JsonElement y = ArrayResourceCard.get(i);
                ResourceCard resourceCard = new Gson().fromJson(y, ResourceCard.class);
                c.getD1().addCard(resourceCard);
            }

            //prova
            /*JsonArray x= jsonObject.getAsJsonArray("resourceCard");
            JsonElement y = x.get(0);
            ResourceCard resourceCard = new Gson().fromJson(y, ResourceCard.class);
            resourceCard.getID();
            resourceCard.getRequirement();
            resourceCard.getPoints();
            resourceCard.getReign();
            resourceCard.isFront();
            resourceCard.getCells();
            resourceCard.getResource();*/

            //per le Goldcard
            JsonArray ArrayGoldCard= jsonObject.getAsJsonArray("GoldCard");
            for (int i = 0; i < ArrayGoldCard.size() ; i++) {
                JsonElement y = ArrayGoldCard.get(i);
                GoldCard goldCard = new Gson().fromJson(y, GoldCard.class);
                c.getD2().addCard(goldCard);
            }
            //per starterCard
            JsonArray ArrayStarterCard= jsonObject.getAsJsonArray("StarterCard");
            for (int i = 0; i < ArrayStarterCard.size() ; i++) {
                JsonElement y = ArrayStarterCard.get(i);
                StarterCard starterCard = new Gson().fromJson(y,StarterCard.class);
                c.getD3().addCard(starterCard);
            }
            //per objectiveCard
            JsonArray ArrayObjectiveCard= jsonObject.getAsJsonArray("ObjectiveCard");
            for (int i = 0; i < ArrayObjectiveCard.size() ; i++) {
                JsonElement y = ArrayObjectiveCard.get(i);
                ObjectiveCard objectiveCard = new Gson().fromJson(y,ObjectiveCard.class);
                c.getD4().addCard(objectiveCard);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}



