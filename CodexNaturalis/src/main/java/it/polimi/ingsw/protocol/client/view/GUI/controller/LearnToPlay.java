package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.exceptions.InvalidIdException;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class LearnToPlay implements Initializable {

    @FXML
    public ImageView nextPlayer;
    @FXML
    public ImageView state;
    @FXML
    public Pane playground;
    @FXML
    public ImageView onTop;
    @FXML
    public ImageView colorName;
    @FXML
    public Label playerName;
    @FXML
    public Pane mainPane;
    @FXML
    public ImageView explanation;

    private final ImageView explanation2 = new ImageView();
    private ImageView selected;

    private final int layoutPlacedStarterX = 678;
    private final int layoutPlacedStarterY = 203;
    private final int fitHeightPlaced = 133;
    private final int fitWidthPlaced = 200;
    private final int fitHeightCommon = 141;
    private final int fitWidthCommon = 208;
    private final int fitHeightCard = 157;
    private final int fitWidthCard = 234;
    private final int layoutYResource = 746;
    private final int layoutXPick0 = 364;
    private final int layoutXPick1 = 587;
    private final int layoutYGold = 895;
    private final int layoutXDeck = 56;
    private final int layoutYObjMy = 606;
    private final int layoutYObj0 = 750;
    private final int layoutYObj1 = 894;
    private final int layoutXObjective = 1659;
    private final int layoutXChoiceObjective1 = 949;
    private final int layoutXChoiceObjective2 = 1279;
    private final int layoutYHand = 814;
    private final int layoutXCard0 = 864;
    private final int layoutXCard1 = 1110;
    private final int layoutXCard2 = 1356;

    ResourceCard d1;
    GoldCard d2;
    ResourceCard f1;
    ResourceCard f2;
    GoldCard f3;
    GoldCard f4;
    StarterCard myS;
    ObjectiveCard myO;
    ObjectiveCard myO1;
    ResourceCard my1;
    ResourceCard my2;
    GoldCard my3;
    ObjectiveCard common1;
    ObjectiveCard common2;
    ImageBinder imageBinder = new ImageBinder();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playerName.setText("Player 1");
        nextPlayer.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NextButton/nextBlue.png"))));
        colorName.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/purpleName.png"))));
        state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/state.png"))));

        createCards();
        addCommonArea();

        final int[] i = {0};
        playground.setOnMouseClicked(e -> {
            if (i[0] < 60) {
                clickHandler(i[0]);
                i[0]++;
            }
        });
    }

    //TODO add a block if the action I want is not performed

    private void clickHandler(int i) {
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
            turnStarter();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/14.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 13){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/15.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 14){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/16.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 15){
            getCardFromPosition(layoutXCard1, layoutYHand).setOnMouseClicked(this::selectStarter);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/17.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 16){
            addClickablePlaceholder(playground, layoutPlacedStarterX, layoutPlacedStarterY, fitHeightPlaced, fitWidthPlaced, this::placeStarter);
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/18.png"))));
            onCLick(explanation, explanation2);
        }  else if (i == 17){
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
            addClickablePlaceholder(mainPane, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::placeObj);
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/25.png"))));
        } else if (i == 24){
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
            turnAroundCards();
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/34.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 33){
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/35.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 34){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/36.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 35){
            // TODO seePlaceHolder(); of available positions
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/37.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 36){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/38.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 37){
            //TODO ADD select and turn around to card
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/39.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 38){
            explanation2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/40.png"))));
            onCLick(explanation, explanation2);
        } else if (i == 39){
            //TODO add click on placeholder
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/41.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 40){
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
            //TODO add pesco carta
            explanation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/rulesTxt/49.png"))));
            onCLick(explanation2, explanation);
        } else if (i == 48){
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

    private void selectStarter(MouseEvent event) {
            ImageView clickedCard = (ImageView) event.getSource();

            if (event.getClickCount() == 1) {
                makeBiggerTransition(clickedCard);

            } else if (event.getClickCount() == 2) {
                turnStarter();
            }
    }

    private void turnAround(int layoutXCard, int layoutYCard) {
        ImageView card = getCardFromPosition(layoutXCard, layoutYCard);
        if (card != null) {
            Card cardData = (Card) card.getUserData();
            card.setImage(imageBinder.getOppositeImage(cardData.getID(), cardData.isFront()));
        }
    }

    private void turnAroundCards() {
        turnAround(layoutXCard0, layoutYHand);
        turnAround(layoutXCard1, layoutYHand);
        turnAround(layoutXCard2, layoutYHand);
    }

    private void addCardsToHand() {
        addNewCardToPane(mainPane, my1.getID(), true, my1, layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, null);
        addNewCardToPane(mainPane, my2.getID(), true, my2, layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, null);
        addNewCardToPane(mainPane, my3.getID(), true, my3, layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, null);
    }

    private void chooseObj(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        makeBiggerTransition(clickedCard);
        selected = clickedCard;
    }

    private void placeStarter(MouseEvent event) {
        ImageView image = (ImageView) event.getSource();
        fadeOutTransition(mainPane, image, 0.5);
        removeCardFromPosition(layoutXCard1, layoutYHand);
        addNewCardToPane(playground, myS.getID(), myS.isFront(), myS, layoutPlacedStarterX, layoutPlacedStarterY,
                fitHeightPlaced, fitWidthPlaced, null);
    }

    public void placeObj(MouseEvent event){
        ImageView image = (ImageView) event.getSource();
        if(selected != null) {
            fadeOutTransition(mainPane, image, 0.5);
            ObjectiveCard obj = (ObjectiveCard) selected.getUserData();

            removeCardFromPosition(layoutXCard1, layoutYHand);
            addNewCardToPane(playground, obj.getID(), true, obj, layoutXObjective, layoutYObjMy,
                    fitHeightPlaced, fitWidthPlaced, null);
        }
    }

    public void addCommonObj(){
        addNewCardToPane(mainPane, common1.getID(), true, common1, layoutXObjective, layoutYObj0, fitHeightCommon, fitWidthCommon, null);
        addNewCardToPane(mainPane, common2.getID(), true, common2, layoutXObjective, layoutYObj1, fitHeightCommon, fitWidthCommon, null);
    }

    public void adMyObj(){
        addNewCardToPane(mainPane, myO.getID(), true, myO, layoutXChoiceObjective1, layoutYHand, fitHeightCard, fitWidthCard, this::chooseObj);
        addNewCardToPane(mainPane, myO1.getID(), true, myO1, layoutXChoiceObjective2, layoutYHand, fitHeightCard, fitWidthCard, this::chooseObj);
    }


    public void addStarter(){
        myS.setFront(true);
        addNewCardToPane(mainPane, myS.getID(), true, myS, layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, null);
    }

    private void turnStarter(){
        Image newImage = imageBinder.getOppositeImage(myS.getID(), myS.isFront());
        getCardFromPosition(layoutXCard1, layoutYHand).setImage(newImage);
    }


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

    private void createCards() {
        try {
            d1 = new ResourceCard(32);
            f1 = new ResourceCard(26); //front up
            f2 = new ResourceCard(14); //front up
            d2 = new GoldCard(74);
            f3 = new GoldCard(68); //front up
            f4 = new GoldCard(56); //front up

            myS = new StarterCard(83); //myStarter
            myO = new ObjectiveCard(94); //myObjective
            myO1 = new ObjectiveCard(101); //player2Objective
            my1 = new ResourceCard(8); //hand 1
            my2 = new ResourceCard(2); //hand 2
            my3 = new GoldCard(50); //hand 3

            common1 = new ObjectiveCard(100); //CommonObjective
            common2 = new ObjectiveCard(92); //CommonObjective
        } catch (InvalidIdException e) {
            throw new RuntimeException(e);
        }

        addCommonArea();
    }

    private void addClickablePlaceholder(Pane pane, int layoutX, int layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler) {
        Random random = new Random();
        int color = random.nextInt(5);

        Image image = switch (color) {
            case 0 ->
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/blue.png")));
            case 1 ->
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/green.png")));
            case 2 -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/red.png")));
            case 3 ->
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/yellow.png")));
            case 4 ->
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/purple.png")));
            default -> null;
        };

        // Check if image with the same position and size already exists
        boolean exists = pane.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .anyMatch(node -> {
                    ImageView imageView = (ImageView) node;
                    return (int) imageView.getLayoutX() == layoutX && (int) imageView.getLayoutY() == layoutY &&
                            (int) imageView.getFitWidth() == fitWidth && (int) imageView.getFitHeight() == fitHeight;
                });

        if (!exists) {
            ImageView imageView = new ImageView(image);
            imageView.setLayoutX(layoutX);
            imageView.setLayoutY(layoutY);
            imageView.setFitHeight(fitHeight);
            imageView.setFitWidth(fitWidth);
            imageView.getStyleClass().add("placeholder");
            imageView.setOnMouseClicked(eventHandler);
            pane.getChildren().add(imageView);
            fadeInTransition(imageView, 0.5);
        }
    }

    private void addCommonArea() {
        addNewCardToPane(mainPane, d1.getID(), false, d1, layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();

        addNewCardToPane(mainPane, d2.getID(), false, d2, layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();

        addNewCardToPane(mainPane, f1.getID(), true, f1, layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();
        addNewCardToPane(mainPane, f2.getID(), true, f2, layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();
        addNewCardToPane(mainPane, f3.getID(), true, f3, layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();
        addNewCardToPane(mainPane, f4.getID(), true, f4, layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();
    }

    private void pickCard(MouseEvent event) {
    }

    private void addNewCardToPane(Pane pane, int cardID, boolean front, Card card, int layoutX, int layoutY, int fitHeight, int fitWidth, EventHandler<MouseEvent> eventHandler) {

        ImageView existingCard = pane.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .map(node -> (ImageView) node)
                .filter(imageView -> {
                    Card existing = (Card) imageView.getUserData();
                    return existing != null && existing.getID() == cardID &&
                            (int) imageView.getLayoutX() == layoutX && (int) imageView.getLayoutY() == layoutY;
                })
                .findFirst()
                .orElse(null);

        if (existingCard == null) {
            // Card with the same ID and position doesn't exist, so remove the existing card and load the new one
            removeCardFromPosition(layoutX, layoutY);
            ImageView newCard = createCardImageView(cardID, front, card, layoutX, layoutY, fitHeight, fitWidth);
            if (eventHandler != null) {
                newCard.setOnMouseClicked(eventHandler);
            }
            if(card instanceof ObjectiveCard && pane == mainPane)
                hooverEffect(newCard);
            else if (card instanceof ObjectiveCard || card instanceof PlaceableCard && pane == mainPane)
                hooverEffect(newCard);{
            }

            Platform.runLater(() -> {
                pane.getChildren().add(newCard);
                fadeInTransition(newCard, 1.0);
                onTop.toFront();
            });
        }
    }

    private void fadeInTransition(Node node, double maxOpacity) {
        if (!mainPane.getChildren().contains(node)) { // Check if the node is not already a child
            FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), node);
            fadeTransitionIn.setFromValue(0.0);
            fadeTransitionIn.setToValue(maxOpacity);
            mainPane.getChildren().add(node); // Add the node to the pane
            fadeTransitionIn.play();
        }
    }

    private void removeCardFromPosition(double layoutX, double layoutY) {
        Platform.runLater(() -> {
            List<Node> nodesToRemove = mainPane.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .filter(node -> {
                        ImageView imageView = (ImageView) node;
                        return imageView.getLayoutX() == layoutX && imageView.getLayoutY() == layoutY;
                    })
                    .toList();

            // Remove nodes after iteration
            for (Node node : nodesToRemove) {
                ImageView imageView = (ImageView) node;
//                imageView.toFront(); // Card to the front
                onTop.toFront();
                if (imageView.getUserData() instanceof Card)
                    fadeOutTransition(mainPane, imageView, 1);
                else
                    fadeOutTransition(mainPane, imageView, 0.5);
            }
            onTop.toFront();
        });
    }

    private ImageView createCardImageView(int cardID, boolean front, Card card, int layoutX, int layoutY, double fitHeight, double fitWidth) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPreserveRatio(true);
        setupCard(imageView, cardID, front, card);
        return imageView;
    }

    private void setupCard(ImageView imageView, int cardID, boolean front, Card card) {
        ImageBinder imageBinder = new ImageBinder();
        ImageView cardImageView = imageBinder.bindImage(cardID, front);
        imageView.setImage(cardImageView.getImage());
        imageView.setUserData(card); // Store card ID and state
    }

    private void fadeOutTransition(Pane pane, Node node, double maxOpacity) {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), node);
        fadeTransitionOut.setFromValue(maxOpacity);
        fadeTransitionOut.setToValue(0.0);

        fadeTransitionOut.setOnFinished(event -> pane.getChildren().remove(node));

        fadeTransitionOut.play();
    }

    private void hooverEffect(ImageView imageView) {
        imageView.setOnMouseEntered(e -> makeBiggerTransition(imageView));
        imageView.setOnMouseExited(e -> {
            ImageView selectedCard = (ImageView) e.getSource();
            if (selectedCard != imageView)
                makeSmallerTransition(imageView);
        });
    }

    private void makeBiggerTransition(ImageView imageView) {
        ScaleTransition makeBigger = new ScaleTransition(Duration.millis(200), imageView);
        makeBigger.setToX(1.05);
        makeBigger.setToY(1.05);
        makeBigger.play();
    }

    private void makeSmallerTransition(ImageView imageView) {
        ScaleTransition makeSmaller = new ScaleTransition(Duration.millis(200), imageView);
        makeSmaller.setToX(1.0);
        makeSmaller.setToY(1.0);
        makeSmaller.play();
    }

    @FXML
    public void back(MouseEvent event) {
        Platform.runLater(SceneManager::MainView);
    }

}
