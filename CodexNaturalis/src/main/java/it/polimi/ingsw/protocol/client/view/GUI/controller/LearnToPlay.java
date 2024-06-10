package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.*;

public class LearnToPlay implements Initializable {
    @FXML
    private Button mainPane2;
    @FXML
    private ImageView nextPlayer;
    @FXML
    private ImageView state;
    @FXML
    private Pane playground;
    @FXML
    private ImageView onTop;
    @FXML
    private ImageView colorName;
    @FXML
    private Label playerName;
    @FXML
    private Pane mainPane;
    @FXML
    private ImageView explanation;
    private final ImageView explanation2 = new ImageView();
    private final double fitHeightCommon = 141;
    private final double fitWidthCommon = 208;
    private final double fitHeightCard = 157;
    private final double fitWidthCard = 234;
    private final double layoutYResource = 746;
    private final double layoutXPick0 = 364;
    private final double layoutXPick1 = 587;
    private final double layoutYGold = 895;
    private final double layoutXDeck = 56;
    private final double layoutXObjective = 1659;
    private final double layoutYHand = 814;
    private final double layoutXCard0 = 864;
    private final double layoutXCard1 = 1110;
    private final double layoutXCard2 = 1356;
    private double layoutYFree;
    private double layoutXFree;
    private Card replaceR;
    private Card replaceG;
    private ImageView selectedCard = null;
    private ImageView clickedPlaceholder = null;
    private ResourceCard d1;
    private GoldCard d2;
    private ResourceCard f1;
    private ResourceCard f2;
    private GoldCard f3;
    private GoldCard f4;
    private StarterCard myS;
    private ObjectiveCard myO;
    private ObjectiveCard myO1;
    private ResourceCard my1;
    private ResourceCard my2;
    private GoldCard my3;
    private ObjectiveCard common1;
    private ObjectiveCard common2;
    private final ImageBinder imageBinder = new ImageBinder();
    private boolean block;
    int i = 0;

    /**
     * This method is called when the FXML file is loaded, it initializes the player name, the images and the cards
     * It also sets the click handler, which will be called when the user clicks on the screen to load the next explanation
     * @param location not used
     * @param resources not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playerName.setText("Player 1");
        nextPlayer.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NextButton/nextBlue.png"))));
        colorName.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/purpleName.png"))));
        state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/state.png"))));

        createCards();
        mainPane2.setOpacity(0.0);

        mainPane2.setOnMouseClicked((e -> {
            i++;
            if (i < 60) {
                clickHandler();
            }
        }));

        playground.setOnMouseClicked(e -> {

        });
    }


    /**
     * This method is called when the user clicks on the screen, it will load the next explanation
     */
    private void clickHandler() {
        double fitHeightPlaced = 133;
        double fitWidthPlaced = 200;

        if (i == 0){
            explanation2.setMouseTransparent(true);
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/2.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 1){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/3.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 2){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/4.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 3){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/5.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 4){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/6.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 5){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/7.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 6){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/8.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 7){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/9.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 8){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/10.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 9){
            addStarter();
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/11.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 10){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/12.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 11){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/13.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 12){
            ImageView starterImg = getCardFromPosition(layoutXCard1, layoutYHand);
            starterImg.setImage(imageBinder.getOppositeImage(myS.getID(), myS.isFront()));
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/14.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 13){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/15.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 14){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/16.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 15){
            getCardFromPosition(layoutXCard1, layoutYHand).setOnMouseClicked(this::selectCard);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/17.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 16){
            mainPane2.setDisable(true);
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/18.png"))));
            addClickablePlaceholder(680, 359, fitHeightPlaced, fitWidthPlaced, this::waitForClickStarter);
            onCLick(explanation, explanation2);
            //waitForClickStarter();
        }  else if (i == 17){
            mainPane.setDisable(false);
            addCommonObj();
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/19.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 18){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/20.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 19){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/21.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 20){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/22.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 21){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/23.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 22){
            adMyObj();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/24.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 23) {
            mainPane2.setDisable(true);
            addClickablePlaceholder(layoutXObjective, 606, fitHeightCommon, fitWidthCommon, this::waitForClickObjective);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/25.png"))));
            onCLick(explanation2, explanation);

        } else if (i == 24){
            mainPane.setDisable(false);
            removeObjectives();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/26.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 25){
            addCardsToHand();
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/27.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 26){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/28.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 27){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/29.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 28){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/30.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 29){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/31.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 30){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/32.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 31){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/33.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 32){
            turnAroundCardsHand();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/34.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 33){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/35.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 34){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/36.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 35){
            addClickablePlaceholder(837, 278, fitHeightPlaced, fitWidthPlaced, this::selectedPlaceHolder);
            addClickablePlaceholder(523, 278, fitHeightPlaced, fitWidthPlaced, this::selectedPlaceHolder);
            addClickablePlaceholder(837, 439, fitHeightPlaced, fitWidthPlaced, this::selectedPlaceHolder);
            addClickablePlaceholder(523, 439, fitHeightPlaced, fitWidthPlaced, this::selectedPlaceHolder);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/37.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 36){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/38.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 37){
            getCardFromPosition(layoutXCard1, layoutYHand).setOnMouseClicked(this::selectCard);
            getCardFromPosition(layoutXCard2, layoutYHand).setOnMouseClicked(this::selectCard);
            getCardFromPosition(layoutXCard0, layoutYHand).setOnMouseClicked(this::selectCard);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/39.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 38){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/40.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 39){
            mainPane2.setDisable(true);
            waitForClickPlace();
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/41.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 40){
            mainPane2.setDisable(false);
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/42.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 41){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/43.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 42){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/44.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 43){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/45.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 44){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/46.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 45){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/47.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 46){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/48.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 47){
            selectFromDeck();
            mainPane2.setDisable(true);
            waitForClickPick();
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/49.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 48){
            mainPane.setDisable(false);
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/50.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 49){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/51.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 50){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/52.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 51){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/53.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 52){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/54.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 53){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/55.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 54){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/56.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 55){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/57.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 56){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/58.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 57){
            Platform.runLater(SceneManager::MainView);
        }

    }

    private void removePlaceholders() {
        ImageView placeholder1 = getCardFromPosition(837, 278);
        ImageView placeholder2 = getCardFromPosition(523, 278);
        ImageView placeholder3 = getCardFromPosition(837, 439);
        ImageView placeholder4 = getCardFromPosition(523, 439);

        if(placeholder1 != null)
            fadeOutTransition(mainPane, placeholder1, 1, false);
        if(placeholder2 != null)
            fadeOutTransition(mainPane, placeholder2, 1, false);
        if(placeholder3 != null)
            fadeOutTransition(mainPane, placeholder3, 1, false);
        if(placeholder4 != null)
            fadeOutTransition(mainPane, placeholder4, 1, false);
    }

    private void removeObjectives() {
        ImageView obj1 = getCardFromPosition(949, layoutYHand);
        ImageView obj2 = getCardFromPosition(1279, layoutYHand);

        if(obj1 != null)
            fadeOutTransition(mainPane, obj1, 1, true);
        if(obj2 != null)
            fadeOutTransition(mainPane, obj2, 1, true);
    }


    /**
     * This method blocks the execution until the user clicks on the card to pick
     * @return true if the user has clicked on a card, false otherwise
     */
    private void waitForClickPick() {
        if(selectedCard != null ) {
            fadeOutTransition(mainPane, selectedCard, 1, false);
            selectedCard.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectPick);

            Card card = (Card) selectedCard.getUserData();

            ImageView newInHand = selectedCard;
            newInHand.setLayoutY(layoutYFree);
            newInHand.setLayoutX(layoutXFree);
            newInHand.setFitHeight(fitHeightCard);
            newInHand.setFitWidth(fitWidthCard);
            mainPane.getChildren().add(newInHand);
            fadeInTransition(newInHand, 1);

                if(selectedCard.getLayoutX() == layoutXPick0) { //resource
                    ImageView cardD1 = getCardFromPosition(layoutXDeck, layoutYResource);
                    if (card.getID() == f1.getID() || card.getID() == f2.getID()) {
                        replaceCardPickedUp(cardD1, d1.getID(), d1.isFront(), replaceR, layoutYResource);
                    } else if (card.getID() == d1.getID()) {
                        fadeOutTransition(mainPane, cardD1, 1, false);
                        ImageView replace = createCardImageView(replaceR.getID(), false, replaceR, layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon);
                        hooverEffect(replace, 1.05);
                        mainPane.getChildren().add(replace);
                        fadeInTransition(replace, 1);
                        onTop.toFront();
                    }
                } else { //gold
                    ImageView cardD2 = getCardFromPosition(layoutXDeck, layoutYGold);
                    if (card.getID() == f3.getID() || card.getID() == f4.getID()) {
                        replaceCardPickedUp(cardD2, d2.getID(), d2.isFront(), replaceG, layoutYGold);
                    } else if (card.getID() == d2.getID()) {
                        fadeOutTransition(mainPane, cardD2, 0.5, false);
                        ImageView replace = createCardImageView(replaceG.getID(), false, replaceG, layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon);
                        hooverEffect(replace, 1.05);
                        mainPane.getChildren().add(replace);
                        fadeInTransition(replace, 1);
                        onTop.toFront();
                    }
                }

            getCardFromPosition(layoutXPick0, layoutYResource).setOnMouseClicked(null);
            getCardFromPosition(layoutXPick1, layoutYResource).setOnMouseClicked(null);
            getCardFromPosition(layoutXPick0, layoutYGold).setOnMouseClicked(null);
            getCardFromPosition(layoutXPick1, layoutYGold).setOnMouseClicked(null);
            getCardFromPosition(layoutXDeck, layoutYResource).setOnMouseClicked(null);
            getCardFromPosition(layoutXDeck, layoutYGold).setOnMouseClicked(null);

            this.selectedCard = null;
            this.clickedPlaceholder = null;
        }
    }

    /**
     * This method replaces the card picked up with the card in the deck, and adds a new one to the deck
     * @param cardD1 the card in the deck
     * @param id the id of the card in the deck
     * @param front the front of the card in the deck
     * @param replaceR2 the card to replace the card in the deck
     * @param layoutYResource the y position of the card in the deck
     */
    private void replaceCardPickedUp(ImageView cardD1, int id, boolean front, Card replaceR2, double layoutYResource) {
        selectedCard.setImage(imageBinder.getOppositeImage(id, front)); //replace faceUp
        fadeOutTransition(mainPane, cardD1, 1, false);
        ImageView replace = createCardImageView(replaceR2.getID(), false, replaceR2, layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon);
        hooverEffect(replace, 1.05);
        mainPane.getChildren().add(replace);
        fadeInTransition(replace, 1);
        onTop.toFront();
    }

    /**
     * this method adds the selectPick event handler to the cards in the deck
     */
    private void selectFromDeck() {
        getCardFromPosition(layoutXDeck, layoutYResource).setOnMouseClicked(this::selectPick);
        getCardFromPosition(layoutXDeck, layoutYGold).setOnMouseClicked(this::selectPick);
        getCardFromPosition(layoutXPick0, layoutYResource).setOnMouseClicked(this::selectPick);
        getCardFromPosition(layoutXPick1, layoutYResource).setOnMouseClicked(this::selectPick);
        getCardFromPosition(layoutXPick0, layoutYGold).setOnMouseClicked(this::selectPick);
        getCardFromPosition(layoutXPick1, layoutYGold).setOnMouseClicked(this::selectPick);
    }

    /**
     * This method is called when the user clicks on a card in the deck, it will save the card as selected
     * @param mouseEvent the event triggered by the click
     */
    private void selectPick(MouseEvent mouseEvent) {
        this.selectedCard = (ImageView) mouseEvent.getSource();
    }

    /**
     * This method is called when the user clicks on a card, it will save the card as selected
     * @param mouseEvent the event triggered by the click
     */
    private void selectCard(MouseEvent mouseEvent) {
        ImageView clickedCard = (ImageView) mouseEvent.getSource();
        if (mouseEvent.getClickCount() == 1) {
            makeBiggerTransition(clickedCard, 1.05);
            this.selectedCard = clickedCard;

        } else if (mouseEvent.getClickCount() == 2) {
            turnAround(mouseEvent);
        }
    }


    /**
     * This method blocks the execution until the user clicks on a card and a placeholder
     */
    private void waitForClickPlace() {
        if(selectedCard != null && clickedPlaceholder != null) {
            fadeOutTransition(mainPane, selectedCard, 1, true);
            layoutYFree = clickedPlaceholder.getLayoutY();
            layoutXFree = clickedPlaceholder.getLayoutX();
            ImageView card1 = getCardFromPosition(layoutXCard0, layoutYHand);
            ImageView card2 = getCardFromPosition(layoutXCard1, layoutYHand);
            ImageView card3 = getCardFromPosition(layoutXCard2, layoutYHand);
            ImageView placeholder1 = getCardFromPosition(837, 278);
            ImageView placeholder2 = getCardFromPosition(523, 278);
            ImageView placeholder3 = getCardFromPosition(837, 439);
            ImageView placeholder4 = getCardFromPosition(523, 439);
            if(placeholder1 != null)
                placeholder1.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
            if(placeholder2 != null)
                placeholder2.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
            if(placeholder3 != null)
                placeholder3.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
            if(placeholder4 != null)
                placeholder4.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
            if(card1 != null)
                card1.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
            if(card2 != null)
                card2.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
            if(card3 != null)
                card3.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
            clickedPlaceholder.setImage(selectedCard.getImage());
            fadeInTransition(clickedPlaceholder, 1);
            fadeOutTransition(mainPane, clickedPlaceholder, 0.5, true);
            onTop.toFront();
            this.selectedCard = null;
            this.clickedPlaceholder = null;
            i++;
        }
    }

    private void waitForClickObjective( MouseEvent event) {
        clickedPlaceholder = (ImageView) event.getSource();

        if(selectedCard != null && clickedPlaceholder != null) {
                clickedPlaceholder.setImage(selectedCard.getImage());
                fadeInTransition(clickedPlaceholder, 1);
                clickedPlaceholder.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
                layoutYFree = clickedPlaceholder.getLayoutY();
                layoutXFree = clickedPlaceholder.getLayoutX();
                selectedCard.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
                onTop.toFront();
                this.selectedCard = null;
                this.clickedPlaceholder = null;
                fadeOutTransition(mainPane, selectedCard, 1, true);
                fadeOutTransition(mainPane, clickedPlaceholder, 0.5, true);


        }
    }

    private void waitForClickStarter(MouseEvent event) {
        clickedPlaceholder = (ImageView) event.getSource();
        if(selectedCard != null && clickedPlaceholder != null) {
//            fadeOutTransition(mainPane, clickedPlaceholder, 0.5, true);
            clickedPlaceholder.setImage(selectedCard.getImage());
            fadeInTransition(clickedPlaceholder, 1);
            fadeOutTransition(mainPane, selectedCard, 1, true);
            clickedPlaceholder.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
            selectedCard.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
            onTop.toFront();
            this.selectedCard = null;
            this.clickedPlaceholder = null;
            i++;
            mainPane2.setDisable(false);
        }
    }

    /**
     * This method is called when the user clicks on a placeholder, it will save the placeholder as clicked
     * @param event the event triggered by the click
     */
    private void selectedPlaceHolder(MouseEvent event) {
        this.clickedPlaceholder = (ImageView) event.getSource();
        if(selectedCard != null && clickedPlaceholder != null) {
            fadeOutTransition(mainPane, selectedCard, 1, true);
            fadeOutTransition(mainPane, clickedPlaceholder, 0.5, false);
            clickedPlaceholder.setImage(selectedCard.getImage());
            fadeInTransition(clickedPlaceholder, 1);
            clickedPlaceholder.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
            layoutYFree = clickedPlaceholder.getLayoutY();
            layoutXFree = clickedPlaceholder.getLayoutX();
            selectedCard.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
            this.selectedCard = null;
            this.clickedPlaceholder = null;
            mainPane2.setDisable(false);
        }
    }

    /**
     * This method adds a clickable placeholder to the screen
     * @param layoutX the x position of the placeholder
     * @param layoutY the y position of the placeholder
     * @param fitHeight the height of the placeholder
     * @param fitWidth the width of the placeholder
     * @param eventHandler the event handler that will be called when the user clicks on the placeholder
     */
    private void addClickablePlaceholder(double layoutX, double layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/yellow.png")));
        ImageView imageView = new ImageView(image);

        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitHeight(fitHeight);
        imageView.setFitWidth(fitWidth);
        imageView.setOnMouseClicked(eventHandler);

        mainPane.getChildren().add(imageView);
        fadeInTransition(imageView, 0.5);
    }

    /**
     * This method is called when the user clicks on a card, it will turn the card around
     * @param layoutXCard the x position of the card
     */
    private void turnAroundCards(double layoutXCard) {
        ImageView card = getCardFromPosition(layoutXCard, 814.0);
        if (card != null) {
            Card cardData = (Card) card.getUserData();
            card.setImage(imageBinder.getOppositeImage(cardData.getID(), cardData.isFront()));
            onTop.toFront();
        }
    }

    /**
     * This method turns around all the cards in the hand
     */
    private void turnAroundCardsHand() {
        turnAroundCards(layoutXCard0);
        turnAroundCards(layoutXCard1);
        turnAroundCards(layoutXCard2);
    }

    /**
     * This method adds the card to the player's hand
     */
    private void addCardsToHand() {
        ImageView image1 = createCardImageView(my1.getID(), my1.isFront(), my1, layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard);
        ImageView image2 = createCardImageView(my2.getID(), my2.isFront(), my2, layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard);
        ImageView image3 = createCardImageView(my3.getID(), my3.isFront(), my3, layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard);

        mainPane.getChildren().add(image1);
        mainPane.getChildren().add(image2);
        mainPane.getChildren().add(image3);

        fadeInTransition(image1, 1);
        fadeInTransition(image2, 1);
        fadeInTransition(image3, 1);

        hooverEffect(image1, 1.05);
        hooverEffect(image2, 1.05);
        hooverEffect(image3, 1.05);
        onTop.toFront();
    }

    /**
     * This method is called when the user clicks on a card, it will save the card as selected
     * @param event the event triggered by the click
     */
    private void chooseObj(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        makeBiggerTransition(clickedCard, 1.05);
        this.selectedCard = clickedCard;
    }

    /**
     * This method adds the cards to the objective area
     */
    public void addCommonObj(){
        ImageView obj1 = createCardImageView(common1.getID(),true, common1, layoutXObjective, 750, fitHeightCommon, fitWidthCommon);
        ImageView obj2 = createCardImageView(common2.getID(), true, common2, layoutXObjective, 894, fitHeightCommon, fitWidthCommon);

        mainPane.getChildren().add(obj1);
        mainPane.getChildren().add(obj2);

        fadeInTransition(obj1, 1);
        fadeInTransition(obj2, 1);

        hooverEffect(obj1, 1.05);
        hooverEffect(obj2, 1.05);
        onTop.toFront();
    }

    /**
     * This method adds the two objective cards to the player's hand
     */
    public void adMyObj(){
        ImageView obj1 = createCardImageView(myO.getID(), true, myO, 949, layoutYHand, fitHeightCard, fitWidthCard);
        ImageView obj2 = createCardImageView(myO1.getID(), true, myO1, 1279, layoutYHand, fitHeightCard, fitWidthCard);

        mainPane.getChildren().add(obj1);
        mainPane.getChildren().add(obj2);

        obj1.onMouseClickedProperty().set(this::chooseObj);
        obj2.onMouseClickedProperty().set(this::chooseObj);

        fadeInTransition(obj1, 1);
        fadeInTransition(obj2, 1);

        hooverEffect(obj1, 1.05);
        hooverEffect(obj2, 1.05);
        onTop.toFront();
    }


    /**
     * This method adds the starter card to the player's hand
     */
    public void addStarter(){
        myS.setFront(true);
        ImageView starter = createCardImageView(myS.getID(), true, myS, layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard);
        mainPane.getChildren().add(starter);
        fadeInTransition(starter, 1);
        hooverEffect(starter, 1.05);
        onTop.toFront();
    }

    /**
     * This method gets the card from the position
     * @param layoutX the x position of the card
     * @param layoutY the y position of the card
     * @return the card at the position
     */
    private ImageView getCardFromPosition(double layoutX, double layoutY) {
        ImageView imageView = null;

        for (Node node : mainPane.getChildren()) {
            if (node instanceof ImageView currentImageView) {
                if (currentImageView.getLayoutX() == layoutX && currentImageView.getLayoutY() == layoutY) {
                    imageView = currentImageView;
                    break;
                }
            }
        }
        return imageView;
    }


    /**
     * This method is called to make the previous explanation disappear and the new one appear
     * @param previous the previous explanation
     * @param image the new explanation
     */
    private void onCLick(ImageView previous, ImageView image){
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), previous);
        fadeTransitionOut.setFromValue(1);
        fadeTransitionOut.setToValue(0.0);

        fadeTransitionOut.setOnFinished(event -> {
            mainPane.getChildren().remove(previous);
            mainPane.getChildren().add(image);
        });

        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(1.0);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));

        SequentialTransition sequentialTransition = new SequentialTransition(
                fadeTransitionOut,
                pauseTransition,
                fadeTransitionIn
        );

        sequentialTransition.play();
    }

    /**
     * This method creates the cards
     */
    private void createCards() {
        try {
            d1 = new ResourceCard(32); //deckResource
            f1 = new ResourceCard(26); //front up
            f2 = new ResourceCard(14); //front up
            d2 = new GoldCard(74); //deckGold
            f3 = new GoldCard(68); //front up
            f4 = new GoldCard(56); //front up

            myS = new StarterCard(83); //myStarter
            myO = new ObjectiveCard(94); //myObjective
            myO1 = new ObjectiveCard(101); //player2Objective
            my1 = new ResourceCard(8); //hand 1
            my2 = new ResourceCard(2); //hand 2
            my3 = new GoldCard(50); //hand 3

            replaceR = new ResourceCard(20); //replaceResource
            replaceG = new GoldCard(62); //replaceGold

            common1 = new ObjectiveCard(100); //CommonObjective
            common2 = new ObjectiveCard(92); //CommonObjective
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        addCommonArea();
    }

    /**
     * This method adds the common area
     */
    private void addCommonArea() {
        ImageView d1 = createCardImageView(this.d1.getID(), false, this.d1, layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon);
        ImageView d2 = createCardImageView(this.d2.getID(), false, this.d2, layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon);
        ImageView f1 = createCardImageView(this.f1.getID(), true, this.f1, layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon);
        ImageView f2 = createCardImageView(this.f2.getID(), true, this.f2, layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon);
        ImageView f3 = createCardImageView(this.f3.getID(), true, this.f3, layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon);
        ImageView f4 = createCardImageView(this.f4.getID(), true, this.f4, layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon);

        mainPane.getChildren().add(d1);
        mainPane.getChildren().add(d2);
        mainPane.getChildren().add(f1);
        mainPane.getChildren().add(f2);
        mainPane.getChildren().add(f3);
        mainPane.getChildren().add(f4);

        fadeInTransition(d1, 1);
        fadeInTransition(d2, 1);
        fadeInTransition(f1, 1);
        fadeInTransition(f2, 1);
        fadeInTransition(f3, 1);
        fadeInTransition(f4, 1);

        hooverEffect(d1, 1.05);
        hooverEffect(d2, 1.05);

        onTop.toFront();
    }

    /**
     * This method is called when the user clicks on the back button
     * It will go back to the main view
     */
    @FXML
    public void back() {
        Platform.runLater(SceneManager::MainView);
    }

}
