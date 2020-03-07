package rkm.client;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.jmdns.ServiceInfo;
import javax.swing.JFrame;
import javax.swing.JPanel;

import rkm.Check;
import rkm.IRobot;
import rkm.RobotImage;
import rkm.threads.EDTProxy;
import rkm.threads.LaterProxy;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * A frame that generates keyboard and mouse events.
 */
final class KeyMouseEventFrame
        extends JFrame
        implements DisconnectableFrame
{
    /**
     * Listens for events in this frame to be sent to the remote robot.
     */
    private KeyMouseEventRelayListener listener;

    private final AutosizeImage hostImage = new AutosizeImage();

    /**
     * How we connect to the remote robot.
     */
    private final RobotConnector connector;

    /**
     * To organize the GUI
     */
    private final JPanel     panel   = new JPanel();

    /**
     * Are we connected to a host?
     */
    private boolean connected;

    /**
     * The server we connect to.
     */
    private final String host;

    /**
     * The port we connect to on the server.
     */
    private final int port;

    private final CardPanel cards = new CardPanel();

    private final MouseListener alphaListener = new AlphaMouseListener(this);
    private final KeyListener alphaKeyListener = new AlphaKeyListener(this);

    /**
     * For stuff we must switch back to the EDT for.
     */
    private final IEDT     EDT = (IEDT) EDTProxy.newInstance(new EDTMethods(this),IEDT.class);

    /**
     * For stuff we must switch away from the EDT for.
     */
    private final ILater LATER = (ILater) LaterProxy.newInstance(new LaterMethods(),ILater.class);

    private static final long serialVersionUID = -4949042734847796561L;

    static {
        AlphaFrame.init();
    }

    /**
     * Use the factory.
     */
    private KeyMouseEventFrame(RobotConnector connector, String host, int port) {
        this.connector = connector;
        this.host = host;
        this.port = port;
        layoutFrame();
    }

    static KeyMouseEventFrame connectTo(RobotConnector connector, ServiceInfo info) {
        String host = info.getHostAddress();
        int    port = info.getPort();
        KeyMouseEventFrame frame = new KeyMouseEventFrame(connector,host,port);
        frame.EDT.setVisible();
        frame.LATER.connectTo(host, port);
        return frame;
    }

    private void layoutFrame() {
        panel.setLayout(new BorderLayout());
        cards.add(panel);
        cards.add(hostImage);
        cards.show(panel);
        add(cards);
    }

    /**
     * Disconnect from the server we're connected to.
     */
    public void disconnect() {
        connected = false;
        EDT.setStatus("Disconnected from" + host);
        EDT.stopListeningToFrame();
    }

    /**
     * Stuff that needs to be done later -- not on the EDT.
     */
    public interface ILater {
        void connectTo(final String host, final int port);
    }

    class LaterMethods implements ILater {
        /**
         * Try connecting to the given host and port.
         */
        public void connectTo(final String host, final int port) {
            try {
                final IRobot robot = connector.connectTo(host, port);
                BufferedImage image = robot.createScreenCapture(IRobot.FULL);
                Check.notNull(image);
                EDT.showImage(image);
                connected = true;
                EDT.startListeningToFrame(robot);
                EDT.setStatus("Connected to " + host);
            } catch (IOException e) {
                EDT.setStatus("Failed connecting to " + host);
            }
        }

    }

    /**
     * Stuff that needs to be done on the EDT.
     */
    public interface IEDT {
        void startListeningToFrame(IRobot robot);
        void stopListeningToFrame();
        void showImage(BufferedImage image);
        void setStatus(String message);
        void setVisible();
    }

    private class EDTMethods implements IEDT {

        private final KeyMouseEventFrame frame;

        EDTMethods(KeyMouseEventFrame frame) {
            this.frame = frame;
        }

        /**
         * When we get disconnected, stop reporting events and restore full alpha.
         */
        public void stopListeningToFrame() {
            Check.isEDT();
            listener.removeFrom(frame);
            getGlassPane().removeMouseListener(alphaListener);
            AWTKeyAdapter.removeKeyListener(alphaKeyListener);
            AlphaFrame.setAlpha(frame,AlphaFrame.NORMAL);
        }

        /**
         * We have connected to the server, so start listening to the frame.
         */
        public void startListeningToFrame(IRobot robot) {
            Check.isEDT();
            // Disconnect on error
            UncaughtExceptionHandler onError = new UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    disconnect();
                }
            };
            // Send stuff to the server later.
            // Otherwise, we would have IO on the EDT.
            // TODO wrap with a Queue Proxy instead
            robot = (IRobot) LaterProxy.newInstance(robot, IRobot.class,onError);
            listener = new KeyMouseEventRelayListener(frame,robot);
            listener.addTo(frame);
            getGlassPane().addMouseListener(alphaListener);
            AWTKeyAdapter.addKeyListener(alphaKeyListener);
            AlphaFrame.setAlpha(frame,AlphaFrame.CONNECTED);
        }

        public void showImage(BufferedImage image) {
            hostImage.setImage(image);
            cards.show(hostImage);
            frame.invalidate();
            frame.repaint();
        }

        public void setStatus(String message) {
            frame.setTitle(message);
        }

        public void setVisible() {
            frame.setIconImages(RobotImage.IMAGES);
            frame.setTitle(host);
            frame.setSize(200,200);
            frame.setVisible(true);
        }
    }

    public static void main(String[] args) throws Exception {
        RobotConnector connector = new FakeRobotConnector();
        ServiceInfo info = new FakeServiceInfo("Server");
        JFrame frame = connectTo(connector,info);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
