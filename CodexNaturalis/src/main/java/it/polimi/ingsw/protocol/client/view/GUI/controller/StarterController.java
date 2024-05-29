package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class StarterController implements Initializable {
    private Player myself;
    private ArrayList<ObjectiveCard> commonObjective;
    private PlayerArea playerArea;
    private CommonArea commonArea;

    private currentStateMessage message;

    private int selectedCardID;
    private ImageView selectedCard;

    @FXML
    private ImageView card1;
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


    public void set(currentStateMessage message) {
        this.myself = message.getPlayer();
        this.commonObjective = message.getCommonObjectiveCards();
        this.playerArea = message.getPlayer().getPlayerArea();
        this.commonArea = message.getPlayer().getCommonArea();
        playerName.setText(myself.getNickname());
        firstSetUp();
        firstSetPions();
    }

    public void firstSetPions(){
        String imagePath;
        Image cardImage;

        //PIONS SETTING (BUT IT SHOULD BE BASED ON POINTS)
        String color = myself.getColor();
        cardImage = switch (color) {
            case "red" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_rouge.png";
                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            }
            case "blue" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_bleu.png";
                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            }
            case "green" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_vert.png";
                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            }
            case "yellow" -> {
                imagePath = "CodexNaturalis/src/main/Resource/img/Pions/CODEX_pion_jaune.png";
                yield new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
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

    public void firstSetUp() {

        setupCard(resourceBack, commonArea.getD1().getList().getFirst().getID(), false, commonArea.getD1().getList().getFirst());
        setupCard(goldBack, commonArea.getD2().getList().getFirst().getID(), false, commonArea.getD2().getList().getFirst());

        setupCard(res0, commonArea.getTableCards().getFirst().getID(), true, commonArea.getTableCards().getFirst());
        setupCard(res1, commonArea.getTableCards().get(1).getID(), true, commonArea.getTableCards().get(1));
        setupCard(gold0, commonArea.getTableCards().get(2).getID(), true, commonArea.getTableCards().get(2));
        setupCard(gold1, commonArea.getTableCards().get(3).getID(), true, commonArea.getTableCards().get(3));

        setupCard(obj0, commonObjective.getFirst().getID(), true, commonObjective.getFirst());
        setupCard(obj1, commonObjective.get(1).getID(), true, commonObjective.get(1));

        setupCard(card1, myself.getPlayerHand().getPlaceableCards().getFirst().getID(), true, myself.getPlayerHand().getPlaceableCards().getFirst());
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

    @FXML
    public void place(MouseEvent event) {
        if (selectedCard != null) {
            selectedCard.setStyle(""); // Remove the blue border from the selected card
            GUIMessages.writeToClient(selectedCardID); // Send the selected card ID to the server
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.message = (currentStateMessage) GUIMessages.readToGUI();
        set(Objects.requireNonNull(message));
    }

    //TODO: IF UPDATE THE STARTER
}
