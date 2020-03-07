package mite;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import mite.handlers.CompositeRequestHandler;
import mite.handlers.EchoRequestHandler;
import mite.handlers.UnsupportedRequestHandler;

/**
 * Opens a server socket and hands off any requests to another thread.
 */
public final class MiteHTTPServer extends Thread {

    /**
     * The socket we listen on.
     */
    private final ServerSocket server;

    /**
     * How we handle requests.
     */
    private final RequestHandler handler;

    /**
     * Threads we use for requests
     */
    private final Executor executor = Executors.newFixedThreadPool(3);

    static final String NAME = "MiteHTTPServer 0.1";

    public MiteHTTPServer(int port, RequestHandler handler)
            throws IOException
    {
        this.server = new ServerSocket(port);
        this.handler = handler;
    }

    @Override
    public void run() {
        System.out.println("Accepting connections on port " + server.getLocalPort(  ));
        while (true) {
            try {
                Socket socket = server.accept();
                executor.execute(new RequestProcessor(socket,handler));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // set the port to listen on
        int port = 80;
        RequestHandler handler = CompositeRequestHandler.newInstance(EchoRequestHandler.newInstance(), UnsupportedRequestHandler.newInstance());
        MiteHTTPServer webserver = new MiteHTTPServer(port,handler);
        webserver.start();
    }

}
