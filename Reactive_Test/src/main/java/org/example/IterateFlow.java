package org.example;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IterateFlow {

    DataStructureFlow dataStructureFlow;

    public IterateFlow(final DataStructureFlow dataStructureFlow){
        this.dataStructureFlow = dataStructureFlow;
    }

    public void iterateElements(final Directory d){

        this.listFlow(d);

        Observable.fromIterable(d.getDirectoryList())
                .observeOn(Schedulers.computation())
                .map(dir -> {
                    return new Pair<>(new IterateFlow(dataStructureFlow), dir);
                })
                .blockingSubscribe(res -> {
                    res.getX().iterateElements(res.getY());
                });

    }

    public void listFlow(final Directory d){
        Observable.fromIterable(d.getJavaFileList())
                .observeOn(Schedulers.computation())
                .map(f -> {
                    Long numRows = this.countNumRows(f);
                    return new Pair(f, numRows);
                })
                .blockingSubscribe(res -> {
                    dataStructureFlow.getFileLengthTree().add(res);
                    System.out.println(dataStructureFlow.getFileLengthTree().size());
                    //dataStructureFlow.fillStructureFlow(res);
                });
    }

    private Long countNumRows(final File file) {
        try {
            return Files.lines(Path.of(file.getPath())).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
