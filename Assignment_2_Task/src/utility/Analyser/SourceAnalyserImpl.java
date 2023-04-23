package Utility.Analyser;

import GUI.Observer.ModelObserver;
import Model.Directory;
import Monitor.FileMonitor;
import Monitor.IntervalMonitor;
import Task.DirectoryTask;
import Utility.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class SourceAnalyserImpl implements SourceAnalyser{

    private int N, nPool;
    private FileMonitor fileMonitor;
    private IntervalMonitor intervalMonitor;
    private ExecutorService executor;
    private final List<ModelObserver> observers = new ArrayList<>();

    @Override
    public void initSource(final int MAXL, final int NI, final int N, final int nPool){
        this.nPool = nPool;
        this.executor = Executors.newFixedThreadPool(nPool);
        this.N = N;
        this.fileMonitor = new FileMonitor();
        this.intervalMonitor = new IntervalMonitor(MAXL, NI);
    }

    @Override
    public FileMonitor getFileMonitor() {
        return fileMonitor;
    }

    @Override
    public IntervalMonitor getIntervalMonitor() {
        return intervalMonitor;
    }

    @Override
    public Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> getReport(Directory d) {
        DirectoryTask directoryTask = new DirectoryTask(d, executor, fileMonitor, intervalMonitor);
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Pair<File, Long>> maxFile = fileMonitor.getFileLengthMap().stream().toList().subList(0,N);
        Map<Pair<Integer, Integer>, Integer> intervalMap = intervalMonitor.getIntervalMap();

        return new Pair<>(intervalMap, maxFile);
    }

    @Override
    public void analyzeSources(Directory d) {
        executor = Executors.newFixedThreadPool(nPool);
        executor.execute(() -> {
            new DirectoryTask(d, executor, fileMonitor, intervalMonitor);
            try {
                Thread.sleep(600);
            } catch (InterruptedException ignored) {}

            for (ModelObserver observer: observers) {
                observer.analyzeEnded();
            }
        });

    }

    @Override
    public void stopAnalyze(){
        executor.shutdownNow();
    }

    public void addObserver(final ModelObserver observer){
        this.observers.add(observer);
    }

}
