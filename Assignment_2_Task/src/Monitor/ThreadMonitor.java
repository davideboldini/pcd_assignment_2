package Monitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadMonitor {

    private int numThread;
    private boolean isAvailable;

    public ThreadMonitor(){
        this.numThread = Runtime.getRuntime().availableProcessors() + 1;
        //this.numThread = 800;
        this.isAvailable = true;
    }

    public synchronized Thread getThread(){
        while(numThread == 0 || !isAvailable){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.isAvailable = false;
        numThread--;
        this.isAvailable = true;
        notify();
        return new Thread();
    }

    public synchronized void putThreadInList(){
        this.isAvailable = false;
        numThread++;
        this.isAvailable = true;
        notifyAll();
    }

    public synchronized int getNumThread() {
        return this.numThread;
    }


    public synchronized LinkedList<Thread> getThreadsFile(){
        while(numThread == 0 || !this.isAvailable){
            try {
                wait();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        this.isAvailable = false;
        LinkedList<Thread> threadResultList = new LinkedList<>();
        while (numThread > 0) {
            numThread--;
            threadResultList.add(new Thread());
        }
        this.isAvailable = true;
        notifyAll();
        return threadResultList;
    }

}
