package Monitor;

import utility.Pair;

import java.io.File;
import java.util.*;

public class FileMonitor {

    private final TreeSet<Pair<File, Long>> fileLengthMap;
    private final List<ModelObserver> observers;
    private boolean isAvailable;

    public FileMonitor(){
        this.isAvailable = true;
        this.fileLengthMap = new TreeSet<>((o1, o2) -> {
            int countCompare = o2.getY().compareTo(o1.getY());
            if (countCompare == 0){
                return o2.getX().compareTo(o1.getX());
            }
            return countCompare;
        });
        this.observers = new ArrayList<>();
    }

    public void clearMap(){
        this.fileLengthMap.clear();
    }

    public synchronized void addFile(final File file, final Long numRows){
        while (!isAvailable){
            try {
                wait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        this.isAvailable = false;
        this.fileLengthMap.add(new Pair<>(file.getAbsoluteFile(), numRows));
        this.isAvailable = true;

        notifyAll();
        notifyObservers();
    }

    public synchronized TreeSet<Pair<File,Long>> getFileLengthMap() {
        while (!isAvailable)
            try {
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        notifyAll();
        return (TreeSet<Pair<File, Long>>) this.fileLengthMap.clone();
    }

    public void addObserver(ModelObserver obs){
        observers.add(obs);
    }

    private void notifyObservers(){
        for (ModelObserver obs: observers){
            obs.modelFileUpdated(this);
        }
    }
}
