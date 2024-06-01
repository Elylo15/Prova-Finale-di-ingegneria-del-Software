package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class NextPageController {
    private static Player current;
    private static ArrayList<ObjectiveCard> commonObjective;
    private static PlayerArea playerArea;
    private static CommonArea commonArea;
    private static PlayerHand playerHand;
    private static ObjectiveCard myObjective;

    @FXML
    private static ImageView layout;
    @FXML
    private static ImageView card0;
    @FXML
    private static ImageView card1;
    @FXML
    private static ImageView card2;
    @FXML
    private static ImageView myObj;
    @FXML
    private static ImageView gold0;
    @FXML
    private static ImageView gold1;
    @FXML
    private static ImageView goldBack;
    @FXML
    private static ImageView obj0;
    @FXML
    private static ImageView obj1;
    @FXML
    private static Label playerName;
    @FXML
    private static ImageView res0;
    @FXML
    private static ImageView res1;
    @FXML
    private static ImageView resourceBack;
    @FXML
    private static ImageView firstPion;
    @FXML
    private static ImageView secondPion;
    @FXML
    private static ImageView thirdPion;
    @FXML
    private static ImageView fourthPion;
    @FXML
    private ImageView scoreBoard;

    @FXML
    private void switchToNextGamePage(MouseEvent event) {
        Scene scene;
        Stage stage;

        if(MyselfPageController.clickedCounter < MyselfPageController.players.length){
            MyselfPageController.clickedCounter++;
            setAll();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("nextGamePage.fxml"));
                Parent root = loader.load();

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } else {
            MyselfPageController.clickedCounter = 0;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("myselfGamePage.fxml"));
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

    protected static void setAll() {

        NextPageController.current = MyselfPageController.players[MyselfPageController.clickedCounter];
        NextPageController.commonObjective = MyselfPageController.commonObjective;
        NextPageController.playerArea = current.getPlayerArea();
        NextPageController.playerHand = current.getPlayerHand();
        NextPageController.myObjective = current.getObjective();
        NextPageController.commonArea = MyselfPageController.commonArea;
        playerName.setText(current.getNickname());
        setCurrent();
        setPions();
    }

    private static void setPions(){
        String imagePath;
        Image cardImage;

        //PIONS SETTING (BUT IT SHOULD BE BASED ON POINTS)
        String color = current.getColor();
        cardImage = switch (color) {
            case "red" -> {
                imagePath = "/img/Pions/CODEX_pion_rouge.png";
                yield new Image(Objects.requireNonNull(NextPageController.class.getResourceAsStream(imagePath)));
            }
            case "blue" -> {
                imagePath = "/img/Pions/CODEX_pion_bleu.png";
                yield new Image(Objects.requireNonNull(NextPageController.class.getResourceAsStream(imagePath)));
            }
            case "green" -> {
                imagePath = "/img/Pions/CODEX_pion_vert.png";
                yield new Image(Objects.requireNonNull(NextPageController.class.getResourceAsStream(imagePath)));
            }
            case "yellow" -> {
                imagePath = "/img/Pions/CODEX_pion_jaune.png";
                yield new Image(Objects.requireNonNull(NextPageController.class.getResourceAsStream(imagePath)));
            }
            default -> null;
        };

        if(firstPion.isVisible()){
            if(secondPion.isVisible()) {
                if (thirdPion.isVisible()) {
                    fourthPion.setImage(cardImage);
                    fourthPion.setVisible(true);
                } else {
                    thirdPion.setImage(cardImage);
                    thirdPion.setVisible(true);
                }
            }
            secondPion.setImage(cardImage);
            secondPion.setVisible(true);
        } else {
            firstPion.setImage(cardImage);
            firstPion.setVisible(true);
        }
    }

    private static void setCurrent() {
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
        setupCard(myObj, myObjective.getID(), true, myObjective);

        // Cards
        setupCard(card0, playerHand.getPlaceableCards().getFirst().getID(), true, playerHand.getPlaceableCards().getFirst());

        setupCard(card1, playerHand.getPlaceableCards().get(1).getID(), true,  playerHand.getPlaceableCards().get(1));

        setupCard(card2, playerHand.getPlaceableCards().get(2).getID(), true, current.getPlayerHand().getPlaceableCards().get(2));
    }

    private static void setupCard(ImageView imageView, int cardID, boolean front, Card card) {
        ImageBinder imageBinder = new ImageBinder();
        ImageView cardImageView = imageBinder.bindImage(cardID, front);
        imageView.setImage(cardImageView.getImage());
        imageView.setUserData(card); // Store card ID and state
    }

    @FXML
    private void turnAround(MouseEvent event) {

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

}