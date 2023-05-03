package assignment.Message;

import assignment.Utility.Pair;

import java.util.Map;

public class MessageInterval {

    private Map<Pair<Integer,Integer>, Integer> intervalMap;

    public MessageInterval(final Map<Pair<Integer,Integer>, Integer> intervalMap){
        this.intervalMap = intervalMap;
    }

    public Map<Pair<Integer, Integer>, Integer> getIntervalMap() {
        return intervalMap;
    }
}
