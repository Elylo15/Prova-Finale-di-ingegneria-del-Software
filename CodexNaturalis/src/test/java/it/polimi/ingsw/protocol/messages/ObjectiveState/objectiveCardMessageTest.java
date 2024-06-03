package it.polimi.ingsw.protocol.messages.ObjectiveState;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.LoadDecks;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ObjectiveCardMessageTest {

    private objectiveCardMessage message;
    private ArrayList<ObjectiveCard> objectiveCards;

    @BeforeEach
    public void setup() throws InvalidIdException {
        CommonArea area = (new LoadDecks()).load();
        objectiveCards = new ArrayList<>();
        objectiveCards.add(area.drawObjectiveCard());
        objectiveCards.add(area.drawObjectiveCard());

        message = new objectiveCardMessage(objectiveCards);
    }

    @Test
    @DisplayName("Objective cards are correctly retrieved")
    public void objectiveCardsAreCorrectlyRetrieved() {
        Assertions.assertEquals(objectiveCards, message.getObjectiveCard());
    }

    @Test
    @DisplayName("Choice is null when objective cards are provided")
    public void choiceIsNullWhenObjectiveCardsAreProvided() {
        Assertions.assertNull(message.getChoice());
    }

    @Test
    @DisplayName("No response is false when objective cards are provided")
    public void noResponseIsFalseWhenObjectiveCardsAreProvided() {
        Assertions.assertFalse(message.isNoResponse());
    }
}