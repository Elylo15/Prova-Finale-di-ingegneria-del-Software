package it.polimi.ingsw.protocol.client.view.GUI.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private static Stage primaryStage;


    //viewGUI will use the methods of the class to visualize the scene needed
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void availableColors(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/availableColors.fxml"));
            primaryStage.setScene(new Scene(root));
           // primaryStage.setFullScreen(true);
           // primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Choose_Socket_RMI(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/Choose_Socket_RMI.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void currentGamePage(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/currentGamePage.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Disconnect(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/Disconnect.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void EndGame(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/endGame.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void expectedPlayers(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/expectedPlayers.fxml"));
            primaryStage.setScene(new Scene(root));
            //primaryStage.setFullScreen(true);
            //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InsertIP(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/InsertIP.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InsertServerOption(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/InsertServerOption.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void JoinMatch(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/JoinMatch.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void JoinRunningMatch(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/JoinRunningMatch.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadMatch(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/LoadMatch.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void MainView() {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/MainView.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void myselfGamePage(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/myselfGamePage.fxml")));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void objectivePage(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/objectivePage.fxml")));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void starterPage(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/starterPage.fxml")));
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unavailableNames(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/unavailableNames.fxml"));
            primaryStage.setScene(new Scene(root));
            //primaryStage.setFullScreen(true);
            //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void waiting(){
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/waiting.fxml"));
            primaryStage.setScene(new Scene(root));
           //primaryStage.setFullScreen(true);
           // primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





















}
