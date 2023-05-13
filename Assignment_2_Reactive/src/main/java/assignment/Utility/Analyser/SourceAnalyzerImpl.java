package assignment.Utility.Analyser;


import assignment.Flows.*;
import assignment.Model.Directory;
import assignment.Model.Events.DirectoryEvent;
import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.Map;
import java.util.TreeSet;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    public static MasterFlow masterFlow;
    public static DataStructureFlow dataStructureFlow;

    public Pair<TreeSet<Pair<File, Long>>, Map<Pair<Integer,Integer>, Integer>> getReport(final Directory d, final int MAXL, final int NI){

        dataStructureFlow = new DataStructureFlow(MAXL, NI);
        masterFlow = new MasterFlow();

        //DirectoryFlow directoryFlow = new DirectoryFlow();
        //directoryFlow.observeDirectory(d);
        //directoryFlow.observeFile(d);

        masterFlow.receiveEvents(new DirectoryEvent(d));

        System.out.println("Fine");
        return new Pair<>(dataStructureFlow.getFileLengthTree(), dataStructureFlow.getIntervalMap());
    }


    public void analyzeSources(final Directory d){

    }

}
