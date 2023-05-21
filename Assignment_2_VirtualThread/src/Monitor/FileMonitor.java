package Monitor;

import utility.Pair;

import java.io.File;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class FileMonitor {

    private final TreeSet<Pair<File, Long>> fileLengthMap;
    private final ReentrantLock lock;
    private final List<ModelObserver> observers = new ArrayList<>();

    public FileMonitor(){
        this.lock = new ReentrantLock();
        this.fileLengthMap = new TreeSet<>((o1, o2) -> {
            int countCompare = o2.getY().compareTo(o1.getY());
            if (countCompare == 0){
                return o2.getX().compareTo(o1.getX());
            }
            return countCompare;
        });
    }

    public void addFile(final File file, final Long numRows){

        try {
            lock.lockInterruptibly();
            this.fileLengthMap.add(new Pair<>(file.getAbsoluteFile(), numRows));
            if (!observers.isEmpty())
                notifyObservers();
        } catch (InterruptedException ignored) {
        } finally {
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

    public TreeSet<Pair<File,Long>> getFileLengthMap() {
        TreeSet<Pair<File, Long>> res = null;
        try {
            lock.lockInterruptibly();
            res = new TreeSet<>(this.fileLengthMap);
        } catch (InterruptedException ignored) {
        } finally {
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
        return res;
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
