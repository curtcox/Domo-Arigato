package rkm.client;

/**
 * A delay that increases exponentially up to a set maximum.
 */
final class BackoffDelay {

    private int delay = 1000;

    private int max = 5 * 60 * 1000;

    public void next() {
        delay *= 2;
        if (delay > max) {
            delay = max;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
