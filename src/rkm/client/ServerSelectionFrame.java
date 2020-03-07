package rkm.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.ServiceInfo;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import rkm.Check;
import rkm.RobotImage;
import rkm.threads.EDTProxy;
import rkm.threads.LaterProxy;

/**
 * A frame that lists servers to connect to.
 */
public final class ServerSelectionFrame
        extends JFrame
{
    /**
     * How we connect to the remote robot.
     */
    private final RobotConnector connector;

    /**
     * To organize the GUI
     */
    private final JPanel     panel   = new JPanel();

    /**
     * List of machines to connect to
     */
    private final JList         list = new JList();

    /**
     * Hold the list of potential servers.
     */
    private final DefaultListModel listModel = new DefaultListModel();

    /**
     * What we show the user -> what we need to connect
     */
    private final Map<String,ServiceInfo> names = new HashMap<String,ServiceInfo>();

    /**
     * Status
     */
    private final JLabel     status   = new JLabel("Scanning...");

    private final CardPanel cards = new CardPanel();

    /**
     * For stuff we must switch back to the EDT for.
     */
    private final IEDT     EDT = (IEDT) EDTProxy.newInstance(new EDTMethods(),IEDT.class);

    /**
     * For stuff we must switch away from the EDT for.
     */
    private final ILater LATER = (ILater) LaterProxy.newInstance(new LaterMethods(),ILater.class);

    private static ServerSelectionFrame FRAME;

    private static final long serialVersionUID = -4949042734847796561L;

    static {
        AlphaFrame.init();
    }

    private ServerSelectionFrame(RobotConnector connector) {
        Check.isEDT();
        this.connector = connector;
        layoutFrame();
        wireEvents();
        LATER.discoverHosts();
    }

    private void layoutFrame() {
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(list),BorderLayout.CENTER);
        panel.add(status,BorderLayout.SOUTH);
        cards.add(panel);
        cards.show(panel);
        add(cards);
    }

    private void wireEvents() {
        list.setModel(listModel);
        ListSelectionModel selectionModel = list.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new HostListListener());
    }

    /**
     * Listens to the host list to connect when one is selected.
     */
    private class HostListListener
            implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event) {
            log("event=" + event);
            // ignore adjusting events
            if (event.getValueIsAdjusting()) {
                return;
            }
            ListSelectionModel lsm = (ListSelectionModel)event.getSource();

            if (lsm.isSelectionEmpty()) {
                log("No host selected empty");
                return;
            }
            // Find out which index is selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            System.out.println("min=" + minIndex);
            System.out.println("max=" + maxIndex);
            if (minIndex!=maxIndex) {
                return;
            }
            int index = minIndex;
            ListModel model = list.getModel();
            String nice = (String) model.getElementAt(index);
            lsm.clearSelection();
            connectTo(nice);
        }
    }

    /**
     * Connect to the given server.
     */
    private void connectTo(String server) {
        Check.isEDT();
        ServiceInfo info = names.get(server);
        KeyMouseEventFrame.connectTo(connector,info);
    }

    /**
     * Stuff that needs to be done later -- not on the EDT.
     */
    public interface ILater {
        void discoverHosts();
        void discoverHostsLater();
    }

    class LaterMethods implements ILater {

        /**
         * Look for hosts to connect to.
         */
        public void discoverHosts() {
            try {
                final ServiceInfo[] services = connector.listRobots();
                EDT.addHosts(services);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private final BackoffDelay delay = new BackoffDelay();

        /**
         * Try to find more hosts after waiting a bit.
         */
        public void discoverHostsLater() {
            delay.next();
            discoverHosts();
        }

    }

    /**
     * Stuff that needs to be done on the EDT.
     */
    public interface IEDT {
        void setStatus(String message);
        void addHosts(ServiceInfo[] services);
    }

    private class EDTMethods implements IEDT {

        public void setStatus(String message) {
            Check.isEDT();
            setTitle(message);
            status.setText(message);
        }


        /**
         * Add the hosts we found to the list of those we can connect to.
         */
        public void addHosts(ServiceInfo[] services) {
            Check.isEDT();
            for (ServiceInfo info : services) {
                String name = info.getServer();
                String suffix = ".local.";
                if (name.endsWith(suffix)) {
                    name = name.substring(0,name.length() - suffix.length());
                }
                if (!names.containsKey(name)) {
                    listModel.addElement(name);
                    names.put(name, info);
                }
            }
            validate();
            LATER.discoverHostsLater();
        }

    }

    public static void main(String[] args) throws Exception {
        show(new FakeRobotConnector());
    }

    static ServerSelectionFrame show(final RobotConnector connector)
            throws InterruptedException, InvocationTargetException
    {
        EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    FRAME = show0(connector);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return FRAME;
    }

    public static ServerSelectionFrame get() {
        return FRAME;
    }

    private static ServerSelectionFrame show0(RobotConnector connector) {
        ServerSelectionFrame frame = new ServerSelectionFrame(connector);
        frame.setTitle("Remote Robot");
        frame.setIconImages(RobotImage.IMAGES);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(200,200);
        frame.setVisible(true);
        return frame;
    }

    private static void log(String message) {
        System.out.println("ServerSelectionForm : " + message);
    }
}
