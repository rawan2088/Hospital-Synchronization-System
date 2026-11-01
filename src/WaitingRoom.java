
public class WaitingRoom {

    int capacity = 0;
    private Patient[] spaces;

    private int in = 0, out = 0;

    WaitingRoom(int capacity) {
        this.capacity = capacity;
        this.spaces = new Patient[capacity];

    }

    // Semaphore mutex = 1, full = 0, empty = capacity;
    // the mutex is for the spaces array
    void addPatient(Patient p) {
        empty.acquire();
        mutex.acquire();
        spaces[in] = p;
        in = (in + 1) % capacity;
        mutex.release();
        full.release();
    }

    Patient takePatient() {
        full.acquire();
        mutex.acquire();
        Patient p = spaces[out];
        spaces[out] = null;
        out = (out + 1) % capacity;
        mutex.release();
        empty.release();
        return p;
    }
}
