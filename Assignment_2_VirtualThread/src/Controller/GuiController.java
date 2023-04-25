package Controller;

import Monitor.*;
import utility.Pair;

import java.io.File;
import java.util.*;


public class GuiController implements Runnable, ModelObserver{

    private boolean running;
    private TreeSet<Pair<File, Long>> fileLengthMap;
    private HashMap<Pair<Integer,Integer>, Integer> intervalMap;
    private final List<GuiObserver> observers;

    public GuiController(){

        this.running = true;
        this.observers = new ArrayList<>();
    }

    public void processStop(){
        this.running = false;
        Thread.currentThread().interrupt();
    }

    public void processStart(){
        this.running = true;
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
            obs.analyzeEnded();
        }
    }
}

