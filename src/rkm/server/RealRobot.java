package rkm.server;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import rkm.IRobot;
import rkm.LockedRobot;

/**
 * Wraps a java.awt.Robot, so that it can be used as an IRobot
 */
public final class RealRobot
        implements IRobot
{

    /**
     * Our AWT robot
     */
    private static final Robot ROBOT = newRobot();

    private static final IRobot SINGLETON = new LockedRobot(new RealRobot());

    private static Robot newRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static IRobot newInstance() {
        return SINGLETON;
    }

    /**
     * Use the factory
     */
    private RealRobot() {}

    public void keyPress(int keycode) {
        ROBOT.keyPress(keycode);
    }

    public void keyRelease(int keycode) {
        ROBOT.keyRelease(keycode);
    }

    public void mouseMove(int x, int y) {
        ROBOT.mouseMove(x, y);
    }

    public void mousePress(int buttons) {
        ROBOT.mousePress(buttons);
    }

    public void mouseRelease(int buttons) {
        ROBOT.mouseRelease(buttons);
    }

    public void mouseWheel(int wheelAmt) {
        ROBOT.mouseWheel(wheelAmt);
    }

    public BufferedImage createScreenCapture(Rectangle screenRect) {
        return ROBOT.createScreenCapture(screenRect);
    }

}
