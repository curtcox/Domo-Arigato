package rkm.client;

import java.io.IOException;

import javax.jmdns.ServiceInfo;

import rkm.IRobot;

/**
 * Used to produce connections.
 * This interface allows the GUI to be tested with a FakeRobotConnector
 */
interface RobotConnector {

    IRobot connectTo(String host, int port) throws IOException;

    ServiceInfo[] listRobots() throws IOException;
}
