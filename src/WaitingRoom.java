
public class WaitingRoom {

    private final int capacity;
    private Patient[] spaces;

    private int in = 0, out = 0;

    private final Semaphore mutex;
    private final Semaphore full;
    private final Semaphore empty;

    WaitingRoom(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than zero");
        }

        this.capacity = capacity;
        this.spaces = new Patient[capacity];
        // the mutex is for the spaces array
        this.mutex = new Semaphore(1);
        this.full = new Semaphore(0);
        this.empty = new Semaphore(capacity);
    }

    void addPatient(Patient p) throws InterruptedException {

        empty.acquire();
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            empty.release();
            throw e;
        }

        try {
            spaces[in] = p;
            in = (in + 1) % capacity;
        } finally {
            mutex.release();
        }
        full.release();
    }

    Patient takePatient() throws InterruptedException {
        full.acquire();
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            full.release();
            throw e;
        }

        Patient p;

        // we always release the mutex in finally so that even with an error it releases
        try {
            p = spaces[out];
            spaces[out] = null;
            out = (out + 1) % capacity;
        } finally {
            mutex.release();
        }

        empty.release();
        return p;
    }
}
