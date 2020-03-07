package rkm.client;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import rkm.FakeRobot;
import rkm.IRobot;

/**
 * A label that displays an automatically resized image.
 */
final class AutosizeImage
        extends JLabel
{
    /**
     * The raw image before scaling.
     */
    private volatile BufferedImage image;

    /**
     * The latest resize number
     */
    private volatile int latest;

    private static final long serialVersionUID = -2954324305383739223L;

    AutosizeImage() {}

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                main();
            }
        });
    }

    private static void main() {
        JFrame frame = TestFrame.newInstance();
        AutosizeImage autosize = new AutosizeImage();
        BufferedImage image = FakeRobot.newInstance().createScreenCapture(IRobot.FULL);
        autosize.setImage(image);
        frame.add(autosize);
    }

    void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void reshape(final int x, final int y, final int w, final int h) {
        latest++;
        final int attempt = latest;
        Thread thread = new Thread() {
            public void run() {
                if (attempt!=latest || image==null || w==0 || h==0) {
                    return; // abandon the attempt if not latest
                }
                int hints = Image.SCALE_SMOOTH;
                final Image scaled = image.getScaledInstance(w, h, hints);
                if (attempt!=latest) {
                    return; // abandon the attempt if not latest
                }
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        if (attempt!=latest) {
                            return; // abandon the attempt if not latest
                        }
                        setIcon(new ImageIcon(scaled));
                        AutosizeImage.super.reshape(x, y, w, h);
                        AutosizeImage.this.repaint();
                    }
                });
            }
        };
        thread.start();
    }
}
