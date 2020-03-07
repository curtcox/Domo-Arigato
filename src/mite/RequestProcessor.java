package mite;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

/**
 * Processes a simple HTTP request.
 * This class contains just enough logic to determine
 * who to hand the request to.
 */
final class RequestProcessor
        implements Runnable
{

    /**
     * Connection to the client
     */
    private final Socket connection;

    /**
     * For reading the request
     */
    private final InputStream in;

    /**
     * For writing the response
     */
    private final OutputStream out;


    /**
     * This will really handle the request.
     */
    private final RequestHandler handler;

    public RequestProcessor(Socket connection, RequestHandler handler) throws IOException {
        this.connection = connection;
        this.handler    = handler;
        out    = connection.getOutputStream();
        in     = connection.getInputStream();
    }

    public void run() {
        try {
            handleRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequest() throws IOException {
        final StringBuilder requestLine = new StringBuilder();
        while (true) {
            int c = in.read();
            if (c == '\r' || c == '\n') break;
            requestLine.append((char) c);
        }

        String request = requestLine.toString();
        log(request);
        if (handler.handles(request)) {
            handler.handle(request,connection,in,out);
            return;
        }
        throw new UnsupportedOperationException(request);
    }

    private static void log(String message) {
        System.out.println("RequestProcessor : " + message);
    }

}
