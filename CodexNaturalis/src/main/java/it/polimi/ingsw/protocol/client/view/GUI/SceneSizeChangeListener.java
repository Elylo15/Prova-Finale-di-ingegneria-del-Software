package it.polimi.ingsw.protocol.client.view.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

public class SceneSizeChangeListener implements ChangeListener<Number> {
    private final Scene scene;
    final double initWidth = 1920;
    final double initHeight = 1080;
    private final Pane contentPane;

    public SceneSizeChangeListener(Scene scene, Pane contentPane) {
        this.scene = scene;
        this.contentPane = contentPane;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        final double newWidth = scene.getWidth();
        final double newHeight = scene.getHeight();
        double scaleFactor = newHeight / initHeight;

        Scale scale = new Scale(scaleFactor, scaleFactor);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);

        contentPane.setPrefWidth(initWidth * scaleFactor);
        contentPane.setPrefHeight(initHeight * scaleFactor);

        System.out.println("Pane Width: " + contentPane.getPrefWidth() + " Pane Height: " + contentPane.getPrefHeight());
        System.out.println("Scale: " + scaleFactor);
    }
}
