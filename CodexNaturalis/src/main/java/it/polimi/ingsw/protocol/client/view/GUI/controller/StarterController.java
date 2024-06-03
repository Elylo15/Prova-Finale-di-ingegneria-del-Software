package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StarterController implements Initializable {
    @FXML
    public Pane mainPane;
    @FXML
    public Label playerName;
    private boolean isPopupVisible = false;
    private ImageView currentPopupImage;

    int fitHeightCommon = 141;
    int fitWidthCommon = 208;

    int fitHeightCard = 157;
    int fitWidthCard = 234;

    int layoutYResource = 746;

    int layoutXPick0 = 364;
    int layoutXPick1 = 587;
    int layoutYGold = 895;

    int layoutXDeck = 56;

    int layoutYObjMy = 606;
    int layoutYObj0 = 750;
    int layoutYObj1 = 894;
    int layoutXObjective = 1659;

    int layoutXChoiceObjective1 = 949;
    int layoutXChoiceObjective2 = 1279;

    int layoutYHand = 814;

    int layoutXCard0 = 864;
    int layoutXCard1 = 1110;
    int layoutXCard2 = 1356;

    private final BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<>();

    private Player myself;
    private PlayerHand myHand;
    private ObjectiveCard myObjective;
    private PlayerArea myPlayerArea;
    private String currentPlayerNickname;

    private CommonArea commonArea;
    private ArrayList<ObjectiveCard> commonObjectives;

    private final ArrayList<Player> players = new ArrayList<>();

    private final ArrayList<ObjectiveCard> objectivesToChose = new ArrayList<>();

    private String currentState;

    private int clickCounter = -1;

    private int selectedStarterFront;
    private int selectedPick;
    private int selectedObjective;
    private ImageView selectedCard;


    @FXML
    private ImageView onTop;

    //TODO: LOAD THEM BASED ON COLOR
    @FXML
    private ImageView firstPion;
    @FXML
    private ImageView secondPion;
    @FXML
    private ImageView thirdPion;
    @FXML
    private ImageView fourthPion;

    /**
     *
     * It is used to initialize the controller, it starts the message listener and processor threads.
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startMessageListener();
        startMessageProcessor();
    }

    /**
     * Starts a new internal thread to listen for messages
     */
    private void startMessageListener() {
        new Thread(() -> {
            while (true) { // You might want to add a stopping condition
                try {
                    Object message = GUIMessages.readToGUI();
                    if (message != null) {
                        messageQueue.put(message);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Starts a new internal thread to process messages
     */
    private void startMessageProcessor() {
        // Start a new thread to process messages from the queue
        new Thread(() -> {
            while (true) { // You might want to add a stopping condition
                try {
                    Object message = messageQueue.take();

                    Platform.runLater(() -> {
                        processMessage(message);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Processes the message received from the server based on its type
     * @param message the message to process
     */
    private void processMessage(Object message) {
        switch (message) {
            case currentStateMessage currentStateMessage -> {
                this.currentState = currentStateMessage.getStateName();
                currentStateCase(currentStateMessage);

                if (currentStateMessage.getCurrentPlayer().getNickname().equals(myself.getNickname())) {
                    if (Objects.equals(currentState, "StarterCardState"))
                        starterCase();
                    else if (Objects.equals(currentState, "PlaceTurnState"))
                        placeTurnCase(currentStateMessage.isLastTurn());
                    else if (Objects.equals(currentState, "PickTurnState"))
                        pickTurnCase();
                }
            }
            case ArrayList<?> list -> {
                if (Objects.equals(currentState, "ObjectiveState") && !list.isEmpty() && list.getFirst() instanceof ObjectiveCard) {
                    objectivesToChose.add((ObjectiveCard) list.get(0));
                    objectivesToChose.add((ObjectiveCard) list.get(1));
                    objectiveCase();
                }
            }
            case updatePlayerMessage update -> updatePlayerCase(update);

            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }


    private void currentStateCase(currentStateMessage message) {
        hidePopup();
        this.myself = message.getPlayer();
        this.commonArea = message.getPlayer().getCommonArea();
        this.commonObjectives = message.getCommonObjectiveCards();
        this.myHand = message.getPlayer().getPlayerHand();
        this.myObjective = message.getPlayer().getObjective();
        this.myPlayerArea = message.getPlayer().getPlayerArea();

        Player currentPlayer = message.getCurrentPlayer();
        currentPlayerNickname = currentPlayer.getNickname();
        if(!Objects.equals(myself.getNickname(), currentPlayer.getNickname())) {
            if (existentPlayer(currentPlayer.getNickname()))
                setPlayer(currentPlayer);
            else
                addPlayer(currentPlayer);
        }

        addCardsToCommonArea();
        onTop.toFront();
        setPage();
        onTop.toFront();

        //TODO: SET PIONS
        //TODO: SET PLAYER AREA

    }

    private void starterCase() {
        hidePopup();
        showImagePopup("/img/Background/StarterCard.png", 593, 298, 226, 655);
        if (clickCounter == -1 && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            addStarterCardsToPane();
            addClickablePane(12, 151, 552, 1568, this::confirmPlaceStarter);
        }
    }

    private void objectiveCase() {
        hidePopup();
        showImagePopup("/img/Background/ObjectiveCard.png", 593, 298, 226, 655);
        if (clickCounter == -1 && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            setObjectives();
            addClickablePane(1651, 605, 141, 225, this::confirmPlaceObjective);
        }
    }

    private void placeTurnCase(boolean isLastTurn) {
        //TODO: PLACE CARD make pop up okay in dimensions
        hidePopup();
        if(isLastTurn)
            showImagePopup("/img/Background/LastTurnStarted.png", 593, 298, 226, 655);
        else
            showImagePopup("/img/Background/YourTurnStarted.png", 593, 298, 226, 655);

    }

    private void pickTurnCase() {
        //TODO: I THINK THIS DOES NOTHING ELSE
    }

    private void updatePlayerCase(updatePlayerMessage update) {
        hidePopup();

        Player currentPlayer = update.getPlayer();
        if(!Objects.equals(myself.getNickname(), currentPlayer.getNickname())) {
            if (existentPlayer(currentPlayer.getNickname()))
                setPlayer(currentPlayer);
            else
                addPlayer(currentPlayer);
        } else {
            this.myself = update.getPlayer();
            this.myHand = update.getPlayer().getPlayerHand();
            this.myObjective = update.getPlayer().getObjective();
            this.myPlayerArea = update.getPlayer().getPlayerArea();
        }

        this.commonArea = update.getPlayer().getCommonArea();
        addCardsToCommonArea();
        onTop.toFront();
        setPage();
        onTop.toFront();
    }

    private void setPage() {

        String nickname = (clickCounter == -1) ? myself.getNickname() : players.get(clickCounter).getNickname();
        playerName.setText(nickname);

        if (!currentState.equals("StarterCardState") && !currentState.equals("ObjectiveState")) {
            if (clickCounter == -1) {
                addCardsToHand();
                addMyObjective();
            } else {
                addPlayerCardsToHand();
                addPlayerObjective();
            }
        }
    }

    public void addCardsToCommonArea(){
        //Add the front up cards to commonArea
        Platform.runLater(() -> {
            addNewCardToMainPane(commonArea.getTableCards().getFirst().getID(), true,
                    commonArea.getTableCards().getFirst(), layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToMainPane(commonArea.getTableCards().get(1).getID(), true,
                    commonArea.getTableCards().get(1), layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToMainPane(commonArea.getTableCards().get(2).getID(), true,
                    commonArea.getTableCards().get(2), layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToMainPane(commonArea.getTableCards().get(3).getID(), true,
                    commonArea.getTableCards().get(3), layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            //Add the cards to deck
            addNewCardToMainPane(commonArea.getD1().getList().getFirst().getID(),false,
                    commonArea.getD1().getList().getFirst(), layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToMainPane(commonArea.getD2().getList().getFirst().getID(),false,
                    commonArea.getD2().getList().getFirst(), layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            //Add objective cards
            if(commonObjectives != null && !commonObjectives.isEmpty()) {
                addNewCardToMainPane(commonObjectives.get(0).getID(), true, commonObjectives.get(0), layoutXObjective,
                        layoutYObj0, fitHeightCommon, fitWidthCommon, this::turnAround);
                onTop.toFront();
                addNewCardToMainPane(commonObjectives.get(1).getID(), true, commonObjectives.get(1), layoutXObjective,
                        layoutYObj1, fitHeightCommon, fitWidthCommon, this::turnAround);
                onTop.toFront();
            }
        });
    }

    private void addCardsToHand(){
        Platform.runLater(() -> {
            addNewCardToMainPane(myHand.getPlaceableCards().getFirst().getID(), true,
                    myHand.getPlaceableCards().getFirst(), layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, this::placeCard);
            onTop.toFront();
            addNewCardToMainPane(myHand.getPlaceableCards().get(1).getID(), true,
                    myHand.getPlaceableCards().get(1), layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::placeCard);
            onTop.toFront();
            addNewCardToMainPane(myHand.getPlaceableCards().get(2).getID(), true,
                    myHand.getPlaceableCards().get(2), layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::placeCard);
            onTop.toFront();
        });
    }

    private void placeCard(MouseEvent event) {

    }

    public void addMyObjective(){
        Platform.runLater(() -> {
            addNewCardToMainPane(myObjective.getID(), true, myObjective, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::turnAround);
            onTop.toFront();
        });
    }

    private void addPlayerCardsToHand() {
        if(players.get(clickCounter) != null) {
            PlayerHand playerHand = players.get(clickCounter).getPlayerHand();
            Platform.runLater(() -> {
                addNewCardToMainPane(playerHand.getPlaceableCards().getFirst().getID(), false, playerHand.getPlaceableCards().getFirst(),
                        layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, null);
                onTop.toFront();
                addNewCardToMainPane(playerHand.getPlaceableCards().get(1).getID(), false, playerHand.getPlaceableCards().get(1),
                        layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, null);
                onTop.toFront();
                addNewCardToMainPane(playerHand.getPlaceableCards().get(2).getID(), false, playerHand.getPlaceableCards().get(2),
                        layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, null);
                onTop.toFront();
            });
        }
    }

    public void addPlayerObjective() {
        if(players.size() > clickCounter && players.get(clickCounter) != null) {
            Platform.runLater(() -> {
                addNewCardToMainPane(players.get(clickCounter).getObjective().getID(), false, players.get(clickCounter).getObjective(),
                        layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, null);
                onTop.toFront();
            });
        }
    }

    private void addStarterCardsToPane() {
        Platform.runLater(() -> {
            addNewCardToMainPane(myself.getPlayerHand().getPlaceableCards().getFirst().getID(),true,
                    myself.getPlayerHand().getPlaceableCards().getFirst(), layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::placeStarter);
            onTop.toFront();
        });
    }


    public void setObjectives() {
        Platform.runLater(() -> {
            addNewCardToMainPane(objectivesToChose.get(0).getID(), true, objectivesToChose.get(0), layoutXChoiceObjective1, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            onTop.toFront();
            addNewCardToMainPane(objectivesToChose.get(1).getID(), true, objectivesToChose.get(1), layoutXChoiceObjective2, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            onTop.toFront();
        });
    }

    private void addClickablePane(int layoutX, int layoutY, int fitHeight, int fitWidth, EventHandler<MouseEvent> eventHandler){
        // Check if a pane with the same position and size already exists
        boolean paneExists = mainPane.getChildren().stream()
                .filter(node -> node instanceof Pane)
                .anyMatch(node -> {
                    Pane pane = (Pane) node;
                    return (int) pane.getLayoutX() == layoutX && (int) pane.getLayoutY() == layoutY &&
                            (int) pane.getPrefWidth() == fitWidth && (int) pane.getPrefHeight() == fitHeight;
                });

        if (!paneExists) {
            // Pane with the same position and size doesn't exist, so add the new one
            Pane clickableArea = new Pane();
            clickableArea.setLayoutX(layoutX);
            clickableArea.setLayoutY(layoutY);
            clickableArea.setPrefSize(fitWidth, fitHeight);
            clickableArea.setStyle("-fx-background-color: lightblue;");
            clickableArea.setOpacity(0.2);
            mainPane.getChildren().add(clickableArea);
            clickableArea.setOnMouseClicked(eventHandler);
        }
        onTop.toFront();
    }

    private boolean existentPlayer(String currentPlayer) {
        for(Player player : players) {
            if(player.getNickname().equals(currentPlayer))
                return true;
        }
        return false;
    }

    private void setPlayer(Player currentPlayer) {
        for(int i = 0 ; i < players.size() ; i++) {
            if(players.get(i).getNickname().equals(currentPlayer.getNickname())) {
                players.set(i, currentPlayer);
            }
        }
    }

    private void addPlayer(Player currentPlayer) {
        players.add(currentPlayer);
    }

    private void removeCardFromPosition(int layoutX, int layoutY) {
        Platform.runLater(() -> {
            mainPane.getChildren().removeIf(node -> {
                if (node instanceof ImageView imageView) {
                    return (int) imageView.getLayoutX() == layoutX && (int) imageView.getLayoutY() == layoutY;
                }
                return false;
            });
            onTop.toFront();
        });
    }

    private void setupCard(ImageView imageView, int cardID, boolean front, Card card) {
        ImageBinder imageBinder = new ImageBinder();
        ImageView cardImageView = imageBinder.bindImage(cardID, front);
        imageView.setImage(cardImageView.getImage());
        imageView.setUserData(card); // Store card ID and state
    }

    private ImageView createCardImageView(int cardID, boolean front, Card card, int layoutX, int layoutY, int fitHeight, int fitWidth) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPreserveRatio(true);
        setupCard(imageView, cardID, front, card);
        return imageView;
    }

    private void addNewCardToMainPane(int cardID, boolean front, Card card, int layoutX, int layoutY, int fitHeight, int fitWidth, EventHandler<MouseEvent> eventHandler) {
        ImageView existingCard = mainPane.getChildren().stream()
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
            Platform.runLater(() -> {
                mainPane.getChildren().add(newCard);
                onTop.toFront();
            });
            // Card with the same ID and position exists, do nothing
        }
    }

    private void turnAround(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        Card card = (Card) clickedCard.getUserData();

        if (card != null) {
            ImageBinder imageBinder = new ImageBinder();
            Image newImage = imageBinder.getOppositeImage(card.getID(), card.isFront());
            clickedCard.setImage(newImage);

            // Update the card state
            card.setFront(!card.isFront());
            onTop.toFront();
        }
    }

    private void placeStarter(MouseEvent event) {
        if (clickCounter == -1) {
            ImageView clickedCard = (ImageView) event.getSource();
            Card card = (Card) clickedCard.getUserData();

            // Remove border from previously selected card
            if (selectedCard != null) {
                selectedCard.getStyleClass().remove("image-view-selected");
            }

            // Set blue border for the selected card
            clickedCard.getStyleClass().add("image-view-selected");
            this.selectedCard = clickedCard;

            // Set the selected card ID
            this.selectedStarterFront = card.isFront() ? 1 : 0;

            // Handle double-click to turn around the card
            if (event.getClickCount() == 2) {
                turnAround(event);
            }
        }
    }

    private void confirmPlaceStarter(MouseEvent event) {
        if (selectedCard != null) {
            selectedCard.getStyleClass().remove("image-view-selected");
            mainPane.getChildren().remove(event.getSource());
            removeCardFromPosition(layoutXCard1, layoutYHand);
            GUIMessages.writeToClient(selectedStarterFront);
        }
    }

    private void chooseObjective(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        ObjectiveCard card = (ObjectiveCard) clickedCard.getUserData();
        int cardID = card.getID();

        if (clickCounter == -1) {
            if (selectedCard != null) {
                selectedCard.getStyleClass().remove("image-view-selected");
            }

            clickedCard.getStyleClass().add("image-view-selected");
            this.selectedCard = clickedCard;
            if(cardID == objectivesToChose.get(0).getID())
                this.selectedObjective = 1;
            else if(cardID == objectivesToChose.get(1).getID())
                this.selectedObjective = 2;

            if (event.getClickCount() == 2) {
                turnAround(event);
            }
        }
    }

    private void confirmPlaceObjective(MouseEvent event) {
        if (selectedCard != null) {
            selectedCard.getStyleClass().remove("image-view-selected");
            mainPane.getChildren().remove(event.getSource());
            removeCardFromPosition(layoutXChoiceObjective1, layoutYHand);
            removeCardFromPosition(layoutXChoiceObjective2, layoutYHand);
            GUIMessages.writeToClient(selectedObjective);
        }
    }

    //TODO ADD IMG TO HAND
    private void pickCard(MouseEvent event) {
        if (clickCounter == -1 && Objects.equals(currentState, "PickTurnState")) {
            ImageView clickedCard = (ImageView) event.getSource();
            Card card = (Card) clickedCard.getUserData();
            int cardID = card.getID();

            if (selectedCard != null)
                selectedCard.getStyleClass().remove("image-view-selected");

            clickedCard.getStyleClass().add("image-view-selected");
            this.selectedCard = clickedCard;
            if (cardID == commonArea.getD1().getList().getFirst().getID())
                this.selectedPick = 1;
            else if (cardID == commonArea.getD2().getList().getFirst().getID())
                this.selectedPick = 2;
            else if (cardID == commonArea.getTableCards().getFirst().getID())
                this.selectedPick = 3;
            else if (cardID == commonArea.getTableCards().get(1).getID())
                this.selectedPick = 4;
            else if (cardID == commonArea.getTableCards().get(2).getID())
                this.selectedPick = 5;
            else if (cardID == commonArea.getTableCards().get(3).getID())
                this.selectedPick = 6;

            if (event.getClickCount() == 2) {
                GUIMessages.writeToClient(selectedStarterFront);
                if (selectedPick == 1) {
                    Platform.runLater(() -> {
                        removeCardFromPosition(layoutXDeck, layoutYResource);
                        addNewCardToMainPane(commonArea.getD1().getList().get(1).getID(), false, commonArea.getD1().getList().get(1),
                                layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
                        onTop.toFront();
                    });
                } else if (selectedPick == 2) {
                    Platform.runLater(() -> {
                        removeCardFromPosition(layoutXDeck, layoutYGold);
                        addNewCardToMainPane(commonArea.getD2().getList().get(1).getID(), false, commonArea.getD2().getList().get(1),
                                layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
                        onTop.toFront();
                    });
                } else if (selectedPick == 3) {
                    pickCardResource(layoutXPick0);
                } else if (selectedPick == 4) {
                    pickCardResource(layoutXPick1);
                } else if (selectedPick == 5) {
                    pickCardGold(layoutXPick0);
                } else if (selectedPick == 6) {
                    pickCardGold(layoutXPick1);
                }
            }

        }
    }

    private void pickCardGold(int layoutXPick0) {
        Platform.runLater(() -> {
            removeCardFromPosition(layoutXPick0, layoutYGold);
            removeCardFromPosition(layoutXDeck, layoutYGold);
            addNewCardToMainPane(commonArea.getD2().getList().get(1).getID(), false, commonArea.getD2().getList().get(1),
                    layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToMainPane(commonArea.getD2().getList().get(0).getID(), true, commonArea.getD2().getList().get(0),
                    layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
        });
    }

    private void pickCardResource(int layoutXPick1) {
        Platform.runLater(() -> {
            removeCardFromPosition(layoutXPick1, layoutYResource);
            removeCardFromPosition(layoutXDeck, layoutYResource);
            addNewCardToMainPane(commonArea.getD1().getList().get(1).getID(), false, commonArea.getD1().getList().get(1),
                    layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToMainPane(commonArea.getD1().getList().get(0).getID(), true, commonArea.getD1().getList().get(0),
                    layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
        });
    }

    @FXML
    private void switchToNextPlayer(MouseEvent event) {
        if(!Objects.equals(currentState,"StarterCardState") && !Objects.equals(currentState, "ObjectiveState")) {
            clickCounter++;
            if (clickCounter == players.size()) {
                clickCounter = -1;
            }
            setPage();
            onTop.toFront();
        } else if (Objects.equals(currentState, "StarterCardState")) {
            hidePopup();
            showImagePopup("/img/Background/StarterCard.png", 593, 298, 226, 655);
        } else if (Objects.equals(currentState, "ObjectiveState")) {
            hidePopup();
            showImagePopup("/img/Background/ObjectiveCard.png", 593, 298, 226, 655);
        }
    }

    private void showImagePopup(String imagePath, int layoutX, int layoutY, int fitHeight, int fitWidth) {
        if (isPopupVisible) {
            return; // Do not show another popup if one is already visible
        }

        Platform.runLater(() -> {
            Image imageLoad = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            ImageView image = new ImageView(imageLoad);
            currentPopupImage = image; // Store reference to the current popup image

            image.setFitWidth(fitWidth);
            image.setFitHeight(fitHeight);
            image.setLayoutX(layoutX);
            image.setLayoutY(layoutY);
            image.setOpacity(0.0); // Initially invisible

            mainPane.getChildren().add(image);

            // Fade-in transition
            FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), image);
            fadeTransitionIn.setFromValue(0.0);
            fadeTransitionIn.setToValue(1.0);

            // Pause to keep the image visible
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));

            // Fade-out transition
            FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), image);
            fadeTransitionOut.setFromValue(1.0);
            fadeTransitionOut.setToValue(0.0);

            // After fade-out, remove the image from the pane and update the flag
            fadeTransitionOut.setOnFinished(event -> {
                mainPane.getChildren().remove(image);
                isPopupVisible = false; // Update the flag to indicate no popup is visible
            });

            // Sequential transition to combine fade-in, pause, and fade-out
            SequentialTransition sequentialTransition = new SequentialTransition(
                    fadeTransitionIn,
                    pauseTransition,
                    fadeTransitionOut
            );

            isPopupVisible = true; // Set the flag to indicate a popup is visible
            sequentialTransition.play();
        });
    }

    private void hidePopup() {
        if (isPopupVisible && currentPopupImage != null) {
            Platform.runLater(() -> {
                FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), currentPopupImage);
                fadeTransitionOut.setFromValue(1.0);
                fadeTransitionOut.setToValue(0.0);

                // After fade-out, remove the image from the pane and update the flag
                fadeTransitionOut.setOnFinished(event -> {
                    mainPane.getChildren().remove(currentPopupImage);
                    isPopupVisible = false; // Update the flag to indicate no popup is visible
                    currentPopupImage = null; // Clear the reference to the current popup image
                });

                fadeTransitionOut.play();
            });
        }
    }





//
//    private void firstSetPions(){
//        String imagePath;
//        Image cardImage;
//
//        //PIONS SETTING (BUT IT SHOULD BE BASED ON POINTS)
//        String color = myself.getColor();
//        cardImage = switch (color) {
//            case "red" -> {
//                imagePath = "/img/Pions/CODEX_pion_rouge.png";
//                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
//            }
//            case "blue" -> {
//                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_bleu.png";
//                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
//            }
//            case "green" -> {
//                imagePath = "/img/Pions/CODEX_pion_vert.png";
//                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
//            }
//            case "purple" -> {
//                imagePath = "/img/Pions/CODEX_pion_jaune.png";
//                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
//            }
//            default -> null;
//        };
//
//        if(firstPion.isVisible()){
//            if(secondPion.isVisible()) {
//                if (thirdPion.isVisible()) {
//                    fourthPion.setImage(cardImage);
//                    fourthPion.setVisible(true);
//                } else {
//                    thirdPion.setImage(cardImage);
//                    thirdPion.setVisible(true);
//                }
//            }
//            secondPion.setImage(cardImage);
//            secondPion.setVisible(true);
//        } else {
//            firstPion.setImage(cardImage);
//            firstPion.setVisible(true);
//        }
//
//    }


}


