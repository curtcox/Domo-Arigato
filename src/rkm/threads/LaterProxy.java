package rkm.threads;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Creates proxies for invoking all of the methods of a given interface on their own thread.
 */
public final class LaterProxy {

    /**
     * Internal handler for when the default one is desired.
     * Don't actually use this.
     */
    private static final UncaughtExceptionHandler DEFAULT = new UncaughtExceptionHandler() {
        public void uncaughtException(Thread t, Throwable e) {
            // there is a bug in this class if the following line ever executes
            e.printStackTrace();
        }
    };

    public static Object newInstance(Object o, Class face) {
        return newInstance(o,face,DEFAULT);
    }

    public static Object newInstance(
            Object o, Class face, UncaughtExceptionHandler onError)
    {
        ClassLoader loader = face.getClassLoader();
        Class[] interfaces = {face};
        InvocationHandler handler = new LaterInvocationHandler(o,onError) ;
        Object proxy = Proxy.newProxyInstance(loader, interfaces, handler);
        return proxy;
    }

    private static final class LaterInvocationHandler
            implements InvocationHandler
    {
        private final Object target;

        private final UncaughtExceptionHandler onError;

        LaterInvocationHandler(Object target, UncaughtExceptionHandler onError) {
            this.target = target;
            this.onError = onError;
        }

        public Object invoke(Object proxy, final Method method, final Object[] args)
                throws Throwable
        {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        method.invoke(target, args);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            if (onError!=DEFAULT) {
                thread.setUncaughtExceptionHandler(onError);
            }
            thread.start();
            return null;
        }
    }

}
