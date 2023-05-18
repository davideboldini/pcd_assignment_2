package Threads;

import Controller.GuiController;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ControllerThread implements Runnable{

    private final Queue<Thread> threadQueue;
    private GuiController guiController;

    public ControllerThread(){
        this.threadQueue = new ConcurrentLinkedQueue<>();
    }

    public void addThread(final Thread t){
        this.threadQueue.add(t);
        t.start();
    }

    public void addGuiThread(final GuiController gc){
        this.guiController = gc;
        Thread.ofVirtual().start(guiController);
    }

    @Override
    public void run() {
        while(!threadQueue.isEmpty()){
            try {
                this.threadQueue.poll().join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (guiController != null){
            this.guiController.processStop();
        }
    }

    public void stopThreads(){
        for (Thread t: threadQueue) {
            threadQueue.remove(t);
            if (t.isAlive() && !t.isInterrupted())
                t.interrupt();
        }
        this.guiController.processStop();
        Thread.currentThread().interrupt();
    }
}
