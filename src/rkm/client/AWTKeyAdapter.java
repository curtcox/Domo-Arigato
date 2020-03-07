package rkm.client;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * For only listening to Toolkit key events.
 * This feels like a hack, but every other way I've tried to
 * get key events is comparatively complex and brittle.
 */
class AWTKeyAdapter implements AWTEventListener {

    /**
     * Map from listeners to adapters, so our users can ignore the
     * fact that adapters are used under the covers.
     */
    private static final Map<KeyListener,AWTKeyAdapter> LISTENERS =
            new WeakHashMap<KeyListener,AWTKeyAdapter>();

    /**
     * The key listener
     */
    private final KeyListener listener;

    AWTKeyAdapter(KeyListener listener) {
        this.listener = listener;
    }

    /**
     * Pass the event on to our key listener.
     */
    public void eventDispatched(AWTEvent event){
        KeyEvent keyEvent = (KeyEvent)event;
        if (keyEvent.getID() == KeyEvent.KEY_RELEASED){
            listener.keyReleased(keyEvent);
        }
        if(keyEvent.getID() == KeyEvent.KEY_PRESSED){
            listener.keyPressed(keyEvent);
        }
        return;
    }

    /**
     * Hook this listener up to toolkit AWT events.
     */
    static void addKeyListener(KeyListener listener) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        AWTKeyAdapter adapter = new AWTKeyAdapter(listener);
        kit.addAWTEventListener(adapter, AWTEvent.KEY_EVENT_MASK);
        LISTENERS.put(listener, adapter);
    }

    /**
     * Unhook this listener from toolkit AWT events.
     */
    static void removeKeyListener(KeyListener listener) {
        AWTKeyAdapter adapter = LISTENERS.get(listener);
        Toolkit kit = Toolkit.getDefaultToolkit();
        kit.removeAWTEventListener(adapter);
    }

}