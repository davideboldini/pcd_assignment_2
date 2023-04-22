package Utility.Analyser;

import Model.Directory;
import Monitor.FileMonitor;
import Monitor.IntervalMonitor;
import Task.DirectoryTask;
import Utility.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.*;

public class SourceAnalyserImpl implements SourceAnalyser{

    private final int MAXL, NI, N, nPool;
    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private final ExecutorService executor;

    public SourceAnalyserImpl(final int MAXL, final int NI, final int N, final int nPool){
        this.nPool = nPool;
        this.executor = Executors.newFixedThreadPool(nPool);
        this.MAXL = MAXL;
        this.NI = NI;
        this.N = N;
        this.fileMonitor = new FileMonitor();
        this.intervalMonitor = new IntervalMonitor(MAXL, NI);
    }

    @Override
    public Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> getReport(Directory d) {
        final Phaser phaser = new Phaser(1);
        DirectoryTask directoryTask = new DirectoryTask(d, executor, nPool, fileMonitor, intervalMonitor);
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Pair<File, Long>> maxFile = fileMonitor.getFileLengthMap().stream().toList().subList(0,N);
        return new Pair<>(intervalMonitor.getIntervalMap(), maxFile);
    }

    @Override
    public Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> analyzeSources(Directory d) {
        return null;
    }
}
