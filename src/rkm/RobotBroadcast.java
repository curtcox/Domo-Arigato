package rkm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;


/**
 * Advertises the availability of the robot.
 * This is our interface with JmDNS.
 */
public final class RobotBroadcast {

    /**
     * fully qualified service type name, such as _http._tcp.local.
     */
    private static final String ROBOT_TYPE  = "_robot._tcp.local.";

    /**
     * JmDNS instance
     */
    private static final JmDNS jmdns;

    static {
        try {
            jmdns = JmDNS.create();
            registerShutdownHook();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                jmdns.close();
            }
        });
    }

    public static ServiceInfo[] listRobots() throws IOException {
        Check.isNotEDT();
        log("finding robots");
        ServiceInfo[] services = jmdns.list(ROBOT_TYPE);
        return services;
    }

    public static void start(int port) throws IOException {
        String type  = ROBOT_TYPE;              // fully qualified service type name, such as _http._tcp.local.
        String name  = "robot";                 //unqualified service instance name, such as foobar
        int weight   = 0;                       //weight of the service
        int priority = 0;                       //priority of the service
        String text  = "Robot on" + hostName(); //string describing the service
        ServiceInfo info = ServiceInfo.create(type, name , port, weight, priority, text);
        log("Registered " + info);
        jmdns.registerService(info);
        log("Registered " + info);
    }

    private static String hostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    private static void log(String message) {
        System.out.println("RobotBroadcast : " + message);
    }
}