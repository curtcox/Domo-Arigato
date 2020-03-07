package rkm.client;

import java.awt.Component;

/**
 * A frame that can be told it has been disconnect.
 */
interface DisconnectableFrame {

    /**
     * Tell the frame it has been disconnected.
     */
    void disconnect();

    /**
     * Get the frame's glass pane.
     * This is used for adding and removing listeners.
     */
    Component getGlassPane();

}
