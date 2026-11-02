import java.util.Random;

public class Doctor extends Thread {
    private WaitingRoom wroom;
    private TreatmentRooms troom;
    private int id;
    private Random rand = new Random();   
    public Doctor(int id,WaitingRoom wroom,TreatmentRooms troom) {
        this.wroom = wroom;
        this.troom=troom;
        this.id=id;
    }
    public void run() {
        try{
        while (true) { 
            //this will be fixed to while there is patient in the waiting room 
          Patient p = wroom.takePatient();
          System.out.println("Doctor " + id + " is assigned to " + p.getname());
          System.out.println("Doctor " + id + " is trying to acquire a treatment room.");
          if (troom.tryAcquire(3000)) {
            try{
            System.out.println("Doctor " + id + " acquired a treatment room.");
            System.out.println("Doctor " + id + " begins treating " + p.getname());
            Thread.sleep(rand.nextInt(3000) + 2000);
            System.out.println("Doctor " + id + " finished treating " + p.getname());
            System.out.println("Doctor " + id + " released the treatment room.");
            //   tell the patient it's done and record it as treated
          }
            catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            finally {
                troom.release();
            }
        }
          else {
            System.out.println("Doctor " + id + " could not acquire a treatment room.");
            System.out.println("Doctor "+ id + " is sending "+ p.getname() +" back to the waiting room");
            wroom.addPatient(p);
            System.out.println(p.getname() + " back in the waiting room.");
          }
           System.out.println("Doctor " + id + " is now available.");
        }
}
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




