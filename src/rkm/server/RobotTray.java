package rkm.server;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import rkm.RobotImage;
import rkm.client.ServerSelectionFrame;

/**
 * For dealing with the system tray.
 */
final class RobotTray {

    /**
     * This is displayed when the user right-clicks.
     */
    private final PopupMenu popup = new PopupMenu();

    /**
     * Image in the system tray.
     */
    private final Image image = RobotImage.IMAGE;

    private static final String TOOLTIP = "Remote Robot server";

    private final TrayIcon trayIcon = new TrayIcon(image, TOOLTIP, popup);

    private static final RobotTray TRAY = new RobotTray();

    private RobotTray() {
        install();
    }


    static void init() {
        // lets users make sure tray has been installed.
    }

    private final class TrayIconListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayMessage("?","?",TrayIcon.MessageType.INFO);
        }
    }

    private static final class ExitItem implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            out("Exiting...");
            System.exit(0);
        }
    }

    private static final class StartStopItem implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            out("Exiting...");
            System.exit(0);
        }
    }

    private static final class OthersItem implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame frame = ServerSelectionFrame.get();
            frame.setVisible(true);
            frame.toFront();
        }
    }

    private static final class HelpItem implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame frame = HelpFrame.get();
            frame.setVisible(true);
            frame.toFront();
        }
    }

    void displayMessage(String caption, String text, TrayIcon.MessageType messageType) {
        trayIcon.displayMessage(caption, text, messageType);
    }

    private static final class TrayMouseListener
            implements MouseListener
    {
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
    }


    private void install() {
        SystemTray tray = SystemTray.getSystemTray();

        MouseListener mouseListener   = new TrayMouseListener();
        ActionListener actionListener = new TrayIconListener();

        addToMenu("Stop"                     , new StartStopItem());
        addToMenu("Connect to other servers" , new OthersItem());
        addToMenu("Exit"                     , new ExitItem());
        addToMenu("Help"                     , new HelpItem());

        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(actionListener);
        trayIcon.addMouseListener(mouseListener);

        try {
            tray.add(trayIcon);
            out("TrayIcon added.");
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void addToMenu(String menuText, ActionListener listener) {
        MenuItem item = new MenuItem(menuText);
        popup.add(item);
        item.addActionListener(listener);
    }

    private static void out(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        TRAY.toString();
    }

}
