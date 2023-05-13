package assignment.Flows;

import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

public class DataStructureFlow {

    private TreeSet<Pair<File, Long>> fileLengthTree;
    private Map<Pair<Integer,Integer>, Integer> intervalMap;

    public DataStructureFlow(final int MAXL, final int NI){
        this.initTreeSet();
        this.initMap(MAXL, NI);
    }

    public TreeSet<Pair<File, Long>> getFileLengthTree() {
        return fileLengthTree;
    }

    public Map<Pair<Integer, Integer>, Integer> getIntervalMap() {
        return intervalMap;
    }

    public void fillStructureFlow(final Pair<File, Long> filePair){
        Observable.just(filePair)
                .subscribeOn(Schedulers.computation())
                .map(pair -> {
                    fileLengthTree.add(pair);
                    addMap(pair.getY());
                    //System.out.println(Thread.currentThread().getName());
                    return new Pair<>(fileLengthTree, intervalMap);
                }).blockingSubscribe(res -> {
                    //System.out.println(Thread.currentThread());
                    System.out.println(res.getX().size());
                });
    }

    private void initTreeSet(){
        this.fileLengthTree = new TreeSet<>((o1, o2) -> {
            int countCompare = o2.getY().compareTo(o1.getY());
            if (countCompare == 0){
                return o2.getX().compareTo(o1.getX());
            }
            return countCompare;
        });
    }

    private void initMap(final int MAXL, final int NI){
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

    private void addMap(final Long numRows){
        this.intervalMap.keySet().stream().filter(interval -> numRows < interval.getY() || (numRows >= interval.getX() && interval.getY().equals(-1))).findFirst().ifPresent(interval -> intervalMap.put(interval, intervalMap.get(interval) + 1));
    }

}
