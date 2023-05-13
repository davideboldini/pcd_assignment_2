package assignment.Utility.Analyser;


import assignment.Model.Directory;
import assignment.Utility.Pair;

import java.io.File;
import java.util.Map;
import java.util.TreeSet;

public interface SourceAnalyzer {

    Pair<TreeSet<Pair<File, Long>>, Map<Pair<Integer,Integer>, Integer>> getReport(final Directory d, final int MAXL, final int NI);



}
