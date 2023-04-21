package Threads;

import Monitor.ThreadMonitor;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ControllerThread implements Runnable{

    private final Queue<Thread> threadQueue;
    private final ThreadMonitor threadMonitor;

    public ControllerThread(final ThreadMonitor threadMonitor){
        this.threadQueue = new ConcurrentLinkedQueue<>();
        this.threadMonitor = threadMonitor;
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
        System.out.println(threadMonitor.getNumThread());
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
