package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MyselfPageController implements Initializable {
    private Player myself;
    private Player current;
    private ArrayList<ObjectiveCard> commonObjective;
    private PlayerArea playerArea;
    private CommonArea commonArea;
    private Object message;

    private int selectedCardID;
    private ImageView selectedCard;
    private int selectedDrawCardID;
    private ImageView selectedDrawCard;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ImageView myPion;
    @FXML
    private ImageView background;
    @FXML
    private ImageView card0;
    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView myObj;
    @FXML
    private ImageView gold0;
    @FXML
    private ImageView gold1;
    @FXML
    private ImageView goldBack;
    @FXML
    private ImageView obj0;
    @FXML
    private ImageView obj1;
    @FXML
    private Label playerName;
    @FXML
    private ImageView res0;
    @FXML
    private ImageView res1;
    @FXML
    private ImageView resourceBack;
    @FXML
    private ImageView scoreBoard;
    @FXML
    public ImageView firstPion;
    @FXML
    public ImageView secondPion;
    @FXML
    public ImageView thirdPion;
    @FXML
    public ImageView fourthPion;

    @FXML
    public void switchToCurrentGamePage(MouseEvent event) {
        if(!Objects.equals(myself.getNickname(), current.getNickname())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("currentGamePage.fxml"));
                Parent root = loader.load();

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void set(currentStateMessage message) {
        this.myself = message.getPlayer();
        this.current  = message.getCurrentPlayer();
        this.commonObjective = message.getCommonObjectiveCards();
        this.playerArea = message.getPlayer().getPlayerArea();
        this.commonArea = message.getPlayer().getCommonArea();
        playerName.setText(myself.getNickname());
        setMyself();
        setPions();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.message = GUIMessages.readToGUI();
        if( this.message instanceof currentStateMessage){
            set(Objects.requireNonNull((currentStateMessage) message));
            CurrentPageController.setAll(Objects.requireNonNull((currentStateMessage)message));
        } else if (this.message instanceof updatePlayerMessage){
           CurrentPageController.update(Objects.requireNonNull((updatePlayerMessage) message));
        }
    }

    private void setMyself() {
        // COMMON AREA
        setupCard(resourceBack, commonArea.getD1().getList().getFirst().getID(), false, commonArea.getD1().getList().getFirst());
        resourceBack.setVisible(true);

        setupCard(goldBack, commonArea.getD2().getList().getFirst().getID(), false, commonArea.getD2().getList().getFirst());
        goldBack.setVisible(true);

        // FRONT CARDS
        setupCard(res0, commonArea.getTableCards().getFirst().getID(), true, commonArea.getTableCards().getFirst());
        res0.setVisible(true);

        setupCard(res1, commonArea.getTableCards().get(1).getID(), true, commonArea.getTableCards().get(1));
        res1.setVisible(true);

        setupCard(gold0, commonArea.getTableCards().get(2).getID(), true, commonArea.getTableCards().get(2));
        gold0.setVisible(true);

        setupCard(gold1, commonArea.getTableCards().get(3).getID(), true, commonArea.getTableCards().get(3));
        gold1.setVisible(true);

        // COMMON OBJECTIVE
        setupCard(obj0, commonObjective.getFirst().getID(), true, commonObjective.getFirst());
        obj0.setVisible(true);

        setupCard(obj1, commonObjective.get(1).getID(), true, commonObjective.get(1));
        obj1.setVisible(true);

        // Objective
        setupCard(myObj, myself.getObjective().getID(), true, myself.getObjective());

        // Cards
        setupCard(card0, current.getPlayerHand().getPlaceableCards().getFirst().getID(), true, current.getPlayerHand().getPlaceableCards().getFirst());

        setupCard(card1, current.getPlayerHand().getPlaceableCards().get(1).getID(), true, current.getPlayerHand().getPlaceableCards().get(1));

        setupCard(card2, current.getPlayerHand().getPlaceableCards().get(2).getID(), true, current.getPlayerHand().getPlaceableCards().get(2));
    }


    private void setupCard(ImageView imageView, int cardID, boolean front, Card card) {
        ImageBinder imageBinder = new ImageBinder();
        ImageView cardImageView = imageBinder.bindImage(cardID, front);
        imageView.setImage(cardImageView.getImage());
        imageView.setUserData(card); // Store card ID and state
    }

    @FXML
    public void turnAround(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();
        Card card = (Card) clickedCard.getUserData();

        if (card != null) {
            ImageBinder imageBinder = new ImageBinder();
            Image newImage = imageBinder.getOppositeImage(card.getID(), card.isFront());
            clickedCard.setImage(newImage);

            // Update the card state
            card.setFront(!card.isFront());
        }
    }

    @FXML
    public void select(MouseEvent event) {
        if(message instanceof currentStateMessage) {
            if(((currentStateMessage) message).getStateName().equals("PlaceTurnState") && !Objects.equals(myself.getNickname(), current.getNickname())) {
                if (event.getClickCount() == 1) {
                    ImageView clickedCard = (ImageView) event.getSource();
                    Card card = (Card) clickedCard.getUserData();

                    if (card != null) {
                        ImageBinder imageBinder = new ImageBinder();
                        Image newImage = imageBinder.getOppositeImage(card.getID(), card.isFront());
                        clickedCard.setImage(newImage);

                        // Update the card state
                        card.setFront(!card.isFront());
                    }
                } else if (event.getClickCount() == 2) {
                    ImageView clickedCard = (ImageView) event.getSource();
                    Integer cardID = (Integer) clickedCard.getUserData();

                    if (cardID != null) {
                        // Remove border from previously selected card
                        if (selectedCard != null)
                            selectedCard.setStyle("");

                        // Set blue border for the selected card
                        clickedCard.setStyle("-fx-border-color: #0000ff; -fx-border-width: 2;");
                        selectedCard = clickedCard;

                        // Set the selected card ID
                        selectedCardID = cardID;
                    }
                }
            }
        }
    }

    //TODO: Implement the method
    //NON HA MOLTO SENSO, MA PER ORA LO LASCIAMO COSI
    @FXML
    public void place(MouseEvent event) {
        if(message instanceof currentStateMessage) {
            if(((currentStateMessage) message).getStateName().equals("PlaceTurnState") && !Objects.equals(myself.getNickname(), current.getNickname())) {
                if (selectedCard != null) {
                    selectedCard.setStyle(""); // Remove the blue border from the selected card
                    GUIMessages.writeToClient(selectedCardID); // Send the selected card ID to the server
                }
            }
        }
    }

    @FXML
    public void selectDraw(MouseEvent event){
        if(message instanceof currentStateMessage) {
            if (((currentStateMessage) message).getStateName().equals("PickTurnState") && !Objects.equals(myself.getNickname(), current.getNickname())) {
                ImageView clickedCard = (ImageView) event.getSource();
                int cardID = (Integer) clickedCard.getUserData();
                if (selectedDrawCard == clickedCard) {
                    GUIMessages.writeToClient(selectedDrawCardID);
                } else if (selectedDrawCard != null) {
                    selectedDrawCard.setStyle("");
                    clickedCard.setStyle("-fx-border-color: #0000ff; -fx-border-width: 2;");
                    selectedDrawCard = clickedCard;
                    selectedDrawCardID = cardID;
                } else {
                    clickedCard.setStyle("-fx-border-color: #0000ff; -fx-border-width: 2;");
                    selectedDrawCard = clickedCard;
                    selectedDrawCardID = cardID;
                }
            }
        }
    }

    public void setPions(){
        String imagePath;
        String color = myself.getColor();
        //PIONS SETTING (BUT IT SHOULD BE BASED ON POINTS)

        switch (color) {
             case "red" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_rouge.png";
                myPion = new ImageView(String.valueOf(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
            }
            case "blue" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_bleu.png";
                myPion = new ImageView(String.valueOf(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
            }
            case "green" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_vert.png";
                myPion = new ImageView(String.valueOf(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
            }
            case "yellow" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_jaune.png";
                myPion = new ImageView(String.valueOf(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
            }
            default -> {
            }
        }

        //Non ha molto senso, ma per ora lo lascio così
        if(firstPion.isVisible()){
            if(secondPion.isVisible()) {
                if (thirdPion.isVisible()) {
                    fourthPion.setImage(myPion.getImage());
                    fourthPion.setVisible(true);
                } else {
                    thirdPion.setImage(myPion.getImage());
                    thirdPion.setVisible(true);
                }
            }
            secondPion.setImage(myPion.getImage());
            secondPion.setVisible(true);
        } else {
            firstPion.setImage(myPion.getImage());
            firstPion.setVisible(true);
        }

    }

    public void addPoints(){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(myPion);
        //Dovrei prendere punto di partenza e fare movimenti in base a quello per non tagliare la scoreboard
        if(myself.getScore() == 1) {
            transition.setToX(1731);
            transition.setToY(485);
            transition.setToY(489);
            transition.setToY(494);
            transition.setToY(499);
        } else if(myself.getScore() == 2) {
            transition.setToX(1794);
            transition.setToY(485);
            transition.setToY(489);
            transition.setToY(494);
            transition.setToY(499);
        } else if(myself.getScore() == 3) {
            transition.setByX(1826);
            transition.setToY(426); //fourth
            transition.setToY(430); //third
            transition.setToY(435); //sec
            transition.setToY(440); //first
        }else if(myself.getScore() == 4) {
            transition.setByX(1762);
            transition.setToY(426); //fourth
            transition.setToY(430); //third
            transition.setToY(435); //sec
            transition.setToY(440); //first
        } else if (myself.getScore() == 5) {
            transition.setByX(1689);
            transition.setToY(426); //fourth
            transition.setToY(430); //third
            transition.setToY(435); //sec
            transition.setToY(440); //first
        } else if (myself.getScore() == 6){
            transition.setByX(1635);
            transition.setToY(426); //fourth
            transition.setToY(430); //third
            transition.setToY(435); //sec
            transition.setToY(440); //first
        } else if (myself.getScore() == 7){
            transition.setByX(1635);
            transition.setToY(367); //fourth
            transition.setToY(371); //third
            transition.setToY(376); //sec
            transition.setToY(381);
        } else if (myself.getScore() == 8){
            transition.setByX(1689);
            transition.setToY(367); //fourth
            transition.setToY(371); //third
            transition.setToY(376); //sec
            transition.setToY(381);
        } else if (myself.getScore() == 9){
            transition.setByX(1762);
            transition.setToY(367); //fourth
            transition.setToY(371); //third
            transition.setToY(376); //sec
            transition.setToY(381);
        } else if (myself.getScore() == 10){
            transition.setByX(1826);
            transition.setToY(367); //fourth
            transition.setToY(371); //third
            transition.setToY(376); //sec
            transition.setToY(381);
        } else if(myself.getScore() == 11){
            transition.setToX(1826);
            transition.setToY(309); //fourth
            transition.setToY(313); //third
            transition.setToY(318); //sec
            transition.setToY(323);
         } else if(myself.getScore() == 12){
            transition.setToX(1762);
            transition.setToY(309); //fourth
            transition.setToY(313); //third
            transition.setToY(318); //sec
            transition.setToY(323);
         } else if(myself.getScore() == 13){
            transition.setToX(1689);
            transition.setToY(309); //fourth
            transition.setToY(313); //third
            transition.setToY(318); //sec
            transition.setToY(323);
         } else if(myself.getScore() == 14){
            transition.setToX(1635);
            transition.setToY(309); //fourth
            transition.setToY(313); //third
            transition.setToY(318); //sec
            transition.setToY(323);
         } else if(myself.getScore() == 15){
            transition.setToX(1635);
            transition.setToY(251); //fourth
            transition.setToY(255); //third
            transition.setToY(260); //sec
            transition.setToY(265);
         } else if(myself.getScore() == 16){
            transition.setToX(1689);
            transition.setToY(251); //fourth
            transition.setToY(255); //third
            transition.setToY(260); //sec
            transition.setToY(265);
         } else if(myself.getScore() == 17){
            transition.setToX(1762);
            transition.setToY(251); //fourth
            transition.setToY(255); //third
            transition.setToY(260); //sec
            transition.setToY(265);
         } else if(myself.getScore() == 18){
            transition.setToX(1826);
            transition.setToY(251); //fourth
            transition.setToY(255); //third
            transition.setToY(260); //sec
            transition.setToY(265);
         } else if(myself.getScore() == 19){
            transition.setToX(1826);
            transition.setToY(193); //fourth
            transition.setToY(197); //third
            transition.setToY(202); //sec
            transition.setToY(207);
         } else if(myself.getScore() == 20){
            transition.setToX(1730);
            transition.setToY(163); //fourth
            transition.setToY(167); //third
            transition.setToY(172); //sec
            transition.setToY(177);
         } else if(myself.getScore() == 21){
            transition.setToX(1635);
            transition.setToY(193); //fourth
            transition.setToY(197); //third
            transition.setToY(202); //sec
            transition.setToY(207);
         } else if(myself.getScore() == 22){
            transition.setToY(133); //fourth
            transition.setToY(137); //third
            transition.setToY(142); //sec
            transition.setToY(147);
         } else if(myself.getScore() == 23){
            transition.setToY(75); //fourth
            transition.setToY(79); //third
            transition.setToY(84); //sec
            transition.setToY(89);
         } else if(myself.getScore() == 24){
            transition.setToX(1671);
            transition.setToY(251); //fourth
            transition.setToY(255); //third
            transition.setToY(260); //sec
            transition.setToY(265);
         } else if(myself.getScore() == 25){
            transition.setToX(1731);
            transition.setToY(17); //fourth
            transition.setToY(21); //third
            transition.setToY(26); //sec
            transition.setToY(31);
         } else if(myself.getScore() == 26){
            transition.setToX(1789);
            transition.setToY(251); //fourth
            transition.setToY(255); //third
            transition.setToY(260); //sec
            transition.setToY(265);
         } else if(myself.getScore() == 27){
            transition.setToX(1826);
            transition.setToY(75); //fourth
            transition.setToY(79); //third
            transition.setToY(84); //sec
            transition.setToY(89);
         } else if(myself.getScore() == 28){
            transition.setToY(133); //fourth
            transition.setToY(137); //third
            transition.setToY(142); //sec
            transition.setToY(147);
         } else if(myself.getScore() == 29){
            transition.setToX(1731);
            transition.setToY(87); //fourth
            transition.setToY(91); //third
            transition.setToY(96); //sec
            transition.setToY(101);
         }

        transition.setDuration(javafx.util.Duration.seconds(1));
        transition.play();
    }

}
