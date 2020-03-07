package rkm;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import rkm.server.RealRobot;

/**
 * Prints robot methods to System.out.
 * Useful for debugging.
 */
public final class FakeRobot
        implements IRobot
{

    private static final IRobot SINGLETON = new LockedRobot(new FakeRobot());

    public static IRobot newInstance() {
        return SINGLETON;
    }

    private FakeRobot() {}

    public BufferedImage createScreenCapture(Rectangle screenRect) {
        IRobot real = RealRobot.newInstance();
        Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image = real.createScreenCapture(captureSize);
        return image;
    }

    public void keyPress(int keycode) {}
    public void keyRelease(int keycode) {}
    public void mouseMove(int x, int y) {}
    public void mousePress(int buttons) {}
    public void mouseRelease(int buttons) {}
    public void mouseWheel(int wheelAmt) {}

}
