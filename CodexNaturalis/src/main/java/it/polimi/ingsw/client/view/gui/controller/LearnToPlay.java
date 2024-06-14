package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ImageBinder;
import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class LearnToPlay implements Initializable {

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
    private final ImageBinder imageBinder = new ImageBinder();
    private final ImageView explanation2 = new ImageView();
    int i = 0;
    boolean pick = true;
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

    /**
     * This method is called when the FXML file is loaded, it initializes the player name, the images and the cards
     * It also sets the click handler, which will be called when the user clicks on the screen to load the next explanation
     *
     * @param location  not used
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

        if (i == 0) {
            explanation2.setMouseTransparent(true);
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/2.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 1) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/3.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 2) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/4.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 3) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/5.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 4) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/6.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 5) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/7.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 6) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/8.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 7) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/9.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 8) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/10.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 9) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/59.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 10) {
            addStarter();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/11.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 11) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/12.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 12) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/13.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 13) {
            ImageView starterImg = Utilities.getCardFromPosition(layoutXCard1, layoutYHand, mainPane);
            starterImg.setImage(imageBinder.getOppositeImage(myS.getID(), myS.isFront()));
            myS.setFront(false);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/14.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 14) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/15.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 15) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/16.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 16) {
            Utilities.getCardFromPosition(layoutXCard1, layoutYHand, mainPane).setOnMouseClicked(this::selectCard);
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/17.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 17) {
            mainPane2.setDisable(true);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/18.png"))));
            addClickablePlaceholder(680, 359, fitHeightPlaced, fitWidthPlaced, this::waitForClickStarter);
            onCLick(explanation, explanation2);
        } else if (i == 18) {
            mainPane2.setDisable(false);
            addCommonObj();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/19.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 19) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/20.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 20) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/21.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 21) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/22.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 22) {
            adMyObj();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/24.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 23) {
            mainPane2.setDisable(true);
            pick = true;
            addClickablePlaceholder(layoutXObjective, 606, fitHeightCommon, fitWidthCommon, this::waitForClickObjective);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/25.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 24) {
            removeObjectives();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/26.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 25) {
            addCardsToHand();
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/27.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 26) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/28.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 27) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/29.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 28) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/30.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 29) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/31.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 30) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/32.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 31) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/33.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 32) {
            turnAroundCardsHand();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/34.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 33) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/35.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 34) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/36.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 35) {
            addClickablePlaceholder(837, 278, fitHeightPlaced, fitWidthPlaced, null);
            addClickablePlaceholder(523, 278, fitHeightPlaced, fitWidthPlaced, null);
            addClickablePlaceholder(837, 439, fitHeightPlaced, fitWidthPlaced, null);
            addClickablePlaceholder(523, 439, fitHeightPlaced, fitWidthPlaced, null);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/37.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 36) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/38.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 37) {
            Utilities.getCardFromPosition(layoutXCard1, layoutYHand, mainPane).setOnMouseClicked(this::selectCard);
            Utilities.getCardFromPosition(layoutXCard2, layoutYHand, mainPane).setOnMouseClicked(this::selectCard);
            Utilities.getCardFromPosition(layoutXCard0, layoutYHand, mainPane).setOnMouseClicked(this::selectCard);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/39.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 38) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/40.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 39) {
            mainPane2.setDisable(true);
            pick = true;
            Utilities.getCardFromPosition(837, 278, mainPane).setOnMouseClicked(this::selectedPlaceHolder);
            Utilities.getCardFromPosition(523, 278, mainPane).setOnMouseClicked(this::selectedPlaceHolder);
            Utilities.getCardFromPosition(837, 439, mainPane).setOnMouseClicked(this::selectedPlaceHolder);
            Utilities.getCardFromPosition(523, 439, mainPane).setOnMouseClicked(this::selectedPlaceHolder);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/41.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 40) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/42.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 41) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/43.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 42) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/44.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 43) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/45.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 44) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/46.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 45) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/47.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 46) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/48.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 47) {
            mainPane2.setDisable(true);
            pick = true;
            Utilities.getCardFromPosition(layoutXDeck, layoutYResource, mainPane).setOnMouseClicked(this::selectPick);
            Utilities.getCardFromPosition(layoutXDeck, layoutYGold, mainPane).setOnMouseClicked(this::selectPick);
            Utilities.getCardFromPosition(layoutXPick0, layoutYResource, mainPane).setOnMouseClicked(this::selectPick);
            Utilities.getCardFromPosition(layoutXPick1, layoutYResource, mainPane).setOnMouseClicked(this::selectPick);
            Utilities.getCardFromPosition(layoutXPick0, layoutYGold, mainPane).setOnMouseClicked(this::selectPick);
            Utilities.getCardFromPosition(layoutXPick1, layoutYGold, mainPane).setOnMouseClicked(this::selectPick);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/49.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 48) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/50.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 49) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/51.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 50) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/52.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 51) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/53.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 52) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/54.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 53) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/55.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 54) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/56.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 55) {
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/57.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 56) {
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/58.png"))));
            onCLick(explanation2, explanation);
        }

    }

    /**
     * This method is used to remove the objective cards from the screen
     */
    private void removeObjectives() {
        ImageView obj1 = Utilities.getCardFromPosition(949, layoutYHand, mainPane);
        ImageView obj2 = Utilities.getCardFromPosition(1279, layoutYHand, mainPane);

        if (obj1 != null)
            Utilities.fadeOutTransition(mainPane, obj1, 1, true);
        if (obj2 != null)
            Utilities.fadeOutTransition(mainPane, obj2, 1, true);
    }

    /**
     * This method is called when the user clicks on a card in the deck, it will save the card as selected
     *
     * @param mouseEvent the event triggered by the click
     */
    private void selectPick(MouseEvent mouseEvent) {
        this.selectedCard = (ImageView) mouseEvent.getSource();
        if (selectedCard != null && pick) {
            pick = false;
            double oldLayoutY = selectedCard.getLayoutY();
            double oldLayoutX = selectedCard.getLayoutX();
            selectedCard.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectPick);
            Card card = (Card) selectedCard.getUserData();
            selectedCard.setLayoutY(layoutYFree);
            selectedCard.setLayoutX(layoutXFree);
            selectedCard.setFitHeight(fitHeightCard);
            selectedCard.setFitWidth(fitWidthCard);

            if (card instanceof ResourceCard) { //resource
                replacePick(oldLayoutY, oldLayoutX, card, f1.getID(), f2.getID(), layoutYResource, d1.getID(), replaceR);
            } else { //gold
                replacePick(oldLayoutY, oldLayoutX, card, f3.getID(), f4.getID(), layoutYGold, d2.getID(), replaceG);
            }

            removeMouseClick(layoutXDeck, layoutXDeck, layoutXPick0, layoutYResource);
            removeMouseClick(layoutXPick1, layoutXPick0, layoutXPick1, layoutYGold);

            this.selectedCard = null;
            this.clickedPlaceholder = null;
            mainPane2.setDisable(false);
        }
    }

    /**
     * This method is used to remove the mouse click event from the cards
     *
     * @param layoutX     layoutX
     * @param layoutX2    layoutX2
     * @param layoutXPick layoutXPick0
     * @param layoutYRG   layoutYRG
     */
    private void removeMouseClick(double layoutX, double layoutX2, double layoutXPick, double layoutYRG) {
        if (Utilities.getCardFromPosition(layoutX, layoutYRG, mainPane) != null)
            Utilities.getCardFromPosition(layoutX, layoutYRG, mainPane).setOnMouseClicked(null);
        if (Utilities.getCardFromPosition(layoutX2, layoutYGold, mainPane) != null)
            Utilities.getCardFromPosition(layoutX2, layoutYGold, mainPane).setOnMouseClicked(null);
        if (Utilities.getCardFromPosition(layoutXPick, layoutYRG, mainPane) != null)
            Utilities.getCardFromPosition(layoutXPick, layoutYRG, mainPane).setOnMouseClicked(null);
    }

    /**
     * This method is used to replace the card picked from the common area with the card from the deck
     *
     * @param oldLayoutY oldLayout to be replaced
     * @param oldLayoutX oldLayout to be replaced
     * @param card       card picked
     * @param id         id of the card
     * @param id2        id of the card
     * @param layoutYRG  layoutYResource
     * @param idDeck     id of the deck
     * @param replaceR   replaceR card that will replace
     */
    private void replacePick(double oldLayoutY, double oldLayoutX, Card card, int id, int id2, double layoutYRG, int idDeck, Card replaceR) {
        if (card.getID() == id || card.getID() == id2) {
            ImageView card1 = Utilities.getCardFromPosition(layoutXDeck, layoutYRG, mainPane);
            card1.setLayoutX(oldLayoutX);
            card1.setLayoutY(oldLayoutY);
            card1.setImage(imageBinder.getOppositeImage(idDeck, false));
        }

        ImageView replace = Utilities.createCardImageView(replaceR.getID(), false, replaceR, layoutXDeck, layoutYRG, fitHeightCommon, fitWidthCommon);
        Utilities.hooverEffect(replace, 1.05);
        mainPane.getChildren().add(replace);
        onTop.toFront();

    }

    /**
     * This method is used when the users select his objective card
     *
     * @param event MouseEvent
     */
    private void waitForClickObjective(MouseEvent event) {
        clickedPlaceholder = (ImageView) event.getSource();
        if (selectedCard != null && clickedPlaceholder != null && pick) {
            pick = false;
            clickedPlaceholder.setImage(selectedCard.getImage());
            clickedPlaceholder.setOpacity(1);
            Utilities.hooverEffect(clickedPlaceholder, 1.05);
            clickedPlaceholder.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::waitForClickObjective);
            selectedCard.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
            onTop.toFront();
            Utilities.fadeOutTransition(mainPane, selectedCard, 1, true);
            this.selectedCard = null;
            this.clickedPlaceholder = null;
            mainPane2.setDisable(false);
        }
    }

    /**
     * This method is used when the user has to place in starter card
     *
     * @param event MouseEvent
     */
    private void waitForClickStarter(MouseEvent event) {
        clickedPlaceholder = (ImageView) event.getSource();
        if (selectedCard != null && clickedPlaceholder != null && pick) {
            pick = false;
            selectedCard.setLayoutX(clickedPlaceholder.getLayoutX());
            selectedCard.setLayoutY(clickedPlaceholder.getLayoutY());
            selectedCard.setFitHeight(clickedPlaceholder.getFitHeight());
            selectedCard.setFitWidth(clickedPlaceholder.getFitWidth());
            Utilities.removeHooverEffect(selectedCard);
            Utilities.fadeInTransition(clickedPlaceholder, 1);
            Utilities.fadeOutTransition(mainPane, clickedPlaceholder, 0.5, true);
            selectedCard.setOnMouseClicked(null);
            onTop.toFront();
            this.selectedCard = null;
            this.clickedPlaceholder = null;
            mainPane2.setDisable(false);
        }
    }

    /**
     * This method is called when the user has to place his card
     *
     * @param event MouseEvent
     */
    private void selectedPlaceHolder(MouseEvent event) {
        this.clickedPlaceholder = (ImageView) event.getSource();
        if (selectedCard != null && clickedPlaceholder != null && pick) {
            Card card = (Card) selectedCard.getUserData();
            if (selectedCard.getLayoutX() == layoutXCard2 && card.isFront()) {
                ImageView noPLace = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/TryAgain/retryPur.png"))));
                noPLace.preserveRatioProperty().setValue(true);
                noPLace.setFitWidth(646);
                noPLace.setFitHeight(167);
                noPLace.setLayoutX(1058);
                noPLace.setLayoutY(171);
                noPLace.setOpacity(0.0);
                mainPane.getChildren().add(noPLace);
                Utilities.fadeTransitionForPopUP(noPLace, mainPane);
                this.selectedCard = null;
            } else {
                pick = false;
                layoutYFree = selectedCard.getLayoutY();
                layoutXFree = selectedCard.getLayoutX();
                ImageView card1 = Utilities.getCardFromPosition(layoutXCard0, layoutYHand, mainPane);
                ImageView card2 = Utilities.getCardFromPosition(layoutXCard1, layoutYHand, mainPane);
                ImageView card3 = Utilities.getCardFromPosition(layoutXCard2, layoutYHand, mainPane);
                ImageView placeholder1 = Utilities.getCardFromPosition(837, 278, mainPane);
                ImageView placeholder2 = Utilities.getCardFromPosition(523, 278, mainPane);
                ImageView placeholder3 = Utilities.getCardFromPosition(837, 439, mainPane);
                ImageView placeholder4 = Utilities.getCardFromPosition(523, 439, mainPane);
                if (placeholder1 != null)
                    placeholder1.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
                if (placeholder2 != null)
                    placeholder2.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
                if (placeholder3 != null)
                    placeholder3.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
                if (placeholder4 != null)
                    placeholder4.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectedPlaceHolder);
                if (card1 != null)
                    card1.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
                if (card2 != null)
                    card2.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
                if (card3 != null)
                    card3.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCard);
                clickedPlaceholder.setImage(selectedCard.getImage());
                clickedPlaceholder.setOpacity(1);
                Utilities.fadeOutTransition(mainPane, selectedCard, 1, true);
                onTop.toFront();
                this.selectedCard = null;
                this.clickedPlaceholder = null;
                mainPane2.setDisable(false);
            }
        }
    }

    /**
     * This method adds a clickable placeholder to the screen
     *
     * @param layoutX      the x position of the placeholder
     * @param layoutY      the y position of the placeholder
     * @param fitHeight    the height of the placeholder
     * @param fitWidth     the width of the placeholder
     * @param eventHandler the event handler that will be called when the user clicks on the placeholder
     */
    private void addClickablePlaceholder(double layoutX, double layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler) {
        Image image = Utilities.randomColorForPlaceholder();
        Utilities.createClickablePane(mainPane, layoutX, layoutY, fitHeight, fitWidth, eventHandler, image);

//        TODO delete this after checking if works

//        ImageView imageView = new ImageView(image);
//
//        imageView.setLayoutX(layoutX);
//        imageView.setLayoutY(layoutY);
//        imageView.setFitHeight(fitHeight);
//        imageView.setFitWidth(fitWidth);
//        imageView.setOnMouseClicked(eventHandler);
//
//        mainPane.getChildren().add(imageView);
//        fadeInTransition(imageView, 0.5);
    }

    /**
     * This method is called when the user clicks on a card, it will turn the card around
     *
     * @param layoutXCard the x position of the card
     */
    private void turnAroundCards(double layoutXCard) {
        ImageView card = Utilities.getCardFromPosition(layoutXCard, 814.0, mainPane);
        if (card != null) {
            Card cardData = (Card) card.getUserData();
            card.setImage(imageBinder.getOppositeImage(cardData.getID(), cardData.isFront()));
            cardData.setFront(!cardData.isFront());
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
        ImageView image1 = Utilities.createCardImageView(my1.getID(), my1.isFront(), my1, layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard);
        ImageView image2 = Utilities.createCardImageView(my2.getID(), my2.isFront(), my2, layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard);
        ImageView image3 = Utilities.createCardImageView(my3.getID(), my3.isFront(), my3, layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard);

        mainPane.getChildren().add(image1);
        mainPane.getChildren().add(image2);
        mainPane.getChildren().add(image3);

        Utilities.fadeInTransition(image1, 1);
        Utilities.fadeInTransition(image2, 1);
        Utilities.fadeInTransition(image3, 1);

        Utilities.hooverEffect(image1, 1.05);
        Utilities.hooverEffect(image2, 1.05);
        Utilities.hooverEffect(image3, 1.05);
        onTop.toFront();
    }

    /**
     * This method is called when the user clicks on a card, it will save the card as selected
     *
     * @param mouseEvent the event triggered by the click
     */
    private void selectCard(MouseEvent mouseEvent) {
        ImageView clickedCard = (ImageView) mouseEvent.getSource();
        if (mouseEvent.getClickCount() == 1) {
            Utilities.makeBiggerTransition(clickedCard, 1.05);
            this.selectedCard = clickedCard;

        } else if (mouseEvent.getClickCount() == 2) {
            Utilities.turnAround(mouseEvent);
        }
    }

    /**
     * This method adds the cards to the objective area
     */
    private void addCommonObj() {
        ImageView obj1 = Utilities.createCardImageView(common1.getID(), true, common1, layoutXObjective, 750, fitHeightCommon, fitWidthCommon);
        ImageView obj2 = Utilities.createCardImageView(common2.getID(), true, common2, layoutXObjective, 894, fitHeightCommon, fitWidthCommon);

        mainPane.getChildren().add(obj1);
        mainPane.getChildren().add(obj2);

        Utilities.fadeInTransition(obj1, 1);
        Utilities.fadeInTransition(obj2, 1);

        Utilities.hooverEffect(obj1, 1.05);
        Utilities.hooverEffect(obj2, 1.05);
        onTop.toFront();
    }

    /**
     * This method adds the two objective cards to the player's hand
     */
    private void adMyObj() {
        ImageView obj1 = Utilities.createCardImageView(myO.getID(), true, myO, 949, layoutYHand, fitHeightCard, fitWidthCard);
        ImageView obj2 = Utilities.createCardImageView(myO1.getID(), true, myO1, 1279, layoutYHand, fitHeightCard, fitWidthCard);

        mainPane.getChildren().add(obj1);
        mainPane.getChildren().add(obj2);

        obj1.onMouseClickedProperty().set(this::selectCard);
        obj2.onMouseClickedProperty().set(this::selectCard);

        Utilities.fadeInTransition(obj1, 1);
        Utilities.fadeInTransition(obj2, 1);

        Utilities.hooverEffect(obj1, 1.05);
        Utilities.hooverEffect(obj2, 1.05);
        onTop.toFront();
    }


    /**
     * This method adds the starter card to the player's hand
     */
    private void addStarter() {
        myS.setFront(true);
        ImageView starter = Utilities.createCardImageView(myS.getID(), true, myS, layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard);
        mainPane.getChildren().add(starter);
        Utilities.fadeInTransition(starter, 1);
        Utilities.hooverEffect(starter, 1.05);
        onTop.toFront();
    }


    /**
     * This method is called to make the previous explanation disappear and the new one appear
     *
     * @param previous the previous explanation
     * @param image    the new explanation
     */
    private void onCLick(ImageView image, ImageView previous) {
        if (!mainPane.getChildren().contains(image)) {
            mainPane2.setDisable(true);
            FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), previous);
            fadeTransitionOut.setFromValue(1);
            fadeTransitionOut.setToValue(0.0);

            fadeTransitionOut.setOnFinished(event -> {
                mainPane.getChildren().remove(previous); // Remove the previous ImageView
                mainPane.getChildren().add(image); // Add the new ImageView
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
            sequentialTransition.onFinishedProperty().set(event -> mainPane2.setDisable(false));

            sequentialTransition.play();
        }
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
            my2 = new ResourceCard(34); //hand 2
            my3 = new GoldCard(51); //hand 3

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
        ImageView d1 = Utilities.createCardImageView(this.d1.getID(), false, this.d1, layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon);
        ImageView d2 = Utilities.createCardImageView(this.d2.getID(), false, this.d2, layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon);
        ImageView f1 = Utilities.createCardImageView(this.f1.getID(), true, this.f1, layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon);
        ImageView f2 = Utilities.createCardImageView(this.f2.getID(), true, this.f2, layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon);
        ImageView f3 = Utilities.createCardImageView(this.f3.getID(), true, this.f3, layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon);
        ImageView f4 = Utilities.createCardImageView(this.f4.getID(), true, this.f4, layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon);

        mainPane.getChildren().add(d1);
        mainPane.getChildren().add(d2);
        mainPane.getChildren().add(f1);
        mainPane.getChildren().add(f2);
        mainPane.getChildren().add(f3);
        mainPane.getChildren().add(f4);

        Utilities.fadeInTransition(d1, 1);
        Utilities.fadeInTransition(d2, 1);
        Utilities.fadeInTransition(f1, 1);
        Utilities.fadeInTransition(f2, 1);
        Utilities.fadeInTransition(f3, 1);
        Utilities.fadeInTransition(f4, 1);

        Utilities.hooverEffect(d1, 1.05);
        Utilities.hooverEffect(d2, 1.05);
        Utilities.hooverEffect(f1, 1.05);
        Utilities.hooverEffect(f2, 1.05);
        Utilities.hooverEffect(f3, 1.05);
        Utilities.hooverEffect(f4, 1.05);

        onTop.toFront();
    }

    /**
     * This method is called when the user clicks on the back button
     * It will go back to the main view
     */
    @FXML
    private void back() {
        Platform.runLater(SceneManager::MainView);
    }

}
