package rkm;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * The interface that java.awt.Robot implements that we care about.
 */
public interface IRobot {

    int MAX = 10000;
    Rectangle FULL = new Rectangle(0,0,0,0);

    void keyPress(int keycode);
    void keyRelease(int keycode);
    void mouseMove(int  x, int y);
    void mousePress(int buttons);
    void mouseRelease(int buttons);
    void mouseWheel(int wheelAmt);
    BufferedImage createScreenCapture(Rectangle screenRect);

}
