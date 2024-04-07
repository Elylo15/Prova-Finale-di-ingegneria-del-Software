package it.polimi.ingsw.model.cards.Json;

import com.google.gson.*;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.*;
import java.io.FileReader;

/**
 * LoadDeck class
 * @author elylo
 */
public class LoadDecks {
    /**
     * method load: takes the cards that are collected in the Cards.json file
     * and converts them into PlaceableCard/ObjectiveCard and places them in the various decks.
     * Once inserted into the deck it puts them inside the CommonArea which is returned
     * @return CommonArea: common area of all players who will be placed in the match
     */
    public CommonArea load() {
        CommonArea c = new CommonArea();
        try {
            // Read the JSON file into a Reader object
            FileReader reader = new FileReader("C:\\Users\\lolel\\IdeaProjects\\ing-sw-2024-Lollino-Ianosel-Locatelli-Martini\\CodexNaturalis\\src\\main\\java\\it\\polimi\\ingsw\\model\\cards\\Json\\Cards.json");

            //Parse the JSON file into a JsonObject
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            //for resourceCard
            JsonArray ArrayResourceCard = jsonObject.getAsJsonArray("resourceCard");
            for (int i = 0; i < ArrayResourceCard.size(); i++) {
                JsonElement y = ArrayResourceCard.get(i);
                ResourceCard resourceCard = new Gson().fromJson(y, ResourceCard.class);
                c.getD1().addCard(resourceCard);
            }

            //for Goldcard
            JsonArray ArrayGoldCard = jsonObject.getAsJsonArray("GoldCard");
            for (int i = 0; i < ArrayGoldCard.size(); i++) {
                JsonElement y = ArrayGoldCard.get(i);
                GoldCard goldCard = new Gson().fromJson(y, GoldCard.class);
                c.getD2().addCard(goldCard);
            }
            //for starterCard
            JsonArray ArrayStarterCard = jsonObject.getAsJsonArray("StarterCard");
            for (int i = 0; i < ArrayStarterCard.size(); i++) {
                JsonElement y = ArrayStarterCard.get(i);
                StarterCard starterCard = new Gson().fromJson(y, StarterCard.class);
                c.getD3().addCard(starterCard);
            }
            //for objectiveCard
            JsonArray ArrayObjectiveCard = jsonObject.getAsJsonArray("ObjectiveCard");
            for (int i = 0; i < ArrayObjectiveCard.size(); i++) {
                JsonElement y = ArrayObjectiveCard.get(i);
                ObjectiveCard objectiveCard = new Gson().fromJson(y, ObjectiveCard.class);
                c.getD4().addCard(objectiveCard);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return c;
    }


}




