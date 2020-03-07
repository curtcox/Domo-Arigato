package rkm;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class RobotImage {

    public static final Image IMAGE = getImage();

    public static final List<Image> IMAGES = getImages();

    /**
     * Return an image.
     */
    private static Image getImage() {
        int width  = 20;
        int height = 20;
        int imageType = BufferedImage.TYPE_INT_RGB;
        BufferedImage image = new BufferedImage(width,height,imageType);
        Graphics2D g = image.createGraphics();
        int x = 2;
        int y = 10;
        g.drawString("D", x, y);
        x += 9; y += 8;
        g.drawString("A", x, y);
        return image;
    }

    private static List<Image> getImages() {
        List<Image> list = new ArrayList<Image>();
        list.add(getImage());
        return list;
    }
}
