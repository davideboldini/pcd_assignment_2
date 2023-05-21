package Threads;

import Controller.GuiController;

import java.util.*;
import java.util.concurrent.*;


public class ControllerThread implements Runnable{

    private final ExecutorService virtualExecutors = Executors.newVirtualThreadPerTaskExecutor();
    private GuiController guiController;
    private final Queue<Future<?>> futureQueue;

    public ControllerThread(){
        this.futureQueue = new ConcurrentLinkedQueue<>();
    }

    public void addThread(final Runnable t){
        Future<?> future = virtualExecutors.submit(t);
        futureQueue.add(future);
    }

    public void addGuiThread(final GuiController gc){
        this.guiController = gc;
        Thread.ofVirtual().start(guiController);
    }

    @Override
    public void run() {

        while (!futureQueue.isEmpty()){
            try {
                this.futureQueue.poll().get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (guiController != null){
            this.guiController.processStop();
        }
    }

    public void stopThreads(){

        virtualExecutors.shutdownNow();

        this.guiController.processStop();

        while (!futureQueue.isEmpty()){
            this.futureQueue.poll().cancel(true);
        }
        //Thread.currentThread().interrupt();
    }
}
