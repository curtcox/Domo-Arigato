package rkm.server.robots;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import rkm.IRobot;

/**
 * Transformations required for OS X.
 */
public final class OSXTransformer
        implements IRobot
{

    /**
     * The robot we defer to.
     */
    private final IRobot robot;

    /**
     * key we are given -> the one we send on
     */
    private static Map<Integer,Integer> MAP = new HashMap<Integer,Integer>();

    static {
        int ALT     = KeyEvent.VK_ALT;
        int CONTROL = KeyEvent.VK_CONTROL;
        int META    = KeyEvent.VK_META;
        MAP.put(ALT,     ALT);
        MAP.put(CONTROL, CONTROL);
        MAP.put(META,    META);
    }

    private OSXTransformer(IRobot robot) {
        this.robot = robot;
    }

    public BufferedImage createScreenCapture(Rectangle screenRect) {
        return robot.createScreenCapture(screenRect);
    }

    public void keyPress(int keycode) {
        robot.keyPress(map(keycode));
    }

    public void keyRelease(int keycode) {
        robot.keyRelease(map(keycode));
    }

    private static int map(int key) {
        if (MAP.containsKey(key)) {
            return MAP.get(key);
        }
        return key;
    }

    public static OSXTransformer newInstance(IRobot robot) {
        return new OSXTransformer(robot);
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
        robot.mouseWheel(-wheelAmt);
    }

}
