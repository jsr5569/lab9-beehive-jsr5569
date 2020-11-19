package world;

import bee.Drone;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * The queen's chamber is where the mating ritual between the queen and her
 * drones is conducted.  The drones will enter the chamber in order.
 * If the queen is ready and a drone is in here, the first drone will
 * be summoned and mate with the queen.  Otherwise the drone has to wait.
 * After a drone mates they perish, which is why there is no routine
 * for exiting (like with the worker bees and the flower field).
 *
 * @author Sean Strout @ RIT CS
 * @author Jarred Reepmeyer
 */
public class QueensChamber {

    /** the collection of all the drone bees who are waiting to mate */
    private ConcurrentLinkedQueue<Drone> matingLine = new ConcurrentLinkedQueue<>();
    /** the current number of drones in the chamber */
    private int numDrones;
    /** whether or not the Queen is ready to mate with a drone*/
    private boolean readyToMate;

    /**
     * Create the chamber. Initially there are no drones in the chamber and
     * the queen is not ready to mate.
     */
    public QueensChamber(){
        this.numDrones = 0;
        this.readyToMate = false;
    }

    /**
     * A drone enters the chamber. The first thing you should display is:
     *
     * *QC* {bee} enters chamber
     *
     * The bees should be stored in some queue like collection. If the queen
     * is ready and this drone is at the front of the collection, they are
     * allowed to mate. Otherwise they must wait. The queen isn't into any of
     * this kinky multiple partner stuff so while she is mating with a drone,
     * she is not ready to mate again. When the drone leaves this method,
     * display the message:
     *
     * *QC* {bee} leaves chamber
     * @param drone - the drone entering the QueensChamber
     */
    public synchronized void enterChamber(Drone drone){
        System.out.println("*QC* " + drone + " enters chamber");
        matingLine.add(drone);
        while (matingLine.peek()!=drone || !this.readyToMate) {//Have the drones wait to mate until they are first in line and the queen is ready
            try{
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        matingLine.poll();//Remove the drone that was first from the queue
        this.readyToMate = false;
        System.out.println("*QC* " + drone + " leaves chamber");
    }

    /**
     * When the queen is ready, they will summon the next drone from the
     * collection (if at least one is there). The queen will mate with the
     * first drone and display a message:
     *
     * *QC* Queen mates with {bee}
     *
     * It is the job of the queen if mating to notify all of the waiting
     * drones so that the first one can be selected since we can't control
     * which drone will unblock. Doing a notify will lead to deadlock if the
     * drone that unblocks is not the front one.
     */
    public synchronized void summonDrone(){
        this.readyToMate = true;
        if(hasDrone()){
            Drone matingDrone = matingLine.element();//The drone that is about to mate with the queen
            matingDrone.setMated();//Make the drone aware of the fact that he will have mated
            notifyAll();//Let the next drone in to mate with the queen
            System.out.println("*QC* Queen mates with " + matingDrone);
        }
    }

    /**
     * At the end of the simulation the queen uses this routine repeatedly to
     * dismiss all the drones that were waiting to mate. #rit_irl...
     */
    public synchronized void dismissDrone(){
            this.readyToMate = true;
            notifyAll();//Let all the bees move out of the loop
    }

    /**
     * Are there any waiting drones? The queen uses this to check if she can
     * mate, and also in conjunction with dismissDrone().
     * @return whether there are any drones in the queue
     */
    public boolean hasDrone(){
        if(matingLine == null || matingLine.isEmpty())
            return false;
        else
            return true;
    }

}