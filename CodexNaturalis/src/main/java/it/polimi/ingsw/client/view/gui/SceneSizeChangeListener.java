package it.polimi.ingsw.client.view.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the controller for the main page of the game.
 * It handles the user's interaction with the GUI on the main page.
 */
public class SceneSizeChangeListener implements ChangeListener<Parent> {
    final double initWidth = 1920;
    final double initHeight = 1080;
    final Pane root = new Pane();
    private Scene scene;

    /**
     * This constructor creates a new SceneSizeChangeListener.
     * It loads the pageName FXML file and sets the mainStage scene to the loaded page.
     * It also sets the initial width and height of the scene.
     *
     * @param pageName  the name of the FXML file to load
     * @param mainStage the main stage of the application
     * @throws IOException if the FXML file is not found
     */
    public SceneSizeChangeListener(String pageName, Stage mainStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        Pane controller = loader.load();
        controller.setPrefWidth(initWidth);
        controller.setPrefHeight(initHeight);
        root.getChildren().add(controller);

        if (mainStage.getScene() == null) {
            scene = new Scene(root, initWidth, initHeight);
            mainStage.setScene(scene);
            mainStage.setResizable(true);
            mainStage.show();
        } else {
            mainStage.getScene().rootProperty().set(root);
        }

        Scale scale = new Scale(1, 1, 0, 0);
        scale.xProperty().bind(root.widthProperty().divide(initWidth));
        scale.yProperty().bind(root.heightProperty().divide(initHeight));
        root.getTransforms().add(scale);

    }

    /**
     * This method is called when the scene is changed.
     * It sets the new scene root to the root of the scene.
     *
     * @param observableValue the observable value
     * @param parent          the old parent
     * @param t1              the new parent
     */
    @Override
    public void changed(ObservableValue<? extends Parent> observableValue, Parent parent, Parent t1) {
        scene.rootProperty().removeListener(this);
        scene.setRoot(root);
        ((Region) t1).setPrefWidth(initWidth);
        ((Region) t1).setPrefHeight(initHeight);
        root.getChildren().clear();
        root.getChildren().add(t1);
        scene.rootProperty().addListener(this);

    }
}
