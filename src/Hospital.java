import java.util.Scanner;

public class Hospital {

    Doctor[] create_doctors(int docs, WaitingRoom wRoom, TreatmentRooms tRoom) {
        Doctor[] doctors = new Doctor[docs];
        for (int i = 0; i < docs; i++) {
            doctors[i] = new Doctor(i + 1, wRoom, tRoom);
            doctors[i].start();
        }
        return doctors;
    }

    Patient[] create_patients(int pats, WaitingRoom wRoom) throws InterruptedException {
        log("Patients arriving: ");
        Patient[] patients = new Patient[pats];
        for (int i = 0; i < pats; i++) {
            patients[i] = new Patient(i + 1, wRoom);
            patients[i].start();
        }
        return patients;
    }

    static synchronized void log(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter waiting room capacity: ");
        int cap = scanner.nextInt();
        System.out.print("Enter Number of doctors: ");
        int docs = scanner.nextInt();
        System.out.print("Enter Number of patients: ");
        int pats = scanner.nextInt();
        scanner.close();

        if (cap <= 0 || docs <= 0 || pats < 0) {
            throw new IllegalArgumentException("Invalid Input");
        }

        Hospital hospital = new Hospital();

        WaitingRoom wRoom = new WaitingRoom(cap);
        TreatmentRooms tRoom = new TreatmentRooms();

        Doctor[] doctors = hospital.create_doctors(docs, wRoom, tRoom);

        Patient[] patients = hospital.create_patients(pats, wRoom);


        for (Patient p : patients) {
            p.join();
        }

        for (Doctor d : doctors) {
            d.interrupt();
        }

        for (Doctor d : doctors) {
            d.join();
        }
        Hospital.log("All patients have been treated.");
        Hospital.log("Simulation completed.");

    }
}
