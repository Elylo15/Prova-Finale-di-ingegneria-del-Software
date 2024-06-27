package it.polimi.ingsw.client.view.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is a controller for the images.
 * It contains a map that binds the image to the image view.
 */
public class ImageBinder {
    Map<Integer, String[]> imageMap = new HashMap<>();

    /**
     * Constructor for the ImageBinder class
     */
    public ImageBinder() {
        for (int i = 1; i < 10; i++)
            imageMap.put(i, new String[]{"/Images/Cards/Front/00" + i + ".png", "/Images/Cards/Back/00" + i + ".png"});
        for (int i = 10; i < 100; i++)
            imageMap.put(i, new String[]{"/Images/Cards/Front/0" + i + ".png", "/Images/Cards/Back/0" + i + ".png"});
        for (int i = 100; i < 103; i++)
            imageMap.put(i, new String[]{"/Images/Cards/Front/" + i + ".png", "/Images/Cards/Back/" + i + ".png"});
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
        if (imagePaths == null) {
            throw new IllegalArgumentException("Invalid image id: " + id);
        }
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
