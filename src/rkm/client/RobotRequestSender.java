package rkm.client;

import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import rkm.Check;
import rkm.IRobot;
import rkm.RobotMethod;

/**
 * Writes robot events to a stream.
 */
final class RobotRequestSender
        implements IRobot
{

    /**
     * Stuff server sends back
     */
    private final DataInputStream in;

    /**
     * Stuff we send to the server
     */
    private final DataOutputStream out;

    RobotRequestSender(InputStream in, OutputStream out) throws IOException {
        this.in  = new DataInputStream(in);
        this.out = new DataOutputStream(out);
        this.out.writeUTF(RobotMethod.START_REQUESTS.getToken());
    }

    public BufferedImage createScreenCapture(Rectangle screenRect) {
        Check.isNotEDT();
        try {
            out.writeUTF(RobotMethod.CREATE_SCREEN_CAPTURE.getToken());
            int x = screenRect.x;
            int y = screenRect.y;
            int w = screenRect.width;
            int h = screenRect.height;
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(w);
            out.writeInt(h);
            out.flush();
            BufferedImage image = ImageIO.read(in);
            Check.notNull(image);
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void keyPress(int keycode) {
        Check.isNotEDT();
        try {
            out.writeUTF(RobotMethod.KEY_PRESS.getToken());
            out.writeInt(keycode);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void keyRelease(int keycode) {
        Check.isNotEDT();
        try {
            out.writeUTF(RobotMethod.KEY_RELEASE.getToken());
            out.writeInt(keycode);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mouseMove(int x, int y) {
        Check.isNotEDT();
        try {
            out.writeUTF(RobotMethod.MOUSE_MOVE.getToken());
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mousePress(int buttons) {
        Check.isNotEDT();
        try {
            out.writeUTF(RobotMethod.MOUSE_PRESS.getToken());
            out.writeInt(translate(buttons));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Translate from mouse event to robot buttons.
     */
    private static int translate(int buttons) {
        if (buttons==MouseEvent.BUTTON1) {
            return InputEvent.BUTTON1_MASK;
        }
        if (buttons==MouseEvent.BUTTON2) {
            return InputEvent.BUTTON2_MASK;
        }
        if (buttons==MouseEvent.BUTTON3) {
            return InputEvent.BUTTON3_MASK;
        }
        throw new IllegalArgumentException("buttons=" + buttons);
    }

    public void mouseRelease(int buttons) {
        Check.isNotEDT();
        try {
            out.writeUTF(RobotMethod.MOUSE_RELEASE.getToken());
            out.writeInt(translate(buttons));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mouseWheel(int wheelAmt) {
        Check.isNotEDT();
        try {
            out.writeUTF(RobotMethod.MOUSE_WHEEL.getToken());
            out.writeInt(wheelAmt);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void log(String message) {
        System.out.println("RobotRequestSender : " + message);
    }
}
