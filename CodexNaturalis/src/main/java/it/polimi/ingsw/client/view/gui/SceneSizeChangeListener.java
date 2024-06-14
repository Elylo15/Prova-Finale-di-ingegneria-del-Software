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

public class SceneSizeChangeListener implements ChangeListener<Parent> {
    final double initWidth = 1920;
    final double initHeight = 1080;
    final Pane root;
    private final Scene scene;

    /**
     * This constructor creates a new SceneSizeChangeListener.
     * It loads the pageName FXML file and sets the mainStage scene to the loaded page.
     * It also sets the initial width and height of the scene.
     *
     * @param pageName  the name of the FXML file to load
     * @param mainStage the main stage of the application
     * @throws IOException if the FXML file is not found
     */
    public SceneSizeChangeListener(String pageName, Stage mainStage, Pane root) throws IOException {
        this.root = root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        Pane controller = loader.load();
        controller.setPrefWidth(initWidth);
        controller.setPrefHeight(initHeight);
        root.getChildren().add(controller);

        Scale scale = new Scale(1, 1, 0, 0);
        scale.xProperty().bind(root.widthProperty().divide(initWidth));
        scale.yProperty().bind(root.heightProperty().divide(initHeight));
        root.getTransforms().add(scale);

        //TODO this can be an idea but I dont know if it works, I still have to try
//        scene = new Scene(root, initWidth, initHeight);
//        mainStage.setScene(scene); this should happen only the first time
        //right now this wont work, need to move that in mainPage
        this.scene = mainStage.getScene();
        scene.setRoot(root);

        mainStage.setResizable(true);
//        mainStage.show(); shown only the first time


        // To change scene without noticing,
        // maybe remove mainStage.show()
        // try to find a solution to resize even if do not change scene, but root
        // root is changed with: mainStage.getScene().setRoot(newRoot)
        // the new root would be the Pane controller
        // maybe i can have an observable that listens to the root property of the scene
        // and if the root changes, it resizes, instead of listening for Parent property
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
