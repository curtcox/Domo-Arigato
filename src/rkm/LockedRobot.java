package rkm;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LockedRobot implements IRobot {

    private final Lock lock = new ReentrantLock();

    private final IRobot robot;

    public LockedRobot(IRobot robot) {
        this.robot = robot;
    }

    private void lock() throws InterruptedException {
        lock.tryLock(10, TimeUnit.SECONDS);
    }

    private void unlock() {
        lock.unlock();
    }

    public BufferedImage createScreenCapture(Rectangle screenRect) {
        try {
            lock();
            return robot.createScreenCapture(screenRect);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    public void keyPress(int keycode) {
        try {
            lock();
            robot.keyPress(keycode);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    public void keyRelease(int keycode) {
        try {
            lock();
            robot.keyRelease(keycode);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    public void mouseMove(int x, int y) {
        try {
            lock();
            robot.mouseMove(x,y);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    public void mousePress(int buttons) {
        try {
            lock();
            robot.mousePress(buttons);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    public void mouseRelease(int buttons) {
        try {
            lock();
            robot.mouseRelease(buttons);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    public void mouseWheel(int wheelAmt) {
        try {
            lock();
            robot.mouseWheel(wheelAmt);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

}
