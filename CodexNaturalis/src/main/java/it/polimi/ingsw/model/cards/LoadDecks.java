package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.CommonArea;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * This class is responsible for loading the decks of cards from a JSON file.
 * It implements the Serializable interface.
 */
public class LoadDecks implements Serializable {
    /**
     * This method loads the cards from the Cards.json file.
     * It converts them into PlaceableCard/ObjectiveCard and places them in the various decks.
     * Once inserted into the deck it puts them inside the CommonArea which is returned.
     *
     * @return CommonArea: common area of all players who will be placed in the match
     */
    public CommonArea load() {
        CommonArea c = new CommonArea();
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = getClass().getResourceAsStream("/Json/Cards.json")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: /Json/Cards.json");
            }
            // Map the JSON file to a Map object
            Map<String, List<?>> cardMap = mapper.readValue(inputStream, new TypeReference<>() {});

            // Convert the map values to the appropriate card types
            List<ResourceCard> resourceCards = mapper.convertValue(cardMap.get("ResourceCard"), new TypeReference<>() {});
            List<GoldCard> goldCards = mapper.convertValue(cardMap.get("GoldCard"), new TypeReference<>() {});
            List<StarterCard> starterCards = mapper.convertValue(cardMap.get("StarterCard"), new TypeReference<>() {});
            List<ObjectiveCard> objectiveCards = mapper.convertValue(cardMap.get("ObjectiveCard"), new TypeReference<>() {});

            // Add the cards to the appropriate decks in the common area
            resourceCards.forEach(card -> c.getD1().addCard(card));
            goldCards.forEach(card -> c.getD2().addCard(card));
            starterCards.forEach(card -> c.getD3().addCard(card));
            objectiveCards.forEach(card -> c.getD4().addCard(card));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return c;
    }

}
