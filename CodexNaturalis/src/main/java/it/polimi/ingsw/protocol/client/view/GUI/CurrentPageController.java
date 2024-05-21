package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
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

public class CurrentPageController implements Initializable {
    private Player myself;
    private Player current;
    private ArrayList<ObjectiveCard> commonObjective;
    private PlayerArea playerArea;
    private CommonArea commonArea;

    private String stateName;

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    public void set(currentStateMessage message) {
        this.myself = message.getPlayer();
        this.current = message.getCurrentPlayer();
        this.commonObjective = message.getCommonObjectiveCards();
        this.playerArea = current.getPlayerArea();
        this.commonArea = current.getCommonArea();
        setCurrent();
        setPions();
    }

    public void update(updatePlayerMessage message) {
        this.current = message.getPlayer();
        this.playerArea = current.getPlayerArea();
        this.commonArea = current.getCommonArea();
        setCurrent();
    }

    public void setPions(){
        String imagePath;
        Image cardImage = null;

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
            default -> cardImage;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerName.setText(current.getNickname());
    }

    private void setCurrent(){
        int ID;
        String imagePath;
        Image cardImage;

        ID = commonArea.getD1().getList().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        resourceBack.setImage(cardImage);
        resourceBack.setVisible(true);

        ID = commonArea.getD2().getList().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        goldBack.setImage(cardImage);
        goldBack.setVisible(true);

        //FRONT CARDS
        ID = commonArea.getTableCards().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        res0.setImage(cardImage);
        res0.setVisible(true);

        ID = commonArea.getTableCards().get(1).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        res1.setImage(cardImage);
        res1.setVisible(true);

        ID = commonArea.getTableCards().get(2).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        gold0.setImage(cardImage);
        gold0.setVisible(true);

        ID = commonArea.getTableCards().get(3).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        gold1.setImage(cardImage);
        gold1.setVisible(true);

        //COMMON OBJECTIVE
        ID = commonObjective.getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        obj0.setImage(cardImage);
        obj0.setVisible(true);

        ID = commonObjective.get(1).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        obj1.setImage(cardImage);
        obj1.setVisible(true);

        //Objective
        ID = myself.getObjective().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        myObj.setImage(cardImage);

        //Cards
        ID = current.getPlayerHand().getPlaceableCards().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card0.setImage(cardImage);

        ID = current.getPlayerHand().getPlaceableCards().get(1).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card1.setImage(cardImage);

        ID = current.getPlayerHand().getPlaceableCards().get(2).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Back/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card2.setImage(cardImage);
    }

    @FXML
    public void turnAround(){

    }

}