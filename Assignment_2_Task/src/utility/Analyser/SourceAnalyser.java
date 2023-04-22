package Utility.Analyser;

import Model.Directory;
import Utility.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface SourceAnalyser {

    Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> getReport(Directory d);
    Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> analyzeSources(Directory d);
}
