package rkm.server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import mite.RequestHandler;
import rkm.server.robots.KeyReleaseRobot;

/**
 * Reads requests from stream and executes them with the given robot.
 */
final class RobotRequestHandler
        implements RequestHandler
{

    public static RobotRequestHandler newInstance(KeyReleaseRobot robot) {
        return new RobotRequestHandler(robot);
    }

    /**
     * Execute methods on this
     */
    private final KeyReleaseRobot robot;

    private RobotRequestHandler(KeyReleaseRobot robot) {
        this.robot = robot;
    }

    public void handle(String request, Socket socket, InputStream in, OutputStream out)
            throws IOException
    {
        log("serving request from " + socket.getInetAddress());
        SingleRobotRequestHandler.handle(robot, socket, in, out);
    }

    public boolean handles(String request) {
        return request.contains("robot");
    }

    private static void log(String message) {
        System.out.println("RobotRequestHandler : " + message);
    }
}
