package rkm.server;


import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import rkm.Check;
import rkm.IRobot;
import rkm.RobotMethod;
import rkm.security.RobotSecurity;
import rkm.server.robots.KeyReleaseRobot;

/**
 * Reads requests from stream and executes them with the given robot.
 */
final class SingleRobotRequestHandler {

    /**
     * Read requests from here
     */
    private final DataInputStream in;

    /**
     * Write any responses here
     */
    private final DataOutputStream out;

    /**
     * The socket the data streams are connected to.
     */
    private final Socket socket;

    /**
     * Execute methods on this
     */
    private final KeyReleaseRobot robot;

    /**
     * Use the factory.
     */
    private SingleRobotRequestHandler(KeyReleaseRobot robot, Socket socket, InputStream in, OutputStream out) {
        this.robot = robot;
        this.socket = socket;
        this.in  = new DataInputStream(in);
        this.out = new DataOutputStream(out);
    }

    static void handle(KeyReleaseRobot robot, Socket socket, InputStream in, OutputStream out)
            throws IOException
    {
        SingleRobotRequestHandler handler = new SingleRobotRequestHandler(robot,socket,in,out);
        handler.handle();
    }

    private void handle()
            throws IOException
    {
        log("serving request from " + socket.getInetAddress());
        RobotSecurity.authenticateClient(in,out);
        log("Launching server side client UI");
        ClientConnectionUI ui = ClientConnectionUI.newInstance(socket);
        log("Starting request handling");
        String request = in.readUTF();
        log(request);
        if (!request.equals(RobotMethod.START_REQUESTS.getToken())) {
            String message = "Expected " + RobotMethod.START_REQUESTS.getToken() + " but got " + request;
            log(message);
            out.writeUTF(message);
            out.close();
            in.close();
        }
        try {
            while (true) {
                handleOneMethod();
            }
        } catch (IOException e) {
            ui.notifyIOException(e);
            log("IOException -- probably a normal connection termination.");
            e.printStackTrace();
        } finally {
            robot.releaseAll();
        }
    }

    private void handleOneMethod() throws IOException {
        Check.isNotEDT();
        String method = in.readUTF();
        log("method=" + method);
        if (RobotMethod.KEY_PRESS.matches(method))             { keyPress();
        } else if (RobotMethod.KEY_RELEASE.matches(method))           { keyRelease();
        } else if (RobotMethod.MOUSE_MOVE.matches(method))            { mouseMove();
        } else if (RobotMethod.MOUSE_PRESS.matches(method))           { mousePress();
        } else if (RobotMethod.MOUSE_RELEASE.matches(method))         { mouseRelease();
        } else if (RobotMethod.MOUSE_WHEEL.matches(method))           { mouseWheel();
        } else if (RobotMethod.CREATE_SCREEN_CAPTURE.matches(method)) { createScreenCapture();
        } else {
            throw new IllegalArgumentException("method=" + method);
        }
    }

    private void keyPress() throws IOException {
        int keycode = in.readInt();
        robot.keyPress(keycode);
    }

    private void keyRelease() throws IOException {
        int keycode = in.readInt();
        robot.keyRelease(keycode);
    }

    private void mousePress() throws IOException {
        int buttons = in.readInt();
        robot.mousePress(buttons);
    }

    private void mouseRelease() throws IOException {
        int buttons = in.readInt();
        robot.mouseRelease(buttons);
    }

    private void mouseMove() throws IOException {
        int x = in.readInt();
        int y = in.readInt();
        int[] xy = scaleToScreenSize(x,y);
        x = xy[0];
        y = xy[1];
        robot.mouseMove(x,y);
    }

    private static int[] scaleToScreenSize(int x, int y) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        DisplayMode mode = gs[0].getDisplayMode();
        int w = mode.getWidth();
        int h = mode.getHeight();

        x = (x * w) / IRobot.MAX;
        y = (y * h) / IRobot.MAX;

        int[] xy = new int[2];
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    private void mouseWheel() throws IOException {
        int wheelAmount = in.readInt();
        robot.mouseWheel(wheelAmount);
    }

    private void createScreenCapture() throws IOException {
        log("capturing");
        int x = in.readInt();
        int y = in.readInt();
        int w = in.readInt();
        int h = in.readInt();
        Rectangle screenRect = new Rectangle(x,y,w,h);
        if (screenRect.equals(IRobot.FULL)) {
            screenRect = fullScreenRect();
        }

        log("capturing " + screenRect);
        BufferedImage image = robot.createScreenCapture(screenRect);
        ImageIO.write( image, "png", out);
        out.flush();
    }


    private Rectangle fullScreenRect() {
        return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public boolean handles(String request) {
        return request.contains("robot");
    }

    private static void log(String message) {
        System.out.println("SingleRobotRequestHandler : " + message);
    }

    public static SingleRobotRequestHandler newInstance(KeyReleaseRobot robot,
                                                        Socket socket, InputStream in, OutputStream out) {
        return new SingleRobotRequestHandler(robot, socket, in, out);
    }
}
