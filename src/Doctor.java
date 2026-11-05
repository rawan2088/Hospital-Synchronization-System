
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

            Hospital.logBlock(
                "Doctor " + id + " is trying to acquire a treatment room."
            );

            if (treatmentRoom.tryAcquire(3000)) {

                try {

                    Hospital.logBlock(
                        "Doctor " + id + " acquired a treatment room.",
                        "Doctor " + id + " begins treating P" + p.get_id() + "."
                    );

                    Thread.sleep(rand.nextInt(3000) + 2000);

                    Hospital.log(
                        "Doctor " + id + " finished treating P" + p.get_id() + "."
                    );

                } finally {

                    treatmentRoom.release();

                    Hospital.log(
                        "Doctor " + id + " released the treatment room."
                    );

                }

                Hospital.logBlock(
                    "Doctor " + id + " is now available."
                );

                p.finishTreatment();

            } else {

                Hospital.logBlock(
                    "Doctor " + id + " could not acquire a treatment room.",
                    "Doctor " + id + " is sending P" + p.get_id()
                            + " back to the waiting room."
                );

                waitingRoom.addPatient(p, false);

                Hospital.logBlock(
                    "Doctor " + id + " is now available."
                );
            }
        }

    } catch (InterruptedException e) {

        Thread.currentThread().interrupt();

    }
}

    
}
