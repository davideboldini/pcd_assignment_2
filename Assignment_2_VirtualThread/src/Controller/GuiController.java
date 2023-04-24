package Controller;

import Monitor.*;
import Threads.ControllerThread;
import Threads.DirectoryThread;
import utility.Pair;

import java.io.File;
import java.util.*;

public class GuiController implements Runnable, ModelObserver{

    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private ControllerThread controller;
    private boolean running;
    private TreeSet<Pair<File, Long>> fileLengthMap;
    private HashMap<Pair<Integer,Integer>, Integer> intervalMap;
    private final List<GuiObserver> observers;

    public GuiController(final FileMonitor fileMonitor, final IntervalMonitor intervalMonitor){
        this.fileMonitor = fileMonitor;
        this.intervalMonitor = intervalMonitor;

        this.fileMonitor.addObserver(this);
        this.intervalMonitor.addObserver(this);

        this.running = false;
        this.observers = new ArrayList<>();
    }

    public void processEvent(final String D, final int N, final int MAXL, final int NI){
        try {
            this.running = true;
            new Thread(() -> {
                try {
                    this.fileMonitor.clearMap();
                    this.intervalMonitor.initMap(MAXL, NI);

                    this.controller = new ControllerThread();
                    Thread tDir = new Thread(controller);

                    this.controller.addThread(new Thread(new DirectoryThread(D, fileMonitor, intervalMonitor, controller)));
                    tDir.start();
                    tDir.join();
                    this.running = false;

                } catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void processStop(){
       this.controller.stopThreads();
       this.running = false;
       Thread.currentThread().interrupt();
    }

    @Override
    public synchronized void modelFileUpdated(final FileMonitor fileMonitor) {
        this.fileLengthMap = fileMonitor.getFileLengthMap();
    }

    @Override
    public synchronized void modelIntervalUpdated(final IntervalMonitor intervalMonitor) {
        this.intervalMap = intervalMonitor.getIntervalMap();
    }


    @Override
    public void run() {
        while(running) {
            try {
                Thread.sleep(500);
            } catch (Exception e){
                e.printStackTrace();
            }
            this.notifyObservers();
        }
        this.notifyEndObservers();
    }

    public void addObserver(GuiObserver obs){
        observers.add(obs);
    }

    private synchronized void notifyObservers(){
        for (GuiObserver obs: observers){
            if (this.fileLengthMap != null && this.intervalMap != null){
                obs.guiFileLengthUpdated(this.fileLengthMap);
                obs.guiIntervalUpdated(this.intervalMap);
            }
        }
    }

    private synchronized void notifyEndObservers(){
        for (GuiObserver obs: observers){
            obs.guiUpdateEnd();
        }
    }
}
