package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.*;
import static it.polimi.ingsw.protocol.client.view.GUI.Utilities.fadeOutTransition;
import static it.polimi.ingsw.protocol.client.view.GUI.controller.SceneManager.playSoundEffect;

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
    private final BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<ObjectiveCard> objectivesToChose = new ArrayList<>();
    private final int[] selectedToPlace = new int[4];
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
    private double scaleFactor = 1.0;
    private double[] boundingBox;
    private double centerXOffset = 0;
    private double centerYOffset = 0;
    private ImageView winner;
    private ImageView red;
    private ImageView blue;
    private ImageView purple;
    private ImageView green;
    private ImageView back;
    private Label playerInfoLabel;
    private ImageView display;
    private ImageView selectedCard;
    private Player myself;
    private PlayerHand myHand;
    private ObjectiveCard myObjective;
    private PlayerArea myPlayerArea;
    private String currentPlayerNickname;
    private CommonArea commonArea;
    private ArrayList<ObjectiveCard> commonObjectives;
    private String currentState;
    private ArrayList<PlaceableCard> allCards = new ArrayList<>();
    private int clickCounter = -1;
    private boolean isLastTurn = false;
    private boolean firstTimePlace = true;
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

    //TODO maybe I could not use this internal thread, and make a while true that just reads with GUIMessages.readToGUI() and then process the message
    //TODO check if to front of on top and of pions works as expected
    //TODO check if fade effect works on playerArea - name - state - colorName - winner
    //TODo check if errors image page handeled correctly


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
        new Thread(() -> {
            while (true) {
                try {
                    Object message = messageQueue.take();

                    Platform.runLater(() -> processMessage(message));
                } catch (Exception e) {
                    System.out.println("Error in processing message");
                }
            }
        }).start();
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
            case ArrayList<?> list -> {
                if (Objects.equals(currentState, "ObjectiveState") && !list.isEmpty() && list.getFirst() instanceof ObjectiveCard) {
                    playSoundEffect("/Audio/you.mp3");

                    objectivesToChose.add((ObjectiveCard) list.get(0));
                    objectivesToChose.add((ObjectiveCard) list.get(1));
                    objectiveCase();
                    setState(currentStateMessageSaved.getPlayer().getColor());
                    choosePopUp(currentStateMessageSaved.getPlayer().getColor());
                }
            }
            case updatePlayerMessage update -> updatePlayerCase(update);
            case responseMessage responseMessage -> {
                if (!responseMessage.getCorrect()) {
                    wrong = true;
                    wrongPopUp(currentStateMessageSaved.getPlayer().getColor());

                    playSoundEffect("/Audio/wrong.mp3");

                    caseCurrentStateMessage(currentStateMessageSaved);
                }
            }
            case declareWinnerMessage declareWinnerMessage -> {
                display = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/SocketRmi/rotate.png"))));
                display.setFitWidth(124);
                display.setFitHeight(106);
                display.setLayoutX(1473);
                display.setLayoutY(108);
                mainPane.getChildren().add(display);
                rotateEffect(display, 3);

                winner(declareWinnerMessage);
                seeWinner();
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    /**
     * Displays or hides the winner, and player info
     */
    private void seeWinner() {
        mainPane.setOnMouseClicked(event -> {
            if (winner.isVisible()) {
                fadeOutTransition(mainPane, winner, 1.0, false);
                fadeOutTransition(mainPane, winnerLabel, 1.0, false);
                fadeOutTransition(mainPane, back, 1.0, false);
                fadeOutTransition(mainPane, playerInfoLabel, 1.0, false);
            }
        });

        display.setOnMouseClicked(event -> {
            if (winner.isVisible()) {
                fadeOutTransition(mainPane, winner, 1.0, false);
                fadeOutTransition(mainPane, winnerLabel, 1.0, false);
                fadeOutTransition(mainPane, back, 1.0, false);
                fadeOutTransition(mainPane, playerInfoLabel, 1.0, false);
            } else {
                fadeInTransition(winner, 1.0);
                fadeInTransition(winnerLabel, 1.0);
                fadeInTransition(back, 1.0);
                fadeInTransition(playerInfoLabel, 1.0);
            }
        });

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
                winner = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Winner/winTwo.png"))));
                winnerLabel.setText(winners.get(0) + "\n" + winners.get(1));

            }
            mainPane.getChildren().add(winner);
            fadeInTransition(winner, 1.0);
        });

        winnerLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 60));
        winnerLabel.setTextFill(Color.rgb(30, 30, 30));
        winnerLabel.setLayoutX(980);
        winnerLabel.setLayoutY(555);
        winnerLabel.setPrefWidth(440);
        winnerLabel.setPrefHeight(120);
        winnerLabel.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(winnerLabel);
        fadeInTransition(winnerLabel, 1.0);
        displayPlayerInfo(scores, objectives);

        Platform.runLater(() -> {
            back = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/image.png"))));
            back.setOnMouseClicked(event -> SceneManager.MainView());
            back.setFitWidth(500);
            back.setFitHeight(131);
            back.setLayoutX(1038);
            back.setLayoutY(882);
            mainPane.getChildren().add(back);
            fadeInTransition(back, 1.0);
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

            // Create a label with the player information
            playerInfoLabel = new Label(playerName + " - " + playerScore + " Points - " + playerObjectives + " Objectives");

            playerInfoLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/FantasyScript.ttf"), 37));
            playerInfoLabel.setTextFill(Color.rgb(30, 30, 30));
            playerInfoLabel.setLayoutX(497);
            playerInfoLabel.setLayoutY(198 + 79 * i);
            playerInfoLabel.setPrefWidth(916);
            playerInfoLabel.setPrefHeight(75);
            playerInfoLabel.setAlignment(Pos.CENTER);
            i++;

            mainPane.getChildren().add(playerInfoLabel);
            fadeInTransition(playerInfoLabel, 1.0);
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
        this.currentState = currentStateMessage.getStateName();
        isLastTurn = currentStateMessage.isLastTurn();
        currentStateCase(currentStateMessage);

        if (isLastTurn) {
            setLastTurn(currentStateMessage.getCurrentPlayer().getColor());
        }

        if (currentStateMessage.getCurrentPlayer().getNickname().equals(myself.getNickname())) {
            if (Objects.equals(currentState, "StarterCardState")) {
                String audioFile = Objects.requireNonNull(SceneManager.class.getResource("/Audio/you.mp3")).toString();
                Media media = new Media(audioFile);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

                setState(currentStateMessage.getPlayer().getColor());
                starterCase();
                choosePopUp(currentStateMessage.getPlayer().getColor());
            } else if (Objects.equals(currentState, "PlaceTurnState")) {
                if (!isLastTurn && !wrong) {
                    String audioFile = Objects.requireNonNull(SceneManager.class.getResource("/Audio/you.mp3")).toString();
                    Media media = new Media(audioFile);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    setState(currentStateMessage.getPlayer().getColor());
                }

                if (firstTimePlace) {
                    firstTimePlace = false;
                }
            }
        } else {
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
     * Displays the correct image for player's turn based on the color of the player
     *
     * @param color the color of the player
     */
    private void setStateNotPlaying(String color) {
        ImageView newStateImage = new ImageView();

        if (Objects.equals(currentState, "StarterCardState") || Objects.equals(currentState, "ObjectiveState")) {
            switch (color) {
                case "red" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Choosing/choosingRed.png"))));
                case "blue" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Choosing/choosingBlue.png"))));
                case "green" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Choosing/choosingGreen.png"))));
                case "purple" ->
                        newStateImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Background/ChoosingPur.png"))));
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

            fadeOutTransition(mainPane, oldImage, 1.0, true);
        }

        mainPane.getChildren().add(newImage);
        fadeInTransition(newImage, 1.0);
        return newImage;
    }

    /**
     * Displays the correct image pop-up indicating the player to wait based on the color of the player
     *
     * @param color the color of the player
     */
    private void waitPopUp(String color) {
        switch (color) {
            case "red" -> showImagePopup("/Images/PleaseWait/waitRed.png", mainPane);
            case "blue" -> showImagePopup("/Images/PleaseWait/waitBlue.png", mainPane);
            case "green" -> showImagePopup("/Images/PleaseWait/waitGreen.png", mainPane);
            case "purple" -> showImagePopup("/Images/PleaseWait/waitPur.png", mainPane);
        }
    }

    /**
     * Displays the correct image pop-up indicating the player to choose based on the color of the player
     *
     * @param color the color of the player
     */
    private void choosePopUp(String color) {
        switch (color) {
            case "red" -> showImagePopup("/Images/YouChoose/youChoRe.png", mainPane);
            case "blue" -> showImagePopup("/Images/YouChoose/youChoBl.png", mainPane);
            case "green" -> showImagePopup("/Images/YouChoose/youChoGr.png", mainPane);
            case "purple" -> showImagePopup("/Images/YouChoose/youChoPu.png", mainPane);
        }
    }

    /**
     * Displays the correct image pop-up indicating the player to retry based on the color of the player
     *
     * @param color the color of the player
     */
    private void wrongPopUp(String color) {
        switch (color) {
            case "red" -> showImagePopup("/Images/TryAgain/retryRed.png", mainPane);
            case "blue" -> showImagePopup("/Images/TryAgain/retryBlue.png", mainPane);
            case "green" -> showImagePopup("/Images/TryAgain/retryGreen.png", mainPane);
            case "purple" -> showImagePopup("/Images/TryAgain/retryPur.png", mainPane);
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
    }

    /**
     * Display the new visualized player's nickname with a fade out of the previous one, and a fade in of the new one
     * @param newNickname the new nickname to display
     */
    private void setPlayerNameWithFade(String newNickname) {
        String currentNickname = playerName.getText();
        if (!currentNickname.equals(newNickname)) {

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), playerName);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                // Change the text after fade out is finished
                playerName.setText(newNickname);

                // Fade in the new text
                fadeInTransition(playerName, 1.0);
            });
        }
    }


    /**
     * Sets the visualized page with the cards of the page's player
     */
    private void setPage() {
        String nickname = (clickCounter == -1) ? myself.getNickname() : players.get(clickCounter).getNickname();
        setPlayerNameWithFade(nickname);

        String color = (clickCounter == -1) ? myself.getColor() : players.get(clickCounter).getColor();
        setColorName(color);

        int next = clickCounter;
        if (clickCounter == players.size() - 1)
            next = -1;
        else
            next++;

        String nextColor = (next == -1) ? myself.getColor() : players.get(next).getColor();
        setNextButton(nextColor);

        if (!currentState.equals("StarterCardState") && !currentState.equals("ObjectiveState")) {
            if (clickCounter == -1) {
                addCardsToHand();
                addMyObjective();
                displayPlayerArea(myPlayerArea);
                if ((Objects.equals(currentState, "PlaceTurnState") && Objects.equals(myself.getNickname(), currentPlayerNickname) && !placeholdersVisible) || wrong)
                    displayPlaceHolders();
                else if (placeholdersVisible)
                    removeAllPlaceholders();
            } else {
                if (placeholdersVisible)
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
    private void addCardsToCommonArea() {

        Platform.runLater(() -> {
            //Add the front up cards to commonArea
            if (commonArea.getTableCards().getFirst() == null)
                removeCardFromPosition(layoutXPick0, layoutYResource, mainPane);
            else
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonArea.getTableCards().getFirst().getID(), true,
                        commonArea.getTableCards().getFirst(), layoutXPick0, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if (commonArea.getTableCards().get(1) == null)
                removeCardFromPosition(layoutXPick1, layoutYResource, mainPane);
            else
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonArea.getTableCards().get(1).getID(), true,
                        commonArea.getTableCards().get(1), layoutXPick1, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if (commonArea.getTableCards().get(2) == null)
                removeCardFromPosition(layoutXPick0, layoutYGold, mainPane);
            else
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonArea.getTableCards().get(2).getID(), true,
                        commonArea.getTableCards().get(2), layoutXPick0, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if (commonArea.getTableCards().get(3) == null)
                removeCardFromPosition(layoutXPick1, layoutYGold, mainPane);
            else
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonArea.getTableCards().get(3).getID(), true,
                        commonArea.getTableCards().get(3), layoutXPick1, layoutYGold, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            //Add the cards to deck
            if (commonArea.getD1().getList().getFirst() == null)
                removeCardFromPosition(layoutXDeck, layoutYResource, mainPane);
            else
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonArea.getD1().getList().getFirst().getID(), false,
                        commonArea.getD1().getList().getFirst(), layoutXDeck, layoutYResource, fitHeightCommon, fitWidthCommon, this::pickCard);
            onTop.toFront();

            if (commonArea.getD2().getList().getFirst() == null)
                removeCardFromPosition(layoutXDeck, layoutYGold, mainPane);
            else
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonArea.getD2().getList().getFirst().getID(), false,
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
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonObjectives.get(0).getID(), commonObjectives.get(0).isFront(), commonObjectives.get(0), layoutXObjective,
                        layoutYObj0, fitHeightCommon, fitWidthCommon, this::turnObjectives);
                onTop.toFront();
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, commonObjectives.get(1).getID(), commonObjectives.get(1).isFront(), commonObjectives.get(1), layoutXObjective,
                        layoutYObj1, fitHeightCommon, fitWidthCommon, this::turnObjectives);
                onTop.toFront();
            }
        });
    }

    /**
     * Displays the cards in the player's hand
     */
    private void addCardsToHand() {
        Platform.runLater(() -> {
            if (myHand.getPlaceableCards().size() < 3) {
                removeCardFromPosition(layoutXCard2, layoutYHand, mainPane);
                onTop.toFront();
            }

            for (int i = 0; myHand.getPlaceableCards().size() > i; i++) {
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, myHand.getPlaceableCards().get(i).getID(), true,
                        myHand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, this::choseCardToPlace);
                onTop.toFront();
            }
            onTop.toFront();
        });
    }

    /**
     * Displays the player's personal objective
     */
    private void addMyObjective() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, myObjective.getID(), myObjective.isFront(), myObjective, layoutXObjective, layoutYObjMy, fitHeightCommon, fitWidthCommon, this::turnObjectives);
            onTop.toFront();
        });
    }

    /**
     * Displays visualized player's cards in the player's hand back
     */
    private void addPlayerCardsToHand() {
        if (players.get(clickCounter) != null) {
            PlayerHand playerHand = players.get(clickCounter).getPlayerHand();
            Platform.runLater(() -> {
                if (playerHand.getPlaceableCards().size() < 3) {
                    removeCardFromPosition(layoutXCard2, layoutYHand, mainPane);
                    onTop.toFront();
                }

                for (int i = 0; i < playerHand.getPlaceableCards().size(); i++) {
                    addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, playerHand.getPlaceableCards().get(i).getID(), false,
                            playerHand.getPlaceableCards().get(i), layoutXCard0 + i * 246, layoutYHand, fitHeightCard, fitWidthCard, null);
                    onTop.toFront();
                }
                onTop.toFront();
            });
        }
    }

    /**
     * Displays the visualized player's secret objective back
     */
    private void addPlayerObjective() {
        if (players.size() > clickCounter && players.get(clickCounter) != null) {
            Platform.runLater(() -> {
                addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, players.get(clickCounter).getObjective().getID(), false, players.get(clickCounter).getObjective(),
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
            addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, myself.getPlayerHand().getPlaceableCards().getFirst().getID(), true,
                    myself.getPlayerHand().getPlaceableCards().getFirst(), layoutXCard1, layoutYHand, fitHeightCard, fitWidthCard, this::placeStarter);
            onTop.toFront();
        });
    }

    /**
     * Displays the two objectives from which the player can choose
     */
    private void setObjectives() {
        Platform.runLater(() -> {
            addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, objectivesToChose.get(0).getID(), true, objectivesToChose.get(0), layoutXChoiceObjective1, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            onTop.toFront();
            addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, objectivesToChose.get(1).getID(), true, objectivesToChose.get(1), layoutXChoiceObjective2, layoutYHand, fitHeightCard,
                    fitWidthCard, this::chooseObjective);
            onTop.toFront();
        });
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
     * Turns the card around when clicked twice
     *
     * @param event the mouse event
     */
    private void turnObjectives(MouseEvent event) {
        if (event.getClickCount() == 2) {
            turnAround(event);
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

                if (selectedCard != null)
                    makeSmallerTransition(selectedCard);

                makeBiggerTransition(clickedCard, 1.05);
                this.selectedCard = clickedCard;

                this.selectedStarterFront = isFront ? 1 : 0;

            } else if (event.getClickCount() == 2) {
                turnAround(event);
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
                makeSmallerTransition(selectedCard);
            }

            makeBiggerTransition(clickedCard, 1.05);
            this.selectedCard = clickedCard;

            if (cardID == objectivesToChose.get(0).getID()) {
                this.selectedObjective = 1;
            } else if (cardID == objectivesToChose.get(1).getID()) {
                this.selectedObjective = 2;
            }
        } else if (event.getClickCount() == 2) {
            turnAround(event);
            onTop.toFront();
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
            makeSmallerTransition(selectedCard);
            ImageView image = (ImageView) event.getSource();
            fadeOutTransition(mainPane, image, 0.5, true);
            removeCardFromPosition(layoutXCard1, layoutYHand, mainPane);
            StarterCard card = (StarterCard) selectedCard.getUserData();
            addNewCardToPane(playground, mainPane, clickCounter, selectedCard, card.getID(), card.isFront(), card, layoutPlacedStarterX, layoutPlacedStarterY,
                    fitHeightPlaced, fitWidthPlaced, null);
            GUIMessages.writeToClient(selectedStarterFront);
        }
    }

    /**
     * If there is a selected card, it sends the information about the selectedCard to the server
     *
     * @param event the mouse event
     */
    private void confirmPlaceObjective(MouseEvent event) {
        if (selectedCard != null) {
            makeSmallerTransition(selectedCard);
            ImageView image = (ImageView) event.getSource();
            fadeOutTransition(mainPane, image, 0.5, true);
            removeCardFromPosition(layoutXChoiceObjective1, layoutYHand, mainPane);
            removeCardFromPosition(layoutXChoiceObjective2, layoutYHand, mainPane);
            ObjectiveCard card = (ObjectiveCard) selectedCard.getUserData();
            addNewCardToPane(mainPane, mainPane, clickCounter, selectedCard, card.getID(), true, card, layoutXObjective, layoutYObjMy, fitHeightCommon,
                    fitWidthCommon, this::turnObjectives);
            onTop.toFront();
            GUIMessages.writeToClient(selectedObjective);
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
                    makeSmallerTransition(selectedCard);
                }

                makeBiggerTransition(clickedCard, 1.05);
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
                onTop.toFront();

                if (Objects.equals(currentState, "PlaceTurnState") && Objects.equals(currentPlayerNickname, myself.getNickname())) {
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
            double placeholderX = clickedPlaceholder.getLayoutX();
            double placeholderY = clickedPlaceholder.getLayoutY();

            double relativePosX;
            double relativePosY;


            if (centerXOffset < 0 || centerYOffset < 0) {
                relativePosX = (placeholderX - layoutPlacedStarterX) / offsetAreaX;
                relativePosY = (placeholderY - layoutPlacedStarterY) / offsetAreaY;
            } else {
                relativePosX = (placeholderX - centerXOffset) / offsetAreaX;
                relativePosY = (placeholderY - centerYOffset) / offsetAreaY;
            }

            this.selectedToPlace[2] = (int) relativePosY;
            this.selectedToPlace[3] = (int) relativePosX;

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

            makeBiggerTransition(clickedCard, 1.05);
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

            GUIMessages.writeToClient(selectedPick);
            this.selectedCard = null;
        }
    }

    /**
     * When clicked, it displays the NextButton player's page or the client's page
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
        } else if ((Objects.equals(currentState, "StarterCardState") || Objects.equals(currentState, "ObjectiveState")) && Objects.equals(currentPlayerNickname, myself.getNickname())) {
            choosePopUp(myself.getColor());
        } else if (Objects.equals(currentState, "StarterCardState") || Objects.equals(currentState, "ObjectiveState")) {
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

        if (pion != null && score == 0 && !isPionAtDesiredPosition(pion, 0)) {
            double[] adjustedPosition = getAdjustedPosition(allPions, positions[0][0], positions[0][1], pion);
            pion.setLayoutX(adjustedPosition[0]);
            pion.setLayoutY(adjustedPosition[1]);
            pion.setFitHeight(49);
            pion.setFitWidth(49);
            pion.setPreserveRatio(true);
            mainPane.getChildren().add(pion);
            updatePionsPositions(allPions, pion);
        } else if (pion != null && !isPionAtDesiredPosition(pion, score)) {
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
                //TODO Invalid score ?? count from 1??
                return;
            }

            double startX = pion.getLayoutX();
            double startY = pion.getLayoutY();

            Timeline timeline = new Timeline();

            int start = getScoreByPosition(startX, startY);

            for (int i = start; i <= score; i++) {
                double[] adjustedPosition = getAdjustedPosition(allPions, positions[i][0], positions[i][1], pion);

                KeyFrame keyFrame = new KeyFrame(Duration.millis((i - start) * 200), e -> {
                    pion.setLayoutX(adjustedPosition[0]);
                    pion.setLayoutY(adjustedPosition[1]);
                    pion.toFront();
                });

                timeline.getKeyFrames().add(keyFrame);
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
        double offsetY = 0;

        for (ImageView pion : pions) {
            if (pion != myPion)
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
        return (pion.getLayoutX() == desiredPosition[0] || pion.getLayoutY() == desiredPosition[1])
                && (pion.getLayoutX() == desiredPosition[0] || pion.getLayoutY() == desiredPosition[1] - offsetPions)
                && (pion.getLayoutX() == desiredPosition[0] || pion.getLayoutY() == desiredPosition[1] - 2 * offsetPions)
                && (pion.getLayoutX() == desiredPosition[0] || pion.getLayoutY() == desiredPosition[1] - 3 * offsetPions);
    }

    /**
     * Update the pions positions if a pion moved
     *
     * @param allPions  the list of all pions
     * @param movedPion the pion that moved
     */
    private void updatePionsPositions(List<ImageView> allPions, ImageView movedPion) {
        Platform.runLater(() -> {
            for (ImageView pion : allPions) {
                if (pion != movedPion && pion != null) {
                    int score = getScoreByPosition(pion.getLayoutX(), pion.getLayoutY());
                    double[] adjustedPosition = getAdjustedPosition(allPions, positions[score][0], positions[score][1], pion);
                    pion.setLayoutX(adjustedPosition[0]);
                    pion.setLayoutY(adjustedPosition[1]);
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
        System.out.println("Displaying placeholders");
        placeholdersVisible = true;
        ArrayList<Integer[]> availablePositions;
        availablePositions = myPlayerArea.getAvailablePosition();

        if (boundingBox == null)
            boundingBox = calculateBoundingBox(allCards);

        double totalWidth = boundingBox[2] - boundingBox[0];
        double totalHeight = boundingBox[3] - boundingBox[1];

        scaleFactor = calculateScaleFactor(totalWidth, totalHeight, availablePositions);



        centerXOffset = (playground.getWidth() - (totalWidth * scaleFactor)) / 2 - boundingBox[0] * scaleFactor;
        centerYOffset = (playground.getHeight() - (totalHeight * scaleFactor)) / 2 - boundingBox[1] * scaleFactor;



        for (Integer[] pos : availablePositions) {
            // Calculate the new layout position of the placeholder
            int newLayoutX = (int) (centerXOffset + pos[1] * offsetAreaX * scaleFactor);
            int newLayoutY = (int) (centerYOffset + pos[0] * offsetAreaY * scaleFactor);
            System.out.println("New layout x: " + newLayoutX + " New layout y: " + newLayoutY);
            addClickablePlaceholder(playground, newLayoutX, newLayoutY, fitHeightPlaced * scaleFactor, fitWidthPlaced * scaleFactor, this::confirmPlaceCard);
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
            fadeOutTransition(playground, placeholder, 0.5, true);
        }
    }

    /**
     * Displays the cards in the playerArea
     *
     * @param playerArea the playerArea to display
     */
    private void displayPlayerArea(PlayerArea playerArea) {
        Platform.runLater(() -> {
            // Remove all existing cards from the player area
            playground.getChildren().forEach(node -> {
                if (node instanceof ImageView && node.getUserData() instanceof PlaceableCard) {
                    fadeOutTransition(playground, node, 1.0, true); // fade out but keep the node
                }
            });

            FadeTransition delayTransition = new FadeTransition(Duration.seconds(1));
            delayTransition.setOnFinished(event -> {
                allCards = playerArea.getAllCards();

                boundingBox = calculateBoundingBox(allCards);
                double totalWidth = boundingBox[2] - boundingBox[0];
                double totalHeight = boundingBox[3] - boundingBox[1];

                System.out.println("Total width: " + totalWidth + " Total height: " + totalHeight);
                System.out.println("Widht: " + playground.getWidth() + " Height: " + playground.getHeight());

                scaleFactor = calculateScaleFactor(totalWidth, totalHeight, playerArea.getAvailablePosition());

                System.out.println("Scale factor: " + scaleFactor);

                centerXOffset = (playground.getWidth() - (totalWidth * scaleFactor)) / 2 - boundingBox[0] * scaleFactor;
                centerYOffset = (playground.getHeight() - (totalHeight * scaleFactor)) / 2 - boundingBox[1] * scaleFactor;

                for (PlaceableCard card : allCards) {
                    int layoutX = (int) (calculateLayoutX(card) * scaleFactor + centerXOffset);
                    int layoutY = (int) (calculateLayoutY(card) * scaleFactor + centerYOffset);

                    ImageView cardImageView = createCardImageView(card.getID(), card.isFront(), card, layoutX, layoutY, fitHeightPlaced * scaleFactor, fitWidthPlaced * scaleFactor);
                    cardImageView.setOpacity(0.0);
                    playground.getChildren().add(cardImageView);
                    fadeInTransition(cardImageView, 1.0);

                    // Adjust z-order
                    adjustCardZOrder(cardImageView, card);
                }

                onTop.toFront();
            });
            delayTransition.play();
        });
    }

    /**
     * Calculates the scale factor based on the total dimensions of the cards and the available positions
     *
     * @param totalWidth         the total width of the cards
     * @param totalHeight        the total height of the cards
     * @param availablePositions the available positions in the playerArea
     * @return the scale factor
     */
    private double calculateScaleFactor(double totalWidth, double totalHeight, ArrayList<Integer[]> availablePositions) {
        double playgroundWidth = playground.getWidth();
        double playgroundHeight = playground.getHeight();

        // Calculate the total dimensions including placeholders
        double totalWithPlaceholdersWidth = totalWidth;
        double totalWithPlaceholdersHeight = totalHeight;

        for (Integer[] pos : availablePositions) {
            double placeholderX = pos[1] * offsetAreaX;
            double placeholderY = pos[0] * offsetAreaY;
            totalWithPlaceholdersWidth = Math.max(totalWithPlaceholdersWidth, placeholderX + fitWidthPlaced);
            totalWithPlaceholdersHeight = Math.max(totalWithPlaceholdersHeight, placeholderY + fitHeightPlaced);
        }

        // Calculate the scale factors
        double widthScaleFactor = playgroundWidth / totalWithPlaceholdersWidth;
        double heightScaleFactor = playgroundHeight / totalWithPlaceholdersHeight;
        double scaleFactor = 1.0;

        if (totalWithPlaceholdersWidth > playgroundWidth || totalWithPlaceholdersHeight > playgroundHeight) {
            scaleFactor = Math.min(widthScaleFactor, heightScaleFactor);
        }

        if(scaleFactor < 0)
            return 1;

        return Math.min(scaleFactor, 1.0);
    }

    /**
     * Calculates the layout position of the card based on the cell position
     *
     * @param card the card to calculate the layout position for
     * @return the layout x position
     */
    private double calculateLayoutX(PlaceableCard card) {
        return card.getCells().getFirst().getColumn() * offsetAreaX;
    }

    /**
     * Calculates the layout position of the card based on the cell position
     *
     * @param card the card to calculate the layout position for
     * @return the layout y position
     */
    private double calculateLayoutY(PlaceableCard card) {
        return card.getCells().getFirst().getRow() * offsetAreaY;
    }

    /**
     * Calculates the bounding box of the cards
     *
     * @param cards the cards to calculate the bounding box for
     * @return the bounding box
     */
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

    /**
     * Adjusts the z-order of the card based on its position relative to other cards
     *
     * @param cardImageView the card image view
     * @param card          the card
     */
    private void adjustCardZOrder(ImageView cardImageView, PlaceableCard card) {
        // Ensure proper layering based on card's position relative to other cards
        boolean isTop = card.getCells().stream().anyMatch(cell -> cell.getTopCard() == card);
        boolean isBottom = card.getCells().stream().anyMatch(cell -> cell.getBottomCard() == card);

//        if (isTop) {
//            cardImageView.toFront();
//        } else if (isBottom) {
//            cardImageView.toBack();
//        } else {
//            playground.getChildren().remove(cardImageView);
//            playground.getChildren().add(cardImageView);
//        }

        if (isTop) {
            cardImageView.setViewOrder(-1); // Negative value brings it to the front
        } else if (isBottom) {
            cardImageView.setViewOrder(1); // Positive value sends it to the back
        } else {
            cardImageView.setViewOrder(0); // Default value for middle z-order
        }

    }

}
