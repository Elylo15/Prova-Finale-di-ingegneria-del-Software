package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
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

public class CurrentPageController {
    private static Player myself;
    private static Player current;
    private static ArrayList<ObjectiveCard> commonObjective;
    private static PlayerArea playerArea;
    private static CommonArea commonArea;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView background;
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
    private ImageView scoreBoard;
    @FXML
    public static ImageView firstPion;
    @FXML
    public static ImageView secondPion;
    @FXML
    public static ImageView thirdPion;
    @FXML
    public static ImageView fourthPion;

    @FXML
    public void switchToMyselfGamePage(MouseEvent event) {
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

    public static void setAll(currentStateMessage message) {
        myself = message.getPlayer();
        current = message.getCurrentPlayer();
        commonObjective = message.getCommonObjectiveCards();
        playerArea = current.getPlayerArea();
        commonArea = current.getCommonArea();
        playerName.setText(current.getNickname());
        setCurrent();
        setPions();
    }

    public static void update(updatePlayerMessage message) {
        current = message.getPlayer();
        playerArea = current.getPlayerArea();
        commonArea = current.getCommonArea();
        playerName.setText(current.getNickname());
        setCurrent();
        setPions();
    }

    public static void setPions(){
        String imagePath;
        Image cardImage = null;

        //PIONS SETTING (BUT IT SHOULD BE BASED ON POINTS)
        String color = myself.getColor();
        cardImage = switch (color) {
            case "red" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_rouge.png";
                yield new Image(Objects.requireNonNull(CurrentPageController.class.getResourceAsStream(imagePath)));
            }
            case "blue" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_bleu.png";
                yield new Image(Objects.requireNonNull(CurrentPageController.class.getResourceAsStream(imagePath)));
            }
            case "green" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_vert.png";
                yield new Image(Objects.requireNonNull(CurrentPageController.class.getResourceAsStream(imagePath)));
            }
            case "yellow" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_jaune.png";
                yield new Image(Objects.requireNonNull(CurrentPageController.class.getResourceAsStream(imagePath)));
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
        setupCard(myObj, myself.getObjective().getID(), true, myself.getObjective());

        // Cards
        setupCard(card0, current.getPlayerHand().getPlaceableCards().getFirst().getID(), true, current.getPlayerHand().getPlaceableCards().getFirst());

        setupCard(card1, current.getPlayerHand().getPlaceableCards().get(1).getID(), true, current.getPlayerHand().getPlaceableCards().get(1));

        setupCard(card2, current.getPlayerHand().getPlaceableCards().get(2).getID(), true, current.getPlayerHand().getPlaceableCards().get(2));
    }

    private static void setupCard(ImageView imageView, int cardID, boolean front, Card card) {
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

}