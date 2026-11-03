import java.util.Scanner;

public class Hospital {

    void create_doctors(int docs, WaitingRoom wRoom, TreatmentRooms tRoom){
        for(int i = 0; i < docs; i++){
            Doctor d = new Doctor(i+1, wRoom, tRoom);
            d.start();
        }
    }

    void create_patients(int pats, WaitingRoom wRoom) throws InterruptedException {
        for(int i = 0; i < pats; i++){
            Patient p = new Patient(i+1, wRoom);
            p.start();
        }
    }

    static synchronized void log(String s){
        System.out.println(s);
    }



    void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter waiting room capacity: ");
        int cap = scanner.nextInt();
        System.out.print("Enter Number of doctors: ");
        int docs = scanner.nextInt();
        System.out.print("Enter Number of patients: ");
        int pats = scanner.nextInt();
        scanner.close();

        Hospital hospital = new Hospital();

        WaitingRoom wRoom = new WaitingRoom(cap);
        TreatmentRooms tRoom = new TreatmentRooms();

        hospital.create_doctors(docs, wRoom, tRoom);
        hospital.create_patients(pats, wRoom);

    }
}

