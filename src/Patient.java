public class Patient extends Thread {
    private final int id;
    private final WaitingRoom waitingRoom;
    private final Semaphore treated = new Semaphore(0);

    Patient(int id, WaitingRoom waitingRoom){
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public int get_id(){
        return this.id;
    }

    public void arrive(){
        Hospital.log("P" + id + " arrived");
        try {
            waitingRoom.addPatient(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void finishTreatment(){
        treated.release();
    }

    public void leave(){
        Hospital.log("P" + id + " left");
    }

    @Override
    public void run() {
        arrive();
        try {
            treated.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leave();

    }

}
