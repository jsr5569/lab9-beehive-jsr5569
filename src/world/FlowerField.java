package world;

import bee.Worker;

/**
 * The field of flowers that are ripe for the worker bees to gather the nectar
 * and pollen resources.  The bees can arrive in any order and they are
 * immediately allowed to start gathering, as long as there is a free flower.
 * Otherwise the bee must wait until a flower becomes free.
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */
public class FlowerField {
    /** the maximum number of workers allowed in the field at the same time */
    public final static int MAX_WORKERS = 10;

    /** the current number of workers in the field */
    private int numWorkers;

    /**
     * Create the flower field.  Initially there are no worker bees in the field.
     */
    public FlowerField() {
        this.numWorkers = 0;
    }

    /**
     * When a worker bee requests entry in to the field, you should first display a message:<br>
     * <br>
     * <tt>*FF* {bee} enters field</tt><br>
     * <br>
     * There is only one condition that would cause a bee to have to wait - if there
     * are no flowers because all the other bees are gathering from them.  In this
     * case they have to wait until a bee exits the field to see if they can go next.
     * There is no control over the order the bees will follow.
     *
     * @param w the worker bee entering the field
     */
    public synchronized void enterField(Worker w){
        System.out.println("*FF* " + w + " enters field");
        while (this.numWorkers == MAX_WORKERS){//Have the workers wait until there is an open space in the field to collect their resource
            try{
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        numWorkers++;//Increase the number of workers in the field when a worker enters the field
    }


    /**
     * When a worker bee is done gathering from a flower, it uses this routine to indicate
     * they are leaving, and to notify a single bees that may be waiting that they should
     * wake up and check that there is indeed a free flower now.  At the end of this
     * routine you should print the message:<br>
     * <br>
     * <tt>*FF* {bee} leaves field</tt><br>
     * <br>
     *
     * @param w the worker bee leaving the field
     */
    public synchronized void exitField(Worker w){
        this.numWorkers--;
        notify();//Let the other worker bees know there is an opening in the field
        System.out.println("*FF* " + w + " leaves field");
    }

}
