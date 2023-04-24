package Monitor;

import utility.Pair;

import java.io.File;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileMonitor {

    private final TreeSet<Pair<File, Long>> fileLengthMap;
    private final List<ModelObserver> observers;
    private final Lock lock;
    private boolean isAvailable;

    public FileMonitor(){
        this.lock = new ReentrantLock();
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

    public void addFile(final File file, final Long numRows){

        try {
            lock.lock();
            this.fileLengthMap.add(new Pair<>(file.getAbsoluteFile(), numRows));
            notifyObservers();
        } finally {
            lock.unlock();
        }

    }

    public TreeSet<Pair<File,Long>> getFileLengthMap() {
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
