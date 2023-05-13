package assignment.Flows;

import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.util.TreeSet;

public class FileLengthFlow {

    private TreeSet<Pair<File, Long>> fileLengthTree;

    public FileLengthFlow(){
        this.initTreeSet();
    }

    public TreeSet<Pair<File, Long>> getFileLengthTree() {
        return fileLengthTree;
    }

    public void observeFileLength(final Pair<File,Long> filePair){
        Observable.just(filePair)
                .subscribeOn(Schedulers.computation())
                .map(pair -> {
                    fileLengthTree.add(pair);
                    return fileLengthTree;
                })
                .subscribe(res -> {
                    //System.out.println(Thread.currentThread());
                    System.out.println(fileLengthTree.size());
                });
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
