package rkm.security;

import java.awt.Component;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.Icon;
import javax.swing.JOptionPane;


/**
 * This encapsulates how the client identifies itself to the server.
 * This involves sending unencrypted credentials over the wire.
 * To eliminate this problem, use SSL.
 */
public final class RobotSecurity {

    public static void authenticateClient(InputStream in, OutputStream out)
            throws IOException
    {
        DataOutputStream dataOut = new DataOutputStream(out);
        DataInputStream   dataIn = new DataInputStream(in);
        authenticateClient(dataIn,dataOut);
    }

    /**
     * This is used by the client to identify it to the server.
     */
    public static void identifyToServer(InputStream in, OutputStream out)
            throws IOException
    {
        DataOutputStream dataOut = new DataOutputStream(out);
        DataInputStream   dataIn = new DataInputStream(in);
        identifyToServer(dataIn,dataOut);
    }

    /**
     * This is used by the server to authenticate a client.
     */
    private static void authenticateClient(DataInputStream in, DataOutputStream out)
            throws IOException
    {
        log("Reading ID from client");
        MachineID id = MachineID.readFrom(in);
        if (id.isAuthorized()) {
            String message = "Already authorized " + id;
            log(message);
        }
        if (userGrantsAuthorizationFor(id)) {
            String message = "Authorization just granted for " + id;
            log(message);
            return;
        }
        String message = "Not authorized for " + id;
        out.writeUTF(message);
        in.close();
        out.close();
        log(message);
    }


    private static void identifyToServer(DataInputStream in, DataOutputStream out)
            throws IOException
    {
        MachineID.LOCAL.writeTo(out);
        out.flush();
        log("ID sent to server");
    }

    /**
     * Ask the user to decide about authorization for the given machine.
     */
    private static boolean userGrantsAuthorizationFor(MachineID id) {
        //Custom button text
        String message = "Grant remote robot control to this machine?";
        String title   = "Remote Robot Request";
        String initialValue = "No";
        String[] options = {
                "Always for this requestor",
                "Just for now",
                initialValue };
        Component parent = null;
        Icon icon = null;
        int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        int choice = JOptionPane.showOptionDialog(
                parent,message,title,
                optionType, messageType,
                icon,
                options, initialValue);

        if (choice==0) {
            id.authorize();
            return true;
        }
        if (choice==1) {
            return true;
        }
        return false;
    }

    private static void log(String message) {
        System.out.println("RobotSecurity : " + message);
    }

    public static void main(String[] args) {
        userGrantsAuthorizationFor(MachineID.LOCAL);
    }
}
