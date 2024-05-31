package it.polimi.ingsw.protocol.client.view.GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageBinder {
    Map<Integer, String[]> imageMap = new HashMap<>();

    /**
     * Constructor for the ImageBinder class
     */
    public ImageBinder() {
        for (int i = 1; i < 10; i++)
            imageMap.put(i, new String[]{"CodexNaturalis/src/main/Resource/img/Cards/Front/00" + i + ".png", "CodexNaturalis/src/main/Resource/img/Cards/Back/" + i + ".png"});
        for (int i = 10; i < 100; i++)
            imageMap.put(i, new String[]{"CodexNaturalis/src/main/Resource/img/Cards/Front/00" + i + ".png", "CodexNaturalis/src/main/Resource/img/Cards/Back/" + i + ".png"});
        for (int i = 100; i < 103; i++)
            imageMap.put(i, new String[]{"CodexNaturalis/src/main/Resource/img/Cards/Front/00" + i + ".png", "CodexNaturalis/src/main/Resource/img/Cards/Back/" + i + ".png"});
    }

    /**
     * Binds the image to the ImageView
     *
     * @param id    id of the image
     * @param front true if the front image is needed
     * @return ImageView
     */
    public ImageView bindImage(int id, boolean front) {
        String[] imagePaths = imageMap.get(id);
        String imagePath = front ? imagePaths[0] : imagePaths[1];
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        return new ImageView(image);
    }

    /**
     * Returns the image
     *
     * @param id    id of the image
     * @param front true if the front image is needed
     * @return Image
     */
    public Image getImage(int id, boolean front) {
        String[] imagePaths = imageMap.get(id);
        String imagePath = front ? imagePaths[0] : imagePaths[1];
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    /**
     * Returns the opposite side image
     *
     * @param id           id of the image
     * @param currentFront true if the current front
     * @return Image
     */
    public Image getOppositeImage(int id, boolean currentFront) {
        return getImage(id, !currentFront);
    }
}
