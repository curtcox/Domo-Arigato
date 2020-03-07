package mite;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This interface is used to define what a GenericHTTPServer does.
 */
public interface RequestHandler {

    /**
     * Return true if this handler handles this request.
     */
    boolean handles(String request);

    /**
     * Handle this request.
     * Note that this method may well be called again from a different
     * thread before it return.  It is the responsibility of the implementor to
     * ensure that that doesn't cause any problems.
     */
    void handle(String request, Socket socket, InputStream in, OutputStream out) throws IOException;

}
