package it.polimi.ingsw.protocol.client.view.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

public class SceneSizeChangeListener implements ChangeListener<Number> {

    private final Scene scene;
    private final double ratio;
    private final double initHeight;
    private final double initWidth;
    private final Pane contentPane;
    private final Scale scale;

    public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
        this.scene = scene;
        this.ratio = ratio;
        this.initHeight = initHeight;
        this.initWidth = initWidth;
        this.contentPane = contentPane;
        this.scale = new Scale(1, 1, 0, 0);
        scene.getRoot().getTransforms().add(scale);
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        final double newWidth = scene.getWidth();
        final double newHeight = scene.getHeight();

        double scaleFactor = (newWidth / newHeight > ratio) ? newHeight / initHeight : newWidth / initWidth;

        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        if (scaleFactor >= 1) {
            contentPane.setPrefWidth(newWidth / scaleFactor);
            contentPane.setPrefHeight(newHeight / scaleFactor);
        } else {
            contentPane.setPrefWidth(Math.max(initWidth, newWidth));
            contentPane.setPrefHeight(Math.max(initHeight, newHeight));
        }
    }
}

