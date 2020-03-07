package rkm.client;

import java.io.IOException;

import javax.jmdns.ServiceInfo;

import rkm.Check;
import rkm.FakeRobot;
import rkm.IRobot;
import rkm.RobotPrinter;

/**
 * For testing without real network connections.
 */
final class FakeRobotConnector
        implements RobotConnector
{

    public IRobot connectTo(String host, int port) throws IOException {
        Check.isNotEDT();
        if (host.contains(" ")) {
            throw new IOException("Bad machine name");
        }
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new RobotPrinter("Client",FakeRobot.newInstance());
    }

    public ServiceInfo[] listRobots() throws IOException {
        Check.isNotEDT();
        ServiceInfo[] services = new ServiceInfo[] {
                new FakeServiceInfo("Server 1"),
                new FakeServiceInfo("Server 2")
        };
        return services;
    }
}
