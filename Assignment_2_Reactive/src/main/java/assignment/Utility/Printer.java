package assignment.Utility;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class Printer {

    public void printFileLength(final TreeSet<Pair<File,Long>> fileTree, final int N){
        System.out.println("----------------------- FILE -----------------------");
        for (Pair<File,Long> entry: fileTree.stream().toList().subList(0, N)) {
            System.out.println("File: " + entry.getX().getAbsolutePath() + " -> " + entry.getY() + " righe");
        }
        System.out.println("\n");
    }

    public void printInterval(final Map<Pair<Integer,Integer>, Integer> intervalMap){
        System.out.println("----------------------- INTERVALLI -----------------------");
        for (Map.Entry<Pair<Integer,Integer>, Integer> entry: intervalMap.entrySet()) {
            System.out.println("Intervallo " + entry.getKey().getX() + "-" + entry.getKey().getY() + " : " + entry.getValue());
        }
        System.out.println("\n");
    }

    public String printFileLengthGui(final TreeSet<Pair<File,Long>> fileTree, final int N){
        StringBuilder out = new StringBuilder();

        for (Pair<File,Long> entry : fileTree.stream().toList().subList(0,N)) {
            out.append("File: ").append(entry.getX().getAbsolutePath()).append(" -> ").append(entry.getY()).append(" righe \n");
        }

        out.append("\n");
        return out.toString();
    }

    public String printIntervalGui(final Map<Pair<Integer,Integer>, Integer> intervalMap){
        StringBuilder out = new StringBuilder();

        for (Map.Entry<Pair<Integer,Integer>, Integer> entry: intervalMap.entrySet()) {
            out.append("Intervallo " + entry.getKey().getX() + "-" + entry.getKey().getY() + " : " + entry.getValue() + "\n");
        }
        out.append("\n");
        return out.toString();
    }

}
