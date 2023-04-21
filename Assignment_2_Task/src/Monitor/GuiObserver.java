package Monitor;

import utility.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

public interface GuiObserver {

    void guiFileLengthUpdated(TreeSet<Pair<File, Long>> fileLengthMap);
    void guiIntervalUpdated(HashMap<Pair<Integer,Integer>, Integer> intervalMap);
    void guiUpdateEnd();
}
