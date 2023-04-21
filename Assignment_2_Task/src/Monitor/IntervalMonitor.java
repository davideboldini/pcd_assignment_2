package Monitor;

import utility.Pair;
import java.util.*;

public class IntervalMonitor {

    private HashMap<Pair<Integer,Integer>, Integer> intervalMap;
    private final int MAXL;
    private final int NI;
    private boolean isAvailable;
    private final List<ModelObserver> observers = new ArrayList<>();

    public IntervalMonitor(final int MAXL, final int NI){
        this.isAvailable = true;
        this.MAXL = MAXL;
        this.NI = NI;
        this.initMap(MAXL, NI);
    }

    public IntervalMonitor(){
        this.MAXL = 0;
        this.NI = 0;
        this.isAvailable = true;
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


    public synchronized void addElementInInterval(final Long numRows){
        while(!isAvailable){
            try {
                wait();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        this.isAvailable = false;

        this.intervalMap.keySet().stream().filter(interval -> numRows < interval.getY() || (numRows >= interval.getX() && interval.getY().equals(-1))).findFirst().ifPresent(interval -> intervalMap.put(interval, intervalMap.get(interval) + 1));

        this.isAvailable = true;
        notifyAll();
        notifyObservers();
    }


    public synchronized HashMap<Pair<Integer,Integer>, Integer> getIntervalMap(){
        while (!isAvailable){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        notifyAll();
        return (HashMap<Pair<Integer, Integer>, Integer>) this.intervalMap.clone();
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
