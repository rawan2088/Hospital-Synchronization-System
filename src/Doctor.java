
import java.util.Random;

public class Doctor extends Thread {
    private WaitingRoom waitingRoom;
    private TreatmentRooms treatmentRoom;
    private int id;
    private Random rand = new Random();

    public Doctor(int id, WaitingRoom wroom, TreatmentRooms troom) {
        this.waitingRoom = wroom;
        this.treatmentRoom = troom;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Patient p = waitingRoom.takePatient();

                Hospital.log("Doctor " + id + " is assigned to P" + p.get_id());
                Hospital.log("Doctor " + id + " is trying to acquire a treatment room.");
                if (treatmentRoom.tryAcquire(3000)) {
                    try {
                        Hospital.log("Doctor " + id + " acquired a treatment room.");
                        Hospital.log("Doctor " + id + " begins treating P" + p.get_id());

                        Thread.sleep(rand.nextInt(1000) + 3000);
                        Hospital.log("Doctor " + id + " finished treating P" + p.get_id());

                    } finally {
        
                        Hospital.log("Doctor " + id + " released the treatment room.");
                        treatmentRoom.release();
                    }
                    p.finishTreatment();

                } else {
                    Hospital.log("Doctor " + id + " could not acquire a treatment room.");
                    Hospital.log("Doctor " + id + " is sending P" + p.get_id() + " back to the waiting room.");

                    waitingRoom.addPatient(p);
                    Hospital.log("P" + p.get_id() + " is back in the waiting room.");
                }
                Hospital.log("Doctor " + id + " is now available.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
