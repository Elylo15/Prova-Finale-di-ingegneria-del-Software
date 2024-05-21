package it.polimi.ingsw.protocol.client.view.GUI;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.ObjectiveCard;
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

    public void set(currentStateMessage message) {
        this.myself = message.getPlayer();
        this.current  = message.getCurrentPlayer();
        this.commonObjective = message.getCommonObjectiveCards();
        this.playerArea = message.getPlayer().getPlayerArea();
        this.commonArea = message.getPlayer().getCommonArea();
        setMyself();
        setPions();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerName.setText(myself.getNickname());
    }

    public void setPions(){
       myPion = StarterController.myPion;
    }

    private void setMyself(){
        int ID;
        String imagePath;
        Image cardImage;

        //COMMON AREA
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
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        myObj.setImage(cardImage);

        //Cards
        ID = current.getPlayerHand().getPlaceableCards().getFirst().getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card0.setImage(cardImage);

        ID = current.getPlayerHand().getPlaceableCards().get(1).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card1.setImage(cardImage);

        ID = current.getPlayerHand().getPlaceableCards().get(2).getID();
        imagePath = "CodexNaturalis/src/main/Resource/img/Cards/Front/" + ID + ".png";
        cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card2.setImage(cardImage);
    }

    @FXML
    public void turnAround(MouseEvent event) {

    }

    @FXML
    public void select(MouseEvent event){

    }

    @FXML
    public void selectDraw(MouseEvent event){}


    public int[] getPlaced(){
        return new int[0];
    }

    public int getDraw(){
        return 0;
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
