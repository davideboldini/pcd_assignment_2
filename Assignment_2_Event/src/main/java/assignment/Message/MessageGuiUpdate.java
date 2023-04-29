package assignment.Message;

import assignment.Utility.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

public class MessageGuiUpdate {

    private TreeSet<Pair<File, Long>> fileLengthMap = null;
    private HashMap<Pair<Integer,Integer>, Integer> intervalMap = null;
    private final String typeMessage;

    public MessageGuiUpdate(final TreeSet<Pair<File, Long>> fileLengthMap, final String typeMessage){
        this.fileLengthMap = fileLengthMap;
        this.typeMessage = typeMessage;
    }

    public MessageGuiUpdate(final HashMap<Pair<Integer, Integer>, Integer> intervalMap, final String typeMessage){
        this.intervalMap = intervalMap;
        this.typeMessage = typeMessage;
    }

    public String getTypeMessage(){
        return this.typeMessage;
    }

    public TreeSet<Pair<File, Long>> getFileLengthMap() {
        return this.fileLengthMap;
    }

    public HashMap<Pair<Integer, Integer>, Integer> getIntervalMap() {
        return this.intervalMap;
    }
}
