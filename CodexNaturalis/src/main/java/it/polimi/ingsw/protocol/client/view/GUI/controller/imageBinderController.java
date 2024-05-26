package it.polimi.ingsw.protocol.client.view.GUI.controller;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.protocol.client.view.GUI.ImageBinder;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class imageBinderController {
    @FXML
    private VBox cardContainer;
    private final ImageBinder imageBinder = new ImageBinder();

    public imageBinderController() {

    }

    private void addCardToContainer(Card card) {
        ImageView cardImageView = imageBinder.bindImage(card.getID(), card.isFront());
        if (cardImageView != null) {
            cardContainer.getChildren().add(cardImageView);
            cardImageView.setOnMouseClicked(event -> {
                card.setFront(!card.isFront());
                cardImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageBinder.imageMap.get(card.getID())[card.isFront() ? 0 : 1]))));
            });
        }
    }


}
