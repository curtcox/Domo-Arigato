package rkm;

import java.awt.EventQueue;

public final class Check {

    public static void notNull(Object o) {
        if (o==null) {
            throw new NullPointerException();
        }
    }

    public static void isEDT() {
        if (!EventQueue.isDispatchThread()) {
            throw new IllegalStateException("Must do on EDT");
        }
    }

    public static void isNotEDT() {
        if (EventQueue.isDispatchThread()) {
            throw new IllegalStateException("Must not do on EDT");
        }
    }

}
