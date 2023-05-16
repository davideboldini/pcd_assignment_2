package assignment.Flows;

import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.util.TreeSet;

public class FileLengthStructure {

    private TreeSet<Pair<File, Long>> fileLengthTree;

    public FileLengthStructure(){
        this.initTreeSet();
    }

    public TreeSet<Pair<File, Long>> getFileLengthTree() {
        return fileLengthTree;
    }

    public void addElement(final Pair<File, Long> elem){
        this.fileLengthTree.add(elem);
    }

    private void initTreeSet(){
        this.fileLengthTree = new TreeSet<>((o1, o2) -> {
            int countCompare = o2.getY().compareTo(o1.getY());
            if (countCompare == 0){
                return o2.getX().compareTo(o1.getX());
            }
            return countCompare;
        });
    }


}
