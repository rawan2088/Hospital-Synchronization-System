
public class Semaphore {

    private int tickets;

    public Semaphore(int initial) {
        tickets = initial;
    }

// this is without timeout, so it will wait until a ticket is available
    public synchronized void acquire() throws InterruptedException {
        while (tickets == 0) {
            wait();
        }

        tickets--;
    }

// realease dose not need timeout, so it will just increase the number of tickets and notify any waiting threads
    public synchronized void release() {
        tickets++;

        notify();
    }

// but this is with timeout, so it will wait until a ticket is available or the timeout expires
    public synchronized boolean tryAcquire(long timeout)
            throws InterruptedException {

        long endTime = System.currentTimeMillis() + timeout;

        while (tickets == 0) {

            long remainingTime = endTime - System.currentTimeMillis();

            if (remainingTime <= 0) {
                return false;
            }

            wait(remainingTime);
        }

        tickets--;

        return true;
    }
}
