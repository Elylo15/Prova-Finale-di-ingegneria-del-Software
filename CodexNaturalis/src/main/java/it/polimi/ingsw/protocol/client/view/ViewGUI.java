package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.protocol.client.view.GUI.IP_PORT.*;
import it.polimi.ingsw.protocol.client.view.GUI.ChooseConnection.*;
import it.polimi.ingsw.protocol.client.view.GUI.Disconnection.*;
import it.polimi.ingsw.protocol.client.view.GUI.ServerOption.*;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.view.GUI.expectedPlayers.expectedPlayersController;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionResponseMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ViewGUI extends View {
    private final Stage stage;
    private Scene scene;

    public ViewGUI() {
        this.stage = new Stage();
    }

    @Override
    public String[] askPortIP(){
        String[] server = new String[2];
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("it/polimi/ingsw/protocol/client/view/GUI/ServerOption/Insert_ServerOption.fxml"));
            Parent root = loader.load();

            InsertIPPortController controller = loader.getController();
            controller.setViewGUI(this);

            server[0] = controller.getIp();
            server[1] = controller.getPort();

        }catch (IOException e){
            e.printStackTrace();
        }
        return server;
    }

    @Override
    public void showPlayerHand(Player player, String viewer) {

    }


    public boolean askSocket(){//true if socket, false if RMI
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("it/polimi/ingsw/protocol/client/view/GUI/ChooseConnection/Choose_Socket_RMI.fxml"));
            Parent root = loader.load();

            ChooseSocketRMIController controller = loader.getController();
            controller.setViewGUI(this);

            return controller.useSocket();

    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public serverOptionMessage serverOptions(serverOptionMessage message) {
        return message;
    }

    @Override
    public void answerToOption(serverOptionResponseMessage message) {

    }


    @Override
    public void updatePlayer(currentStateMessage message) {

    }


    @Override
    public void playerDisconnected() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("it/polimi/ingsw/protocol/client/view/GUI/Disconnection/Disconnect.fxml"));
            Parent root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void answerToConnection(connectionResponseMessage message) {

    }


    @Override
    public String unavailableNames(unavailableNamesMessage message) {
        return "";
    }

    @Override
    public void answer(responseMessage message) {

    }

    @Override
    public String availableColors(availableColorsMessage message) {
        return "";
    }

    @Override
    public int placeStarter() {
        return 0;
    }

    /**
     * allow the user to choose how many players will play
     * @return number of expected players
     */
    @Override
    public int expectedPlayers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("it/polimi/ingsw/protocol/client/view/GUI/expectedPlayers/expectedPlayers.fxml"));
            Parent root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            expectedPlayersController controller = loader.getController();
            controller.setViewGUI(this);
            //int num = controller.getNumber();

        } catch (IOException e) {
            e.printStackTrace();
        }
         //return num;

        return 0;
    }


    @Override
    public int chooseObjective(ArrayList<ObjectiveCard> objectives) {
        return 0;
    }


    @Override
    public int[] placeCard() {
        return new int[0];
    }

    @Override
    public void update(updatePlayerMessage update) {

    }

    @Override
    public int pickCard() {
        return 0;
    }

    @Override
    public void endGame(declareWinnerMessage message) {

    }

    @Override
    public void showCommonArea(CommonArea commonArea) {

    }


//    private void decks(ArrayList<Card> deck){
//        List<ImageView> imageViews = new ArrayList<>();
//
//        for (Card card : deck) {
//            ImageView imageView = new ImageView();
//            imageView.imageProperty().bind(card.imageProperty());
//            imageViews.add(imageView);
//        }
//
//        HBox hbox = new HBox();
//        hbox.getChildren().addAll(imageViews);
//    }
}

