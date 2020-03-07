package rkm.threads;

import java.awt.EventQueue;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Creates proxies for invoking all of the methods of a given interface
 * on the EDT.
 */
public final class EDTProxy {

    public static Object newInstance(Object o, Class face) {
        ClassLoader loader = face.getClassLoader();
        Class[] interfaces = {face};
        InvocationHandler handler = new EDTInvocationHandler(o) ;
        Object proxy = Proxy.newProxyInstance(loader, interfaces, handler);
        return proxy;
    }

    private static final class EDTInvocationHandler
            implements InvocationHandler
    {
        private final Object target;

        EDTInvocationHandler(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, final Method method, final Object[] args)
                throws Throwable
        {
            EventQueue.invokeLater(
                    new Runnable() {
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
                    }
            );
            return null;
        }
    }

}
