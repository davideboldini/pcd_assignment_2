package assignment.Message;

import assignment.Message.Type.MessageType;
import assignment.Utility.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

public class MessageUpdate {

    private TreeSet<Pair<File, Long>> fileLengthMap;
    private HashMap<Pair<Integer,Integer>, Integer> intervalMap;
    private final MessageType typeMessage;

    public MessageUpdate(final TreeSet<Pair<File, Long>> fileLengthMap, final MessageType typeMessage){
        this.fileLengthMap = fileLengthMap;
        this.typeMessage = typeMessage;
    }

    public MessageUpdate(final HashMap<Pair<Integer, Integer>, Integer> intervalMap, final MessageType typeMessage){
        this.intervalMap = intervalMap;
        this.typeMessage = typeMessage;
    }

    public MessageType getTypeMessage(){
        return this.typeMessage;
    }

    public TreeSet<Pair<File, Long>> getFileLengthMap() {
        return this.fileLengthMap;
    }

    public HashMap<Pair<Integer, Integer>, Integer> getIntervalMap() {
        return this.intervalMap;
    }
}
