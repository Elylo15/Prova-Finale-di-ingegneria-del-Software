package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.GUI.ChooseConnection.ChooseSocketRMIController;
import it.polimi.ingsw.GUI.IP_PORT.InsertIPPortController;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionResponseMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("it/polimi/ingsw/GUI/ServerOption/Insert_ServerOption.fxml"));
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


    public boolean askSocket(){//true if socket, false if RMI
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("it/polimi/ingsw/GUI/ChooseConnection/Choose_Socket_RMI.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("it/polimi/ingsw/GUI/Disconnection/Disconnect.fxml"));
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

    @Override
    public int expectedPlayers() {
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

    @Override
    public void showPlayerHand(currentStateMessage message) {

    }


}
