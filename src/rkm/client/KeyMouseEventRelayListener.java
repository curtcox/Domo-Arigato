/**
 *
 */
package rkm.client;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

import rkm.FakeRobot;
import rkm.IRobot;
import rkm.RobotPrinter;
import rkm.threads.LaterProxy;

/**
 * Listens for key and mouse events.
 * If an error is encountered, the frame being listened to will be disconnected.
 */
final class KeyMouseEventRelayListener
        implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener
{
    /**
     * The frame we listen to.
     */
    private final DisconnectableFrame frame;

    /**
     * Turns toolkit AWT events into key events
     */
    private final AWTKeyAdapter adapter;

    /**
     * Send events here.
     */
    private final IRobot robot;

    KeyMouseEventRelayListener(DisconnectableFrame frame,IRobot robot) {
        this.frame = frame;
        this.robot = robot;
        this.adapter = new AWTKeyAdapter(this);
    }

    public void keyTyped(KeyEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public void keyPressed(KeyEvent e) {
        try {
            int keycode = e.getKeyCode();
            robot.keyPress(keycode);
        } catch (Throwable t) {
            frame.disconnect();
            t.printStackTrace();
        }
    }

    public void keyReleased(KeyEvent e) {
        try {
            int keycode = e.getKeyCode();
            robot.keyRelease(keycode);
        } catch (Throwable t) {
            frame.disconnect();
            t.printStackTrace();
        }
    }

    private static final int MAX = 10000;

    public void mouseMoved(MouseEvent e) {
        try {
            int mx = e.getX();
            int my = e.getY();
            Component contents = frame.getGlassPane();
            int fw = contents.getWidth();
            int fh = contents.getHeight();
            int x = mx * MAX / fw;
            int y = my * MAX / fh;
            robot.mouseMove(x, y);
        } catch (Throwable t) {
            frame.disconnect();
            t.printStackTrace();
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        try {
            int wheelAmount = e.getWheelRotation();
            robot.mouseWheel(wheelAmount);
        } catch (Throwable t) {
            frame.disconnect();
            t.printStackTrace();
        }
    }

    public void mousePressed(MouseEvent e) {
        try {
            int buttons = e.getButton();
            robot.mousePress(buttons);
        } catch (Throwable t) {
            frame.disconnect();
            t.printStackTrace();
        }
    }

    public void mouseReleased(MouseEvent e) {
        try {
            int buttons = e.getButton();
            robot.mouseRelease(buttons);
        } catch (Throwable t) {
            frame.disconnect();
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                main0();
            }
        });
    }

    void addTo(DisconnectableFrame frame) {
        Component target = frame.getGlassPane();
        target.setVisible(true);
        target.setFocusable(true);
        target.addMouseListener(this);
        target.addMouseMotionListener(this);
        target.addMouseWheelListener(this);
        AWTKeyAdapter.addKeyListener(this);
        log("stopped listening");
    }


    void removeFrom(DisconnectableFrame frame) {
        Component target = frame.getGlassPane();
        target.setVisible(false);
        target.setFocusable(false);
        target.removeMouseListener(this);
        target.removeMouseMotionListener(this);
        target.removeMouseWheelListener(this);
        AWTKeyAdapter.removeKeyListener(this);
    }

    private static void main0() {
        class FakeFrame extends JFrame implements DisconnectableFrame {
            public void disconnect() {
            }
        }
        FakeFrame frame = new FakeFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400,400);
        IRobot robot = new RobotPrinter("Client",FakeRobot.newInstance());
        KeyMouseEventRelayListener listener = new KeyMouseEventRelayListener(frame,robot);
        listener.addTo(frame);
    }

    private static void log(String message) {
        System.out.println("KeyMouseEventRelay : " + message);
    }
}