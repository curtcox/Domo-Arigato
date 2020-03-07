package rkm.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Alpha frame support.
 * This uses JNA for now.
 */
final class AlphaFrame {

    static final float NORMAL    = 1.0f;
    static final float CONNECTED = 0.3f;
    static final float TAPPED    = 0.8f;

    /**
     * Called by init().
     */
    static {
        try {
            System.setProperty("sun.java2d.noddraw", "true");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This class must be initialized before any frames are created.
     * Otherwise, the frames will purport to support alpha transparency,
     * but will have all sorts of annoying visual bugs.
     */
    static void init() {}

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                main0();
            }
        });
    }

    private static void main0() {
        JFrame frame = TestFrame.newInstance();
        setAlpha(frame,1.0f);
    }

    static void setAlpha(JFrame frame, float alpha) {
//        import com.sun.jna.examples.WindowUtils;
//        System.out.println("alpha? " + WindowUtils.isWindowAlphaSupported());
//        if (WindowUtils.isWindowAlphaSupported()) {
//            WindowUtils.setWindowAlpha(frame, alpha);
//        }
    }
}
