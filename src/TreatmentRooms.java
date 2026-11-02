public class TreatmentRooms {

    private final Semaphore treatmentRooms;

    TreatmentRooms()
    {
        treatmentRooms= new Semaphore(2);
    }

    public boolean tryAcquire(long timeInMills) throws InterruptedException
    {
        return treatmentRooms.tryAcquire(timeInMills);
    }

    public void release()
    {
        treatmentRooms.release();
    }
}
