package rkm.server.robots;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import rkm.IRobot;

/**
 * Releases any pressed keys when asked.
 * This is handy for restoring when a connection breaks.
 */
public final class KeyReleaseRobot
        implements IRobot
{

    /**
     * Keys that we have pressed right now.
     */
    private final Set<Integer> pressed = new HashSet<Integer>();

    /**
     * The robot we defer to.
     */
    private final IRobot robot;

    public KeyReleaseRobot(IRobot robot) {
        this.robot = robot;
    }

    public BufferedImage createScreenCapture(Rectangle screenRect) {
        return robot.createScreenCapture(screenRect);
    }

    public void keyPress(int keycode) {
        robot.keyPress(keycode);
        pressed.add(keycode);
    }

    public void keyRelease(int keycode) {
        robot.keyRelease(keycode);
        pressed.remove(keycode);
    }

    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void mousePress(int buttons) {
        robot.mousePress(buttons);
    }

    public void mouseRelease(int buttons) {
        robot.mouseRelease(buttons);
    }

    public void mouseWheel(int wheelAmt) {
        robot.mouseWheel(wheelAmt);
    }

    public void releaseAll() {
        for (int keycode : pressed) {
            keyRelease(keycode);
        }
    }

}
