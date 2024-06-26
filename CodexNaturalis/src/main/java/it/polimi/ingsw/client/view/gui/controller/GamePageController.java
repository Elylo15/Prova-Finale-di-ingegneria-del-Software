package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.Utilities;
import it.polimi.ingsw.client.view.gui.message.GUIMessages;
import it.polimi.ingsw.messages.currentStateMessage;
import it.polimi.ingsw.messages.endGameState.declareWinnerMessage;
import it.polimi.ingsw.messages.playerTurnState.updatePlayerMessage;
import it.polimi.ingsw.messages.responseMessage;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.view.gui.Utilities.*;

/**
 * This class is a controller for the gui scene where the game is played.
 * It contains the game board and the player's cards, objectives, and resources.
 * It displays the game state and the player's turn.
 * When the player's turn is active, it allows the player to interact with the game board.
 */
public class GamePageController implements Initializable {
    private final double offsetPions = 5.0;
    private final Map<ImageView, Timeline> activeTimelines = new HashMap<>();
    private final double[][] positions = {
            {1668.0, 468.0},
            {1726.0, 468.0}, {1784.0, 468.0},
            {1812.0, 415.0}, {1755.0, 415.0}, {1697.0, 415.0}, {1639.0, 415.0},
            {1639.0, 362.0}, {1697.0, 362.0}, {1755.0, 362.0}, {1812.0, 362.0},
            {1812.0, 311.0}, {1755.0, 311.0}, {1697.0, 311.0}, {1639.0, 311.0},
            {1639.0, 256.0}, {1697.0, 256.0}, {1755.0, 256.0}, {1812.0, 256.0},
            {1812.0, 204.0}, {1726.0, 175.0}, {1639.0, 204.0},
            {1639.0, 150.0}, {1639.0, 96.0},
            {1673.0, 53.0}, {1726.0, 45.0}, {1781.0, 53.0},
            {1812.0, 150.0}, {1812.0, 96.0},
            {1726.0, 108.0}
    };
    private final double offsetAreaX = 156;
    private final double offsetAreaY = 79;
    private final double layoutPlacedStarterX = 678;
    private final double layoutPlacedStarterY = 203;
    private final double fitHeightPlaced = 133;
    private final double fitWidthPlaced = 200;
    private final double fitHeightCommon = 141;
    private final double fitWidthCommon = 208;
    private final double fitHeightCard = 157;
    private final double fitWidthCard = 234;
    private final double layoutYResource = 746;
    private final double layoutXPick0 = 364;
    private final double layoutXPick1 = 587;
    private final double layoutYGold = 895;
    private final double layoutXDeck = 56;
    private final double layoutYObjMy = 606;
    private final double layoutYObj0 = 750;
    private final double layoutYObj1 = 894;
    private final double layoutXObjective = 1659;
    private final double layoutXChoiceObjective1 = 949;
    private final double layoutXChoiceObjective2 = 1279;
    private final double layoutYHand = 814;
    private final double layoutXCard0 = 864;
    private final double layoutXCard1 = 1110;
    private final double layoutXCard2 = 1356;
    private final Label winnerLabel = new Label();
    private final ArrayList<Label> playerInfoLabel = new ArrayList<>();
    private final BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<ObjectiveCard> objectivesToChose = new ArrayList<>();
    private final int[] selectedToPlace = new int[4];
    private double layoutXSaved = 0;
    @FXML
    private ImageView online;
    @FXML
    private ImageView rotate;
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label player4;
    @FXML
    private Pane mainPane;
    @FXML
    private Label playerName;
    @FXML
    private Pane playground;
    @FXML
    private ImageView state;
    @FXML
    private ImageView onTop;
    @FXML
    private ImageView nextPlayer;
    @FXML
    private ImageView colorName;
    private ImageView winner;
    private ImageView red = null;
    private ImageView blue = null;
    private ImageView purple = null;
    private ImageView green = null;
    private ImageView back;
    private ImageView selectedCard;
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
    private int selectedStarterFront;
    private int selectedPick;
    private int selectedObjective;
    private currentStateMessage currentStateMessageSaved;
    private boolean wrong = false;
    private boolean first = true;
    private boolean isUpdate;
    private final AtomicBoolean endgame = new AtomicBoolean(false);

    // References to Threads
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 200, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    Future<Void> messageListener;
    Future<Void> messageProcessor;




    /**
     * It is used to initialize the controller, it starts the message listener and processor threads.
     *
     * @param url            not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rotateEffect(rotate, 2);
        startMessageListener();
        startMessageProcessor();
        playerName.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 54));
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 43);
        player1.setFont(font);
        player2.setFont(font);
        player3.setFont(font);
        player4.setFont(font);
    }

    /**
     * Starts a new internal thread to listen for messages
     */
    private void startMessageListener() {
        messageListener = executor.submit(() -> {
            while (!endgame.get()) {
                try {
                    Object message = GUIMessages.readToGUI();
                    if (message != null) {
                        messageQueue.put(message);

                    }
                } catch (Exception ignore) {
                }
            }
            return null;
        });
    }

    /**
     * Starts a new internal thread to process messages
     */
    private void startMessageProcessor() {
        messageProcessor = executor.submit(() -> {
            while (!endgame.get()) {
                try {
                    Object message = messageQueue.take();

                    Platform.runLater(() -> processMessage(message));
                } catch (Exception ignore) {
                }
            }
            return null;
        });
    }

    /**
     * Processes the message received from the server based on its type.
     * Displays text and shows popups or images based on the message received.
     *
     * @param message the message to process
     */
    private void processMessage(Object message) {
        switch (message) {
            case currentStateMessage currentStateMessage -> {
                currentStateMessageSaved = currentStateMessage;
                caseCurrentStateMessage(currentStateMessageSaved);
            }
            case ArrayList<?> objects -> handleObjectiveMessage(objects);
            case updatePlayerMessage updatePlayerMessage -> updatePlayerCase(updatePlayerMessage);
            case responseMessage responseMessage -> handleResponseMessage(responseMessage);
            case declareWinnerMessage declareWinnerMessage -> displayWinner(declareWinnerMessage);
            case String disconnected -> {
                if (Objects.equals(disconnected, "disconnected")) {
                    endgame.set(true);
                    messageListener.cancel(true);
                    messageProcessor.cancel(true);
                }
            }
            default -> System.out.println("Unknown message received");
        }
    }

    private void handleObjectiveMessage(ArrayList<?> list) {
        if (Objects.equals(currentState, "objectiveState") && !list.isEmpty() && list.get(0) instanceof ObjectiveCard) {
            playSoundEffect("/Audio/you.mp3");
            objectivesToChose.add((ObjectiveCard) list.get(0));
            objectivesToChose.add((ObjectiveCard) list.get(1));
            objectiveCase();
            setState(currentStateMessageSaved.getPlayer().getColor());
            choosePopUp(currentStateMessageSaved.getPlayer().getColor());
        }
    }

    /**
     * Handles the responseMessage received from the server
     * Displays a pop-up indicating the player to retry if the response is incorrect
     *
     * @param responseMessage the responseMessage received from the server
     */
    private void handleResponseMessage(responseMessage responseMessage) {
        if (!responseMessage.getCorrect()) {
            wrong = true;
            wrongPopUp(currentStateMessageSaved.getPlayer().getColor());
            playSoundEffect("/Audio/wrong.mp3");
            caseCurrentStateMessage(currentStateMessageSaved);
        }
    }

    /**
     * Displays the winner of the game
     *
     * @param declareWinnerMessage the message containing the winner
     */
    private void displayWinner(declareWinnerMessage declareWinnerMessage) {
        rotate.onMouseClickedProperty().setValue(null);
        rotate.addEventHandler(MouseEvent.MOUSE_CLICKED, this::hideSeeWinner);
        winner(declareWinnerMessage);
    }

    /**
     * Displays or hides the winner, and player info
     */
    private void hideSeeWinner(MouseEvent event) {
        if (winner.getOpacity() == 1.0) {
            Utilities.fadeOutTransition(mainPane, winner, 1, false);
            Utilities.fadeOutTransition(mainPane, winnerLabel, 0.8, false);
            Utilities.fadeOutTransition(mainPane, back, 1.0, false);
            for (Label label : playerInfoLabel) Utilities.fadeOutTransition(mainPane, label, 0.8, false);
        } else {
            Utilities.fadeInTransition(winner, 1);
            Utilities.fadeInTransition(winnerLabel, 0.8);
            Utilities.fadeInTransition(back, 1.0);
            for (Label label : playerInfoLabel) Utilities.fadeInTransition(label, 0.8);

            winner.toFront();
            winnerLabel.toFront();
            for (Label label : playerInfoLabel) label.toFront();
            back.toFront();
        }
    }

    /**
     * Displays the winner of the game and the player info
     *
     * @param message the message containing the winner
     */
    private void winner(declareWinnerMessage message) {
        HashMap<String, Integer> objectives = message.getNumberOfObjects();
        HashMap<String, Integer> scores = message.getPlayersPoints();

        List<String> winners = getWinners(objectives, scores);

        Platform.runLater(() -> {
            if (winners.size() == 1) {
                String color = Objects.requireNonNull(findPlayerByName(winners.getFirst())).getColor();

                if (Objects.equals(color, "red"))
                    winner = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Winner/winRed.png"))));
                else if (Objects.equals(color, "blue"))
                    winner = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Winner/winBlue.png"))));
                else if (Objects.equals(color, "green"))
                    winner = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Winner/winGreen.png"))));
                else if (Objects.equals(color, "purple"))
                    winner = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Winner/winPur.png"))));

                winnerLabel.setText(winners.getFirst());
            } else {
                winner.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Winner/winTwo.png"))));
                winnerLabel.setText(winners.getFirst() + "\n" + winners.get(1));
            }
            mainPane.getChildren().add(winner);
            Utilities.fadeInTransition(winner, 1.0);


            winnerLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 60));
            winnerLabel.setTextFill(Color.rgb(53, 31, 23));
            winnerLabel.setLayoutX(980);
            winnerLabel.setLayoutY(555);
            winnerLabel.setPrefWidth(440);
            winnerLabel.setPrefHeight(120);
            winnerLabel.setAlignment(Pos.CENTER);
            mainPane.getChildren().add(winnerLabel);
            Utilities.fadeInTransition(winnerLabel, 0.8);
            displayPlayerInfo(scores, objectives);

            back = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Buttons/image.png"))));
            back.setOnMouseClicked(event -> GUIMessages.writeToClient(true));
            back.setFitWidth(500);
            back.setFitHeight(131);
            back.setLayoutX(1038);
            back.setLayoutY(882);
            mainPane.getChildren().add(back);
            Utilities.fadeInTransition(back, 1.0);
            hooverEffect(back, 1.05);
            if (winners.contains(myself.getNickname()))
                playSoundEffect("/Audio/win.mp3");
            else
                playSoundEffect("/Audio/lose.mp3");
        });

    }

    /**
     * Returns the winners of the game
     *
     * @param objectives the objectives of the players
     * @param scores     the scores of the players
     * @return the list of winners
     */
    private List<String> getWinners(HashMap<String, Integer> objectives, HashMap<String, Integer> scores) {
        List<String> winners = new ArrayList<>();
        int highestScore = Integer.MIN_VALUE;
        int mostObjectives = Integer.MIN_VALUE;

        // First pass to determine the highest score and most objectives
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String player = entry.getKey();
            int score = entry.getValue();
            int playerObjectives = objectives.getOrDefault(player, 0);

            if (score > highestScore || (score == highestScore && playerObjectives > mostObjectives)) {
                highestScore = score;
                mostObjectives = playerObjectives;
            }
        }

        // Second pass to collect all players with the highest score and most objectives
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String player = entry.getKey();
            int score = entry.getValue();
            int playerObjectives = objectives.getOrDefault(player, 0);

            if (score == highestScore && playerObjectives == mostObjectives) {
                winners.add(player);
            }
        }

        return winners;
    }

    /**
     * Displays the player information
     *
     * @param scores     the scores of the players
     * @param objectives the objectives of the players
     */
    private void displayPlayerInfo(HashMap<String, Integer> scores, HashMap<String, Integer> objectives) {
        int i = 0;
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String playerName = entry.getKey();
            Integer playerScore = entry.getValue();
            Integer playerObjectives = objectives.get(playerName);

            // Label with the player information
            Label playerInfo = new Label(playerName + " - " + playerScore + " Points - " + playerObjectives + " Objectives");

            playerInfo.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 37));
            playerInfo.setTextFill(Color.rgb(53, 31, 23));
            playerInfo.setLayoutX(497);
            playerInfo.setLayoutY(198 + 79 * i);
            playerInfo.setPrefWidth(916);
            playerInfo.setPrefHeight(75);
            playerInfo.setAlignment(Pos.CENTER);

            playerInfoLabel.add(playerInfo);
            mainPane.getChildren().add(playerInfoLabel.get(i));
            Utilities.fadeInTransition(playerInfoLabel.get(i), 0.8);

            playerInfoLabel.get(i).toFront();
            i++;
        }
    }

    /**
     * Finds a player by its nickname
     *
     * @param name the nickname of the player
     * @return the player with the given nickname
     */
    private Player findPlayerByName(String name) {
        players.add(myself);

        for (Player player : players) {
            if (player.getNickname().equalsIgnoreCase(name)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Processes the currentStateMessage received from the server
     * Displays the information about the current state of the game and the player's turn
     *
     * @param currentStateMessage the currentStateMessage received from the server
     */
    private void caseCurrentStateMessage(currentStateMessage currentStateMessage) {
        isUpdate = false;
        this.currentState = currentStateMessage.getStateName();
        isLastTurn = currentStateMessage.isLastTurn();
        currentStateCase(currentStateMessage);
        setRotate(currentStateMessage.getPlayer().getColor());


        if (currentStateMessage.getCurrentPlayer().getNickname().equals(myself.getNickname())) {
            if (Objects.equals(currentState, "StarterCardState")) {
                playSoundEffect("/Audio/you.mp3");
                setState(currentStateMessage.getPlayer().getColor());
                starterCase();
                choosePopUp(currentStateMessage.getPlayer().getColor());
            } else if (Objects.equals(currentState, "PlaceTurnState") || Objects.equals(currentState, "PickTurnState")) {
                if (isLastTurn)
                    setLastTurn(currentStateMessage.getCurrentPlayer().getColor());
                else
                    setState(currentStateMessage.getPlayer().getColor());

                if (!wrong && !Objects.equals(currentState, "PickTurnState"))
                    playSoundEffect("/Audio/you.mp3");
            }
        } else {
            if (isLastTurn)
                setLastTurn(currentStateMessage.getCurrentPlayer().getColor());
            else
                setStateNotPlaying(currentStateMessage.getCurrentPlayer().getColor());
        }
    }

    /**
     * Displays the correct image for player's turn based on the color of the player
     *
     * @param color the color of the player
     */
    private void setState(String color) {
        ImageView newStateImage = new ImageView();

        switch (color) {
            case "red" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/YourTurn/youRed.png"))));
            case "blue" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/YourTurn/youBlue.png"))));
            case "green" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/YourTurn/youGreen.png"))));
            case "purple" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/YourTurn/youPur.png"))));
        }

        state = setNewImage(state, newStateImage);
    }

    /**
     * Displays the correct color for the rotating circle based on the color of the player
     *
     * @param color the color of the player
     */
    private void setRotate(String color) {
        ImageView newStateImage = new ImageView();
        switch (color) {
            case "red" ->
                    rotate.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/red.png"))));
            case "blue" ->
                    rotate.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/blue.png"))));
            case "green" ->
                    rotate.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/green.png"))));
            case "purple" ->
                    rotate.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/purple.png"))));
        }

        state = setNewImage(state, newStateImage);
    }

    /**
     * Displays the correct image for player's turn based on the color of the player
     *
     * @param color the color of the player
     */
    private void setStateNotPlaying(String color) {
        ImageView newStateImage = new ImageView();

        if (Objects.equals(currentState, "StarterCardState") || Objects.equals(currentState, "objectiveState")) {
            switch (color) {
                case "red" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Choosing/choosingRed.png"))));
                case "blue" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Choosing/choosingBlue.png"))));
                case "green" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Choosing/choosingGreen.png"))));
                case "purple" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Choosing/choosingPur.png"))));
            }
        } else {
            switch (color) {
                case "red" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/IsPlaying/redPlaying.png"))));
                case "blue" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/IsPlaying/bluePlaying.png"))));
                case "green" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/IsPlaying/greenPlaying.png"))));
                case "purple" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/IsPlaying/purPlaying.png"))));
            }
        }

        state = setNewImage(state, newStateImage);
    }

    /**
     * Displays the correct image for last turn based on the color of the player
     *
     * @param color the color of the player
     */
    private void setLastTurn(String color) {
        ImageView newStateImage = new ImageView();

        switch (color) {
            case "red" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/LastTurn/lastRed.png"))));
            case "blue" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/LastTurn/lastBlue.png"))));
            case "green" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/LastTurn/lastGreen.png"))));
            case "purple" ->
                    newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/LastTurn/lastPur.png"))));
        }

        state = setNewImage(state, newStateImage);
    }

    /**
     * Displays the correct image for next player button based on the color of the player
     *
     * @param color the color of the player
     */
    private void setNextButton(String color) {
        switch (color) {
            case "red" ->
                    nextPlayer.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NextButton/nextRed.png"))));
            case "blue" ->
                    nextPlayer.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NextButton/nextBlue.png"))));
            case "green" ->
                    nextPlayer.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NextButton/nextGreen.png"))));
            case "purple" ->
                    nextPlayer.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NextButton/nextPur.png"))));
        }
    }

    /**
     * Displays the correct template for the player's name based on the color of the player
     *
     * @param color the color of the player
     */
    private void setColorName(String color) {
        ImageView newColor = new ImageView();

        switch (color) {
            case "red" ->
                    newColor.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/redName.png"))));
            case "blue" ->
                    newColor.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/blueName.png"))));
            case "green" ->
                    newColor.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/greenName.png"))));
            case "purple" ->
                    newColor.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/NameTemplate/purpleName.png"))));
        }

        colorName = setNewImage(colorName, newColor);
    }

    /**
     * Displays the correct image for the player using a fade in/fade out transition
     *
     * @param oldImage the old image to fade out
     * @param newImage the new image to fade in
     */
    private ImageView setNewImage(ImageView oldImage, ImageView newImage) {
        if (oldImage != null) {
            newImage.setLayoutX(oldImage.getLayoutX());
            newImage.setLayoutY(oldImage.getLayoutY());
            newImage.setFitHeight(oldImage.getFitHeight());
            newImage.setFitWidth(oldImage.getFitWidth());
            newImage.setPreserveRatio(oldImage.isPreserveRatio());

            Utilities.fadeOutTransition(mainPane, oldImage, 1.0, true);
        }

        mainPane.getChildren().add(newImage);
        Utilities.fadeInTransition(newImage, 1.0);
        return newImage;
    }

    /**
     * Displays the correct image pop-up indicating the player to wait based on the color of the player
     *
     * @param color the color of the player
     */
    private void waitPopUp(String color) {
        switch (color) {
            case "red" -> Utilities.showImagePopup("/Images/PleaseWait/waitRed.png", mainPane);
            case "blue" -> Utilities.showImagePopup("/Images/PleaseWait/waitBlue.png", mainPane);
            case "green" -> Utilities.showImagePopup("/Images/PleaseWait/waitGreen.png", mainPane);
            case "purple" -> Utilities.showImagePopup("/Images/PleaseWait/waitPur.png", mainPane);
        }
    }

    /**
     * Displays the correct image pop-up indicating the player to choose based on the color of the player
     *
     * @param color the color of the player
     */
    private void choosePopUp(String color) {
        switch (color) {
            case "red" -> Utilities.showImagePopup("/Images/YouChoose/youChoRe.png", mainPane);
            case "blue" -> Utilities.showImagePopup("/Images/YouChoose/youChoBl.png", mainPane);
            case "green" -> Utilities.showImagePopup("/Images/YouChoose/youChoGr.png", mainPane);
            case "purple" -> Utilities.showImagePopup("/Images/YouChoose/youChoPu.png", mainPane);
        }
    }

    /**
     * Displays the correct image pop-up indicating the player to retry based on the color of the player
     *
     * @param color the color of the player
     */
    private void wrongPopUp(String color) {
        switch (color) {
            case "red" -> Utilities.showImagePopup("/Images/TryAgain/retryRed.png", mainPane);
            case "blue" -> Utilities.showImagePopup("/Images/TryAgain/retryBlue.png", mainPane);
            case "green" -> Utilities.showImagePopup("/Images/TryAgain/retryGreen.png", mainPane);
            case "purple" -> Utilities.showImagePopup("/Images/TryAgain/retryPur.png", mainPane);
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


        if (!Objects.equals(currentState, "objectiveState") && !Objects.equals(currentState, "StarterCardState") || first) {
            //Load the first commonArea card images the first time. Then don't update them if the state is objectiveState or StarterCardState,
            // to avoid a change in cards before visualizing the player hand. Otherwise, it will look like the cards drawn are objectives.
            first = false;
            addCardsToCommonArea();
        } else
            addCardsToCommonArea();

        addCommonObjective();
        setPage();
        setPions(myself);

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
        isUpdate = true;
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
        if (!Objects.equals(currentState, "objectiveState") && !Objects.equals(currentState, "StarterCardState"))
            addCardsToCommonArea();
        setPage();
    }

    /**
     * Display the new visualized player's nickname with a fade out of the previous one, and a fade in of the new one
     *
     * @param newNickname the new nickname to display
     */
    private void setPlayerNameWithFade(String newNickname) {
        String currentNickname = playerName.getText();
        if (!currentNickname.equals(newNickname)) {

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), playerName);
            fadeOut.setFromValue(0.79);
            fadeOut.setToValue(0.0);
            fadeOut.play();
            fadeOut.setOnFinished(event -> {
                playerName.setText(newNickname);
                Utilities.fadeInTransition(playerName, 0.79);
            });
        }
    }


    /**
     * Sets the visualized page with the cards of the page's player
     */
    private void setPage() {
        String color = (clickCounter == -1) ? myself.getColor() : players.get(clickCounter).getColor();
        setColorName(color);

        String nickname = (clickCounter == -1) ? myself.getNickname() : players.get(clickCounter).getNickname();
        setPlayerNameWithFade(nickname);
        playerName.toFront();

        int next = clickCounter;
        if (clickCounter == players.size() - 1) //To get the nextPlayer button color
            next = -1;
        else
            next++;

        String nextColor = (next == -1) ? myself.getColor() : players.get(next).getColor();
        setNextButton(nextColor);

        if (!currentState.equals("StarterCardState") && !currentState.equals("objectiveState")) {
            if (clickCounter == -1) {
                addCardsToHand();
                addMyObjective();
                if ((Objects.equals(currentState, "PlaceTurnState") && Objects.equals(myself.getNickname(), currentPlayerNickname) && !isUpdate) || wrong) //If it is wrong is my turn for sure
                    displayPlayerAreaAndPlaceholders(myPlayerArea);
                else
                    displayPlayerArea(myPlayerArea);
            } else {
                addPlayerCardsToHand();
                addPlayerObjective();
                displayPlayerArea(players.get(clickCounter).getPlayerArea());
            }
        }
    }


    /**
     * Displays the commonArea
     */
    private void addCardsToCommonArea() {

        Platform.runLater(() -> {
            //Add the front up cards to commonArea
            if (commonArea.getTableCards().getFirst() == null) { //if there is no card (It should return null because commonArea is set to store null in the ArrayList if there is no card)
                removeCardFromPosition(layoutXPick0, layoutYResource); //if there is an image remove it. The method will do nothing if there is not
            } else //if there is a card, add the image
                addNewCardToPane(mainPane, commonArea.getTableCards().getFirst().getID(), true,
                        commonArea.getTableCards().getFirst(), layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);

            if (commonArea.getTableCards().get(1) == null) {
                removeCardFromPosition(layoutXPick1, layoutYResource);
            } else
                addNewCardToPane(mainPane, commonArea.getTableCards().get(1).getID(), true,
                        commonArea.getTableCards().get(1), layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);

            if (commonArea.getTableCards().get(2) == null) {
                removeCardFromPosition(layoutXPick0, layoutYGold);
            } else
                addNewCardToPane(mainPane, commonArea.getTableCards().get(2).getID(), true,
                        commonArea.getTableCards().get(2), layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);

            if (commonArea.getTableCards().get(3) == null) {
                removeCardFromPosition(layoutXPick1, layoutYGold);
            } else
                addNewCardToPane(mainPane, commonArea.getTableCards().get(3).getID(), true,
                        commonArea.getTableCards().get(3), layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);

            //Add the cards from the decks
            if (commonArea.getD1().getList().isEmpty()) {
                removeCardFromPosition(layoutXDeck, layoutYResource);
            } else
                addNewCardToPane(mainPane, commonArea.getD1().getList().getFirst().getID(), false,
                        commonArea.getD1().getList().getFirst(), layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);

            if (commonArea.getD2().getList().isEmpty()) {
                removeCardFromPosition(layoutXDeck, layoutYGold);
            } else
                addNewCardToPane(mainPane, commonArea.getD2().getList().getFirst().getID(), false,
                        commonArea.getD2().getList().getFirst(), layoutXDeck, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
        });
    }

    /**
     * Displays the commonObjective
     */
    private void addCommonObjective() {
        Platform.runLater(() -> {
            if (commonObjectives != null && !commonObjectives.isEmpty()) {
                addNewCardToPane(mainPane, commonObjectives.get(0).getID(), true, commonObjectives.get(0), layoutXObjective,
                        layoutYObj0, fitHeightCommon, fitWidthCommon, this::turnObjectives);
                commonObjectives.get(0).setFront(true); //Set the front, they may arrive backwards, but I want them to be visualized front
                //This way I will have no problem trying to doubleClick and turn them around
                addNewCardToPane(mainPane, commonObjectives.get(1).getID(), true, commonObjectives.get(1), layoutXObjective,
                        layoutYObj1, fitHeightCommon, fitWidthCommon, this::turnObjectives);
                commonObjectives.get(1).setFront(true);
            }
        });
    }

    /**
     * Displays the cards in the player's hand
     */
    private void addCardsToHand() {
        if (clickCounter != -1) //If the player I am visualizing is actually not myself
            addPlayerCardsToHand();
        else if (endgame.get()) { //If the game is finished, the cards will be displayed without any eventHandler
            Platform.runLater(() -> {
                for (int i = 0; i < myHand.getPlaceableCards().size(); i++) {
                    addNewCardToPane(mainPane, myHand.getPlaceableCards().get(i).getID(), true,
                            myHand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, null);
                }
                if (myHand.getPlaceableCards().size() == 2) //If I have two cards, the third image will be empty
                    removeCardFromPosition(layoutXCard2, layoutYHand); //This is need to avoid keeping a card in the empty place
                //if some other player has 3 cards, I visualize his page, then get back to mine
            });
        } else {
            Platform.runLater(() -> {
                //Get the images of the cards, or null if the position is empty
                ImageView card0 = Utilities.getCardFromPosition(layoutXCard0, layoutYHand, mainPane);
                ImageView card1 = Utilities.getCardFromPosition(layoutXCard1, layoutYHand, mainPane);
                ImageView card2 = Utilities.getCardFromPosition(layoutXCard2, layoutYHand, mainPane);
                if (myHand.getPlaceableCards().size() == 3) {
                    //If I have three cards, add them all
                    for (int i = 0; i < 3; i++)
                        addNewCardToPane(mainPane, myHand.getPlaceableCards().get(i).getID(), myHand.getPlaceableCards().get(i).isFront(),
                                myHand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                } else if (myHand.getPlaceableCards().size() == 2) { //No other player will have 2 cards

                    //if I have 2 cards, I placed one
                    if (card0 == null)
                        layoutXSaved = layoutXCard0;
                    else if (card1 == null)
                        layoutXSaved = layoutXCard1;
                    else if (card2 == null)
                        layoutXSaved = layoutXCard2;
                    else if (layoutXSaved == 0)
                        layoutXSaved = layoutXCard2;

                    //Remove the card, so when switching to other pages, and back to mine, the cards will be displayed correctly
                    removeCardFromPosition(layoutXSaved, layoutYHand);

                    //I saved the empty position, so I can add the remaining cards in the correct position
                    if (layoutXSaved == layoutXCard0) {
                        addNewCardToPane(mainPane, myHand.getPlaceableCards().getFirst().getID(), myHand.getPlaceableCards().getFirst().isFront(), myHand.getPlaceableCards().getFirst(),
                                layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                        addNewCardToPane(mainPane, myHand.getPlaceableCards().get(1).getID(), myHand.getPlaceableCards().get(1).isFront(), myHand.getPlaceableCards().get(1),
                                layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                    } else if (layoutXSaved == layoutXCard1) {
                        addNewCardToPane(mainPane, myHand.getPlaceableCards().getFirst().getID(), myHand.getPlaceableCards().getFirst().isFront(), myHand.getPlaceableCards().getFirst(),
                                layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                        addNewCardToPane(mainPane, myHand.getPlaceableCards().get(1).getID(), myHand.getPlaceableCards().get(1).isFront(), myHand.getPlaceableCards().get(1),
                                layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                    } else {
                        addNewCardToPane(mainPane, myHand.getPlaceableCards().getFirst().getID(), myHand.getPlaceableCards().getFirst().isFront(), myHand.getPlaceableCards().getFirst(),
                                layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                        addNewCardToPane(mainPane, myHand.getPlaceableCards().get(1).getID(), myHand.getPlaceableCards().get(1).isFront(), myHand.getPlaceableCards().get(1),
                                layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                    }
                }
            });
        }
    }


    /**
     * Displays visualized player's cards in the player's hand back
     */
    private void addPlayerCardsToHand() {
        //This works as the addCardsToHand, but for the player I am visualizing, so the cards will be back and have no eventHandler
        if (clickCounter == -1)
            addCardsToHand();
        else if (endgame.get()) {
            Platform.runLater(() -> {
                PlayerHand hand = players.get(clickCounter).getPlayerHand();
                for (int i = 0; i < hand.getPlaceableCards().size(); i++) {
                    addNewCardToPane(mainPane, hand.getPlaceableCards().get(i).getID(), true,
                            hand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, null);
                }
                if (hand.getPlaceableCards().size() == 2)
                    removeCardFromPosition(layoutXCard2, layoutYHand);
            });
        } else {
            PlayerHand hand = players.get(clickCounter).getPlayerHand();
            ImageView card0 = Utilities.getCardFromPosition(layoutXCard0, layoutYHand, mainPane);
            ImageView card1 = Utilities.getCardFromPosition(layoutXCard1, layoutYHand, mainPane);
            ImageView card2 = Utilities.getCardFromPosition(layoutXCard2, layoutYHand, mainPane);
            Platform.runLater(() -> {
                if (hand.getPlaceableCards().size() == 3) {
                    for (int i = 0; i < 3; i++)
                        addNewCardToPane(mainPane, hand.getPlaceableCards().get(i).getID(), false,
                                hand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, null);
                } else if (hand.getPlaceableCards().size() == 2) {
                    if (card0 == null)
                        layoutXSaved = layoutXCard0;
                    else if (card1 == null)
                        layoutXSaved = layoutXCard1;
                    else if (card2 == null)
                        layoutXSaved = layoutXCard2;
                    else if (layoutXSaved == 0)
                        layoutXSaved = layoutXCard2;

                    removeCardFromPosition(layoutXSaved, layoutYHand);

                    if (layoutXSaved == layoutXCard0) {
                        addNewCardToPane(mainPane, hand.getPlaceableCards().getFirst().getID(), false, hand.getPlaceableCards().getFirst(),
                                layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, null);
                        addNewCardToPane(mainPane, hand.getPlaceableCards().get(1).getID(), false, hand.getPlaceableCards().get(1),
                                layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, null);
                    } else if (layoutXSaved == layoutXCard1) {
                        addNewCardToPane(mainPane, hand.getPlaceableCards().getFirst().getID(), false, hand.getPlaceableCards().getFirst(),
                                layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, null);
                        addNewCardToPane(mainPane, hand.getPlaceableCards().get(1).getID(), false, hand.getPlaceableCards().get(1),
                                layoutXCard2, layoutYHand, fitHeightCard, fitWidthCard, null);
                    } else {
                        addNewCardToPane(mainPane, hand.getPlaceableCards().getFirst().getID(), false, hand.getPlaceableCards().getFirst(),
                                layoutXCard0, layoutYHand, fitHeightCard, fitWidthCard, null);
                        addNewCardToPane(mainPane, hand.getPlaceableCards().get(1).getID(), false, hand.getPlaceableCards().get(1),
                                layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, null);
                    }
                }
            });
        }
    }

    /**
     * Displays the player's personal objective
     */
    private void addMyObjective() {
        if (clickCounter != -1)
            addPlayerObjective();
        else {
            if (myObjective != null) {
                Platform.runLater(() -> {
                    addNewCardToPane(mainPane, myObjective.getID(), true, myObjective, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::turnObjectives);
                    myObjective.setFront(true); //Forcefully set it to true, in case it is received backwards
                });
            }
        }
    }

    /**
     * Displays the visualized player's secret objective back
     */
    private void addPlayerObjective() {
        if (clickCounter == -1)
            addMyObjective();
        else {
            if (endgame.get()) {
                Platform.runLater(() -> addNewCardToPane(mainPane, players.get(clickCounter).getObjective().getID(), true, players.get(clickCounter).getObjective(),
                        layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::turnObjectives));
                players.get(clickCounter).getObjective().setFront(true);
            } else
                Platform.runLater(() -> addNewCardToPane(mainPane, players.get(clickCounter).getObjective().getID(), false, players.get(clickCounter).getObjective(),
                        layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, null));
        }
    }

    /**
     * Displays the starterCard in player's hand
     */
    private void addStarterCardsToPane() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, myself.getPlayerHand().getPlaceableCards().getFirst().getID(), true,
                    myself.getPlayerHand().getPlaceableCards().getFirst(), layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::placeStarter);
            myself.getPlayerHand().getPlaceableCards().getFirst().setFront(true); //Forcefully set it to true, in case it is received backwards
        });
    }

    /**
     * Displays the two objectives from which the player can choose
     */
    private void setObjectives() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, objectivesToChose.get(0).getID(), true, objectivesToChose.get(0), layoutXChoiceObjective1, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            objectivesToChose.get(0).setFront(true);
            addNewCardToPane(mainPane, objectivesToChose.get(1).getID(), true, objectivesToChose.get(1), layoutXChoiceObjective2, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            objectivesToChose.get(1).setFront(true);
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
    private void addClickablePlaceholder(Pane pane, double layoutX, double layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler) {
        Image image = Utilities.randomColorForPlaceholder();

        // Check if image with the same position and size already exists
        boolean exists = pane.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .anyMatch(node -> {
                    ImageView imageView = (ImageView) node;
                    return imageView.getLayoutX() == layoutX && imageView.getLayoutY() == layoutY &&
                            imageView.getFitWidth() == fitWidth && imageView.getFitHeight() == fitHeight;
                });

        if (!exists) {


            Utilities.createClickablePane(pane, layoutX, layoutY, fitHeight, fitWidth, eventHandler, image);
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
                if (imageView.getUserData() instanceof Card)
                    Utilities.fadeOutTransition(mainPane, imageView, 1, true);
                else
                    Utilities.fadeOutTransition(mainPane, imageView, 0.5, true);
            }
        });
    }

    /**
     * Removes a card from the playerHand, when the card is placed.
     *
     * @param layoutX  the x position of the card
     * @param layoutY  the y position of the card
     * @param onFinish the runnable to call when the card is removed
     */
    private void removeCardFromPositionForHandCard(double layoutX, double layoutY, Runnable onFinish) {
        Platform.runLater(() -> {
            List<Node> nodesToRemove = mainPane.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .filter(node -> {
                        ImageView imageView = (ImageView) node;
                        return imageView.getLayoutX() == layoutX && imageView.getLayoutY() == layoutY;
                    })
                    .toList();

            if (nodesToRemove.isEmpty()) {  //Nothing to remove
                if (onFinish != null) {
                    onFinish.run();
                }
                return;
            }

            AtomicInteger nodesRemovedCount = new AtomicInteger(0);
            int totalNodesToRemove = nodesToRemove.size();

            for (Node node : nodesToRemove) {
                ImageView imageView = (ImageView) node;
                Runnable nodeRemoveCallback = () -> {
                    if (nodesRemovedCount.incrementAndGet() == totalNodesToRemove && onFinish != null) {
                        onFinish.run();
                    }
                };

                if (imageView.getUserData() instanceof Card)
                    fadeOutTransitionForHandCard(mainPane, imageView, 1, nodeRemoveCallback);
                else
                    fadeOutTransitionForHandCard(mainPane, imageView, 0.5, nodeRemoveCallback);
            }
        });
    }

    /**
     * Fades out the card and removes it from the pane
     *
     * @param pane     the pane where the card is
     * @param card     the card to remove
     * @param duration the duration of the fade out transition
     * @param onFinish the runnable to call when the card is removed
     */
    private void fadeOutTransitionForHandCard(Pane pane, ImageView card, double duration, Runnable onFinish) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), card);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> {
            pane.getChildren().remove(card);
            if (onFinish != null) {
                onFinish.run();
            }
        });
        fadeTransition.play();
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
    private void addNewCardToPane(Pane pane, int cardID, boolean front, Card card, double layoutX, double layoutY, double fitHeight, double fitWidth, EventHandler<MouseEvent> eventHandler) {
        ImageView existingCard = pane.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .map(node -> (ImageView) node)
                .filter(imageView -> {
                    Card existing = (Card) imageView.getUserData();
                    return existing != null && existing.getID() == cardID &&
                            imageView.getLayoutX() == layoutX && imageView.getLayoutY() == layoutY;
                })
                .findFirst()
                .orElse(null);

        if (existingCard == null) {
            // Card with the same ID and position doesn't exist, so remove the existing card and load the new one
            removeCardFromPosition(layoutX, layoutY);
            ImageView newCard = Utilities.createCardImageView(cardID, front, card, layoutX, layoutY, fitHeight, fitWidth);
            if (eventHandler != null) {
                newCard.setOnMouseClicked(eventHandler);
            }
            if (pane == mainPane)
                Utilities.hooverEffect(newCard, 1.05);

            Platform.runLater(() -> {
                pane.getChildren().add(newCard);
                Utilities.fadeInTransition(newCard, 1.0);
                onTop.toFront();
            });
            // If Card with the same ID and position exists, do nothing
        }
    }

    private void turnObjectives(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Utilities.turnAround(event);
            onTop.toFront();
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

                if (selectedCard != null) {
                    Utilities.makeSmallerTransition(selectedCard);
                    Utilities.hooverEffect(selectedCard, 1.05);
                }

                Utilities.removeHooverEffect(clickedCard);
                Utilities.makeBiggerTransition(clickedCard, 1.05);
                selectedCard = clickedCard;

                selectedStarterFront = isFront ? 1 : 0;

            } else if (event.getClickCount() == 2) {
                Utilities.turnAround(event);
                onTop.toFront();
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
                Utilities.makeSmallerTransition(selectedCard);
                Utilities.hooverEffect(selectedCard, 1.05);
            }

            Utilities.removeHooverEffect(clickedCard);
            Utilities.makeBiggerTransition(clickedCard, 1.05);
            selectedCard = clickedCard;

            if (cardID == objectivesToChose.get(0).getID()) {
                selectedObjective = 1;
            } else if (cardID == objectivesToChose.get(1).getID()) {
                selectedObjective = 2;
            }
        } else if (event.getClickCount() == 2) {
            Utilities.turnAround(event);
            onTop.toFront();
            if (cardID == objectivesToChose.get(0).getID()) {
                selectedObjective = 1;
            } else if (cardID == objectivesToChose.get(1).getID()) {
                selectedObjective = 2;
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
            Utilities.fadeOutTransition(mainPane, image, 0.5, true);
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
            ObjectiveCard card = (ObjectiveCard) selectedCard.getUserData();
            Utilities.fadeOutTransition(mainPane, image, 0.5, true);
            ImageView obj1 = Utilities.getCardFromPosition(layoutXChoiceObjective1, layoutYHand, mainPane);
            ImageView obj2 = Utilities.getCardFromPosition(layoutXChoiceObjective2, layoutYHand, mainPane);
            Utilities.fadeOutTransition(mainPane, obj1, 1, true);
            Utilities.fadeOutTransition(mainPane, obj2, 1, true);
            addNewCardToPane(mainPane, card.getID(), card.isFront(), card, layoutXObjective, layoutYObjMy, fitHeightCommon,
                    fitWidthCommon, this::turnObjectives);
            GUIMessages.writeToClient(selectedObjective);
            selectedCard = null;
        }
    }

    /**
     * When the mouse is clicked once, and if the page visualized is the client's, it selects the card to place
     * When the mouse is clicked twice, it turns the card around
     *
     * @param event the mouse event
     */
    private void choseCardToPlace(MouseEvent event) {
        if (clickCounter == -1) {
            ImageView clickedCard = (ImageView) event.getSource();
            PlaceableCard card = (PlaceableCard) clickedCard.getUserData();
            int cardID = card.getID();

            if (event.getClickCount() == 1 && Objects.equals(currentState, "PlaceTurnState") && Objects.equals(currentPlayerNickname, myself.getNickname())) {
                if (selectedCard != null) {
                    Utilities.makeSmallerTransition(selectedCard);
                    Utilities.hooverEffect(selectedCard, 1.05);
                }

                Utilities.removeHooverEffect(clickedCard);
                Utilities.makeBiggerTransition(clickedCard, 1.05);
                selectedCard = clickedCard;

                if (cardID == myHand.getPlaceableCards().getFirst().getID())
                    selectedToPlace[0] = 0;
                else if (cardID == myHand.getPlaceableCards().get(1).getID())
                    selectedToPlace[0] = 1;
                else if (cardID == myHand.getPlaceableCards().get(2).getID())
                    selectedToPlace[0] = 2;

                selectedToPlace[1] = card.isFront() ? 1 : 0;
            } else if (event.getClickCount() == 2) {
                Utilities.turnAround(event);
                onTop.toFront();

                if (Objects.equals(currentState, "PlaceTurnState") && Objects.equals(currentPlayerNickname, myself.getNickname())) {
                    if (cardID == myHand.getPlaceableCards().getFirst().getID())
                        selectedToPlace[0] = 0;
                    else if (cardID == myHand.getPlaceableCards().get(1).getID())
                        selectedToPlace[0] = 1;
                    else if (cardID == myHand.getPlaceableCards().get(2).getID())
                        selectedToPlace[0] = 2;

                    if (selectedToPlace[1] == 1)
                        selectedToPlace[1] = 0;
                    else
                        selectedToPlace[1] = 1;
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
            Utilities.makeSmallerTransition(selectedCard);

            // Get the placeholder that was clicked
            ImageView clickedPlaceholder = (ImageView) event.getSource();

            // Retrieve the original row and column from the ImageView properties
            int originalRow = (int) clickedPlaceholder.getProperties().get("originalRow");
            int originalColumn = (int) clickedPlaceholder.getProperties().get("originalColumn");

            // Update the selectedToPlace array with the original positions
            selectedToPlace[2] = originalRow;
            selectedToPlace[3] = originalColumn;

            removeCardFromPositionForHandCard(selectedCard.getLayoutX(), selectedCard.getLayoutY(), () -> {
                GUIMessages.writeToClient(selectedToPlace);
                wrong = false;
                selectedCard = null;
            });
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
                Utilities.makeSmallerTransition(selectedCard);
            }

            selectedCard = clickedCard;
            if (!commonArea.getD1().getList().isEmpty() && cardID == commonArea.getD1().getList().getFirst().getID())
                selectedPick = 1;
            else if (!commonArea.getD2().getList().isEmpty() && cardID == commonArea.getD2().getList().getFirst().getID())
                selectedPick = 2;
            else if (commonArea.getTableCards().getFirst() != null && cardID == commonArea.getTableCards().getFirst().getID())
                selectedPick = 3;
            else if (commonArea.getTableCards().get(1) != null && cardID == commonArea.getTableCards().get(1).getID())
                selectedPick = 4;
            else if (commonArea.getTableCards().get(2) != null && cardID == commonArea.getTableCards().get(2).getID())
                selectedPick = 5;
            else if (commonArea.getTableCards().get(3) != null && cardID == commonArea.getTableCards().get(3).getID())
                selectedPick = 6;

            GUIMessages.writeToClient(selectedPick);
            selectedCard = null;
        }
    }

    /**
     * When clicked, it displays the NextButton player's page or the client's page
     * If the state is StarterCardState or objectiveState, it displays a popup
     */
    @FXML
    private void switchToNextPlayer() {
        nextPlayer.setDisable(true);

        if (!Objects.equals(currentState, "StarterCardState") && !Objects.equals(currentState, "objectiveState")) {
            clickCounter++;
            if (clickCounter == players.size()) {
                clickCounter = -1;
            }
            setPage();
        } else if ((Objects.equals(currentState, "StarterCardState") || Objects.equals(currentState, "objectiveState")) && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            choosePopUp(myself.getColor());
        } else if (Objects.equals(currentState, "StarterCardState") || Objects.equals(currentState, "objectiveState")) {
            waitPopUp(myself.getColor());
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> nextPlayer.setDisable(false));
        pause.play();
    }

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

            if (Objects.equals(color, "red")) {
                if(red == null) {
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_rouge.png")));
                    red = new ImageView(img);
                }
                setPionPosition(red, score, allPions);
            } else if (Objects.equals(color, "blue") ){
                if(blue == null) {
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_bleu.png")));
                    blue = new ImageView(img);
                }
                setPionPosition(blue, score, allPions);
            } else if (Objects.equals(color, "purple")) {
                if(purple == null) {
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_purple.png")));
                    purple = new ImageView(img);
                }
                setPionPosition(purple, score, allPions);
            } else if (Objects.equals(color, "green")) {
                if(green == null) {
                    img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Pions/CODEX_pion_vert.png")));
                    green = new ImageView(img);
                }
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
        if (pion != null && !mainPane.getChildren().contains(pion)) {
            double[] adjustedPosition = getAdjustedPosition(allPions, positions[score][0], positions[score][1], pion);
            pion.setLayoutX(adjustedPosition[0]);
            pion.setLayoutY(adjustedPosition[1]);
            pion.setFitHeight(49);
            pion.setFitWidth(49);
            pion.setPreserveRatio(true);
            mainPane.getChildren().add(pion);
        } else if (pion != null && !isPionAtDesiredPosition(pion, score) && !Objects.equals(currentState, "CurrentState")) {
            if (myself.getNickname().equals(currentPlayerNickname))
                playSoundEffect("/Audio/points.mp3");
            Platform.runLater(() -> {
                double startX = pion.getLayoutX();
                double startY = pion.getLayoutY();
                int start = getScoreByPosition(startX, startY);
                if(start >= 0 && start <= 29)
                    toTop(allPions, pion, positions[start][0]);

                addPoints(pion, score, allPions);
            });
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
                return;  // Invalid score
            }

            double startX = pion.getLayoutX();
            double startY = pion.getLayoutY();
            int start = getScoreByPosition(startX, startY);

            if(start < 0 || start > 29)
                return;  // Invalid start position

            if (activeTimelines.containsKey(pion)) {
                return;  // Do nothing if a timeline is already active
            }

            Timeline timeline = new Timeline();

            KeyFrame keyFrame1 = new KeyFrame(Duration.millis(800), e -> {
                pion.setLayoutX(pion.getLayoutX());
                pion.setLayoutY(pion.getLayoutY());
            });

            timeline.getKeyFrames().add(keyFrame1);

            for (int i = start + 1; i <= score; i++) {
                double[] adjustedPosition = getAdjustedPosition(allPions, positions[i][0], positions[i][1], pion);

                KeyFrame keyFrame = new KeyFrame(Duration.millis((i - start + 1) * 800), e -> {
                    pion.setLayoutX(adjustedPosition[0]);
                    pion.setLayoutY(adjustedPosition[1]);
                    pion.toFront();
                });

                timeline.getKeyFrames().add(keyFrame);
            }

            timeline.setOnFinished(e -> activeTimelines.remove(pion));

            activeTimelines.put(pion, timeline);

            timeline.play();

        });
    }

    /**
     * Move the pions down if there are pions in the same x position, with higher y position,
     * to adapt the pions positions to the one that moved
     *
     * @param pions   the list of all pions
     * @param myPion  the pion that moved
     * @param positionX the x position of the moved pion
     */
    private void toTop(List<ImageView> pions, ImageView myPion, double positionX) {
        List<ImageView> sameXNonMyPions = pions.stream()
                .filter(Objects::nonNull)
                .filter(pion -> pion != myPion)
                .filter(pion -> pion.getLayoutX() == positionX)
                .sorted(Comparator.comparingDouble(ImageView::getLayoutY).reversed())
                .toList();

        if (sameXNonMyPions.isEmpty())
            return;

        Platform.runLater(() -> {

            Timeline timeline = new Timeline();

            for (ImageView pion : sameXNonMyPions) {
                if (pion.getLayoutY() < myPion.getLayoutY()) {
                    KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), e -> {
                        int score = getScoreByPosition(pion.getLayoutX(), pion.getLayoutY() + offsetPions);
                        if (score >= 0 && score <= 29) {
                            boolean found = sameXNonMyPions.stream()
                                    .anyMatch(p -> p != pion && p.getLayoutY() == pion.getLayoutY() + offsetPions);

                            if (!found) {
                                pion.setLayoutY(pion.getLayoutY() + offsetPions);
                            } else
                                pion.setLayoutY(pion.getLayoutY());

                        } else
                            pion.setLayoutY(pion.getLayoutY());
                    });
                    timeline.getKeyFrames().add(keyFrame);
                }
            }
            timeline.play();
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
    private double[] getAdjustedPosition(List<ImageView> pions, double targetX, double targetY, ImageView myPion) {
        double[] adjustedPosition = {targetX, targetY};

        List<ImageView> sameXNonMyPions = pions.stream()
                .filter(Objects::nonNull)
                .filter(pion -> pion != myPion)
                .filter(pion -> pion.getLayoutX() == targetX)
                .sorted(Comparator.comparingDouble(ImageView::getLayoutY).reversed())
                .toList();

        for (ImageView pion : sameXNonMyPions) {
            if (pion.getLayoutY() == adjustedPosition[1]) {
                adjustedPosition[1] -= offsetPions;
            } else {
                break;
            }
        }

        return adjustedPosition;
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
        return (pion.getLayoutX() == desiredPosition[0] && pion.getLayoutY() == desiredPosition[1]) //Starting from base position, go upwards
                || (pion.getLayoutX() == desiredPosition[0] && pion.getLayoutY() == desiredPosition[1] - offsetPions)
                || (pion.getLayoutX() == desiredPosition[0] && pion.getLayoutY() == desiredPosition[1] - 2 * offsetPions)
                || (pion.getLayoutX() == desiredPosition[0] && pion.getLayoutY() == desiredPosition[1] - 3 * offsetPions);
    }

    /**
     * Gets the score based on the position.
     *
     * @param x the x position
     * @param y the y position
     */
    private int getScoreByPosition(double x, double y) {
        for (int score = 0; score < positions.length; score++) {
            if ((positions[score][0] == x && positions[score][1] == y) //Starting from up position, check upwards
                    || (positions[score][0] == x && (positions[score][1] - offsetPions)== y ) //367 e piu 5
                    || (positions[score][0] == x && (positions[score][1] - 2 * offsetPions) == y )
                    || (positions[score][0] == x && (positions[score][1] - 3 * offsetPions) == y )) {
                return score;
            }
        }

        return -1; // Not found
    }

    /**
     * Displays the player area
     *
     * @param playerArea the player area to display
     */
    private void displayPlayerArea(PlayerArea playerArea) {
        Platform.runLater(() -> {
            ArrayList<PlaceableCard> newCards = playerArea.getAllCards();
            List<Node> currentNodes = new ArrayList<>(playground.getChildren());

            boolean needsUpdate = false;
            int countCard = 0;

            for (Node node : currentNodes)
                if (node instanceof ImageView)
                    countCard++;

            if (newCards.size() != countCard) {
                needsUpdate = true;
            } else if (countCard > 0) {
                for (Node node : currentNodes) {
                    if (node instanceof ImageView imageView) {
                        PlaceableCard card = (PlaceableCard) imageView.getUserData();
                        if (card != null) {
                            boolean foundMatchingCard = false;
                            for (PlaceableCard newCard : newCards) {
                                if (card.getID() == newCard.getID()) {
                                    foundMatchingCard = true;
                                    break; // Exit inner loop if match is found
                                }
                            }
                            if (!foundMatchingCard) {
                                needsUpdate = true;
                                break; // Exit outer loop if update
                            }
                        } else {
                            needsUpdate = true; //ImageView.getUserData() is null
                            break;
                        }
                    }
                }
            }

            if (needsUpdate) {
                playground.getChildren().clear();

                ArrayList<ImageView> cardImageViews = new ArrayList<>();

                for (PlaceableCard card : newCards) {
                    ImageView cardImageView = Utilities.createCardImageView(
                            card.getID(), card.isFront(), card,
                            card.getCells().getFirst().getColumn() * offsetAreaX,
                            card.getCells().getFirst().getRow() * offsetAreaY,
                            fitHeightPlaced, fitWidthPlaced
                    );

                    cardImageViews.add(cardImageView);
                    Utilities.fadeInTransition(cardImageView, 1.0);
                }

                playground.getChildren().addAll(cardImageViews);

                // Adjust z-order of the cards
                for (ImageView cardImageView : cardImageViews) {
                    PlaceableCard card = getCardFromImageView(cardImageView, newCards);
                    if (card instanceof StarterCard)
                        adjustCardZOrder(cardImageView, new ArrayList<>());
                }

                // Adjust size and position to fit
                adjustSizeAndPosition(playground, cardImageViews);

                Utilities.fadeInTransition(playground, 1.0);
            }
        });
    }

    /**
     * Displays the player area and placeholders for placing cards
     *
     * @param playerArea the player area to display
     */
    private void displayPlayerAreaAndPlaceholders(PlayerArea playerArea) {
        Platform.runLater(() -> {
            ArrayList<PlaceableCard> newCards = playerArea.getAllCards();

            playground.getChildren().clear();

            ArrayList<ImageView> cardImageViews = new ArrayList<>();

            for (PlaceableCard card : newCards) {
                ImageView cardImageView = Utilities.createCardImageView(
                        card.getID(), card.isFront(), card,
                        card.getCells().getFirst().getColumn() * offsetAreaX,
                        card.getCells().getFirst().getRow() * offsetAreaY,
                        fitHeightPlaced, fitWidthPlaced
                );

                cardImageViews.add(cardImageView);
                Utilities.fadeInTransition(cardImageView, 1.0);
            }

            ArrayList<ImageView> placeholderImageViews = new ArrayList<>();

            ArrayList<Integer[]> availablePositions = playerArea.getAvailablePosition();
            for (Integer[] pos : availablePositions) {
                double layoutX = pos[1] * offsetAreaX;  // Inverted x and y
                double layoutY = pos[0] * offsetAreaY;
                ImageView placeholderImageView = createPlaceholderImageView(
                        layoutX, layoutY,
                        this::confirmPlaceCard, pos[0], pos[1]
                );
                placeholderImageViews.add(placeholderImageView);
                Utilities.fadeInTransition(placeholderImageView, 0.5);
            }

            ArrayList<ImageView> allImageViews = new ArrayList<>();
            allImageViews.addAll(placeholderImageViews);
            allImageViews.addAll(cardImageViews);

            playground.getChildren().addAll(allImageViews);

            for (ImageView cardImageView : cardImageViews) {
                PlaceableCard card = getCardFromImageView(cardImageView, newCards);
                if (card instanceof StarterCard)
                    adjustCardZOrder(cardImageView, new ArrayList<>());

            }

            adjustSizeAndPosition(playground, allImageViews);
            Utilities.fadeInTransition(playground, 1.0);
        });
    }

    /**
     * Adjusts the z-order of the card based on its position relative to other cards
     *
     * @param starterImage the card image view
     */
    private void adjustCardZOrder(ImageView starterImage, ArrayList<ImageView> nextLevelCards) {
        if (starterImage == null)
            return;
        if (nextLevelCards.isEmpty()) {
            starterImage.toFront();

            StarterCard starterCard = (StarterCard) starterImage.getUserData();

            ArrayList<ImageView> newNextLevelCard = starterCard.getCells().stream()
                    .filter(cell -> cell.getTopCard() != null && cell.getTopCard() != starterCard)
                    .map(Cell::getTopCard)
                    .map(card -> (ImageView) playground.getChildren().stream()
                            .filter(node -> node instanceof ImageView)
                            .filter(node -> node.getUserData() == card)
                            .findFirst().orElse(null))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (newNextLevelCard.isEmpty())
                return;
            adjustCardZOrder(starterImage, newNextLevelCard);
        } else {
            for (ImageView nextLevelCard : nextLevelCards)
                nextLevelCard.toFront();

            ArrayList<ImageView> newNextLevelCard = nextLevelCards.stream()
                    .map(card -> (PlaceableCard) card.getUserData())
                    .flatMap(card -> card.getCells().stream()
                            .filter(cell -> cell.getTopCard() != null && cell.getTopCard() != card))
                    .map(Cell::getTopCard)
                    .map(card -> (ImageView) playground.getChildren().stream()
                            .filter(node -> node instanceof ImageView)
                            .filter(node -> node.getUserData() == card)
                            .findFirst().orElse(null))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (newNextLevelCard.isEmpty())
                return;

            adjustCardZOrder(starterImage, newNextLevelCard);
        }
    }

    /**
     * Get the PlaceableCard associated with a given ImageView
     *
     * @param imageView the ImageView
     * @param cards     list of all PlaceableCards
     * @return the PlaceableCard associated with the ImageView
     */
    private PlaceableCard getCardFromImageView(ImageView imageView, List<PlaceableCard> cards) {
        PlaceableCard cardImage = (PlaceableCard) imageView.getUserData();
        if (cardImage != null) {
            for (PlaceableCard card : cards) {
                if (card.getID() == cardImage.getID()) {
                    return card;
                }
            }
        }
        return null;
    }

    /**
     * Creates an ImageView for a placeholder at the specified position
     *
     * @param layoutX        the x position of the placeholder
     * @param layoutY        the y position of the placeholder
     * @param eventHandler   the event handler to call when the placeholder is clicked
     * @param originalRow    the original row of the placeholder
     * @param originalColumn the original column of the placeholder
     * @return the ImageView for the placeholder
     */
    private ImageView createPlaceholderImageView(double layoutX, double layoutY, EventHandler<MouseEvent> eventHandler, int originalRow, int originalColumn) {
        Image image = Utilities.randomColorForPlaceholder();

        ImageView imageView = new ImageView(image);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitHeight(133.0);
        imageView.setFitWidth(200.0);
        imageView.setOpacity(0.5);

        // Store the original row and column in the ImageView properties
        imageView.getProperties().put("originalRow", originalRow);
        imageView.getProperties().put("originalColumn", originalColumn);

        imageView.setOnMouseClicked(eventHandler);

        return imageView;
    }

    /**
     * Adjusts the size and position of all image views to fit within the playground
     *
     * @param playground the playground pane
     * @param imageViews the list of image views
     */
    private void adjustSizeAndPosition(Pane playground, ArrayList<ImageView> imageViews) {
        // Calculate bounding box of all elements
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (ImageView imageView : imageViews) {
            minX = Math.min(minX, imageView.getLayoutX());
            minY = Math.min(minY, imageView.getLayoutY());
            maxX = Math.max(maxX, imageView.getLayoutX() + imageView.getFitWidth());
            maxY = Math.max(maxY, imageView.getLayoutY() + imageView.getFitHeight());
        }

        double scaleX = playground.getWidth() / (maxX - minX);
        double scaleY = playground.getHeight() / (maxY - minY);
        double scale = Math.min(scaleX, scaleY);
        if (scale > 1.0) scale = 1.0; //No scale up!

        for (ImageView imageView : imageViews) {
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
            imageView.setLayoutX((imageView.getLayoutX() - minX) * scale + (playground.getWidth() - (maxX - minX) * scale) / 2);
            imageView.setLayoutY((imageView.getLayoutY() - minY) * scale + (playground.getHeight() - (maxY - minY) * scale) / 2);
        }
    }

    /**
     * Displays the online players
     */
    @FXML
    private void openOnline() {
        if (!online.isVisible()) {
            online.setVisible(true);
            ArrayList<String> onlinePLayers = currentStateMessageSaved.getOnlinePlayers();
            Platform.runLater(() -> {
                for (int i = 0; i < onlinePLayers.size(); i++) {
                    if (i == 0) {
                        player1.setText(onlinePLayers.get(i));
                        player1.setVisible(true);
                    } else if (i == 1) {
                        player2.setText(onlinePLayers.get(i));
                        player2.setVisible(true);
                    } else if (i == 2) {
                        player3.setText(onlinePLayers.get(i));
                        player3.setVisible(true);
                    } else if (i == 3) {
                        player4.setText(onlinePLayers.get(i));
                        player4.setVisible(true);
                    }
                }
            });
        } else {
            online.setVisible(false);
            player1.setVisible(false);
            player2.setVisible(false);
            player3.setVisible(false);
            player4.setVisible(false);
        }
    }
}
