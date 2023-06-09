package utility.Analyser;

import Model.Directory;
import utility.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface SourceAnalyzer {
    void initSource(int MAXL, int NI);

    Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> getReport(Directory d) throws InterruptedException;

    void analyzeSources(Directory d) throws InterruptedException;

    void stopAnalyze();
}
