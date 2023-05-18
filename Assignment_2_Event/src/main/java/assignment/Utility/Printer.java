package assignment.Utility;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;

public class Printer {

    public void printFileLength(final TreeSet<Pair<File,Long>> fileTree, final int N){
        System.out.println("----------------------- FILE -----------------------");
        System.out.println(AsciiTable.getTable(fileTree.stream().toList().subList(0, N), Arrays.asList(
                new Column().header("Nome").headerAlign(HorizontalAlign.CENTER).dataAlign(HorizontalAlign.LEFT).with(file -> file.getX().getName()),
                new Column().header("Numero righe").dataAlign(HorizontalAlign.CENTER).with(rows -> rows.getY().toString())
        )));
        System.out.println("\n");

    }

    public void printInterval(final Map<Pair<Integer,Integer>, Integer> intervalMap){
        System.out.println("----------------------- INTERVALLI -----------------------");
        System.out.println(AsciiTable.getTable(intervalMap.entrySet(), Arrays.asList(
                new Column().header("Intervallo").headerAlign(HorizontalAlign.CENTER).with(interval -> interval.getKey().toString()),
                new Column().dataAlign(HorizontalAlign.CENTER).with(val -> val.getValue().toString())
        )));
        System.out.println("\n");
    }

    public String printFileLengthGui(final TreeSet<Pair<File,Long>> fileTree, final int N){

        String out = AsciiTable.getTable(fileTree.stream().toList().subList(0, N), Arrays.asList(
                new Column().header("Nome").headerAlign(HorizontalAlign.CENTER).dataAlign(HorizontalAlign.LEFT).with(file -> file.getX().getName()),
                new Column().header("Numero righe").dataAlign(HorizontalAlign.CENTER).with(rows -> rows.getY().toString())
        )) +
                "\n";
        return out;
    }

    public String printIntervalGui(final Map<Pair<Integer,Integer>, Integer> intervalMap){

        String out = (AsciiTable.getTable(intervalMap.entrySet(), Arrays.asList(
                new Column().with(interval -> interval.getKey().toString()),
                new Column().dataAlign(HorizontalAlign.CENTER).with(val -> val.getValue().toString())
        ))) +
                "\n";
        return out;
    }

}
