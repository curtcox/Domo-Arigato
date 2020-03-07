package rkm.server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.prefs.Preferences;

import mite.MiteHTTPServer;
import mite.RequestHandler;
import mite.handlers.CompositeRequestHandler;
import mite.handlers.EchoRequestHandler;
import mite.handlers.UnsupportedRequestHandler;
import rkm.FakeRobot;
import rkm.IRobot;
import rkm.RobotBroadcast;
import rkm.RobotMode;
import rkm.RobotPrinter;
import rkm.server.robots.KeyReleaseRobot;
import rkm.server.robots.OSXTransformer;

/**
 * Start a HTTP server that will allow this machine to be remotely
 * controlled by a RobotClient.
 */
public final class RobotServer {

    private static final String PORT = "port";

    /**
     * Where we store the machine id and authorizations.
     */
    private static final Preferences PREFS = Preferences.systemRoot().node(RobotServer.class.toString());

    static void start(IRobot robot) throws IOException {
        RequestHandler handler = CompositeRequestHandler.newInstance(
                RobotRequestHandler.newInstance(new KeyReleaseRobot(robot)),
                EchoRequestHandler.newInstance(),
                UnsupportedRequestHandler.newInstance());
        for (int i=0; i<10; i++) {
            try {
                int port = pickAPort();
                MiteHTTPServer webserver = new MiteHTTPServer(port,handler);
                webserver.start();
                setRecordedPort(port);
                RobotBroadcast.start(port);
                return;
            } catch (IOException e) {
                log(e.getMessage());
                // probably something is already on that port -- try a different port
            }
        }
    }

    /**
     * Return a random port number between 1024 and 64K.
     */
    private static int pickAPort() {
        Random random = new Random();
        int BASE = 1024;
        int MAX  = 64 * 1024;
        int port = random.nextInt(MAX - BASE);
        return port + BASE;
    }


    /**
     * Return true if a server is already running for this user/machine
     * and false otherwise.
     */
    public static boolean isRunning() {
        int port = getRecordedPort();
        log("Looking for server on port " + port);
        try {
            Socket socket = new Socket("127.0.0.1",port);
            // A timeout of 30 seconds should be plenty on the local machine.
            // The server should respond in a tiny fraction of a second,
            // but it might be completely paged out.
            int TIMEOUT = 30 * 1000;
            socket.setSoTimeout(TIMEOUT);
            InputStream   in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            String lookingFor = "lookingFor";
            String request = "GET /echo/" + lookingFor + " HTTP/1.1\r";
            out.write(request.getBytes());
            byte[] buffer = new byte[40];
            in.read(buffer);
            String response = new String(buffer);
            boolean found = response.contains(lookingFor);
            if (found) {
                log("Robot server found");
            } else {
                log("Wrong server type");
            }
            return found;
        } catch (IOException e) {
            log("Server not responding");
            return false;
        }
    }

    /**
     * What port was the last robot server running on?
     */
    private static int getRecordedPort() {
        return PREFS.getInt(PORT, 6502);
    }

    /**
     * Record the port we're running on.
     */
    private static void setRecordedPort(int port) {
        PREFS.putInt(PORT, port);
    }

    public static void start() throws IOException {
        if (RobotMode.NOW==RobotMode.NORMAL) {
            start(wrapForPlatform(RealRobot.newInstance()));
            return;
        }
        start(new RobotPrinter("Server", FakeRobot.newInstance()));
        RobotTray.init();
    }

    /**
     * See
     * http://mgrand.home.mindspring.com/java-system-properties.htm
     * http://lopica.sourceforge.net/os.html
     */
    private static IRobot wrapForPlatform(IRobot robot) {
        String name = System.getProperty("os.name").toLowerCase();
        if (name.contains("Mac")) {
            return OSXTransformer.newInstance(robot);
        }
        return robot;
    }

    public static void main(String[] args) throws IOException {
        start();
    }

    private static void log(String message) {
        System.out.println("RobotServer : " + message);
    }
}
