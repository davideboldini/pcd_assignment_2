package assignment.Utility.Analyser;


import assignment.Agent.GUI.GuiFormAgent;
import assignment.Model.Directory;
import assignment.Utility.Pair;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.io.File;
import java.util.Map;
import java.util.TreeSet;

public interface SourceAnalyzer {
    Vertx getVertx();

    Pair<Promise<TreeSet<Pair<File, Long>>>, Promise<Map<Pair<Integer,Integer>, Integer>>> getReport(Directory d, int MAXL, int NI);

    void analyzeSources(Directory d, int MAXL, int NI, GuiFormAgent guiForm);

    void stopExecution();

    void restartVertx();
}
