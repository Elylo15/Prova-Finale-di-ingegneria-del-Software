package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.ObjectiveCard;
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

    public static ImageView myPion;
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

    public void firstSetUp(){
        int ID;
        String imagePath;
        Image cardImage;

        ID = commonArea.getD1().getList().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        resourceBack.setImage(cardImage);

        ID = commonArea.getD2().getList().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        goldBack.setImage(cardImage);

        //FRONT CARDS
        ID = commonArea.getTableCards().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        res0.setImage(cardImage);

        ID = commonArea.getTableCards().get(1).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        res1.setImage(cardImage);

        ID = commonArea.getTableCards().get(2).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        gold0.setImage(cardImage);

        ID = commonArea.getTableCards().get(3).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        gold1.setImage(cardImage);

        //COMMON OBJECTIVE
        ID = commonObjective.getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        obj0.setImage(cardImage);

        ID = commonObjective.get(1).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        obj1.setImage(cardImage);

        //starterCard
        ID = myself.getPlayerHand().getPlaceableCards().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card1.setImage(cardImage);
    }


    @FXML
    public void turnAround(MouseEvent event) {

    }

    @FXML
    public void select(MouseEvent event) {

    }

    //on double click la gira, e se schiaccia su playerArea la piazza

    public int getChoice(){
      return 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerName.setText(myself.getNickname());
    }
}
