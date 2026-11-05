
import java.util.Random;

public class Doctor extends Thread {

    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRoom;
    private final int id;
    private final Random rand = new Random();

    public Doctor(int id, WaitingRoom wroom, TreatmentRooms troom) {
        this.waitingRoom = wroom;
        this.treatmentRoom = troom;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Patient p = waitingRoom.takePatient(id);
                Hospital.log("Doctor " + id + " is trying to acquire a treatment room.");

                // boolean treated = false;
                if (treatmentRoom.tryAcquire(3000)) {
                    try {
                        Hospital.log("Doctor " + id + " acquired a treatment room.");
                        //calling wRoom's function to release seat in waiting room after doctor acquires a room
                        Hospital.log("Doctor " + id + " begins treating P" + p.get_id());
                        Thread.sleep(rand.nextInt(3000) + 2000);
                        // Thread.sleep(6000);
                        Hospital.log("Doctor " + id + " finished treating P" + p.get_id());
                        // waitingRoom.releaseSeat();
                    } finally {
                        Hospital.log("Doctor " + id + " released the treatment room.");
                        treatmentRoom.release();
                    }
                    Hospital.log("Doctor " + id + " is now available.");
                    p.finishTreatment();
                } else {
                    Hospital.log("Doctor " + id + " could not acquire a treatment room.");
                    Hospital.log("Doctor " + id + " is sending P" + p.get_id() + " back to the waiting room.");
                    waitingRoom.addPatient(p);
                    Hospital.log("Doctor " + id + " is now available.");
                    // waitingRoom.releaseSeat();
                }
            }

        } catch (InterruptedException e) {
            // hospital intterrupted the doctor thread. 
            Thread.currentThread().interrupt();
        }
    }
}
