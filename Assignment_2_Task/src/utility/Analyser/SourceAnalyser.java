package Utility.Analyser;

import GUI.Observer.ModelObserver;
import Model.Directory;
import Monitor.FileMonitor;
import Monitor.IntervalMonitor;
import Utility.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface SourceAnalyser {

    void initSource(int MAXL, int NI, int N, int nPool);

    FileMonitor getFileMonitor();

    IntervalMonitor getIntervalMonitor();

    Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> getReport(Directory d);
    void analyzeSources(Directory d);
    void stopAnalyze();
    void addObserver(final ModelObserver observer);
}
