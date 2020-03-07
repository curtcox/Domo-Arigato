package rkm.security;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.prefs.Preferences;

/**
 * This is a persistent identifier for a user machine pair.
 * This maps to a person on a machine.
 */
final class MachineID {

    /**
     * The bytes that uniquely identify this machine.
     */
    private final byte[] bytes;

    /**
     * How many random bytes there are in an id
     */
    private static final int SIZE = 20;

    /**
     * The key that we use to store the ID for this machine / user id.
     */
    private static final String KEY = "ID";

    /**
     * Where we store the machine id and authorizations.
     */
    private static final Preferences PREFS = Preferences.systemRoot().node(MachineID.class.toString());


    /**
     * The machine id for this machine / user id we are running under.
     */
    public static final MachineID LOCAL = getLocalMachine();

    /**
     * Create a new ID using these bytes.
     * @param bytes
     */
    private MachineID(byte[] bytes) {
        if (bytes.length!=SIZE) {
            String message = "Expected " + SIZE + " but found " + bytes.length;
            throw new IllegalArgumentException(message);
        }
        this.bytes = bytes;
    }

    /**
     * Return the bytes for this machine.
     * This is called when the class is loaded.
     */
    private static MachineID getLocalMachine() {
        byte[] bytes = null;
        bytes = PREFS.getByteArray(KEY, bytes);
        if (bytes==null || bytes.length!=SIZE) {
            bytes = getRandomBytes();
            PREFS.putByteArray(KEY, bytes);
        }
        return new MachineID(bytes);
    }

    /**
     * Pick some bytes at random.
     * @return
     */
    private static byte[] getRandomBytes() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[SIZE];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Read an ID from the given stream.
     */
    static MachineID readFrom(DataInputStream in)
            throws IOException
    {
        int size = in.readInt();
        byte[] bytes = new byte[size];
        in.read(bytes);
        return new MachineID(bytes);
    }

    /**
     * Write this id to the given stream.
     */
    void writeTo(DataOutputStream out)
            throws IOException
    {
        out.writeInt(SIZE);
        out.write(bytes);
    }

    /**
     * Return true if this id is authorized.
     */
    boolean isAuthorized() {
        return PREFS.getBoolean(Hex.fromBytes(bytes), false);
    }

    /**
     * Authorize this id from now on.
     */
    void authorize() {
        PREFS.putBoolean(Hex.fromBytes(bytes), true);
    }

    public String toString() {
        return "<Machine>" + Hex.fromBytes(bytes) + "</Machine>";
    }

    public static void main(String[] args) {
        System.out.println("" + LOCAL);
    }

}
