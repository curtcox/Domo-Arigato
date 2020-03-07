package mite.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import mite.RequestHandler;

/**
 * Handler that defers to other handlers.
 */
public final class CompositeRequestHandler
        implements RequestHandler
{

    public static CompositeRequestHandler newInstance(
            RequestHandler... handlers) {
        return new CompositeRequestHandler(handlers);
    }

    private final RequestHandler[] handlers;

    private CompositeRequestHandler(RequestHandler... handlers) {
        this.handlers = handlers;
    }

    public void handle(String request, Socket socket, InputStream in, OutputStream out)
            throws IOException
    {
        for (RequestHandler handler : handlers) {
            if (handler.handles(request)) {
                handler.handle(request, socket, in, out);
                return;
            }
        }
    }

    public boolean handles(String request) {
        for (RequestHandler handler : handlers) {
            if (handler.handles(request)) {
                return true;
            }
        }
        return false;
    }

}
