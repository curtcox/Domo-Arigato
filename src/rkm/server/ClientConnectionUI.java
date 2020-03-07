package rkm.server;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import javax.swing.JFrame;

import rkm.Check;
import rkm.RobotImage;
import rkm.threads.EDTProxy;

/**
 * The server-side UI that corresponds to a single client connection.
 */
final class ClientConnectionUI {

    private final JFrame frame = new JFrame();

    private final Closeable closeable;

    private final WindowListener windowListener = new WindowListener() {

        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowOpened(WindowEvent e) {}
        public void windowClosed(WindowEvent event) {}
        public void windowClosing(WindowEvent event) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * For stuff we must switch back to the EDT for.
     */
    private final IEDT     EDT = (IEDT) EDTProxy.newInstance(new EDTMethods(this),IEDT.class);

    /**
     * "java.net socket classes should implement java.io.Closeable"
     * http://bugs.sun.com/view_bug.do?bug_id=6499348
     */
    static ClientConnectionUI newInstance(final Socket socket) {
        Closeable closeable = new Closeable() {
            public void close() throws IOException {
                socket.close();
            }
        };
        String title = socket.getInetAddress().getHostName();
        return newInstance(closeable,title);
    }

    static ClientConnectionUI newInstance(final Closeable closeable, final String title) {
        Callable<ClientConnectionUI> callable = new Callable<ClientConnectionUI>() {
            public ClientConnectionUI call() throws Exception {
                return new ClientConnectionUI(closeable,title);
            }
        };
        RunnableFuture<ClientConnectionUI> future = new FutureTask<ClientConnectionUI>(callable);
        EventQueue.invokeLater(future);
        ClientConnectionUI ui;
        try {
            ui = future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        ui.EDT.show();
        return ui;
    }

    ClientConnectionUI(Closeable closeable, String title) {
        Check.isEDT();
        this.closeable = closeable;
        frame.setTitle(title);
        frame.setIconImages(RobotImage.IMAGES);
        frame.addWindowListener(windowListener);
    }

    public void notifyIOException(IOException e) {
        EDT.setTitle("Closed");
    }

    public interface IEDT {
        void show();
        void setTitle(String title);
    }

    private class EDTMethods implements IEDT {
        private ClientConnectionUI ui;

        EDTMethods(ClientConnectionUI ui) {
            this.ui = ui;
        }

        public void show() {
            frame.setSize(200,50);
            frame.setVisible(true);
        }

        public void setTitle(String title) {
            frame.setTitle(title);
        }
    }

    public static void main(String[] args) {
        Closeable closeable = new Closeable() {
            public void close()  {
                System.out.println("Closed");
                System.exit(0);
            }
        };
        String title = "Client";
        newInstance(closeable,title);
    }
}
