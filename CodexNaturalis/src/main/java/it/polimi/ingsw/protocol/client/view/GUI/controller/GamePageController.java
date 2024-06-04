package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GamePageController implements Initializable {
    @FXML
    private Pane mainPane;
    @FXML
    private Label playerName;
    @FXML
    private Pane playground;
    @FXML
    private ImageView state;
    @FXML
    private Text explanation;

    private ImageView red;
    private ImageView blue;
    private ImageView purple;
    private ImageView green;

    private final double offsetPions = 5;
    private final double[][] positions = {
            {1668 , 468},
            {1726, 468}, {1784, 468},
            {1812, 415}, {1755, 415}, {1697, 415}, {1639, 415},
            {1639, 362}, {1697, 362}, {1755, 362}, {1812, 362},
            {1812, 311}, {1755, 311}, {1697, 311}, {1639, 311},
            {1639, 256}, {1697, 256}, {1755, 256}, {1812, 256},
            {1812, 204}, {1726, 175}, {1639, 204},
            {1639, 150}, {1639, 96},
            {1673, 53}, {1726, 45}, {1781, 53},
            {1812, 150}, {1812, 96},
            {1726, 108 }
    };

    private final int offsetAreaX = 100;
    private final int offsetAreaY = 50;

    private final int layoutPlacedStarterX = 648;
    private final int layoutPlacedStarterY = 215;

    private final int fitHeightPlaced = 133;
    private final int  fitWidthPlaced = 200;

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

    private final BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<>();

    private boolean isLastTurn = false;

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
    private boolean firstTimePlace = true;
    private boolean firstTimeDraw = true;

    private int selectedStarterFront;
    private int selectedPick;
    private int selectedObjective;
    private ImageView selectedCard;
    private final int[] selectedToPlace = new int[4];


    @FXML
    private ImageView onTop;


    /**
     * It is used to initialize the controller, it starts the message listener and processor threads.
     *
     * @param url            not used
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
            while (true) {
                try {
                    Object message = GUIMessages.readToGUI();
                    if (message != null) {
                        messageQueue.put(message);

                    }
                } catch (Exception e) {
                   System.out.println("Error in reading message");
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

                    Platform.runLater(() -> processMessage(message));
                } catch (Exception e) {
                    System.out.println("Error in processing message");
                }
            }
        }).start();
    }

    //TODO add font to resources, and make expl look ok
    /**
     * Processes the message received from the server based on its type
     *
     * @param message the message to process
     */
    private void processMessage(Object message) {
        switch (message) {
            case currentStateMessage currentStateMessage -> {
                this.currentState = currentStateMessage.getStateName();
                isLastTurn = currentStateMessage.isLastTurn();
                currentStateCase(currentStateMessage);

                if(isLastTurn){
                    state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/LastTurnState.png"))));
                }

                if (currentStateMessage.getCurrentPlayer().getNickname().equals(myself.getNickname())) {
                    if (Objects.equals(currentState, "StarterCardState")) {
                        starterCase();
                        state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/starterState.png"))));
                        showImagePopup("/Images/Background/starterState.png");
                        explanation.setText("Click twice if you want to turn the card around, then place it");
                    } else if (Objects.equals(currentState, "PlaceTurnState")) {
                        placeTurnCase();
                        if(!isLastTurn) {
                            state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/youTurn.png"))));
                            showImagePopup("/Images/Background/yourTurn.png");
                        }

                        if(firstTimePlace) {
                            firstTimePlace = false;
                            explanation.setText("Select a card, turn it around if you want, then place it");
                        }

                    } else if (Objects.equals(currentState, "PickTurnState") && !isLastTurn && firstTimeDraw) {
                            firstTimeDraw = false;
                            explanation.setText("Click on the card you want to draw");
//                        I think it will be still setted from placeTurnCase
//                        if (!isLastTurn)
//                            state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/yourTurn.png"))));
                    }
                } else {
                    state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/waitingState.png"))));
                    explanation.setText("");
                }
            }
            case ArrayList<?> list -> {
                if (Objects.equals(currentState, "ObjectiveState") && !list.isEmpty() && list.getFirst() instanceof ObjectiveCard) {
                    objectivesToChose.add((ObjectiveCard) list.get(0));
                    objectivesToChose.add((ObjectiveCard) list.get(1));
                    objectiveCase();
                    state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/objectiveState.png"))));
                    showImagePopup("/Images/Background/objectiveState.png");
                    explanation.setText("Select your secret Objective, then place it");
                }
            }
            case updatePlayerMessage update -> updatePlayerCase(update);

            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }


    private void currentStateCase(currentStateMessage message) {
        this.myself = message.getPlayer();
        this.commonArea = message.getPlayer().getCommonArea();
        this.commonObjectives = message.getCommonObjectiveCards();
        this.myHand = message.getPlayer().getPlayerHand();
        this.myObjective = message.getPlayer().getObjective();
        this.myPlayerArea = message.getPlayer().getPlayerArea();

        Player currentPlayer = message.getCurrentPlayer();
        currentPlayerNickname = currentPlayer.getNickname();
        if (!Objects.equals(myself.getNickname(), currentPlayer.getNickname())) {
            if (existentPlayer(currentPlayer.getNickname()))
                setPlayer(currentPlayer);
            else
                addPlayer(currentPlayer);
            setPions(currentPlayer);
        }

        addCardsToCommonArea();
        onTop.toFront();
        setPage();
        onTop.toFront();
        setPions(myself);
        onTop.toFront();

    }

    private void starterCase() {
        if (clickCounter == -1 && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            addStarterCardsToPane();
            addClickablePlaceholder(playground, layoutPlacedStarterX, layoutPlacedStarterY, fitHeightPlaced, fitWidthPlaced, this::confirmPlaceStarter);
        }
    }

    private void objectiveCase() {
        if (clickCounter == -1 && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            setObjectives();
            addClickablePlaceholder(mainPane, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::confirmPlaceObjective);
        }
    }

    private void placeTurnCase() {
        seePlaceHolders();
    }

    private void updatePlayerCase(updatePlayerMessage update) {

        Player currentPlayer = update.getPlayer();
        if (!Objects.equals(myself.getNickname(), currentPlayer.getNickname())) {
            if (existentPlayer(currentPlayer.getNickname()))
                setPlayer(currentPlayer);
            else
                addPlayer(currentPlayer);
            setPions(currentPlayer);
        } else {
            this.myself = update.getPlayer();
            this.myHand = update.getPlayer().getPlayerHand();
            this.myObjective = update.getPlayer().getObjective();
            this.myPlayerArea = update.getPlayer().getPlayerArea();
            setPions(myself);
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
                displayPlayerArea(myPlayerArea);
            } else {
                addPlayerCardsToHand();
                addPlayerObjective();
                displayPlayerArea(players.get(clickCounter).getPlayerArea());
            }
        }
    }

    public void addCardsToCommonArea() {
        //Add the front up cards to commonArea
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, commonArea.getTableCards().getFirst().getID(), true,
                    commonArea.getTableCards().getFirst(), layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToPane(mainPane, commonArea.getTableCards().get(1).getID(), true,
                    commonArea.getTableCards().get(1), layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToPane(mainPane, commonArea.getTableCards().get(2).getID(), true,
                    commonArea.getTableCards().get(2), layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToPane(mainPane, commonArea.getTableCards().get(3).getID(), true,
                    commonArea.getTableCards().get(3), layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            //Add the cards to deck
            addNewCardToPane(mainPane, commonArea.getD1().getList().getFirst().getID(), false,
                    commonArea.getD1().getList().getFirst(), layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
            addNewCardToPane(mainPane, commonArea.getD2().getList().getFirst().getID(), false,
                    commonArea.getD2().getList().getFirst(), layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            //Add objective cards
            if (commonObjectives != null && !commonObjectives.isEmpty()) {
                addNewCardToPane(mainPane, commonObjectives.get(0).getID(), true, commonObjectives.get(0), layoutXObjective,
                        layoutYObj0, fitHeightCommon, fitWidthCommon, this::turnAround);
                onTop.toFront();
                addNewCardToPane(mainPane, commonObjectives.get(1).getID(), true, commonObjectives.get(1), layoutXObjective,
                        layoutYObj1, fitHeightCommon, fitWidthCommon, this::turnAround);
                onTop.toFront();
            }
        });
    }

    private void addCardsToHand() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, myHand.getPlaceableCards().getFirst().getID(), true,
                    myHand.getPlaceableCards().getFirst(), layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
            onTop.toFront();
            addNewCardToPane(mainPane, myHand.getPlaceableCards().get(1).getID(), true,
                    myHand.getPlaceableCards().get(1), layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
            onTop.toFront();
            addNewCardToPane(mainPane, myHand.getPlaceableCards().get(2).getID(), true,
                    myHand.getPlaceableCards().get(2), layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
            onTop.toFront();
        });
    }

    public void addMyObjective() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, myObjective.getID(), true, myObjective, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::turnAround);
            onTop.toFront();
        });
    }

    private void addPlayerCardsToHand() {
        if (players.get(clickCounter) != null) {
            PlayerHand playerHand = players.get(clickCounter).getPlayerHand();
            Platform.runLater(() -> {
                addNewCardToPane(mainPane, playerHand.getPlaceableCards().getFirst().getID(), false, playerHand.getPlaceableCards().getFirst(),
                        layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, null);
                onTop.toFront();
                addNewCardToPane(mainPane, playerHand.getPlaceableCards().get(1).getID(), false, playerHand.getPlaceableCards().get(1),
                        layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, null);
                onTop.toFront();
                addNewCardToPane(mainPane, playerHand.getPlaceableCards().get(2).getID(), false, playerHand.getPlaceableCards().get(2),
                        layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, null);
                onTop.toFront();
            });
        }
    }

    public void addPlayerObjective() {
        if (players.size() > clickCounter && players.get(clickCounter) != null) {
            Platform.runLater(() -> {
                addNewCardToPane(mainPane, players.get(clickCounter).getObjective().getID(), false, players.get(clickCounter).getObjective(),
                        layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, null);
                onTop.toFront();
            });
        }
    }

    private void addStarterCardsToPane() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, myself.getPlayerHand().getPlaceableCards().getFirst().getID(), true,
                    myself.getPlayerHand().getPlaceableCards().getFirst(), layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::placeStarter);
            onTop.toFront();
        });
    }

    public void setObjectives() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, objectivesToChose.get(0).getID(), true, objectivesToChose.get(0), layoutXChoiceObjective1, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            onTop.toFront();
            addNewCardToPane(mainPane, objectivesToChose.get(1).getID(), true, objectivesToChose.get(1), layoutXChoiceObjective2, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            onTop.toFront();
        });
    }

    private void addClickablePlaceholder(Pane pane, int layoutX, int layoutY, int fitHeight, int fitWidth, EventHandler<MouseEvent> eventHandler) {
        Random random = new Random();
        int color = random.nextInt(5);

        Image image = switch (color) {
            case 0 -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/blue.png")));
            case 1 -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/green.png")));
            case 2 -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/red.png")));
            case 3 -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/yellow.png")));
            case 4 -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Placeholders/purple.png")));
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


    private boolean existentPlayer(String currentPlayer) {
        for (Player player : players) {
            if (player.getNickname().equals(currentPlayer))
                return true;
        }
        return false;
    }

    private void setPlayer(Player currentPlayer) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getNickname().equals(currentPlayer.getNickname())) {
                players.set(i, currentPlayer);
            }
        }
    }

    private void addPlayer(Player currentPlayer) {
        players.add(currentPlayer);
    }

    private void removeCardFromPosition(int layoutX, int layoutY) {
        Platform.runLater(() -> {
            mainPane.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .filter(node -> {
                        ImageView imageView = (ImageView) node;
                        return (int) imageView.getLayoutX() == layoutX && (int) imageView.getLayoutY() == layoutY;
                    })
                    .forEach(node -> {
                        ImageView imageView = (ImageView) node;
                        imageView.toFront(); // Card to the front
                        onTop.toFront();
                        fadeOutTransition(mainPane, imageView, 1.0);
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
            if (card instanceof ObjectiveCard || card instanceof PlaceableCard && pane == mainPane) {
                hooverEffect(newCard);
            }
            Platform.runLater(() -> {
                pane.getChildren().add(newCard);
                fadeInTransition(newCard, 1.0);
                onTop.toFront();
            });
            // If Card with the same ID and position exists, do nothing
        }
    }

    private void turnAround(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        Card card = (Card) clickedCard.getUserData();

        if (card != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), clickedCard);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                ImageBinder imageBinder = new ImageBinder();
                Image newImage = imageBinder.getOppositeImage(card.getID(), card.isFront());
                clickedCard.setImage(newImage);

                card.setFront(!card.isFront());

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), clickedCard);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);

                fadeIn.play();
            });

            fadeOut.play();
        }
    }
    private void placeStarter(MouseEvent event) {
        if (clickCounter == -1) {
            ImageView clickedCard = (ImageView) event.getSource();
            StarterCard card = (StarterCard) clickedCard.getUserData();

            if (event.getClickCount() == 1) {

                if (selectedCard != null)
                    makeSmallerTransition(selectedCard);

                makeBiggerTransition(clickedCard);
                this.selectedCard = clickedCard;

                this.selectedStarterFront = card.isFront() ? 1 : 0;

            } else if (event.getClickCount() == 2) {
                turnAround(event);
            }
        }
    }


    private void chooseObjective(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        ObjectiveCard card = (ObjectiveCard) clickedCard.getUserData();
        int cardID = card.getID();

        if (event.getClickCount() == 1) {
            if (selectedCard != null) {
                makeSmallerTransition(selectedCard);
            }

            makeBiggerTransition(clickedCard);
            this.selectedCard = clickedCard;

            if (cardID == objectivesToChose.get(0).getID()) {
                this.selectedObjective = 1;
            } else if (cardID == objectivesToChose.get(1).getID()) {
                this.selectedObjective = 2;
            }
        } else if (event.getClickCount() == 2) {
            turnAround(event);
        }
    }

    private void confirmPlaceStarter(MouseEvent event) {
        if (selectedCard != null) {
            makeSmallerTransition(selectedCard);
            ImageView image = (ImageView) event.getSource();
            fadeOutTransition(mainPane, image, 0.5);
            removeCardFromPosition(layoutXCard1, layoutYHand);
            StarterCard card = (StarterCard) selectedCard.getUserData();
            addNewCardToPane(playground, card.getID(), card.isFront(), card,layoutPlacedStarterX, layoutPlacedStarterY,
                    fitHeightPlaced, fitWidthPlaced, null);
            GUIMessages.writeToClient(selectedStarterFront);
        }
    }

    private void confirmPlaceObjective(MouseEvent event) {
        if (selectedCard != null) {
            makeSmallerTransition(selectedCard);
            ImageView image = (ImageView) event.getSource();
            fadeOutTransition(mainPane, image, 0.5);
            removeCardFromPosition(layoutXChoiceObjective1, layoutYHand);
            removeCardFromPosition(layoutXChoiceObjective2, layoutYHand);
            ObjectiveCard card = (ObjectiveCard) selectedCard.getUserData();
            addNewCardToPane(mainPane, card.getID(), true, card, layoutXObjective, layoutYObjMy, fitHeightCommon,
                    fitWidthCommon, this::turnAround);
            GUIMessages.writeToClient(selectedObjective);
        }
    }

    private void hooverEffect(ImageView imageView){
        imageView.setOnMouseEntered(e -> makeBiggerTransition(imageView));
        imageView.setOnMouseExited(e -> {
            if (selectedCard != imageView)
                makeSmallerTransition(imageView);
        });
    }

    private void makeBiggerTransition(ImageView imageView) {
        ScaleTransition makeBigger = new ScaleTransition(Duration.millis(200), imageView);
        makeBigger.setToX(1.1);
        makeBigger.setToY(1.1);
        makeBigger.play();
    }

    private void makeSmallerTransition(ImageView imageView) {
        ScaleTransition makeSmaller = new ScaleTransition(Duration.millis(200), imageView);
        makeSmaller.setToX(1.0);
        makeSmaller.setToY(1.0);
        makeSmaller.play();
    }


    //TODO see if it works as expected
    private void choseCardToPlace(MouseEvent event) {
        if(clickCounter == -1 && Objects.equals(currentState, "PlaceTurnState")) {
            ImageView clickedCard = (ImageView) event.getSource();
            PlaceableCard card = (PlaceableCard) clickedCard.getUserData();
            int cardID = card.getID();

            if (event.getClickCount() == 1) {
                if (selectedCard != null) {
                    makeSmallerTransition(selectedCard);
                }

                makeBiggerTransition(clickedCard);
                this.selectedCard = clickedCard;

                if (cardID == myHand.getPlaceableCards().getFirst().getID())
                    this.selectedToPlace[0] = 0;
                else if (cardID == myHand.getPlaceableCards().get(1).getID())
                    this.selectedToPlace[0] = 1;
                else if (cardID == myHand.getPlaceableCards().get(2).getID())
                    this.selectedToPlace[0] = 2;

                this.selectedToPlace[1] = card.isFront() ? 1 : 0;
            }
        }

        if (event.getClickCount() == 2)
            turnAround(event);

    }

    //TODO see if it works as expected
    private void confirmPlaceCard(MouseEvent event) {
        if (selectedCard != null) {
            makeSmallerTransition(selectedCard);

            ImageView clickedPlaceholder = (ImageView) event.getSource();
            int placeholderX = (int) clickedPlaceholder.getLayoutX();
            int placeholderY = (int) clickedPlaceholder.getLayoutY();
            int relativePosX = (placeholderX - layoutPlacedStarterX) / offsetAreaX; //TODO is this correct?
            int relativePosY = (placeholderY - layoutPlacedStarterY) / offsetAreaY;

            this.selectedToPlace[2] = relativePosX;
            this.selectedToPlace[3] = relativePosY;

            PlaceableCard card = (PlaceableCard) selectedCard.getUserData();

            removeAllPlaceholders();

            removeCardFromPosition((int) selectedCard.getLayoutX(), (int) selectedCard.getLayoutY());

            addNewCardToPane(playground, card.getID() , card.isFront(), card, selectedToPlace[2], selectedToPlace[3],
                    fitHeightCard, fitWidthCard, null);
           GUIMessages.writeToClient(selectedToPlace);
        }
    }

    private int[] findFirstAvailableHandPosition() {
        int[][] handPositions = {
                {layoutXCard0, layoutYHand},
                {layoutXCard1, layoutYHand},
                {layoutXCard2, layoutYHand}
        };

        for (int[] position : handPositions) {
            boolean occupied = mainPane.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .anyMatch(node -> {
                        ImageView imageView = (ImageView) node;
                        return (int) imageView.getLayoutX() == position[0] &&
                                (int) imageView.getLayoutY() == position[1];
                    });
            if (!occupied) {
                return position;
            }
        }
        return null; // No available positions
    }


    private void pickCard(MouseEvent event) {
        if (clickCounter == -1 && Objects.equals(currentState, "PickTurnState") && !isLastTurn) {
            ImageView clickedCard = (ImageView) event.getSource();
            Card card = (Card) clickedCard.getUserData();
            int cardID = card.getID();

            if (selectedCard != null) {
                makeSmallerTransition(selectedCard);
            }

            makeBiggerTransition(clickedCard);
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
                GUIMessages.writeToClient(selectedPick);

                int layoutX;
                int layoutY;

                int[] availablePosition = findFirstAvailableHandPosition();
                if (availablePosition != null) {
                    layoutX = availablePosition[0];
                    layoutY = availablePosition[1];
                } else {
                    layoutY = 0;
                    layoutX = 0;
                }

                if (selectedPick == 1) {
                    Platform.runLater(() -> {
                        addNewCardToPane(mainPane, commonArea.getD1().getList().get(1).getID(), false, commonArea.getD1().getList().get(1),
                                layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
                        clickedCard.toFront();
                        onTop.toFront();
                        removeCardFromPosition(layoutXDeck, layoutYResource);
                        addNewCardToPane(mainPane, cardID, true, card, layoutX, layoutY, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                        onTop.toFront();
                    });
                } else if (selectedPick == 2) {
                    Platform.runLater(() -> {
                        addNewCardToPane(mainPane, commonArea.getD2().getList().get(1).getID(), false, commonArea.getD2().getList().get(1),
                                layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
                        clickedCard.toFront();
                        onTop.toFront();
                        removeCardFromPosition(layoutXDeck, layoutYGold);
                        addNewCardToPane(mainPane, cardID, true, card, layoutX, layoutY, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                    });
                } else if (selectedPick == 3) {
                    pickResource(card, cardID, layoutX, layoutY, layoutXPick0);
                } else if (selectedPick == 4) {
                    pickResource(card, cardID, layoutX, layoutY, layoutXPick1);
                } else if (selectedPick == 5) {
                    pickGold(card, cardID, layoutX, layoutY, layoutXPick0);
                } else if (selectedPick == 6) {
                    pickGold(card, cardID, layoutX, layoutY, layoutXPick1);
                }
            }
        }
    }

    private void pickGold(Card card, int cardID, int layoutX, int layoutY, int layoutXPick) {
        addNewCardToPane(mainPane, commonArea.getD2().getList().get(1).getID(), false, commonArea.getD2().getList().get(1),
                layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();
        removeCardFromPosition(layoutXPick, layoutYGold);
        removeCardFromPosition(layoutXDeck, layoutYGold);
        addNewCardToPane(mainPane, commonArea.getD2().getList().getFirst().getID(), true, commonArea.getD2().getList().getFirst(),
                layoutXPick, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
        addNewCardToPane(mainPane, cardID, true, card, layoutX, layoutY, fitHeightCard, fitWidthCard, this::choseCardToPlace);
    }

    private void pickResource(Card card, int cardID, int layoutX, int layoutY, int layoutXPick) {
        addNewCardToPane(mainPane, commonArea.getD1().getList().get(1).getID(), false, commonArea.getD1().getList().get(1),
                layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
        onTop.toFront();
        removeCardFromPosition(layoutXPick, layoutYResource);
        removeCardFromPosition(layoutXDeck, layoutYResource);
        addNewCardToPane(mainPane, commonArea.getD1().getList().getFirst().getID(), true, commonArea.getD1().getList().getFirst(),
                layoutXPick, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
        addNewCardToPane(mainPane, cardID, true, card, layoutX, layoutY, fitHeightCard, fitWidthCard, this::choseCardToPlace);
    }


    //TODO create images for when non you
    @FXML
    private void switchToNextPlayer() {
        if (!Objects.equals(currentState, "StarterCardState") && !Objects.equals(currentState, "ObjectiveState")) {
            clickCounter++;
            if (clickCounter == players.size()) {
                clickCounter = -1;
            }
            setPage();
            onTop.toFront();
        } else if (Objects.equals(currentState, "StarterCardState") && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            showImagePopup("/Images/Background/starterState.png");
        } else if (Objects.equals(currentState, "ObjectiveState") && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            showImagePopup("/Images/Background/objectiveState.png");
       }  // else if(Objects.equals(currentState, "StarterCardState")) {
//            showImagePopup("/Images/Background/waitStarter.png");
//        } else if(Objects.equals(currentState, "ObjectiveState")) {
//            showImagePopup("/Images/Background/waitObjective.png");
//        }
    }

    private void showImagePopup(String imagePath) {
        Platform.runLater(() -> {
            Image imageLoad = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            ImageView image = new ImageView(imageLoad);

            image.preserveRatioProperty().setValue(true);
            image.setLayoutX(1074);
            image.setLayoutY(639);
            image.setOpacity(0.0); // Initially invisible

            mainPane.getChildren().add(image);

            fadeTransitionForPopUP(image);
        });
    }

    private void fadeInTransition(ImageView image, double maxOpacity) {
        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(maxOpacity);

        fadeTransitionIn.play();
    }

    private void fadeOutTransition(Pane pane, ImageView image, double maxOpacity) {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionOut.setFromValue(maxOpacity);
        fadeTransitionOut.setToValue(0.0);

        fadeTransitionOut.setOnFinished(event -> pane.getChildren().remove(image));

        fadeTransitionOut.play();
    }


    private void fadeTransitionForPopUP(ImageView image) {
        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(1.0);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionOut.setFromValue(1.0);
        fadeTransitionOut.setToValue(0.0);

        fadeTransitionOut.setOnFinished(event -> mainPane.getChildren().remove(image));

        SequentialTransition sequentialTransition = new SequentialTransition(
                fadeTransitionIn,
                pauseTransition,
                fadeTransitionOut
        );

        sequentialTransition.play();
    }


    //TODO try to see if the movement works
    private void setPions(Player player){
        Platform.runLater(() -> {
            String color = player.getColor();
            List<ImageView> allPions = Arrays.asList(red, blue, purple, green);
            int score = player.getScore();
            Image img;

            if(Objects.equals(color, "red") && red == null){
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_rouge.png")));
                red = new ImageView(img);
                setPionPosition(red, score, allPions);
            } else if (Objects.equals(color, "blue") && blue == null) {
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_bleu.png")));
                blue = new ImageView(img);
                setPionPosition(blue, score, allPions);
            } else if (Objects.equals(color, "purple") && purple == null) {
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_purple.png")));
                purple = new ImageView(img);
                setPionPosition(purple, score, allPions);
            } else if (Objects.equals(color, "green") && green == null) {
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_vert.png")));
                green = new ImageView(img);
                setPionPosition(green, score, allPions);
            } else if(Objects.equals(color, "red")){
                setPionPosition(red, score, allPions);
            } else if(Objects.equals(color, "blue")){
                setPionPosition(blue, score, allPions);
            } else if(Objects.equals(color, "purple")){
                setPionPosition(purple, score, allPions);
            } else if(Objects.equals(color, "green")){
                setPionPosition(green, score, allPions);
            }
        });
    }

    private void setPionPosition(ImageView pion, int score, List<ImageView> allPions){

        if (pion != null && score == 0 && isPionAtDesiredPosition(pion, 0)) {
            double[] adjustedPosition = getAdjustedPosition(allPions, positions[0][0] , positions[0][1]);
            pion.setLayoutX(adjustedPosition[0]);
            pion.setLayoutY(adjustedPosition[1]);
            pion.setFitHeight(49);
            pion.setFitWidth(49);
            pion.setPreserveRatio(true);
            mainPane.getChildren().add(pion);
        } else  if (pion != null && isPionAtDesiredPosition(pion, score)) {
            addPoints(pion, score, allPions);
            updatePionsPositions(allPions, pion);
            pion.toFront();
        }
    }

    private void addPoints(ImageView pion, int score, List<ImageView> allPions) {
        Platform.runLater(() -> {
            if (score < 0 || score > 29) {
                // Invalid score ?? count from 1??
                return;
            }

            // Create a path
            Path path = new Path();
            double startX = pion.getLayoutX();
            double startY = pion.getLayoutY();
            path.getElements().add(new MoveTo(startX, startY));

            int steps = 0;
            // Intermediate points to the path - stop at position that corresponds to the score
            for (int i = getScoreByPosition((int) startX, (int) startY); i <= score; i++) {
                steps++;
                double[] adjustedPosition = getAdjustedPosition(allPions, positions[i][0], positions[i][1]);
                path.getElements().add(new LineTo(adjustedPosition[0], adjustedPosition[1]));
            }

            PathTransition pathTransition = new PathTransition();
            int speed = 100; //TODO I dont know if its too fast or not
            int durationInSeconds = steps / speed;
            pathTransition.setDuration(Duration.seconds(durationInSeconds));
            pathTransition.setPath(path);
            pathTransition.setNode(pion);
            pathTransition.setCycleCount(1);
            pathTransition.setAutoReverse(false);

            pathTransition.play();
        });
    }

    private double[] getAdjustedPosition(List<ImageView> pions, double targetX, double targetY) {
        double offsetY = 0;

        for (ImageView pion : pions) {
            if (pion != null && pion.getLayoutX() == targetX && pion.getLayoutY() == targetY + offsetY) {
                offsetY -= offsetPions;
            }
        }
        return new double[] { targetX, targetY + offsetY };
    }

    private boolean isPionAtDesiredPosition(ImageView pion, int score) {
        double[] desiredPosition = positions[score];
        return pion.getLayoutX() != desiredPosition[0] && pion.getLayoutY() != desiredPosition[1]
                || pion.getLayoutX() != desiredPosition[0]  && pion.getLayoutY() != desiredPosition[1] - offsetPions
                || pion.getLayoutX() != desiredPosition[0] && pion.getLayoutY() != desiredPosition[1] - 2 * offsetPions
                || pion.getLayoutX() != desiredPosition[0] && pion.getLayoutY() != desiredPosition[1] - 3 * offsetPions;
    }

    private void updatePionsPositions(List<ImageView> allPions, ImageView movedPion) {
        Platform.runLater(() -> {
            double movedPionY = movedPion.getLayoutY();
            for (ImageView pion : allPions) {
                if (pion != movedPion && pion != null && pion.getLayoutY() < movedPionY) {
                    double[] adjustedPosition = getAdjustedPosition(allPions, pion.getLayoutX(), pion.getLayoutY());
                    pion.setLayoutX(adjustedPosition[0]);
                    pion.setLayoutY(adjustedPosition[1]);
                }
            }
        });
    }

    private int getScoreByPosition(double x, double y) {

        for (int score = 0; score <= positions.length; score++) {
            double[] position = positions[score];
            if (position[0] == x && position[1] == y) {
                return score;
            }
        }

        return -1; //not found
    }

    private void seePlaceHolders(){
        ArrayList<Integer[]> availablePositions = myPlayerArea.getAvailablePosition();
        for (Integer[] pos : availablePositions) {
            int newLayoutX = layoutPlacedStarterX + pos[0] * offsetAreaX;
            int newLayoutY = layoutPlacedStarterY + pos[1] * offsetAreaY;
            addClickablePlaceholder(mainPane, newLayoutX, newLayoutY, fitHeightPlaced, fitWidthPlaced, this::confirmPlaceCard);
        }
    }

    private void removeAllPlaceholders() {
        List<ImageView> placeholders = playground.getChildren().stream()
                .filter(node -> node instanceof ImageView && node.getStyleClass().contains("placeholder"))
                .map(node -> (ImageView) node)
                .toList();

        for (ImageView placeholder : placeholders) {
            fadeOutTransition(playground, placeholder, 0.5);
        }
    }


    public void displayPlayerArea(PlayerArea playerArea) {
        Platform.runLater(() -> {
            // Remove all existing cards from the player area
            playground.getChildren().removeIf(node -> node instanceof ImageView && node.getUserData() instanceof PlaceableCard);

            ArrayList<PlaceableCard> allCards = playerArea.getAllCards();

            List<ImageView> cardImageViews = new ArrayList<>();
            for (PlaceableCard card : allCards) {
                int layoutX = calculateLayoutX(card);
                int layoutY = calculateLayoutY(card);

                ImageView cardImageView = createCardImageView(card.getID(), card.isFront(), card, layoutX, layoutY, fitHeightCard, fitWidthCard);
                playground.getChildren().add(cardImageView);
                cardImageViews.add(cardImageView);

                // Adjust z-order
                adjustCardZOrder(cardImageView, card);
            }

            ArrayList<Integer[]> availablePositions = playerArea.getAvailablePosition();

            // Check space for placeholders
            adjustElementsToFit(cardImageViews, availablePositions);
            onTop.toFront();
        });
    }

    private int calculateLayoutX(PlaceableCard card) {
        return layoutPlacedStarterX + card.getCells().getFirst().getColumn() * offsetAreaX;
    }

    private int calculateLayoutY(PlaceableCard card) {
        return layoutPlacedStarterY + card.getCells().getFirst().getRow() * offsetAreaY;
    }

    private void adjustElementsToFit(List<ImageView> cardImageViews, ArrayList<Integer[]> availablePositions) {
        double playgroundWidth = playground.getWidth();
        double playgroundHeight = playground.getHeight();

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (ImageView imageView : cardImageViews) {
            double imageViewMinX = imageView.getLayoutX();
            double imageViewMaxX = imageView.getLayoutX() + imageView.getFitWidth();
            double imageViewMinY = imageView.getLayoutY();
            double imageViewMaxY = imageView.getLayoutY() + imageView.getFitHeight();

            if (imageViewMinX < minX) minX = imageViewMinX;
            if (imageViewMaxX > maxX) maxX = imageViewMaxX;
            if (imageViewMinY < minY) minY = imageViewMinY;
            if (imageViewMaxY > maxY) maxY = imageViewMaxY;
        }

        for (Integer[] pos : availablePositions) {
            double posMinX = layoutPlacedStarterX + pos[0] * offsetAreaX;
            double posMaxX = posMinX + fitWidthCard;
            double posMinY = layoutPlacedStarterY + pos[1] * offsetAreaY;
            double posMaxY = posMinY + fitHeightCard;

            if (posMinX < minX) minX = posMinX;
            if (posMaxX > maxX) maxX = posMaxX;
            if (posMinY < minY) minY = posMinY;
            if (posMaxY > maxY) maxY = posMaxY;
        }

        double overflowX = Math.max(0, maxX - playgroundWidth);
        double overflowY = Math.max(0, maxY - playgroundHeight);

        if (overflowX > 0 || overflowY > 0) {
            for (ImageView imageView : cardImageViews) {
                imageView.setLayoutX(imageView.getLayoutX() - overflowX);
                imageView.setLayoutY(imageView.getLayoutY() - overflowY);
            }
        }

        if (minX < 0 || minY < 0 || overflowX > 0 || overflowY > 0) {
            double scaleX = playgroundWidth / (maxX - minX);
            double scaleY = playgroundHeight / (maxY - minY);
            double scale = Math.min(scaleX, scaleY);

            for (ImageView imageView : cardImageViews) {
                imageView.setFitWidth(imageView.getFitWidth() * scale);
                imageView.setFitHeight(imageView.getFitHeight() * scale);
                imageView.setLayoutX((imageView.getLayoutX() - minX) * scale);
                imageView.setLayoutY((imageView.getLayoutY() - minY) * scale);
            }
        }
    }

    private void adjustCardZOrder(ImageView cardImageView, PlaceableCard card) {
        Cell cell = card.getCells().getFirst();

        if (cell.getTopCard() == card) {
            cardImageView.toFront();
        } else if (cell.getBottomCard() == card) {
            cardImageView.toBack();
        }

        if (cell.getTopCard() != card && cell.getBottomCard() != card) {
            int cardIndex = playground.getChildren().indexOf(cardImageView);
            playground.getChildren().remove(cardImageView);
            playground.getChildren().add(cardIndex, cardImageView);
        }
    }


}
