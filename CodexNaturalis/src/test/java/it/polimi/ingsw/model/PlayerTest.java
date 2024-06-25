package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.model.cards.exceptions.noPlaceCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    PlayerHand deck;
    PlayerArea playerArea;
    CommonArea commonArea;

    @BeforeEach
    void setUp() {
        commonArea = (new LoadDecks()).load();
        player = new Player("Bianca", "Blue", commonArea);
        deck = player.getPlayerHand();
        playerArea = player.getPlayerArea();
        commonArea.getD1().shuffle();
        commonArea.getD2().shuffle();
        commonArea.getD3().shuffle();
        commonArea.getD4().shuffle();
    }

    @Test
    void drawStarter_DeckSizeShouldBeOne() {
        player.drawStarter();
        assertEquals(1, deck.getPlaceableCards().size());
    }

    @Test
    void drawStarter_DeckShouldContainStarter() {
        player.drawStarter();

        PlaceableCard starter = player.getPlayerHand().getPlaceableCards().getFirst();
        assertInstanceOf(StarterCard.class, starter);
    }

    @Test
    void PlaceStarterFront_DeckSizeShouldBeZeroAndCardPlaced() {
        player.drawStarter();
        player.placeStarter(1);
        assertEquals(0, deck.getPlaceableCards().size());
        assertTrue(player.getPlayerArea().contains(0, 0));
    }

    @Test
    void PlaceStarterBack_DeckSizeShouldBeZeroAndCardPlaced() {
        player.drawStarter();
        player.placeStarter(0);
        assertEquals(0, deck.getPlaceableCards().size());
        assertTrue(player.getPlayerArea().contains(0, 0));
    }

    @Test
    void initialHand_DeckSizeShouldBeThree() {
        player.initialHand();
        assertEquals(3, deck.getPlaceableCards().size());
    }

    @Test
    void initialHand_DeckShouldContainTwoResourceAndOneGold() {
        player.initialHand();
        assertInstanceOf(ResourceCard.class, deck.getPlaceableCards().get(0));
        assertInstanceOf(ResourceCard.class, deck.getPlaceableCards().get(1));
        assertInstanceOf(GoldCard.class, deck.getPlaceableCards().get(2));
    }

    @Test
    void drawObjectives_ShouldBeObjectiveCards() {
        ObjectiveCard[] objective = player.drawObjectives();
        assertInstanceOf(ObjectiveCard.class, objective[0]);
        assertInstanceOf(ObjectiveCard.class, objective[1]);
    }

    @Test
    void pickObjective1_ShouldBeObjectiveCard() {
        int pick = 1;

        ObjectiveCard[] objective = player.drawObjectives();
        player.pickObjectiveCard(pick, objective);
        assertInstanceOf(ObjectiveCard.class, player.getObjective());
    }

    @Test
    void pickObjective2_ShouldBeObjectiveCard() {
        int pick = 2;

        ObjectiveCard[] objective = player.drawObjectives();
        player.pickObjectiveCard(pick, objective);
        assertInstanceOf(ObjectiveCard.class, player.getObjective());
    }


    @Test
    void playTurnTest() {
        player.initialHand();
        try {
            player.playTurn(1, 2, 3, 1);
            assertEquals(2, deck.getPlaceableCards().size());
        } catch (noPlaceCardException e) {
            assertThrows(noPlaceCardException.class, () -> player.playTurn(1, 2, 3, 1));
        }

    }

    @Test
    void pickNewCard1_ShouldThrowException() {
        int pick = 1;
        int size = commonArea.getD1().getSize();

        for (int i = 0; i < size; i++) {
            commonArea.getD1().removeCard();
        }

        assertThrows(InvalidIdException.class, () -> player.pickNewCard(pick));
    }

    @Test
    void pickNewCard2_ShouldThrowException() {
        int pick = 2;
        int size = commonArea.getD1().getSize();

        for (int i = 0; i < size; i++) {
            commonArea.getD2().removeCard();
        }

        assertThrows(InvalidIdException.class, () -> player.pickNewCard(pick));
    }

    @Test
    void pickNewCard3_4_5_6_ShouldThrowException() {
        int size = commonArea.getTableCards().size();

        assertThrows(InvalidIdException.class, () -> player.pickNewCard(3));
        assertThrows(InvalidIdException.class, () -> player.pickNewCard(4));
        assertThrows(InvalidIdException.class, () -> player.pickNewCard(5));
        assertThrows(InvalidIdException.class, () -> player.pickNewCard(6));
    }

    @Test
    void pickNewCard7_ShouldThrowException() {
        assertThrows(InvalidIdException.class, () -> player.pickNewCard(7));
    }

    @Test
    void pickNewCard_ShouldBeResourceFromDeck() throws InvalidIdException {
        int pick = 1;

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        assertInstanceOf(ResourceCard.class, card);
    }

    @Test
    void pickedCard_ShouldBeFoldFromDeck() throws InvalidIdException {
        int pick = 2;

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        assertInstanceOf(GoldCard.class, card);
    }

    @Test
    void pickedCard_ShouldBeResourceLeft() throws InvalidIdException {
        int pick = 3;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().add(0, resourceCard1);
        commonArea.getTableCards().add(1, resourceCard2);
        commonArea.getTableCards().add(2, goldCard1);
        commonArea.getTableCards().add(3, goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        assertEquals(6, cardId);
    }

    @Test
    void pickedCard_ShouldBeResourceRight() throws InvalidIdException {
        int pick = 4;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().set(0, resourceCard1);
        commonArea.getTableCards().add(1, resourceCard2);
        commonArea.getTableCards().add(2, goldCard1);
        commonArea.getTableCards().add(3, goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        assertEquals(7, cardId);
    }

    @Test
    void pickedCard_ShouldBeGoldLeft() throws InvalidIdException {
        int pick = 5;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().add(0, resourceCard1);
        commonArea.getTableCards().add(1, resourceCard2);
        commonArea.getTableCards().add(2, goldCard1);
        commonArea.getTableCards().add(3, goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        assertEquals(43, cardId);
    }

    @Test
    void pickedCard_ShouldBeGoldRight() throws InvalidIdException {
        int pick = 6;

        //set cards
        ResourceCard resourceCard1 = new ResourceCard(6);
        ResourceCard resourceCard2 = new ResourceCard(7);
        GoldCard goldCard1 = new GoldCard(43);
        GoldCard goldCard2 = new GoldCard(47);
        commonArea.getTableCards().add(0, resourceCard1);
        commonArea.getTableCards().add(1, resourceCard2);
        commonArea.getTableCards().add(2, goldCard1);
        commonArea.getTableCards().add(3, goldCard2);

        player.pickNewCard(pick);
        PlaceableCard card = deck.getPlaceableCards().getLast();
        int cardId = card.getID();
        assertEquals(47, cardId);
    }

    @Test
    void getScore_InitialScore_ShouldReturnZero() {
        assertEquals(0, player.getScore());
    }

    @Test
    void getScore_AfterPlayTurn_ShouldReturnUpdatedScore() throws noPlaceCardException, InvalidIdException {
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        playerArea.placeStarterCard(starterCard, true);

        ArrayList<Resource> resourceCard = new ArrayList<>();
        resourceCard.add(Resource.Empty);
        resourceCard.add(Resource.Blocked);
        resourceCard.add(Resource.Empty);
        resourceCard.add(Resource.Animal);

        player.getPlayerHand().addNewPlaceableCard(new ResourceCard(29, 1, Reign.Animal, true, resourceCard));
        player.getPlayerHand().addNewPlaceableCard(new ResourceCard(2));
        player.getPlayerHand().addNewPlaceableCard(new GoldCard(49));
        System.out.println(player.getPlayerHand());
        player.playTurn(0, 1, 1, 1); //place first card in playerHand
        System.out.println(player.getScore());
        System.out.println(player.getPlayerHand());
        assertEquals(1, player.getScore());
    }

    @Test
    public void getPlayerArea_ShouldReturnPlayerArea() {
        assertNotNull(player.getPlayerArea());
        assertInstanceOf(PlayerArea.class, player.getPlayerArea());
    }

    @Test
    public void getObjective_ShouldReturnObjectiveCard() {
        ObjectiveCard[] objective = player.drawObjectives();
        player.pickObjectiveCard(1, objective);

        assertInstanceOf(ObjectiveCard.class, player.getObjective());
    }

    @Test
    public void getPlayerHand_ShouldReturnPlayerHand() {
        assertInstanceOf(PlayerHand.class, player.getPlayerHand());
    }

    @Test
    public void getNickname_ShouldReturnNickname() {
        assertEquals("Bianca", player.getNickname());
    }

    @Test
    public void getColor_ShouldReturnColor() {
        assertEquals("Blue", player.getColor());
    }

    @Test
    public void getCommonArea_ShouldReturnCommonArea() {
        assertInstanceOf(CommonArea.class, player.getCommonArea());
    }

    @Test
    public void placeHandCard() throws InvalidIdException, noPlaceCardException {
        ArrayList<Resource> card1 = new ArrayList<>();
        card1.add(Resource.Fungus);
        card1.add(Resource.Empty);
        card1.add(Resource.Fungus);
        card1.add(Resource.Blocked);
        ArrayList<Resource> card29 = new ArrayList<>();
        card29.add(Resource.Empty);
        card29.add(Resource.Blocked);
        card1.add(Resource.Empty);
        card29.add(Resource.Animal);
        ArrayList<Resource> card57 = new ArrayList<>();
        card57.add(Resource.Empty);
        card57.add(Resource.Blocked);
        card57.add(Resource.Quill);
        card57.add(Resource.Blocked);
        ArrayList<Resource> requirement = new ArrayList<>();
        requirement.add(Resource.Plant);
        requirement.add(Resource.Plant);
        requirement.add(Resource.Plant);

        player.getPlayerHand().addNewPlaceableCard(new ResourceCard(1, 0, Reign.Fungus, true, card1));
        player.getPlayerHand().addNewPlaceableCard(new ResourceCard(29, 1, Reign.Animal, true, card29));
        player.getPlayerHand().addNewPlaceableCard(new GoldCard(57, 3, Reign.Plant, true, card57, requirement));
        System.out.println(player.getPlayerHand());

        //should throw an exception if the player choose a card number that is not 0, 1 or 2 because pickPlaceableCard will return null
        assertThrows(noPlaceCardException.class, () -> player.playTurn(4, 1, 1, 1));

        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.Empty);
        resources.add(Resource.Plant);
        resources.add(Resource.Insect);
        resources.add(Resource.Empty);
        ArrayList<Resource> permanentResources = new ArrayList<>();
        permanentResources.add(Resource.Insect);
        ArrayList<Resource> bottomResources = new ArrayList<>();
        bottomResources.add(Resource.Fungus);
        bottomResources.add(Resource.Plant);
        bottomResources.add(Resource.Insect);
        bottomResources.add(Resource.Animal);
        PlaceableCard starterCard = new StarterCard(81, 0, null, true, resources, permanentResources, bottomResources);
        playerArea.placeStarterCard(starterCard, true);

        player.playTurn(1, 1, 1, 1);
        System.out.println(player.getPlayerHand());


    }

}
