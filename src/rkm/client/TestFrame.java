package rkm.client;

import javax.swing.JFrame;

/**
 * Quick way to create frames for testing.
 */
final class TestFrame {

    static JFrame newInstance() {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }
}
