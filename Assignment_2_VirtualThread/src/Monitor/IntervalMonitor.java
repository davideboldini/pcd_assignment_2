package Monitor;

import utility.Pair;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IntervalMonitor {

    private HashMap<Pair<Integer,Integer>, Integer> intervalMap;
    private final int NI = 0;
    private final ReentrantLock lock;
    private final List<ModelObserver> observers = new ArrayList<>();

    public IntervalMonitor(final int MAXL, final int NI){
        this.lock = new ReentrantLock();
        this.initMap(MAXL, NI);
    }

    public void initMap(final int MAXL, final int NI){
        this.intervalMap = new LinkedHashMap<>();

        int intervalSize = MAXL;

        if (NI > 1) {
            intervalSize = MAXL / (NI - 1);
        } else {
            this.intervalMap.put(new Pair<>(0, MAXL), 0);
            return;
        }

        for (int i = 0; i < (NI - 1); i++){
            if ( ((i + 1) * intervalSize)-1 != (MAXL - 1) && i == (NI - 1)-1){
                this.intervalMap.put(new Pair<>(i * intervalSize, (MAXL - 1)), 0);
            }else {
                this.intervalMap.put(new Pair<>(i * intervalSize, ((i + 1) * intervalSize)-1), 0);
            }
        }

        this.intervalMap.put(new Pair<>(MAXL, -1), 0);
    }


    public void addElementInInterval(final Long numRows){

        try {
            lock.lockInterruptibly();
            this.intervalMap.keySet().stream().filter(interval -> numRows < interval.getY() || (numRows >= interval.getX() && interval.getY().equals(-1))).findFirst().ifPresent(interval -> intervalMap.put(interval, intervalMap.get(interval) + 1));
            if (!observers.isEmpty())
                notifyObservers();
        } catch (InterruptedException ignored) {
        } finally {
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }

    public HashMap<Pair<Integer,Integer>, Integer> getIntervalMap(){
        HashMap<Pair<Integer,Integer>, Integer> res = null;
        try {
            lock.lockInterruptibly();
            res = new HashMap<>(this.intervalMap);
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
            obs.modelIntervalUpdated(this);
        }
    }

}
