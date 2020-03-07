/**
 *
 */
package rkm.client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

/**
 * Listens for mouse events on the frame and adjusts the frame alpha.
 */
final class AlphaMouseListener implements MouseListener {
    /**
     *
     */
    private final JFrame frame;

    /**
     * @param keyMouseEventFrame
     */
    AlphaMouseListener(JFrame keyMouseEventFrame) {
        frame = keyMouseEventFrame;
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        AlphaFrame.setAlpha(frame,AlphaFrame.TAPPED);
    }

    public void mouseReleased(MouseEvent e) {
        AlphaFrame.setAlpha(frame,AlphaFrame.CONNECTED);
    }
}