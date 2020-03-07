package rkm.client;

import java.lang.reflect.InvocationTargetException;

import rkm.RobotMode;

/**
 * Launches a robot GUI client.
 * The client can be used to control a machine running a robot server.
 */
public final class RobotClient {

    public static void show()
            throws InterruptedException, InvocationTargetException
    {
        RobotConnector connector = new SocketRobotConnector();
        ServerSelectionFrame.show(connector);
    }

    public static void main(String[] args) throws Exception {
        show();
    }
}
