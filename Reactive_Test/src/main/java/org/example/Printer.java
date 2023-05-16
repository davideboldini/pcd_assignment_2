package org.example;

import java.io.File;
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
        String out = "";
        out += ("----------------------- FILE -----------------------");
        for (Pair<File,Long> entry: fileTree.stream().toList().subList(0, N)) {
            out += ("File: " + entry.getX().getAbsolutePath() + " -> " + entry.getY() + " righe");
        }
        out += ("\n");
        return out;
    }

}
