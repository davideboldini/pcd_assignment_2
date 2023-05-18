package assignment.Utility.Analyser;


import assignment.GUI.Gui;
import assignment.Model.Directory;
import assignment.Utility.Pair;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.File;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

public interface SourceAnalyzer {

    CompletableFuture<Pair<TreeSet<Pair<File, Long>>, Map<Pair<Integer,Integer>, Integer>>> getReport(final Directory d, final int MAXL, final int NI);
    void analyzeSources(final Directory d, final int MAXL, final int NI,
                                   final PublishSubject<Pair<TreeSet<Pair<File, Long>>, Map<Pair<Integer,Integer>, Integer>>> guiPublishSubject);

    void stopFlows();

}
