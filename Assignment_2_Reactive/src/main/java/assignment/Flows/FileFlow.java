package assignment.Flows;

import assignment.Utility.Analyser.SourceAnalyzerImpl;
import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileFlow {

    private List<File> fileList;
    private File file;

    public FileFlow(final List<File> fileList){
        this.fileList = new ArrayList<>(fileList);
    }
    public FileFlow(final File file) {
        this.file = file;
    }

    public void observeFileList(){
        Flowable.fromIterable(fileList)
                .flatMap(file -> Flowable.just(file)
                        .subscribeOn(Schedulers.computation())
                        .map(f -> {
                            Long numRows = this.countNumRows(file);
                            Pair<File, Long> pair = new Pair<>(file, numRows);
                            return pair;
                        })
                ).blockingSubscribe(res -> {
                    //SourceAnalyzerImpl.intervalFlow.observeInterval(res.getY());
                    //SourceAnalyzerImpl.fileLengthFlow.observeFileLength(res);
                    SourceAnalyzerImpl.dataStructureFlow.fillStructureFlow(res);
                    //System.out.println(res);
                });
    }

    public void observeFile(){
        Flowable.just(file)
                .observeOn(Schedulers.computation())
                .map(file -> {return new Pair<File, Long>(file, this.countNumRows(file));})
                .blockingSubscribe(pair -> {
                    SourceAnalyzerImpl.dataStructureFlow.fillStructureFlow(pair);
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
