package Threads;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ControllerThread implements Runnable{

    private final Queue<Thread> threadQueue;

    public ControllerThread(){
        this.threadQueue = new ConcurrentLinkedQueue<>();
    }

    public void addThread(final Thread t){
        this.threadQueue.add(t);
        t.start();
    }

    @Override
    public void run() {
        while(!threadQueue.isEmpty()){
            try {
                this.threadQueue.poll().join();
                //threadMonitor.putThreadInList();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopThreads(){
        for (Thread t: threadQueue) {
            threadQueue.remove(t);

            if (t.isAlive() && !t.isInterrupted())
                t.interrupt();
        }
        Thread.currentThread().interrupt();
    }
}
