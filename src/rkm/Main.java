package rkm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import rkm.client.RobotClient;
import rkm.server.RobotServer;

/**
 * This is the only normal external entry point.
 * It starts the client and server.
 */
public final class Main {

    public static void main(String[] args)
            throws Exception
    {
        start();
    }

    private static void start()
            throws IOException, InterruptedException, InvocationTargetException
    {
        startClient();
        if (!RobotServer.isRunning()) {
            RobotServer.start();
        }
    }

    private static void startClient()
            throws InterruptedException, InvocationTargetException
    {
        RobotClient.show();
    }
}
