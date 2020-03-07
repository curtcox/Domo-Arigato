package rkm.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.jmdns.ServiceInfo;

import rkm.Check;
import rkm.IRobot;
import rkm.RobotBroadcast;
import rkm.RobotMode;
import rkm.RobotPrinter;
import rkm.security.RobotSecurity;

/**
 * Connects to Live RobotServerS.
 * Compare with FakeRobotConnector.
 */
final class SocketRobotConnector
        implements RobotConnector
{

    SocketRobotConnector() {}

    public IRobot connectTo(String host, int port) throws IOException {
        Check.isNotEDT();
        if (host.contains(" ")) {
            throw new IOException("Bad machine name");
        }
        log("connecting to " + host + ":" + port);
        Socket socket = new Socket(host,port);
        InputStream   in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        String string = "GET /robot HTTP/1.1\r";
        out.write(string.getBytes());
        log("connected to " + host + ":" + port);
        RobotSecurity.identifyToServer(in,out);
        IRobot robot = new RobotRequestSender(in,out);
        if (RobotMode.NOW==RobotMode.TEST || RobotMode.NOW==RobotMode.DEBUG) {
            robot = new RobotPrinter("Client ",robot);
        }
        return robot;
    }

    public ServiceInfo[] listRobots() throws IOException {
        return RobotBroadcast.listRobots();
    }

    private static void log(String message) {
        System.out.println("SocketRobotConnector " + message);
    }
}
