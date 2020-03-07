/**
 *
 */
package rkm.client;

import java.net.InetAddress;
import java.util.Enumeration;

import javax.jmdns.ServiceInfo;

final class FakeServiceInfo
        extends ServiceInfo
{

    private final String server;

    FakeServiceInfo(String server) {
        this.server = server;
    }

    @Override
    public InetAddress getAddress() {
        return null;
    }

    @Override
    public String getHostAddress() {
        return "hostAddress";
    }

    @Override
    public InetAddress getInetAddress() {
        return null;
    }

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public String getNiceTextString() {
        return "Nice Text String";
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public byte[] getPropertyBytes(String name) {
        return null;
    }

    @Override
    public Enumeration<?> getPropertyNames() {
        return null;
    }

    @Override
    public String getPropertyString(String name) {
        return null;
    }

    @Override
    public String getQualifiedName() {
        return null;
    }

    @Override
    public String getServer() {
        return server;
    }

    @Override
    public byte[] getTextBytes() {
        return null;
    }

    @Override
    public String getTextString() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public String getURL(String protocol) {
        return null;
    }

    @Override
    public int getWeight() {
        return 0;
    }

}