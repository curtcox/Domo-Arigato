/**
 *
 */
package rkm.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * Listens for key events on the frame and adjusts the frame alpha.
 */
final class AlphaKeyListener implements KeyListener {

    private final JFrame frame;

    /**
     * @param keyMouseEventFrame
     */
    AlphaKeyListener(JFrame frame) {
        this.frame = frame;
    }

    public void keyPressed(KeyEvent e) {
        AlphaFrame.setAlpha(frame,AlphaFrame.TAPPED);
    }

    public void keyReleased(KeyEvent e) {
        AlphaFrame.setAlpha(frame,AlphaFrame.CONNECTED);
    }

    public void keyTyped(KeyEvent e) {}

}