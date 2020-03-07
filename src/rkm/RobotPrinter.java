package rkm;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

/**
 * Prints robot methods to System.out.
 * Useful for debugging.
 */
public final class RobotPrinter
        implements IRobot
{

    private final String name;

    private final PrintStream out = System.out;

    private final IRobot robot;

    public RobotPrinter(String name, IRobot robot) {
        this.name  = name;
        this.robot = robot;
    }

    public BufferedImage createScreenCapture(Rectangle screenRect) {
        out("capturing=" + screenRect);
        return robot.createScreenCapture(screenRect);
    }

    public void keyPress(int keycode) {
        robot.keyPress(keycode);
        out("keyPress=" + keycode);
    }

    public void keyRelease(int keycode) {
        robot.keyRelease(keycode);
        out("keyRelease=" + keycode);
    }

    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
        out("mouseMove=" + x + "," + y);
    }

    public void mousePress(int buttons) {
        robot.mousePress(buttons);
        out("mousePress=" + buttons);
    }

    public void mouseRelease(int buttons) {
        robot.mouseRelease(buttons);
        out("mouseRelease=" + buttons);
    }

    public void mouseWheel(int wheelAmt) {
        robot.mouseWheel(wheelAmt);
        out("mouseWheel=" + wheelAmt);
    }

    private final void out(String message) {
        out.println(name + " : " + message);
    }
}
