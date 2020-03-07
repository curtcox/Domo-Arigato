package rkm.server;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import rkm.RobotImage;

/**
 * Supply simple help.
 */
public final class HelpFrame
        extends JFrame
{

    private JEditorPane editorPane = createEditorPane();

    private static HelpFrame FRAME = new HelpFrame();

    /**
     * To make Eclipse happy
     */
    private static final long serialVersionUID = 1L;

    private HelpFrame() {
        super("Domo Arigato Help");
        add(new JScrollPane(editorPane));
        this.setIconImages(RobotImage.IMAGES);
        setSize(650,350);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private static final String HELP_TEXT =
            "<html>" +
                    "<h1>Domo Arigato Help</h1>" +
                    "This application is written to allow the use of <b>multiple monitors</b> attached to " +
                    "<b>multiple computers</b>, using a single keyboard and mouse. It is roughly functionally equivalent " +
                    "to more popular program Synergy. " +
                    "<p>" +
                    "Compared with Synergy, Domo Arigato:" +
                    "<ul>" +
                    "<li> is far less mature and established" +
                    "<li> has fewer features" +
                    "<li> is written entirely in Java" +
                    "<li> uses a different user interface paradigm" +
                    "<li> should be easier to set up" +
                    "</ul>" +
                    "<p>" +
                    "Domo arigato is not a weak pun on Multics.  Apologies to Styx, Ken Thompson, and Apple." +
                    "</html>";

    private JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText(HELP_TEXT);
        return editorPane;
    }

    public static HelpFrame get() {
        return FRAME;
    }

    public static void main(String[] args) {
        JFrame frame = get();
        frame.setVisible(true);
        frame.setSize(650,350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
