package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.animation.*;
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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class GamePageController implements Initializable {
    private final double offsetPions = 5;
    private final double[][] positions = {
            {1668, 468},
            {1726, 468}, {1784, 468},
            {1812, 415}, {1755, 415}, {1697, 415}, {1639, 415},
            {1639, 362}, {1697, 362}, {1755, 362}, {1812, 362},
            {1812, 311}, {1755, 311}, {1697, 311}, {1639, 311},
            {1639, 256}, {1697, 256}, {1755, 256}, {1812, 256},
            {1812, 204}, {1726, 175}, {1639, 204},
            {1639, 150}, {1639, 96},
            {1673, 53}, {1726, 45}, {1781, 53},
            {1812, 150}, {1812, 96},
            {1726, 108}
    };
    private final int offsetAreaX = 156;
    private final int offsetAreaY = 79;
    public ImageView nextPlayer;
    double centerXOffset = 0;
    double centerYOffset = 0;
    double scale = 1.0;
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
    private final BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<ObjectiveCard> objectivesToChose = new ArrayList<>();
    private final int[] selectedToPlace = new int[4];
    private ArrayList<PlaceableCard> allCards = new ArrayList<>();
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
    @FXML
    private ImageView onTop;
    private ImageView red;
    private ImageView blue;
    private ImageView purple;
    private ImageView green;
    private Player myself;
    private PlayerHand myHand;
    private ObjectiveCard myObjective;
    private PlayerArea myPlayerArea;
    private String currentPlayerNickname;
    private CommonArea commonArea;
    private ArrayList<ObjectiveCard> commonObjectives;
    private String currentState;
    private int clickCounter = -1;
    private boolean isLastTurn = false;
    private boolean firstTimePlace = true;
    private boolean firstTimeDraw = true;
    private ImageView selectedCard;
    private int selectedStarterFront;
    private int selectedPick;
    private int selectedObjective;
    private boolean placeholdersVisible = false;
    private currentStateMessage currentStateMessageSaved;
    private boolean wrong = false;

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

    //TODO add font to resources, and make explanation look ok

    /**
     *
     *
     * Processes the message received from the server based on its type
     * Displays text and shows popups based on the message received
     *
     * @param message the message to process
     */
    private void processMessage(Object message) {
        switch (message) {
            case currentStateMessage currentStateMessage -> {
                currentStateMessageSaved = currentStateMessage;
                caseCurrentState(currentStateMessageSaved);
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
            case responseMessage responseMessage -> {
                if (!responseMessage.getCorrect()) {
                    showImagePopup("/Images/Background/wrong.png");
                    wrong = true;
                    caseCurrentState(currentStateMessageSaved);
                }
            }

            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    private void caseCurrentState(currentStateMessage currentStateMessage) {
        this.currentState = currentStateMessage.getStateName();
        isLastTurn = currentStateMessage.isLastTurn();
        currentStateCase(currentStateMessage);

        if (isLastTurn) {
            state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/LastTurnState.png"))));
        }

        if (currentStateMessage.getCurrentPlayer().getNickname().equals(myself.getNickname())) {
            if (Objects.equals(currentState, "StarterCardState")) {
                starterCase();
                state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/starterState.png"))));
                showImagePopup("/Images/Background/starterState.png");
                explanation.setText("Click twice if you want to turn the card around, then place it");
            } else if (Objects.equals(currentState, "PlaceTurnState")) {
                if (!isLastTurn && !wrong) {
                    state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/yourTurn.png"))));
                    showImagePopup("/Images/Background/yourTurn.png");
                }

                if (firstTimePlace) {
                    firstTimePlace = false;
                    explanation.setText("Select a card, turn it around if you want, then place it");
                }

            } else if (Objects.equals(currentState, "PickTurnState") && !isLastTurn && firstTimeDraw) {
                firstTimeDraw = false;
                explanation.setText("Click on the card you want to draw");
            }
        } else {
            state.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/waitingState.png"))));
            explanation.setText("");
        }
    }

    /**
     * Sets the page with the information received
     * Displays the pions of the player, and of the current player (if they are not the same)
     *
     * @param message currentStateMessage received from the server
     */
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
        addCommonObjective();
        onTop.toFront();
        setPage();
        onTop.toFront();
        setPions(myself);
        onTop.toFront();

    }

    /**
     * Displays the starterCard on the page if the player is visualizing his own page
     */
    private void starterCase() {
        if (clickCounter == -1 && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            addStarterCardsToPane();
            addClickablePlaceholder(playground, layoutPlacedStarterX, layoutPlacedStarterY, fitHeightPlaced, fitWidthPlaced, this::confirmPlaceStarter);
        }
    }

    /**
     * Displays the objectiveCard on the page if the player is visualizing his own page
     */
    private void objectiveCase() {
        if (clickCounter == -1 && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            setObjectives();
            addClickablePlaceholder(mainPane, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::confirmPlaceObjective);
        }
    }

    /**
     * Updates the page with the information received
     * Updates the pions of the player, or of the current player (if they are not the same)
     *
     * @param update updatePlayerMessage received from the server
     */
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

    /**
     * Sets the visualized page with the cards of the page's player
     */
    private void setPage() {
        String nickname = (clickCounter == -1) ? myself.getNickname() : players.get(clickCounter).getNickname();
        playerName.setText(nickname);

        if (!currentState.equals("StarterCardState") && !currentState.equals("ObjectiveState")) {
            if (clickCounter == -1) {
                addCardsToHand();
                addMyObjective();
                displayPlayerArea(myPlayerArea);
                if((Objects.equals(currentState, "PlaceTurnState") && Objects.equals(myself.getNickname(), currentPlayerNickname) && !placeholdersVisible) || wrong)
                    displayPlaceHolders();
                else if(placeholdersVisible)
                    removeAllPlaceholders();
            } else {
                if(placeholdersVisible)
                    removeAllPlaceholders();
                addPlayerCardsToHand();
                addPlayerObjective();
                displayPlayerArea(players.get(clickCounter).getPlayerArea());
            }
        }
    }

    /**
     * Displays the commonArea
     */
    public void addCardsToCommonArea() {

        Platform.runLater(() -> {
            //Add the front up cards to commonArea
            if(commonArea.getTableCards().getFirst() == null)
                removeCardFromPosition(layoutXPick0, layoutYResource);
            else
                addNewCardToPane(mainPane, commonArea.getTableCards().getFirst().getID(), true,
                        commonArea.getTableCards().getFirst(), layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if(commonArea.getTableCards().get(1) == null)
                removeCardFromPosition(layoutXPick1, layoutYResource);
            else
                addNewCardToPane(mainPane, commonArea.getTableCards().get(1).getID(), true,
                        commonArea.getTableCards().get(1), layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if(commonArea.getTableCards().get(2) == null)
                removeCardFromPosition(layoutXPick0, layoutYGold);
            else
                addNewCardToPane(mainPane, commonArea.getTableCards().get(2).getID(), true,
                        commonArea.getTableCards().get(2), layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if(commonArea.getTableCards().get(3) == null)
                removeCardFromPosition(layoutXPick1, layoutYGold);
            else
                addNewCardToPane(mainPane, commonArea.getTableCards().get(3).getID(), true,
                        commonArea.getTableCards().get(3), layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            //Add the cards to deck
            if(commonArea.getD1().getList().getFirst() == null)
                removeCardFromPosition(layoutXDeck, layoutYResource);
            else
                addNewCardToPane(mainPane, commonArea.getD1().getList().getFirst().getID(), false,
                        commonArea.getD1().getList().getFirst(), layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if(commonArea.getD2().getList().getFirst() == null)
                removeCardFromPosition(layoutXDeck, layoutYGold);
            else
                addNewCardToPane(mainPane, commonArea.getD2().getList().getFirst().getID(), false,
                        commonArea.getD2().getList().getFirst(), layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();
        });
    }

    /**
     * Displays the commonObjective
     */
    private void addCommonObjective() {
        Platform.runLater(() -> {
            if (commonObjectives != null && !commonObjectives.isEmpty()) {
                addNewCardToPane(mainPane, commonObjectives.get(0).getID(), commonObjectives.get(0).isFront(), commonObjectives.get(0), layoutXObjective,
                        layoutYObj0, fitHeightCommon, fitWidthCommon, this::turnObjectives);
                onTop.toFront();
                addNewCardToPane(mainPane, commonObjectives.get(1).getID(), commonObjectives.get(1).isFront(), commonObjectives.get(1), layoutXObjective,
                        layoutYObj1, fitHeightCommon, fitWidthCommon, this::turnObjectives);
                onTop.toFront();
            }
        });
    }

    private void addCardsToHand() {
        Platform.runLater(() -> {
            if(myHand.getPlaceableCards().size() < 3)
                removeCardFromPosition(layoutXCard2, layoutYHand);

            for(int i = 0; myHand.getPlaceableCards().size() > i;  i++){
                    addNewCardToPane(mainPane, myHand.getPlaceableCards().get(i).getID(), true,
                            myHand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                    onTop.toFront();
            }
        });
    }

    public void addMyObjective() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, myObjective.getID(), myObjective.isFront(), myObjective, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::turnObjectives);
            onTop.toFront();
        });
    }

    private void addPlayerCardsToHand() {
        if (players.get(clickCounter) != null) {
            PlayerHand playerHand = players.get(clickCounter).getPlayerHand();
            Platform.runLater(() -> {
                if(playerHand.getPlaceableCards().size() < 3)
                    removeCardFromPosition(layoutXCard2, layoutYHand);

                for(int i = 0; i < playerHand.getPlaceableCards().size(); i++) {
                        addNewCardToPane(mainPane, playerHand.getPlaceableCards().get(i).getID(), false,
                                playerHand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, null);
                        onTop.toFront();
                }
            });
        }
    }

    /**
     * Displays the visualized player's secret objective back
     */
    public void addPlayerObjective() {
        if (players.size() > clickCounter && players.get(clickCounter) != null) {
            Platform.runLater(() -> {
                addNewCardToPane(mainPane, players.get(clickCounter).getObjective().getID(), false, players.get(clickCounter).getObjective(),
                        layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, null);
                onTop.toFront();
            });
        }
    }

    /**
     * Displays the starterCard
     */
    private void addStarterCardsToPane() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, myself.getPlayerHand().getPlaceableCards().getFirst().getID(), true,
                    myself.getPlayerHand().getPlaceableCards().getFirst(), layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::placeStarter);
            onTop.toFront();
        });
    }

    /**
     * Displays the two objectives from which the player can choose
     */
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

    /**
     * Displays the cards placeholders that can be clicked to select the position where to place the card
     * in the playground and to place the objective in the objective area
     *
     * @param pane         the pane where to display the placeholders
     * @param layoutX      the x position of the placeholder
     * @param layoutY      the y position of the placeholder
     * @param fitHeight    the height of the placeholder
     * @param fitWidth     the width of the placeholder
     * @param eventHandler the event handler to call when the placeholder is clicked
     */
    private void addClickablePlaceholder(Pane pane, int layoutX, int layoutY, int fitHeight, int fitWidth, EventHandler<MouseEvent> eventHandler) {
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

    /**
     * Checks if the currentPlayer is already in the list of players
     *
     * @param currentPlayer the nickname of the player to check
     * @return true if the player is already in the list, false otherwise
     */
    private boolean existentPlayer(String currentPlayer) {
        for (Player player : players) {
            if (player.getNickname().equals(currentPlayer))
                return true;
        }
        return false;
    }

    /**
     * Updates the player in the list of players
     *
     * @param currentPlayer the player to update
     */
    private void setPlayer(Player currentPlayer) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getNickname().equals(currentPlayer.getNickname())) {
                players.set(i, currentPlayer);
            }
        }
    }

    /**
     * Adds the player to the list of players
     *
     * @param currentPlayer the player to add
     */
    private void addPlayer(Player currentPlayer) {
        players.add(currentPlayer);
    }

    /**
     * Removes a card from the pane.
     *
     * @param layoutX the x position of the card
     * @param layoutY the y position of the card
     */
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
                if(imageView.getUserData() instanceof Card)
                    fadeOutTransition(mainPane, imageView, 1);
                else
                    fadeOutTransition(mainPane, imageView, 0.5);
            }
            onTop.toFront();
        });
    }

    /**
     * Bind the image to the card and set the card to the imageView
     *
     * @param imageView the imageView to set the card to
     * @param cardID    the ID of the card
     * @param front     if the card is front up
     * @param card      the card to set to the imageView
     */
    private void setupCard(ImageView imageView, int cardID, boolean front, Card card) {
        ImageBinder imageBinder = new ImageBinder();
        ImageView cardImageView = imageBinder.bindImage(cardID, front);
        imageView.setImage(cardImageView.getImage());
        imageView.setUserData(card); // Store card ID and state
    }

    /**
     * Creates an imageView with the card and sets the card to the imageView
     *
     * @param cardID    the ID of the card
     * @param front     if the card is front up
     * @param card      the card to set to the imageView
     * @param layoutX   the x position of the imageView
     * @param layoutY   the y position of the imageView
     * @param fitHeight the height of the imageView
     * @param fitWidth  the width of the imageView
     * @return the imageView with the card set
     */
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

    /**
     * Adds a new card to the pane if it doesn't exist already in the same position and with the same ID
     *
     * @param pane         the pane where to add the card
     * @param cardID       the ID of the card
     * @param front        if the card is front up
     * @param card         the card to add
     * @param layoutX      the x position of the card
     * @param layoutY      the y position of the card
     * @param fitHeight    the height of the card
     * @param fitWidth     the width of the card
     * @param eventHandler the event handler to call when the card is clicked
     */
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
            if(card instanceof ObjectiveCard && pane == mainPane && clickCounter != -1)
                hooverEffect(newCard);
            else if (card instanceof ObjectiveCard || card instanceof PlaceableCard && pane == mainPane && clickCounter == -1)
                hooverEffect(newCard);{
            }

            Platform.runLater(() -> {
                pane.getChildren().add(newCard);
                fadeInTransition(newCard, 1.0);
                onTop.toFront();
            });
            // If Card with the same ID and position exists, do nothing
        }
    }

    private void turnObjectives(MouseEvent event){
         if (event.getClickCount() == 2)
            turnAround(event);
    }

    /**
     * Turns the card around when clicked
     *
     * @param event the mouse event
     */
    private void turnAround(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        Card card = (Card) clickedCard.getUserData();

        if (card != null) {
            clickedCard.setDisable(true);

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

                fadeIn.setOnFinished(e2 -> {
                    clickedCard.setDisable(false);
                });

                fadeIn.play();
            });

            fadeOut.play();
        }
    }

    /**
     * Selects the card when clicked once, making it bigger and stores this selection in selectedCard
     * Turns the card around when clicked twice
     *
     * @param event the mouse event
     */
    private void placeStarter(MouseEvent event) {
        if (clickCounter == -1) {
            ImageView clickedCard = (ImageView) event.getSource();
            StarterCard card = (StarterCard) clickedCard.getUserData();
            boolean isFront = card.isFront();

            if (event.getClickCount() == 1) {

                if (selectedCard != null)
                    makeSmallerTransition(selectedCard);

                makeBiggerTransition(clickedCard);
                this.selectedCard = clickedCard;

                this.selectedStarterFront = isFront ? 1 : 0;

            } else if (event.getClickCount() == 2) {
                turnAround(event);
                if (selectedStarterFront == 1)
                    selectedStarterFront = 0;
                else
                    selectedStarterFront = 1;

            }
        }
    }

    /**
     * Selects the card when clicked once, making it bigger and stores this selection in selectedCard
     * Turns the card around when clicked twice
     *
     * @param event the mouse event
     */
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
            if (cardID == objectivesToChose.get(0).getID()) {
                this.selectedObjective = 1;
            } else if (cardID == objectivesToChose.get(1).getID()) {
                this.selectedObjective = 2;
            }

        }
    }

    /**
     * If there is a selected card, it sends the information about the selectedCard to the server
     *
     * @param event the mouse event
     */
    private void confirmPlaceStarter(MouseEvent event) {
        if (selectedCard != null) {
            ImageView image = (ImageView) event.getSource();
            fadeOutTransition(mainPane, image, 0.5);
            removeCardFromPosition(layoutXCard1, layoutYHand);
            StarterCard card = (StarterCard) selectedCard.getUserData();
            addNewCardToPane(playground, card.getID(), card.isFront(), card, layoutPlacedStarterX, layoutPlacedStarterY,
                    fitHeightPlaced, fitWidthPlaced, null);
            GUIMessages.writeToClient(selectedStarterFront);
            selectedCard = null;
        }
    }

    /**
     * If there is a selected card, it sends the information about the selectedCard to the server
     *
     * @param event the mouse event
     */
    private void confirmPlaceObjective(MouseEvent event) {
        if (selectedCard != null) {
            ImageView image = (ImageView) event.getSource();
            fadeOutTransition(mainPane, image, 0.5);
            removeCardFromPosition(layoutXChoiceObjective1, layoutYHand);
            removeCardFromPosition(layoutXChoiceObjective2, layoutYHand);
            ObjectiveCard card = (ObjectiveCard) selectedCard.getUserData();
            addNewCardToPane(mainPane, card.getID(), true, card, layoutXObjective, layoutYObjMy, fitHeightCommon,
                    fitWidthCommon, this::turnAround);
            GUIMessages.writeToClient(selectedObjective);
            selectedCard = null;
        }
    }

    /**
     * When the mouse enters the imageView, it makes it bigger
     * When the mouse exits the imageView, it makes it smaller
     *
     * @param imageView the imageView to apply the effect to
     */
    private void hooverEffect(ImageView imageView) {
        imageView.setOnMouseEntered(e -> makeBiggerTransition(imageView));
        imageView.setOnMouseExited(e -> {
            if (selectedCard != imageView)
                makeSmallerTransition(imageView);
        });
    }

    /**
     * When the mouse enters the imageView, it makes it bigger
     *
     * @param imageView the imageView to apply the effect to
     */
    private void makeBiggerTransition(ImageView imageView) {
        ScaleTransition makeBigger = new ScaleTransition(Duration.millis(200), imageView);
        makeBigger.setToX(1.05);
        makeBigger.setToY(1.05);
        makeBigger.play();
    }

    /**
     * When the mouse exits the imageView, it makes it smaller
     *
     * @param imageView the imageView to apply the effect to
     */
    private void makeSmallerTransition(ImageView imageView) {
        ScaleTransition makeSmaller = new ScaleTransition(Duration.millis(200), imageView);
        makeSmaller.setToX(1.0);
        makeSmaller.setToY(1.0);
        makeSmaller.play();
    }

    //TODO see if it works as expected

    /**
     * When the mouse is clicked once, and if the page visualized is the client's, it selects the card to place
     * When the mouse is clicked twice, it turns the card around
     *
     * @param event the mouse event
     */
    private void choseCardToPlace(MouseEvent event) {
        if (clickCounter == -1 ){
            ImageView clickedCard = (ImageView) event.getSource();
            PlaceableCard card = (PlaceableCard) clickedCard.getUserData();
            int cardID = card.getID();

            if (event.getClickCount() == 1 && Objects.equals(currentState, "PlaceTurnState") && Objects.equals(currentPlayerNickname, myself.getNickname())) {
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
            } else if (event.getClickCount() == 2) {
                turnAround(event);

                if(Objects.equals(currentState, "PlaceTurnState") && Objects.equals(currentPlayerNickname, myself.getNickname())) {
                    if (cardID == myHand.getPlaceableCards().getFirst().getID())
                        this.selectedToPlace[0] = 0;
                    else if (cardID == myHand.getPlaceableCards().get(1).getID())
                        this.selectedToPlace[0] = 1;
                    else if (cardID == myHand.getPlaceableCards().get(2).getID())
                        this.selectedToPlace[0] = 2;

                    if (selectedStarterFront == 1)
                        this.selectedToPlace[1] = 0;
                    else
                        this.selectedToPlace[1] = 1;
                }
            }
        }

    }


    /**
     * When the mouse is clicked, it selects the position where to place the card
     * and send all the information needed to the server
     *
     * @param event the mouse event
     */
    private void confirmPlaceCard(MouseEvent event) {
        if (selectedCard != null) {
            makeSmallerTransition(selectedCard);

            ImageView clickedPlaceholder = (ImageView) event.getSource();
            int placeholderX = (int) clickedPlaceholder.getLayoutX();
            int placeholderY = (int) clickedPlaceholder.getLayoutY();

            int relativePosX;
            int relativePosY;


            if (centerXOffset < 0 || centerYOffset < 0) {
                relativePosX = (placeholderX - layoutPlacedStarterX) / offsetAreaX;
                relativePosY = (placeholderY - layoutPlacedStarterY) / offsetAreaY;
            } else {
                relativePosX = (placeholderX - (int) centerXOffset) / offsetAreaX;
                relativePosY = (placeholderY - (int) centerYOffset) / offsetAreaY;
            }

            this.selectedToPlace[2] = relativePosY;
            this.selectedToPlace[3] = relativePosX;

            wrong = false;
            GUIMessages.writeToClient(selectedToPlace);
        }
    }

    /**
     * When the mouse is clicked, it selects the card to pick
     *
     * @param event the mouse event
     */
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

            //TODO it may not be necessary

//            if (selectedPick == 1) {
//                Platform.runLater(() -> {
//                    //add selected card from deck to hand
//                    addNewCardToPane(mainPane, commonArea.getD1().getList().get(0).getID(), true, commonArea.getD1().getList().get(0),
//                            layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
//                    onTop.toFront();
//                    //add new card to deck if deck not empty
//                    if(commonArea.getD1().getList().get(1) != null)
//                        addNewCardToPane(mainPane, commonArea.getD1().getList().get(1).getID(), false, commonArea.getD1().getList().get(1),
//                                layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
//                    clickedCard.toFront();
//                    onTop.toFront();
//                });
//            } else if (selectedPick == 2) {
//                Platform.runLater(() -> {
//                    //add selected card from deck to hand
//                    addNewCardToPane(mainPane, commonArea.getD2().getList().get(0).getID(), true, commonArea.getD2().getList().get(0),
//                            layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
//                    onTop.toFront();
//                    //add new card to deck if deck not empty
//                    if (commonArea.getD2().getList().get(1) != null)
//                        addNewCardToPane(mainPane, commonArea.getD2().getList().get(1).getID(), false, commonArea.getD2().getList().get(1),
//                                layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
//                    clickedCard.toFront();
//                    onTop.toFront();
//                });
//            } else if (selectedPick == 3) {
//                //add selected card to hand
//                addNewCardToPane(mainPane, commonArea.getTableCards().getFirst().getID(), true, commonArea.getTableCards().getFirst(),
//                        layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
//                onTop.toFront();
//                //add new card to table if deck not empty
//                if (commonArea.getD1().getList().get(0) != null)
//                    addNewCardToPane(mainPane, commonArea.getD1().getList().get(0).getID(), true, commonArea.getD1().getList().get(0),
//                            layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//                //add new card to deck if deck not empty
//                if(commonArea.getD1().getList().get(1) != null)
//                    addNewCardToPane(mainPane, commonArea.getD1().getList().get(1).getID(), false, commonArea.getD1().getList().get(1),
//                            layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//            } else if (selectedPick == 4) {
//                //add selected card to hand
//                addNewCardToPane(mainPane, commonArea.getTableCards().get(1).getID(), true, commonArea.getTableCards().get(1),
//                        layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
//                onTop.toFront();
//                //add new card to table if deck not empty
//                if (commonArea.getD1().getList().get(0) != null)
//                    addNewCardToPane(mainPane, commonArea.getD1().getList().get(0).getID(), true, commonArea.getD1().getList().get(0),
//                            layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//                //add new card to deck if deck not empty
//                if(commonArea.getD1().getList().get(1) != null)
//                    addNewCardToPane(mainPane, commonArea.getD1().getList().get(1).getID(), false, commonArea.getD1().getList().get(1),
//                            layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//            } else if (selectedPick == 5) {
//                //add selected card to hand
//                addNewCardToPane(mainPane, commonArea.getTableCards().get(2).getID(), true, commonArea.getTableCards().get(2),
//                        layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
//                onTop.toFront();
//                //add new card to table if deck not empty
//                if (commonArea.getD2().getList().get(0) != null)
//                    addNewCardToPane(mainPane, commonArea.getD2().getList().get(0).getID(), true, commonArea.getD2().getList().get(0),
//                            layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//                //add new card to deck if deck not empty
//                if(commonArea.getD2().getList().get(1) != null)
//                    addNewCardToPane(mainPane, commonArea.getD2().getList().get(1).getID(), false, commonArea.getD2().getList().get(1),
//                            layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//            } else if (selectedPick == 6) {
//                //add selected card to hand
//                addNewCardToPane(mainPane, commonArea.getTableCards().get(3).getID(), true, commonArea.getTableCards().get(3),
//                        layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
//                onTop.toFront();
//                //add new card to table if deck not empty
//                if (commonArea.getD2().getList().get(0) != null)
//                    addNewCardToPane(mainPane, commonArea.getD2().getList().get(0).getID(), true, commonArea.getD2().getList().get(0),
//                            layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//                //add new card to deck if deck not empty
//                if(commonArea.getD2().getList().get(1) != null)
//                    addNewCardToPane(mainPane, commonArea.getD2().getList().get(1).getID(), false, commonArea.getD2().getList().get(1),
//                            layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
//                onTop.toFront();
//            }

            GUIMessages.writeToClient(selectedPick);

        }
    }


    //TODO create images for when non you

    /**
     * When clicked, it displays the next player's page or the client's page
     * If the state is StarterCardState or ObjectiveState, it displays a popup
     */
    @FXML
    private void switchToNextPlayer() {
        nextPlayer.setDisable(true);

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
        }   else if(Objects.equals(currentState, "StarterCardState")) {
            showImagePopup("/Images/Background/pleaseWait.png");
        } else if(Objects.equals(currentState, "ObjectiveState")) {
            showImagePopup("/Images/Background/pleaseWait.png");
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> nextPlayer.setDisable(false));
        pause.play();
    }

    /**
     * Displays the popup with the image
     *
     * @param imagePath the path of the image
     */
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

    /**
     * Fades in the image
     *
     * @param image      the image to fade in
     * @param maxOpacity the maximum opacity
     */
    private void fadeInTransition(ImageView image, double maxOpacity) {
        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(maxOpacity);

        fadeTransitionIn.play();
    }

    /**
     * Fades out the image and removes it
     *
     * @param pane       the pane where the image is
     * @param image      the image to fade out
     * @param maxOpacity the maximum opacity
     */
    private void fadeOutTransition(Pane pane, ImageView image, double maxOpacity) {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(1), image);
        fadeTransitionOut.setFromValue(maxOpacity);
        fadeTransitionOut.setToValue(0.0);

        fadeTransitionOut.setOnFinished(event -> pane.getChildren().remove(image));

        fadeTransitionOut.play();
    }


    /**
     * Fades in the image and then fades it out
     *
     * @param image the image to fade in and out
     */
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

    /**
     * Displays the pions positions based on color
     *
     * @param player the player to set the pions positions
     */
    private void setPions(Player player) {
        Platform.runLater(() -> {
            String color = player.getColor();
            List<ImageView> allPions = Arrays.asList(red, blue, purple, green);
            int score = player.getScore();
            Image img;

            if (Objects.equals(color, "red") && red == null) {
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
            } else if (Objects.equals(color, "red")) {
                setPionPosition(red, score, allPions);
            } else if (Objects.equals(color, "blue")) {
                setPionPosition(blue, score, allPions);
            } else if (Objects.equals(color, "purple")) {
                setPionPosition(purple, score, allPions);
            } else if (Objects.equals(color, "green")) {
                setPionPosition(green, score, allPions);
            }
        });
    }

    /**
     * Sets the pion position based on score
     *
     * @param pion     the pion to set the position
     * @param score    the score of the player
     * @param allPions the list of all pions
     */
    private void setPionPosition(ImageView pion, int score, List<ImageView> allPions) {

            System.out.println("Score: " + score);

        if (pion != null && score == 0 && !isPionAtDesiredPosition(pion, 0)) {
            double[] adjustedPosition = getAdjustedPosition(allPions, positions[0][0], positions[0][1], pion);
            pion.setLayoutX(adjustedPosition[0]);
            pion.setLayoutY(adjustedPosition[1]);
            pion.setFitHeight(49);
            pion.setFitWidth(49);
            pion.setPreserveRatio(true);
            mainPane.getChildren().add(pion);
        } else if (pion != null && !isPionAtDesiredPosition(pion, score)) {
            System.out.println("Moving to: " + score);
            addPoints(pion, score, allPions);
            updatePionsPositions(allPions, pion);
        }
    }

    /**
     * Move the pion to the desired position based on his score
     *
     * @param pion     the pion to add points to
     * @param score    the score of the player
     * @param allPions the list of all pions
     */
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
            for (int i = getScoreByPosition(startX, startY); i <= score; i++) {
                steps++;
                double[] adjustedPosition = getAdjustedPosition(allPions, positions[i][0], positions[i][1], pion);
                path.getElements().add(new LineTo(adjustedPosition[0], adjustedPosition[1]));
            }

            //TODO WHY DOES THE PION DISAPPEAR!?
            PathTransition pathTransition = new PathTransition();
            int speed = 100; //TODO I dont know if its too fast or not
            int durationInSeconds = steps / speed;
            pathTransition.setDuration(Duration.seconds(1));
            pathTransition.setPath(path);
            pathTransition.setNode(pion);
            pathTransition.setCycleCount(1);
            pathTransition.setAutoReverse(false);
            pion.toFront();
            pathTransition.play();
        });
    }

    /**
     * Get the adjusted position of the pion, if in his desired position there is already a pion
     *
     * @param pions   the list of all pions
     * @param targetX the x position of the pion
     * @param targetY the y position of the pion
     * @return the adjusted position of the pion
     */
    private double[] getAdjustedPosition(List<ImageView> pions, double targetX, double targetY,ImageView myPion) {
        double offsetY = 0;

        for (ImageView pion : pions) {
            if(pion != myPion)
                if (pion != null && pion.getLayoutX() == targetX && pion.getLayoutY() == targetY + offsetY) {
                    offsetY -= offsetPions;
            }
        }
        return new double[]{targetX, targetY + offsetY};
    }

    /**
     * Check if the pion is at the desired position
     *
     * @param pion  the pion to check
     * @param score the score of the player
     * @return true if the pion is at the desired position, false otherwise
     */
    private boolean isPionAtDesiredPosition(ImageView pion, int score) {
        double[] desiredPosition = positions[score];
        if ((pion.getLayoutX() != desiredPosition[0] && pion.getLayoutY() != desiredPosition[1])
                || (pion.getLayoutX() != desiredPosition[0] && pion.getLayoutY() != desiredPosition[1] - offsetPions)
                || (pion.getLayoutX() != desiredPosition[0] && pion.getLayoutY() != desiredPosition[1] - 2 * offsetPions)
                || (pion.getLayoutX() != desiredPosition[0] && pion.getLayoutY() != desiredPosition[1] - 3 * offsetPions))
            return false;

        return true;
    }

    /**
     * Update the pions positions if a pion moved
     *
     * @param allPions  the list of all pions
     * @param movedPion the pion that moved
     */
    private void updatePionsPositions(List<ImageView> allPions, ImageView movedPion) {
        Platform.runLater(() -> {
            double movedPionY = movedPion.getLayoutY();
            for (ImageView pion : allPions) {
                if (pion != movedPion && pion != null && pion.getLayoutY() < movedPionY) {
                    double[] adjustedPosition = getAdjustedPosition(allPions, pion.getLayoutX(), pion.getLayoutY(), pion);
                    pion.setLayoutX(adjustedPosition[0]);
                    pion.setLayoutY(adjustedPosition[1]);
                    pion.toFront();
                }
            }
        });
    }

    /**
     * Gets the score based on the position
     *
     * @param x the x position
     * @param y the y position
     */
    private int getScoreByPosition(double x, double y) {

        for (int score = 0; score < positions.length; score++) {
            double[] position = positions[score];
            if (position[0] == x && position[1] == y || position[0] == x && position[1] == y + offsetPions
                    || position[0] == x && position[1] == y + 2 * offsetPions || position[0] == x && position[1] == y + 3 * offsetPions) {
                return score;
            }
        }

        return -1; //not found
    }

    /**
     * Displays the placeholders of the available positions in the playerArea
     */
    private void displayPlaceHolders() {
        placeholdersVisible = true;
        ArrayList<Integer[]> availablePositions = new ArrayList<>();
        availablePositions = myPlayerArea.getAvailablePosition();

        double[] boundingBox = calculateBoundingBox(allCards);
        double totalWidth = boundingBox[2] - boundingBox[0];
        double totalHeight = boundingBox[3] - boundingBox[1];

        centerXOffset = (playground.getWidth() - totalWidth) / 2 - boundingBox[0];
        centerYOffset = (playground.getHeight() - totalHeight) / 2 - boundingBox[1];

        int newLayoutX;
        int newLayoutY;

        for (Integer[] pos : availablePositions) {
            // Calculate the new layout position of the placeholder
            if(centerXOffset <0 || centerYOffset <0) {
                newLayoutX = layoutPlacedStarterX + pos[1] * offsetAreaX;
                newLayoutY = layoutPlacedStarterY + pos[0] * offsetAreaY;
            } else {
                newLayoutX = (int) centerXOffset + pos[1] * offsetAreaX;
                newLayoutY = (int) centerYOffset + pos[0] * offsetAreaY;
            }

            System.out.println("Center: " + centerXOffset + " " + centerYOffset);

            addClickablePlaceholder(playground, newLayoutX, newLayoutY, fitHeightPlaced, fitWidthPlaced, this::confirmPlaceCard);
        }
    }

    /**
     * Removes all the placeholders from the playerArea
     */
    private void removeAllPlaceholders() {
        placeholdersVisible = false;
        List<ImageView> placeholders = playground.getChildren().stream()
                .filter(node -> node instanceof ImageView && node.getStyleClass().contains("placeholder"))
                .map(node -> (ImageView) node)
                .toList();

        for (ImageView placeholder : placeholders) {
            fadeOutTransition(playground, placeholder, 0.5);
        }
    }

    /**
     * Displays the cards in the playerArea
     *
     * @param playerArea the playerArea to display
     */
    public void displayPlayerArea(PlayerArea playerArea) {
        Platform.runLater(() -> {
            // Remove all existing cards from the player area
            playground.getChildren().removeIf(node -> node instanceof ImageView && node.getUserData() instanceof PlaceableCard);

            allCards = playerArea.getAllCards();

            double[] boundingBox = calculateBoundingBox(allCards);
            double totalWidth = boundingBox[2] - boundingBox[0];
            double totalHeight = boundingBox[3] - boundingBox[1];

            centerXOffset = (playground.getWidth() - totalWidth) / 2 - boundingBox[0];
            centerYOffset = (playground.getHeight() - totalHeight) / 2 - boundingBox[1];

            for (PlaceableCard card : allCards) {
                int layoutX =  calculateLayoutX(card) + (int) centerXOffset;
                int layoutY = calculateLayoutY(card) +  (int) centerYOffset;


                ImageView cardImageView = createCardImageView(card.getID(), card.isFront(), card, layoutX, layoutY, fitHeightPlaced, fitWidthPlaced);
                playground.getChildren().add(cardImageView);
                cardImageView.toFront();

                // Adjust z-order
                adjustCardZOrder(cardImageView, card);
            }


            onTop.toFront();
        });
    }

    private int calculateLayoutX(PlaceableCard card) {
        return card.getCells().getFirst().getColumn() * offsetAreaX;
    }

    private int calculateLayoutY(PlaceableCard card) {
        return card.getCells().getFirst().getRow() * offsetAreaY;
    }

    private double[] calculateBoundingBox(ArrayList<PlaceableCard> cards) {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (PlaceableCard card : cards) {
            double posX = calculateLayoutX(card);
            double posY = calculateLayoutY(card);

            if (posX < minX) minX = posX;
            if (posY < minY) minY = posY;
            if (posX + fitWidthPlaced > maxX) maxX = posX + fitWidthPlaced;
            if (posY + fitHeightPlaced > maxY) maxY = posY + fitHeightPlaced;
        }

        return new double[]{minX, minY, maxX, maxY};
    }

    private void adjustCardZOrder(ImageView cardImageView, PlaceableCard card) {
        // Ensure proper layering based on card's position relative to other cards
        boolean isTop = card.getCells().stream().anyMatch(cell -> cell.getTopCard() == card);
        boolean isBottom = card.getCells().stream().anyMatch(cell -> cell.getBottomCard() == card);

        if (isTop) {
            cardImageView.toFront();
        } else if (isBottom) {
            cardImageView.toBack();
        } else {
            playground.getChildren().remove(cardImageView);
            playground.getChildren().add(cardImageView);
        }
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
            double posMaxX = posMinX + fitWidthPlaced;
            double posMinY = layoutPlacedStarterY + pos[1] * offsetAreaY;
            double posMaxY = posMinY + fitHeightPlaced;

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
            scale = Math.min(scaleX, scaleY);

            for (ImageView imageView : cardImageViews) {
                imageView.setFitWidth(imageView.getFitWidth() * scale);
                imageView.setFitHeight(imageView.getFitHeight() * scale);
                imageView.setLayoutX((imageView.getLayoutX() - minX) * scale);
                imageView.setLayoutY((imageView.getLayoutY() - minY) * scale);
            }

            // Adjust placeholder positions
            for (ImageView placeholder : playground.getChildren().stream().filter(node -> node instanceof ImageView && node.getStyleClass().contains("placeholder")).map(node -> (ImageView) node).collect(Collectors.toList())) {
                double placeholderX = placeholder.getLayoutX();
                double placeholderY = placeholder.getLayoutY();
                placeholder.setLayoutX((placeholderX - minX) * scale);
                placeholder.setLayoutY((placeholderY - minY) * scale);
            }
        }
    }



}
