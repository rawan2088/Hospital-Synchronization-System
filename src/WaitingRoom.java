
public class WaitingRoom {

    private final int capacity;
    private final Patient[] spaces;

    private int in = 0, out = 0;

    private final Semaphore mutex;
    private final Semaphore full;
    private final Semaphore empty;

    private final Object entryLock = new Object();

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
        //synchronized block to avoid printing wRoom is full before another patient tries to print entered
        synchronized(entryLock){
            if (!empty.tryAcquire(0)) {
                Hospital.log("The waiting room is full.");
                Hospital.log("P" + p.get_id()
                        + " is waiting outside for an available seat.");

                empty.acquire();
            }
            Hospital.log("P" + p.get_id() + " entered the waiting room.");
        }

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

    Patient takePatient(int doctorId) throws InterruptedException {
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
            // moved this to wRoom to avoid p2 printing "p2 entered wRoom" before doctor is assigned to p1 and is taken out of the wRoom
            Hospital.log("Doctor " + doctorId + " is assigned to P" + p.get_id() + ".");
        } finally {
            mutex.release();
        }

        return p;
    }

    // moved this statement to a separate function to be called by doctor after acquiring a room
    void releaseSeat(){
        empty.release();
    }
}
